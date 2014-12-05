package WayofTime.alchemicalWizardry.common.demonVillage.tileEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.Int3;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.demonVillage.BuildingSchematic;
import WayofTime.alchemicalWizardry.common.demonVillage.DemonBuilding;
import WayofTime.alchemicalWizardry.common.demonVillage.DemonCrosspath;
import WayofTime.alchemicalWizardry.common.demonVillage.DemonVillagePath;
import WayofTime.alchemicalWizardry.common.demonVillage.DemonVillagePath.Int3AndBool;
import WayofTime.alchemicalWizardry.common.demonVillage.GridSpace;
import WayofTime.alchemicalWizardry.common.demonVillage.GridSpaceHolder;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.DemonPacketRegistry;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.DemonType;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.IHoardDemon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TEDemonPortal extends TileEntity
{
	public DemonType type = DemonType.FIRE;
	
	public static boolean printDebug = false;
	
	public static int limit = 100;
	
    public static int buildingGridDelay = 25;
    public static int roadGridDelay = 10;
    public static int demonHoardDelay = 40;
    public static float demonRoadChance = 0.3f;
    public static float demonHouseChance = 0.6f;
    public static float demonPortalChance = 0.5f;
    public static float demonHoardChance = 0.8f;
    public static float portalTickRate = 1f;

    public static int[] tierCostList = new int[]{1000, 5000, 10000};
    
    public static Set<IHoardDemon> hoardList = new HashSet();

    public static List<DemonBuilding> buildingList = new ArrayList();
    public Random rand = new Random();
    private GridSpace[][] area;

    private int negXRadius; //These variables indicate how much the grid has expanded from the 1x1
    private int posXRadius; //matrix in each direction
    private int negZRadius;
    private int posZRadius;

    private boolean isInitialized;

    public int houseCooldown;
    public int roadCooldown;
    public int tier; //Tier of the demon portal - Should select buildings 2 below to this
    public int demonHouseCooldown;
    public int demonHoardCooldown;
    
    public float pointPool;
    
    public String nextDemonPortalName = "";
    public ForgeDirection nextDemonPortalDirection = ForgeDirection.DOWN;
    
    public int buildingStage = -1;
    
    public int delayBeforeParty = 0;
    
    public int lockdownTimer;

    public TEDemonPortal()
    {
        super();

        negXRadius = posXRadius = negZRadius = posZRadius = 1;

        area = new GridSpace[negXRadius + posXRadius + 1][negZRadius + posZRadius + 1];
        for (int xIndex = -negXRadius; xIndex <= posXRadius; xIndex++)
        {
            for (int zIndex = -negZRadius; zIndex <= posZRadius; zIndex++)
            {
                if (Math.abs(xIndex) == 1 || Math.abs(zIndex) == 1)
                {
                    this.setGridSpace(xIndex, zIndex, new GridSpace(GridSpace.ROAD, 4));
                } else
                {
                    this.setGridSpace(xIndex, zIndex, new GridSpace());
                }
            }
        }

        isInitialized = false;

        this.setGridSpace(0, 0, new GridSpace(GridSpace.MAIN_PORTAL, yCoord));

        this.houseCooldown = 0;
        this.roadCooldown = 0;
        this.tier = 0;
        this.lockdownTimer = 0;
    }
    
    public boolean isLockedDown()
    {
    	return this.lockdownTimer > 0;
    }
    
    public float getRoadChance()
    {
    	if(isLockedDown())
    	{
    		return 0;
    	}
    	return demonRoadChance;
    }
    
    public float getHouseChance()
    {
    	if(isLockedDown())
    	{
    		return 0;
    	}
    	return demonHouseChance;
    }
    
    public float getDemonPortalChance()
    {
    	if(isLockedDown())
    	{
    		return 0;
    	}
    	return demonPortalChance;
    }
    
    public float getDemonHoardChance()
    {
    	return demonHoardChance;
    }
    
    public boolean decreaseRandomCooldown(int amount)
    {
    	float totalChance = 0;

    	Map<String, Float> map = new HashMap();
    	map.put("roadChance", this.getRoadChance());
    	map.put("houseChance", this.getHouseChance());
    	map.put("demonPortalChance", this.getDemonPortalChance());
    	map.put("demonHoardChance", this.getDemonHoardChance());
    	
    	String action = "";
    	
    	for(Entry<String, Float> entry : map.entrySet())
    	{
    		totalChance += entry.getValue();
    	}
    	
    	float pointer = rand.nextFloat() * totalChance;
    	
    	for(Entry<String, Float> entry : map.entrySet())
    	{
    		float value = entry.getValue();
    		if(pointer <= value)
    		{
    			action = entry.getKey();
    			break;
    		}else
    		{
    			pointer -= value;
    		}
    	}
    	
    	if(action.equals("roadChance"))
    	{
    		if(roadCooldown > 0)
    		{
        		roadCooldown -= amount;
    		}else
    		{
    			return false;
    		}
    	}else if(action.equals("houseChance"))
    	{
    		if(houseCooldown > 0)
    		{
        		houseCooldown -= amount;
    		}else
    		{
    			return false;
    		}
    	}else if(action.equals("demonPortalChance"))
    	{
    		demonHouseCooldown += amount;
    	}else if(action.equals("demonHoardChance"))
    	{
    		if(demonHoardCooldown > 0)
    		{
        		demonHoardCooldown -= amount;
    		}else
    		{
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    public void notifyDemons(EntityLivingBase demon, EntityLivingBase target, double radius) //TODO
    {
    	this.lockdownTimer = 1000;
    	for(IHoardDemon thrallDemon : this.hoardList)
    	{
    		if(thrallDemon instanceof EntityCreature)
    		{
    			if(thrallDemon != demon && ((EntityCreature) thrallDemon).getAttackTarget() == null && !((EntityCreature) thrallDemon).isOnSameTeam(target))
    			{
    				double xf = demon.posX;
    				double yf = demon.posY;
    				double zf = demon.posZ;
    				
    				double xi = ((EntityCreature) thrallDemon).posX;
    				double yi = ((EntityCreature) thrallDemon).posY;
    				double zi = ((EntityCreature) thrallDemon).posZ;

    				if((xi-xf)*(xi-xf) + (yi-yf)*(yi-yf) + (zi-zf)*(zi-zf) <= radius*radius)
    				{
        				((EntityCreature) thrallDemon).setAttackTarget(target);
    				}else
    				{
    					((EntityCreature) thrallDemon).getNavigator().tryMoveToEntityLiving(target, 2);
    				}
    			}
    		}
    	}
    }
    
    public void notifyDemons(int xf, int yf, int zf, double radius)
    {
    	for(IHoardDemon thrallDemon : this.hoardList)
    	{
    		if(thrallDemon instanceof EntityCreature)
    		{
    			if(((EntityCreature) thrallDemon).getAttackTarget() == null)
    			{    				
    				double xi = ((EntityCreature) thrallDemon).posX;
    				double yi = ((EntityCreature) thrallDemon).posY;
    				double zi = ((EntityCreature) thrallDemon).posZ;

    				if((xi-xf)*(xi-xf) + (yi-yf)*(yi-yf) + (zi-zf)*(zi-zf) <= radius*radius)
    				{
    					((EntityCreature) thrallDemon).getNavigator().tryMoveToXYZ(xf, yf, zf, 1);
    				}
    			}
    		}
    	}
    }
    
    public void enthrallDemon(EntityLivingBase demon)
    {
    	if(demon instanceof IHoardDemon)
    	{
    		boolean enthrall = ((IHoardDemon) demon).thrallDemon(this);
    		if(enthrall)
    		{
        		this.hoardList.add((IHoardDemon)demon);
    		}
    	}
    }

    public void initialize()
    {
        if (isInitialized)
        {
            return;
        }
        
        DemonType[] types = DemonType.values();
        this.type = types[rand.nextInt(types.length)];
        
        for (int xIndex = -negXRadius; xIndex <= posXRadius; xIndex++)
        {
            for (int zIndex = -negZRadius; zIndex <= posZRadius; zIndex++)
            {
                if (Math.abs(xIndex) == 1 || Math.abs(zIndex) == 1)
                {
                    this.setGridSpace(xIndex, zIndex, new GridSpace(GridSpace.ROAD, yCoord));
                } else if (xIndex == 0 && zIndex == 0)
                {
                    this.setGridSpace(0, 0, new GridSpace(GridSpace.MAIN_PORTAL, yCoord));
                } else
                {
                    this.setGridSpace(xIndex, zIndex, new GridSpace());
                }
            }
        }

        this.houseCooldown = TEDemonPortal.buildingGridDelay;
        this.roadCooldown = TEDemonPortal.roadGridDelay;
        this.demonHoardCooldown = TEDemonPortal.demonHoardDelay;
        
        this.createRandomRoad();
        
        if (this.createRandomBuilding(DemonBuilding.BUILDING_PORTAL, tier) >= 1)
        {
        	System.out.println("Portal building successfully found!");
        	this.buildingStage = 0;
        }

        isInitialized = true;
    }
    
    public void createParty()
    {
    	worldObj.createExplosion(null, xCoord + rand.nextInt(10) - rand.nextInt(10), yCoord, zCoord + rand.nextInt(10) - rand.nextInt(10), 5*rand.nextFloat(), false);
    }

    public void start()
    {
    	this.delayBeforeParty = 200;
    }
    
    /**
     * Randomly increase one of the cooldowns such that a road, house, or a demon portal tier is caused. Demons are also randomly spawned via this mechanic.
     */
    @Override
    public void updateEntity()
    {
    	if(worldObj.isRemote)
    	{
    		return;
    	}
    	
    	if(this.delayBeforeParty > 0)
        {
        	this.delayBeforeParty--;
        	
        	if(rand.nextInt(20) == 0)
        	{
            	this.createParty();
        	}
        	
        	if(delayBeforeParty <= 0)
        	{
        		worldObj.createExplosion(null, xCoord, yCoord, zCoord, 15, false);
        		this.initialize();
        	}
        	
        	return;
        }
    	
        if (!isInitialized)
        {
            return;
        }
        
        lockdownTimer = Math.max(0, this.lockdownTimer - 1);
        this.incrementPoints();
        this.assignPoints();
        
        if(printDebug)
        AlchemicalWizardry.logger.info("Roads: " + roadCooldown + " Buildings: " + houseCooldown + " Lockdown: " + lockdownTimer);

        if(buildingStage >= 0 && buildingStage <=2)
        {
        	if(printDebug)
        	AlchemicalWizardry.logger.info("BuildingStage = " + buildingStage);
        	if(printDebug)
        	AlchemicalWizardry.logger.info("Tier = " + this.tier);
        	this.createPortalBuilding(buildingStage, nextDemonPortalName, tier);
        	buildingStage++;
        	return;
        }

        if(this.roadCooldown <= 0)
        {
            int roadsMade = this.createRandomRoad();
            if (roadsMade > 0)
            {
                this.roadCooldown = TEDemonPortal.roadGridDelay * roadsMade;
                //this.demonHouseCooldown += this.roadCooldown;
            }
        }
        
        if(this.houseCooldown <= 0)
        {
            int gridsUsed = this.createRandomBuilding(DemonBuilding.BUILDING_HOUSE, tier);
            if (gridsUsed > 0)
            {
                this.houseCooldown = TEDemonPortal.buildingGridDelay * gridsUsed;
                //this.demonHouseCooldown += this.houseCooldown;
            }
        }
        
        if(this.demonHoardCooldown <= 0)
        {
        	int complexityCost = this.createRandomDemonHoard(this, tier, this.type, this.isLockedDown());
        	if(complexityCost > 0)
        	{
        		this.demonHoardCooldown = TEDemonPortal.demonHoardDelay * complexityCost;
        	}
        }

        if(this.tier < this.tierCostList.length && this.demonHouseCooldown > this.tierCostList[this.tier])
        {
            this.tier++;

            if (this.createRandomBuilding(DemonBuilding.BUILDING_PORTAL, tier) >= 1)
            {
            	this.buildingStage = 0;
            }
        }

        
//        this.houseCooldown = Math.max(0, this.houseCooldown - 1); //Current dummy implementation of the increasing costs
//        this.roadCooldown = Math.max(0, this.roadCooldown - 1);
    }
    
    public void assignPoints()
    {
    	if((int)this.pointPool > 0)
    	{
    		if(this.decreaseRandomCooldown((int)this.pointPool))
    		{
        		this.pointPool -= (int)this.pointPool;
    		}
    	}
    }
    
    public void incrementPoints()
    {
    	this.pointPool += portalTickRate;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.negXRadius = par1NBTTagCompound.getInteger("negXRadius");
        this.negZRadius = par1NBTTagCompound.getInteger("negZRadius");
        this.posXRadius = par1NBTTagCompound.getInteger("posXRadius");
        this.posZRadius = par1NBTTagCompound.getInteger("posZRadius");
        this.houseCooldown = par1NBTTagCompound.getInteger("houseCooldown");
        this.roadCooldown = par1NBTTagCompound.getInteger("roadCooldown");
        this.demonHoardCooldown = par1NBTTagCompound.getInteger("demonHoardCooldown");

        area = new GridSpace[negXRadius + posXRadius + 1][negZRadius + posZRadius + 1];

        NBTTagList tagList = par1NBTTagCompound.getTagList("Grid", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            int length = (negZRadius + posZRadius + 1);

            int x = i / length;
            int z = i % length;

            NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
            GridSpace space = GridSpace.getGridFromTag(tag);

            area[x][z] = space;
        }

        this.isInitialized = par1NBTTagCompound.getBoolean("init");

        this.tier = par1NBTTagCompound.getInteger("tier");
        this.demonHouseCooldown = par1NBTTagCompound.getInteger("demonHouseCooldown");
        
        this.nextDemonPortalName = par1NBTTagCompound.getString("nextDemonPortalName");
        this.buildingStage = par1NBTTagCompound.getInteger("buildingStage");
        this.nextDemonPortalDirection = ForgeDirection.getOrientation(par1NBTTagCompound.getInteger("nextDemonPortalDirection"));
        
        this.pointPool = par1NBTTagCompound.getFloat("pointPool");
        this.lockdownTimer = par1NBTTagCompound.getInteger("lockdownTimer");
        this.delayBeforeParty = par1NBTTagCompound.getInteger("delayBeforeParty");
        this.type = DemonType.valueOf(par1NBTTagCompound.getString("demonType"));
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("negXRadius", negXRadius);
        par1NBTTagCompound.setInteger("negZRadius", negZRadius);
        par1NBTTagCompound.setInteger("posXRadius", posXRadius);
        par1NBTTagCompound.setInteger("posZRadius", posZRadius);
        par1NBTTagCompound.setInteger("houseCooldown", houseCooldown);
        par1NBTTagCompound.setInteger("roadCooldown", roadCooldown);
        par1NBTTagCompound.setInteger("demonHoardCooldown", demonHoardCooldown);

        NBTTagList gridList = new NBTTagList();

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            for (int j = 0; j <= negZRadius + posZRadius; j++)
            {
                int index = i + (negZRadius + posZRadius + 1) * j;

                GridSpace space = area[i][j];
                NBTTagCompound nextTag;

                if (space == null)
                {
                    nextTag = new GridSpace().getTag();
                } else
                {
                    nextTag = space.getTag();
                }

                gridList.appendTag(nextTag);
            }
        }

        par1NBTTagCompound.setTag("Grid", gridList);

        par1NBTTagCompound.setBoolean("init", this.isInitialized);
        par1NBTTagCompound.setInteger("tier", this.tier);
        par1NBTTagCompound.setInteger("demonHouseCooldown", this.demonHouseCooldown);
        
        par1NBTTagCompound.setString("nextDemonPortalName", nextDemonPortalName);
        par1NBTTagCompound.setInteger("buildingStage", buildingStage);
        
        par1NBTTagCompound.setInteger("nextDemonPortalDirection", this.nextDemonPortalDirection.ordinal());
        par1NBTTagCompound.setFloat("pointPool", pointPool);
        par1NBTTagCompound.setInteger("lockdownTimer", this.lockdownTimer);
        par1NBTTagCompound.setInteger("delayBeforeParty", delayBeforeParty);
        par1NBTTagCompound.setString("demonType", this.type.toString());
    }

    public int createRandomDemonHoard(TEDemonPortal teDemonPortal, int tier, DemonType type, boolean spawnGuardian)
    {
    	int next = rand.nextInt(4);
        ForgeDirection dir;

        switch (next)
        {
            case 0:
                dir = ForgeDirection.NORTH;
                break;
            case 1:
                dir = ForgeDirection.SOUTH;
                break;
            case 2:
                dir = ForgeDirection.EAST;
                break;
            case 3:
                dir = ForgeDirection.WEST;
                break;
            default:
                dir = ForgeDirection.NORTH;
        }

        Int3 road = findRoadSpaceFromDirection(dir, (rand.nextInt(negXRadius + negZRadius + posXRadius + posZRadius)) + 1);
        if(road == null)
        {
        	return 0;
        }
        
    	return DemonPacketRegistry.spawnDemons(teDemonPortal, worldObj, xCoord + road.xCoord * 5, road.yCoord + 1, zCoord + road.zCoord * 5, type, tier, spawnGuardian);
    }
    
    public int createRandomRoad() //Return the number of road spaces
    {
        int next = rand.nextInt(4);
        ForgeDirection dir;

        switch (next)
        {
            case 0:
                dir = ForgeDirection.NORTH;
                break;
            case 1:
                dir = ForgeDirection.SOUTH;
                break;
            case 2:
                dir = ForgeDirection.EAST;
                break;
            case 3:
                dir = ForgeDirection.WEST;
                break;
            default:
                dir = ForgeDirection.NORTH;
        }

        int length = 5;

        Int3 road = findRoadSpaceFromDirection(dir, (rand.nextInt(negXRadius + negZRadius + posXRadius + posZRadius)) + 1);

        int x = road.xCoord;
        int yLevel = road.yCoord;
        int z = road.zCoord;

        if(printDebug)
        AlchemicalWizardry.logger.info("X: " + x + " Z: " + z + " Direction: " + dir.toString());

        List<ForgeDirection> directions = this.findValidExtentionDirection(x, z);

        if (directions.size() <= 0)
        {
            return 0;
        }

        int maxDistance = 5;

        int distance = 0;
        ForgeDirection dominantDirection = null;

        for (ForgeDirection direction : directions)
        {
            int amt = this.getLength(direction, maxDistance, x, z);
            if (amt > distance)
            {
                distance = amt;
                dominantDirection = direction;
            } else if (amt == distance && rand.nextBoolean())
            {
                dominantDirection = direction;
            }
        }

        if (dominantDirection == null)
        {
            return 0;
        }
        if(printDebug)
        AlchemicalWizardry.logger.info("I got here!");
        if(printDebug)
        AlchemicalWizardry.logger.info("Distance: " + distance + " Direction: " + dominantDirection.toString() + " yLevel: " + yLevel);

        this.createGriddedRoad(x, yLevel, z, dominantDirection, distance + 1, true);

        return distance;
    }

    public List<ForgeDirection> findValidExtentionDirection(int x, int z)
    {
        List<ForgeDirection> directions = new LinkedList();

        if (this.getGridSpace(x, z) == null || !this.getGridSpace(x, z).isRoadSegment())
        {
            return directions;
        }

        GridSpace nextGrid = this.getGridSpace(x + 1, z);
        if (nextGrid.isEmpty())
        {
            directions.add(ForgeDirection.EAST);
        }

        nextGrid = this.getGridSpace(x - 1, z);
        if (nextGrid.isEmpty())
        {
            directions.add(ForgeDirection.WEST);
        }

        nextGrid = this.getGridSpace(x, z + 1);
        if (nextGrid.isEmpty())
        {
            directions.add(ForgeDirection.SOUTH);
        }

        nextGrid = this.getGridSpace(x, z - 1);
        if (nextGrid.isEmpty())
        {
            directions.add(ForgeDirection.NORTH);
        }

        return directions;
    }

    public int getLength(ForgeDirection dir, int maxLength, int x, int z) //Number of spaces forward
    {
        for (int i = 1; i <= maxLength; i++)
        {
            GridSpace space = this.getGridSpace(x + i * dir.offsetX, z + i * dir.offsetZ);
            if (space.isEmpty())
            {
                for (int k = 1; k <= this.getRoadSpacer(); k++)
                {
                    GridSpace space1 = this.getGridSpace(x + i * dir.offsetX + dir.offsetZ * k, z + i * dir.offsetZ + dir.offsetX * k);
                    GridSpace space2 = this.getGridSpace(x + i * dir.offsetX - dir.offsetZ * k, z + i * dir.offsetZ - dir.offsetX * k);

                    if (space1.isRoadSegment() || space2.isRoadSegment())
                    {
                        return i - 1;
                    }
                }

                continue;
            }
            if (space.isRoadSegment())
            {
                return i;
            } else
            {
                return i - 1;
            }
        }
        return maxLength;
    }

    public Int3 findRoadSpaceFromDirection(ForgeDirection dir, int amount) //TODO
    {
        int index = 0;
        if (dir == ForgeDirection.NORTH)
        {
        	if(printDebug)
            System.out.print("NORTH!");
            for (int i = Math.max(0, -limit + negZRadius); i <= negZRadius + Math.min(posZRadius, limit); i++)
            {
                for (int j = Math.max(0, -limit + negXRadius); j <= negXRadius + Math.min(posXRadius, limit); j++)
                {
                    GridSpace space = area[j][i];
                    if (space.isRoadSegment())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(j - negXRadius, space.getYLevel(), i - negZRadius);
                        }
                    }
                }
            }
        } else if (dir == ForgeDirection.SOUTH)
        {
            for (int i = negZRadius + Math.min(posZRadius, limit); i >= Math.max(0, -limit + negZRadius); i--)
            {
                for (int j = Math.max(0, -limit + negXRadius); j <= negXRadius + Math.min(posXRadius, limit); j++)
                {
                    GridSpace space = area[j][i];
                    if (space.isRoadSegment())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(j - negXRadius, space.getYLevel(), i - negZRadius);
                        }
                    }
                }
            }
        } else if (dir == ForgeDirection.EAST)
        {
            for (int i = negXRadius + Math.min(posXRadius, limit); i >= Math.max(0, -limit + negXRadius); i--)
            {
                for (int j = Math.max(0, -limit + negZRadius); j <= negZRadius + Math.min(posZRadius, limit); j++)
                {
                    GridSpace space = area[i][j];
                    if (space.isRoadSegment())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(i - negXRadius, space.getYLevel(), j - negZRadius);
                        }
                    }
                }
            }
        } else if (dir == ForgeDirection.WEST)
        {
            for (int i = Math.max(0, -limit + negXRadius); i <= negXRadius + Math.min(posXRadius, limit); i++)
            {
                for (int j = Math.max(0, -limit + negZRadius); j <= negZRadius + Math.min(posZRadius, limit); j++)
                {
                    GridSpace space = area[i][j];
                    if (space.isRoadSegment())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(i - negXRadius, space.getYLevel(), j - negZRadius);
                        }
                    }
                }
            }
        }

        return new Int3(0, 0, 0);
    }

    public Int3 findEmptySpaceNearRoad(ForgeDirection dir, int amount, int closeness)
    {
        int index = 0;
        if (dir == ForgeDirection.NORTH)
        {
        	if(printDebug)
            System.out.print("NORTH!");
            for (int i = 0; i <= negZRadius + posZRadius; i++)
            {
                for (int j = 0; j <= negXRadius + posXRadius; j++)
                {
                    GridSpace space = area[j][i];
                    if (space.isEmpty())
                    {
                        int yLevel = this.findNearestRoadYLevel(j - negXRadius, i - negZRadius, closeness);
                        if (yLevel == -1)
                        {
                            continue;
                        }
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(j - negXRadius, yLevel, i - negZRadius);
                        }
                    }
                }
            }
        } else if (dir == ForgeDirection.SOUTH)
        {
            for (int i = negZRadius + posZRadius; i >= 0; i--)
            {
                for (int j = 0; j <= negXRadius + posXRadius; j++)
                {
                    GridSpace space = area[j][i];
                    int yLevel = this.findNearestRoadYLevel(j - negXRadius, i - negZRadius, closeness);
                    if (yLevel == -1)
                    {
                        continue;
                    }
                    if (space.isEmpty())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(j - negXRadius, yLevel, i - negZRadius);
                        }
                    }
                }
            }
        } else if (dir == ForgeDirection.EAST)
        {
            for (int i = negXRadius + posXRadius; i >= 0; i--)
            {
                for (int j = 0; j <= negZRadius + posZRadius; j++)
                {
                    GridSpace space = area[i][j];
                    int yLevel = this.findNearestRoadYLevel(i - negXRadius, j - negZRadius, closeness);
                    if (yLevel == -1)
                    {
                        continue;
                    }
                    if (space.isEmpty())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(i - negXRadius, yLevel, j - negZRadius);
                        }
                    }
                }
            }
        } else if (dir == ForgeDirection.WEST)
        {
            for (int i = 0; i <= negXRadius + posXRadius; i++)
            {
                for (int j = 0; j <= negZRadius + posZRadius; j++)
                {
                    GridSpace space = area[i][j];
                    int yLevel = this.findNearestRoadYLevel(i - negXRadius, j - negZRadius, closeness);
                    if (yLevel == -1)
                    {
                        continue;
                    }
                    if (space.isEmpty())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(i - negXRadius, yLevel, j - negZRadius);
                        }
                    }
                }
            }
        }

        return new Int3(0, 0, 0);
    }

    public Int3 findEmptySpaceFromDirection(ForgeDirection dir, int amount)
    {
        int index = 0;
        if (dir == ForgeDirection.NORTH)
        {
        	if(printDebug)
            System.out.print("NORTH!");
            for (int i = 0; i <= negZRadius + posZRadius; i++)
            {
                for (int j = 0; j <= negXRadius + posXRadius; j++)
                {
                    GridSpace space = area[j][i];
                    if (space.isEmpty())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(j - negXRadius, space.getYLevel(), i - negZRadius);
                        }
                    }
                }
            }
        } else if (dir == ForgeDirection.SOUTH)
        {
            for (int i = negZRadius + posZRadius; i >= 0; i--)
            {
                for (int j = 0; j <= negXRadius + posXRadius; j++)
                {
                    GridSpace space = area[j][i];
                    if (space.isEmpty())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(j - negXRadius, space.getYLevel(), i - negZRadius);
                        }
                    }
                }
            }
        } else if (dir == ForgeDirection.EAST)
        {
            for (int i = negXRadius + posXRadius; i >= 0; i--)
            {
                for (int j = 0; j <= negZRadius + posZRadius; j++)
                {
                    GridSpace space = area[i][j];
                    if (space.isEmpty())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(i - negXRadius, space.getYLevel(), j - negZRadius);
                        }
                    }
                }
            }
        } else if (dir == ForgeDirection.WEST)
        {
            for (int i = 0; i <= negXRadius + posXRadius; i++)
            {
                for (int j = 0; j <= negZRadius + posZRadius; j++)
                {
                    GridSpace space = area[i][j];
                    if (space.isEmpty())
                    {
                        index++;
                        if (index >= amount)
                        {
                            return new Int3(i - negXRadius, space.getYLevel(), j - negZRadius);
                        }
                    }
                }
            }
        }

        return new Int3(0, 0, 0);
    }

    public int createGriddedRoad(int gridXi, int yi, int gridZi, ForgeDirection dir, int gridLength, boolean convertStarter) //Total grid length
    {
        if (gridLength == 0 || gridLength == 1)
        {
            return 0;
        }

        if (convertStarter)
        {

        }

        int initGridX = gridXi;
        int initGridZ = gridZi;
        int initY = yi;

        if (convertStarter)
        {
            this.setGridSpace(initGridX, initGridZ, new GridSpace(GridSpace.CROSSROAD, initY));

            DemonCrosspath crosspath = new DemonCrosspath(xCoord + initGridX * 5, initY, zCoord + initGridZ * 5);
            crosspath.createCrosspath(worldObj);
        }

        for (int index = 0; index < gridLength - 1; index++)
        {
            DemonVillagePath path = new DemonVillagePath(xCoord + initGridX * 5, initY, zCoord + initGridZ * 5, dir, 6);

            Int3AndBool temp = path.constructFullPath(this, worldObj, this.getRoadStepClearance());
            Int3 next = temp.coords;

            if (next != null)
            {
                initY = next.yCoord;
                if(printDebug)
                AlchemicalWizardry.logger.info("" + initY);
            }
            
            if(!temp.bool)
            {
            	return index;
            }

            initGridX += dir.offsetX;
            initGridZ += dir.offsetZ;

            if (!this.getGridSpace(initGridX, initGridZ).isRoadSegment())
            {
                this.setGridSpace(initGridX, initGridZ, new GridSpace(GridSpace.ROAD, initY));
            }
        }
        
        return gridLength - 1;
    }

    public void expandAreaInNegX()
    {
        GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 2][negZRadius + posZRadius + 1];
        for (int i = 0; i <= negZRadius + posZRadius; i++)
        {
            newGrid[0][i] = new GridSpace();
        }

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            for (int j = 0; j <= negZRadius + posZRadius; j++)
            {
                newGrid[i + 1][j] = area[i][j];
            }
        }

        area = newGrid;
        negXRadius += 1;
    }

    public void expandAreaInPosX()
    {
        GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 2][negZRadius + posZRadius + 1];

        for (int i = 0; i <= negZRadius + posZRadius; i++)
        {
            newGrid[negXRadius + posXRadius + 1][i] = new GridSpace();
        }

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            for (int j = 0; j <= negZRadius + posZRadius; j++)
            {
                newGrid[i][j] = area[i][j];
            }
        }

        area = newGrid;
        posXRadius += 1;
    }

    public void expandAreaInNegZ()
    {
        GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 1][negZRadius + posZRadius + 2];

        if(printDebug)
        AlchemicalWizardry.logger.info("x " + newGrid.length + "z " + newGrid[0].length);

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            newGrid[i][0] = new GridSpace();
        }

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            for (int j = 0; j <= negZRadius + posZRadius; j++)
            {
                newGrid[i][j + 1] = area[i][j];
            }
        }

        area = newGrid;
        negZRadius += 1;
    }

    public void expandAreaInPosZ()
    {
        GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 1][negZRadius + posZRadius + 2];

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            newGrid[i][negZRadius + posZRadius + 1] = new GridSpace();
        }

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            for (int j = 0; j <= negZRadius + posZRadius; j++)
            {
                newGrid[i][j] = area[i][j];
            }
        }

        area = newGrid;
        posZRadius += 1;
    }

    public GridSpace getGridSpace(int x, int z)
    {
        if (x > posXRadius || x < -negXRadius || z > posZRadius || z < -negZRadius)
        {
            return new GridSpace();
        } else
        {
            return (area[x + negXRadius][z + negZRadius]);
        }
    }

    public void setGridSpace(int x, int z, GridSpace space)
    {
        if (x > posXRadius)
        {
            this.expandAreaInPosX();
            this.setGridSpace(x, z, space);
            return;
        } else if (x < -negXRadius)
        {
            this.expandAreaInNegX();
            this.setGridSpace(x, z, space);
            return;
        } else if (z > posZRadius)
        {
            this.expandAreaInPosZ();
            this.setGridSpace(x, z, space);
            return;
        } else if (z < -negZRadius)
        {
            this.expandAreaInNegZ();
            this.setGridSpace(x, z, space);
            return;
        } else
        {
            area[x + negXRadius][z + negZRadius] = space;
        }
    }

    public void rightClickBlock(EntityPlayer player, int side)
    {
        if (worldObj.isRemote)
        {
            return;
        }

        this.initialize();

        if (ForgeDirection.getOrientation(side) == ForgeDirection.UP)
        {
            this.createRandomBuilding(DemonBuilding.BUILDING_HOUSE, 0);
        } else if (ForgeDirection.getOrientation(side) == ForgeDirection.DOWN)
        {
            this.createRandomBuilding(DemonBuilding.BUILDING_PORTAL, 0);
        } else
        {
            this.createRandomRoad();
        }
    }

    public int createRandomBuilding(int type, int tier)
    {
        switch (type)
        {
            case DemonBuilding.BUILDING_HOUSE:
                return this.createRandomHouse(tier);
            case DemonBuilding.BUILDING_PORTAL:
                return this.createPortalBuilding(tier);
        }

        return 0;
    }

    public int createPortalBuilding(int buildingTier) //TODO Telepose block next time, then build the new building.
    {
    	if(printDebug)
    	AlchemicalWizardry.logger.info("Hello, I am here!");
        int x = 0;
        int z = 0;

        GridSpace home = this.getGridSpace(x, z);
        int yLevel = home.getYLevel();

        GridSpaceHolder grid = this.createGSH();

        List<ForgeDirection> directions = new ArrayList();

        for (int i = 2; i < 6; i++)
        {
            ForgeDirection testDir = ForgeDirection.getOrientation(i);
            directions.add(testDir);
        }

        if (directions.isEmpty())
        {
            return 0;
        }

        HashMap<ForgeDirection, List<DemonBuilding>> schemMap = new HashMap();

        for (ForgeDirection nextDir : directions)
        {
            for (DemonBuilding build : TEDemonPortal.buildingList)
            {
                if (build.buildingType != DemonBuilding.BUILDING_PORTAL || build.buildingTier != buildingTier)
                {
                    continue;
                }
                System.out.println("This one matches!");
                if (schemMap.containsKey(nextDir))
                {
                    schemMap.get(nextDir).add(build);
                } else
                {
                    schemMap.put(nextDir, new ArrayList());
                    schemMap.get(nextDir).add(build);
                }
            }
        }

        if (schemMap.keySet().isEmpty())
        {
            return 0;
        }

        ForgeDirection chosenDirection = (ForgeDirection) schemMap.keySet().toArray()[new Random().nextInt(schemMap.keySet().size())];
        DemonBuilding build = schemMap.get(chosenDirection).get(new Random().nextInt(schemMap.get(chosenDirection).size()));
        Int3 portalSpace = build.getDoorSpace(chosenDirection);

        
        this.nextDemonPortalDirection = chosenDirection;
        this.nextDemonPortalName = build.getName();

//        build.destroyAllInField(worldObj, xCoord + (x) * 5, yLevel, zCoord + (z) * 5, chosenDirection.getOpposite());
//        
//        int yOffset = portalSpace.yCoord;
//        build.buildAll(worldObj, xCoord + (x) * 5, yLevel, zCoord + (z) * 5, chosenDirection.getOpposite());
//        build.setAllGridSpaces(x, z, yLevel, chosenDirection.getOpposite(), GridSpace.MAIN_PORTAL, grid);
//        this.loadGSH(grid);

        return build.getNumberOfGridSpaces();
    }
    
    /**
     * The Stage is at what point the portal is in reacting to the creation of the Demon Portal. 
     * Stage == 0 means just the saving
     * Stage == 1 means to telepose the portal
     * Stage == 2 means the teleposition is complete and that the building may be constructed
     */
    public void createPortalBuilding(int stage, String name, int tier)
    {
    	for(DemonBuilding build : TEDemonPortal.buildingList)
    	{
    		if(build.buildingType != DemonBuilding.BUILDING_PORTAL || build.buildingTier != tier)
    		{
    			continue;
    		}
    		
    		if(build.getName().equals(this.nextDemonPortalName))
    		{
    			int x = 0;
    	        int z = 0;

    	        GridSpace home = this.getGridSpace(x, z);
    	        int yLevel = home.getYLevel();

    	        GridSpaceHolder grid = this.createGSH();
    	        
    	        ForgeDirection chosenDirection = this.nextDemonPortalDirection;
				Int3 portalSpace = build.getDoorSpace(chosenDirection);
				int yOffset = portalSpace.yCoord;
    	        
    			switch(stage)
    			{
    			case 0:
    				
    				break;
    				
    			case 1:
    				int yDestination = yLevel + yOffset;
    				if(yCoord != yDestination)
    				{
    					BlockTeleposer.swapBlocks(worldObj, worldObj, xCoord, yCoord, zCoord, xCoord, yDestination, zCoord);
    				}else
    				{
    					//Nuthin - just as a reminder that we can now increment properly
    				}
    				break;
    				
    			case 2:
    				build.destroyAllInField(worldObj, xCoord + (x) * 5, yLevel, zCoord + (z) * 5, chosenDirection.getOpposite());
    		        
    		        build.buildAll(this, worldObj, xCoord + (x) * 5, yLevel, zCoord + (z) * 5, chosenDirection.getOpposite(), true);
    		        build.setAllGridSpaces(x, z, yLevel, chosenDirection.getOpposite(), GridSpace.MAIN_PORTAL, grid);
    		        this.loadGSH(grid);
    				break;
    			}
    			
    			return;
    		}
    	}
    }

    public int createRandomHouse(int buildingTier)
    {
        int next = rand.nextInt(4);
        ForgeDirection dir;

        switch (next)
        {
            case 0:
                dir = ForgeDirection.NORTH;
                break;
            case 1:
                dir = ForgeDirection.SOUTH;
                break;
            case 2:
                dir = ForgeDirection.EAST;
                break;
            case 3:
                dir = ForgeDirection.WEST;
                break;
            default:
                dir = ForgeDirection.NORTH;
        }

        Int3 space = this.findRoadSpaceFromDirection(dir, 1 * (rand.nextInt(negXRadius + negZRadius + posXRadius + posZRadius)) + 1);

        int x = space.xCoord;
        int z = space.zCoord;
        int yLevel = space.yCoord;

        if(printDebug)
        AlchemicalWizardry.logger.info("Road space - x: " + x + " z: " + z);

        GridSpaceHolder grid = this.createGSH();

        if (!this.getGridSpace(x, z).isRoadSegment())
        {
            return 0;
        }

        List<ForgeDirection> directions = new ArrayList();

        for (int i = 2; i < 6; i++)
        {
            ForgeDirection testDir = ForgeDirection.getOrientation(i);
            if (this.getGridSpace(x + testDir.offsetX, z + testDir.offsetZ).isEmpty())
            {
                directions.add(testDir);
            }
        }

        if (directions.isEmpty())
        {
            return 0;
        }

        HashMap<ForgeDirection, List<DemonBuilding>> schemMap = new HashMap();

        for (ForgeDirection nextDir : directions)
        {
            for (DemonBuilding build : TEDemonPortal.buildingList)
            {
                if (build.buildingTier != buildingTier || build.buildingType != DemonBuilding.BUILDING_HOUSE)
                {
                    continue;
                }
                Int3 offsetSpace = build.getGridOffsetFromRoad(nextDir, yLevel);
                int xOff = offsetSpace.xCoord;
                int zOff = offsetSpace.zCoord;

                if (build.isValid(grid, x + xOff, z + zOff, nextDir.getOpposite()))
                {
                    if (schemMap.containsKey(nextDir))
                    {
                        schemMap.get(nextDir).add(build);
                    } else
                    {
                        schemMap.put(nextDir, new ArrayList());
                        schemMap.get(nextDir).add(build);
                    }
                } else
                {
                	if(printDebug)
                    AlchemicalWizardry.logger.info("This ISN'T valid!");
                }
            }
        }

        if (schemMap.keySet().isEmpty())
        {
            return 0;
        }

        ForgeDirection chosenDirection = (ForgeDirection) schemMap.keySet().toArray()[new Random().nextInt(schemMap.keySet().size())];
        DemonBuilding build = schemMap.get(chosenDirection).get(new Random().nextInt(schemMap.get(chosenDirection).size()));

        Int3 offsetSpace = build.getGridOffsetFromRoad(chosenDirection, yLevel);
        int xOff = offsetSpace.xCoord;
        int zOff = offsetSpace.zCoord;

        build.destroyAllInField(worldObj, xCoord + (x + xOff) * 5, yLevel, zCoord + (z + zOff) * 5, chosenDirection.getOpposite());
        build.buildAll(this, worldObj, xCoord + (x + xOff) * 5, yLevel, zCoord + (z + zOff) * 5, chosenDirection.getOpposite(), true);
        build.setAllGridSpaces(x + xOff, z + zOff, yLevel, chosenDirection.getOpposite(), GridSpace.HOUSE, grid);
        this.loadGSH(grid);
        
        DemonVillagePath path = new DemonVillagePath(xCoord + (x) * 5, yLevel, zCoord + (z) * 5, chosenDirection, 2);

        Int3AndBool temp = path.constructFullPath(this, worldObj, this.getRoadStepClearance());

        return build.getNumberOfGridSpaces();
    }

    public int findNearestRoadYLevel(int xCoord, int zCoord, int maxDistance)
    {
        for (int l = 1; l <= maxDistance; l++)
        {
            for (int i = -l; i <= l; i++)
            {
                for (int j = -l; j <= l; j++)
                {
                    if (Math.abs(i) != l && Math.abs(j) != l)
                    {
                        continue;
                    }

                    if (this.getGridSpace(xCoord + i, zCoord + j).isRoadSegment())
                    {
                        return this.getGridSpace(xCoord + i, zCoord + j).getYLevel();
                    }
                }
            }
        }

        return -1;
    }

    public void createRoad(int xi, int yi, int zi, ForgeDirection dir, int length, boolean doesNotDrop)
    {
        int curX = xi;
        int curY = yi;
        int curZ = zi;
        int roadRadius = this.getRoadRadius();

        if (dir.offsetY != 0)
        {
            return;
        }

        DemonVillagePath path = new DemonVillagePath(xi, yi, zi, dir, length);

        path.constructFullPath(this, worldObj, this.getRoadStepClearance());
    }

    public int placeMaterialOnNextAvailable()
    {
        return 0;
    }

    public int getRoadRadius()
    {
        return 1;
    }

    public Block getRoadBlock()
    {
    	switch(this.tier)
    	{
    	case 0:
    		return rand.nextFloat() < 0.6 ? Blocks.cobblestone : Blocks.mossy_cobblestone;
    	case 1:
    		return Blocks.stonebrick;
    	default:
    		return Blocks.nether_brick;
    	}
    }

    public int getRoadMeta()
    {
    	switch(this.tier)
    	{
    	case 1:
    		return rand.nextFloat() < 0.6 ? 1 : 0;
    	}
        return 0;
    }

    public int getRoadStepClearance()
    {
        return 10;
    }

    public int getRoadSpacer()
    {
        return 1;
    }

    public GridSpaceHolder createGSH()
    {
        GridSpaceHolder grid = new GridSpaceHolder();
        grid.area = this.area;
        grid.negXRadius = this.negXRadius;
        grid.negZRadius = this.negZRadius;
        grid.posXRadius = this.posXRadius;
        grid.posZRadius = this.posZRadius;

        return grid;
    }

    public void loadGSH(GridSpaceHolder grid)
    {
        this.area = grid.area;
        this.negXRadius = grid.negXRadius;
        this.negZRadius = grid.negZRadius;
        this.posXRadius = grid.posXRadius;
        this.posZRadius = grid.posZRadius;
    }

    public static void loadBuildingList()
    {
        String folder = "config/BloodMagic/schematics";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        File file = new File(folder);
        File[] files = file.listFiles();
        BufferedReader br;

        try
        {
            for (File f : files)
            {
                br = new BufferedReader(new FileReader(f));
                BuildingSchematic schema = gson.fromJson(br, BuildingSchematic.class);
                TEDemonPortal.buildingList.add(new DemonBuilding(schema));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void addToPoints(int addition)
    {
        this.demonHouseCooldown += addition;
    }

	public void notifyPortalOfBreak() 
	{
		for(IHoardDemon demon : hoardList)
		{
			if(demon instanceof Entity)
			{
				((Entity) demon).setDead();
			}
		}
	}
}
