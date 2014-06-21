package WayofTime.alchemicalWizardry.common.tileEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.BuildingSchematic;
import WayofTime.alchemicalWizardry.common.demonVillage.DemonCrosspath;
import WayofTime.alchemicalWizardry.common.demonVillage.DemonVillagePath;
import WayofTime.alchemicalWizardry.common.demonVillage.GridSpace;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TEDemonPortal extends TileEntity
{
	public static List<BuildingSchematic> buildingList = new ArrayList();
	public Random rand = new Random();
	private GridSpace[][] area;
	
	private int negXRadius; //These variables indicate how much the grid has expanded from the 1x1
	private int posXRadius; //matrix in each direction
	private int negZRadius;
	private int posZRadius;
	
	private boolean isInitialized;
	
	public TEDemonPortal()
	{
		super();
		
		negXRadius = posXRadius = negZRadius = posZRadius = 1;
		
		area = new GridSpace[negXRadius + posXRadius + 1][negZRadius + posZRadius + 1];
		for(int xIndex = -negXRadius; xIndex <= posXRadius; xIndex++)
		{
			for(int zIndex = -negZRadius; zIndex <= posZRadius; zIndex++)
			{
				if(Math.abs(xIndex) == 1 || Math.abs(zIndex) == 1)
				{
					this.setGridSpace(xIndex, zIndex, new GridSpace(GridSpace.ROAD,4));
				}else
				{
					this.setGridSpace(xIndex, zIndex, new GridSpace());
				}
			}
		}
		
		isInitialized = false;
		
		this.setGridSpace(0, 0, new GridSpace(GridSpace.MAIN_PORTAL, yCoord));
	}
	
	public void initialize()
	{
		if(isInitialized)
		{
			return;
		}
		
		for(int xIndex = -negXRadius; xIndex <= posXRadius; xIndex++)
		{
			for(int zIndex = -negZRadius; zIndex <= posZRadius; zIndex++)
			{
				if(Math.abs(xIndex) == 1 || Math.abs(zIndex) == 1)
				{
					this.setGridSpace(xIndex, zIndex, new GridSpace(GridSpace.ROAD,yCoord));
				}else
				{
					this.setGridSpace(xIndex, zIndex, new GridSpace());
				}
			}
		}
		
		isInitialized = true;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.negXRadius = par1NBTTagCompound.getInteger("negXRadius");
        this.negZRadius = par1NBTTagCompound.getInteger("negZRadius");
        this.posXRadius = par1NBTTagCompound.getInteger("posXRadius");
        this.posZRadius = par1NBTTagCompound.getInteger("posZRadius");
        
        area = new GridSpace[negXRadius + posXRadius + 1][negZRadius + posZRadius + 1];
        
        NBTTagList tagList = par1NBTTagCompound.getTagList("Grid",Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tagList.tagCount(); i++)
        {
        	int length = (negZRadius+posZRadius+1);
        	
        	int x = i/length;
        	int z = i%length;
        	
            NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
            GridSpace space = GridSpace.getGridFromTag(tag);
            
            area[x][z] = space;
        }
        
        this.isInitialized = par1NBTTagCompound.getBoolean("init");
    }
	
	@Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("negXRadius", negXRadius);
        par1NBTTagCompound.setInteger("negZRadius", negZRadius);
        par1NBTTagCompound.setInteger("posXRadius", posXRadius);
        par1NBTTagCompound.setInteger("posZRadius", posZRadius);

        NBTTagList gridList = new NBTTagList();

        for(int i=0; i<=negXRadius+posXRadius; i++)
        {
        	for(int j=0; j<=negZRadius+posZRadius; j++)
        	{
        		int index = i + (negZRadius+posZRadius+1)*j;
        		
        		GridSpace space = area[i][j];
        		NBTTagCompound nextTag;
        		
        		if(space == null)
        		{
        			nextTag = new GridSpace().getTag();
        		}else
        		{
        			nextTag = space.getTag();
        		}
        		
        		gridList.appendTag(nextTag);
        	}
        }

        par1NBTTagCompound.setTag("Grid", gridList);
        
        par1NBTTagCompound.setBoolean("init", isInitialized);
    }
	
	public void createRandomRoad()
	{
		int next = rand.nextInt(4);
		ForgeDirection dir;
		
		switch(next)
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
		
		Int3 road = findRoadSpaceFromDirection(dir, (rand.nextInt(negXRadius + negZRadius + posXRadius + posZRadius))+1);
		
		int x = road.xCoord;
		int yLevel = road.yCoord;
		int z = road.zCoord;
		
		System.out.println("X: " + x + " Z: " + z + " Direction: " + dir.toString());
		
		List<ForgeDirection> directions = this.findValidExtentionDirection(x, z);
		
		if(directions.size() <= 0)
		{
			return;
		}
		
		int maxDistance = 5;
		
		int distance = 0;
		ForgeDirection dominantDirection = null;
		
		for(ForgeDirection direction: directions)
		{
			int amt = this.getLength(direction, maxDistance, x, z);
			if(amt > distance)
			{
				distance = amt;
				dominantDirection = direction;
			}else if(amt == distance && rand.nextBoolean())
			{
				dominantDirection = direction;
			}
		}
		
		if(dominantDirection == null)
		{
			return;
		}
		System.out.println("I got here!");
		System.out.println("Distance: " + distance + " Direction: " + dominantDirection.toString() + " yLevel: " + yLevel);
		
		this.createGriddedRoad(x, yLevel, z, dominantDirection, distance+1, true);
	}
	
	public List<ForgeDirection> findValidExtentionDirection(int x, int z)
	{
		List<ForgeDirection> directions = new LinkedList();
		
		if(this.getGridSpace(x, z) == null || !this.getGridSpace(x, z).isRoadSegment())
		{
			return directions;
		}
		
		GridSpace nextGrid = this.getGridSpace(x+1, z);
		if(nextGrid.isEmpty())
		{
			directions.add(ForgeDirection.EAST);
		}
		
		nextGrid = this.getGridSpace(x-1, z);
		if(nextGrid.isEmpty())
		{
			directions.add(ForgeDirection.WEST);
		}
		
		nextGrid = this.getGridSpace(x, z+1);
		if(nextGrid.isEmpty())
		{
			directions.add(ForgeDirection.SOUTH);
		}
		
		nextGrid = this.getGridSpace(x, z-1);
		if(nextGrid.isEmpty())
		{
			directions.add(ForgeDirection.NORTH);
		}
		
		return directions;
	}
	
	public int getLength(ForgeDirection dir, int maxLength, int x, int z) //Number of spaces forward
	{
		for(int i=1; i<=maxLength; i++)
		{
			GridSpace space = this.getGridSpace(x + i*dir.offsetX, z + i*dir.offsetZ);
			if(space.isEmpty())
			{
				for(int k=1; k<=this.getRoadSpacer(); k++)
				{
					GridSpace space1 = this.getGridSpace(x + i*dir.offsetX + dir.offsetZ*k, z + i*dir.offsetZ + dir.offsetX*k);
					GridSpace space2 = this.getGridSpace(x + i*dir.offsetX - dir.offsetZ*k, z + i*dir.offsetZ - dir.offsetX*k);

					if(space1.isRoadSegment() || space2.isRoadSegment())
					{
						return i-1;
					}
				}
				
				continue;
			}
			if(space.isRoadSegment())
			{
				return i;
			}else
			{
				return i-1;
			}
		}
		return maxLength;
	}
	
	public Int3 findRoadSpaceFromDirection(ForgeDirection dir, int amount)
	{
		int index = 0;
		if(dir == ForgeDirection.NORTH)
		{
			System.out.print("NORTH!");
			for(int i=0; i<= negZRadius + posZRadius; i++)
			{
				for(int j=0; j<= negXRadius + posXRadius; j++)
				{
					GridSpace space = area[j][i];
					if(space.isRoadSegment())
					{
						index++;
						if(index >= amount)
						{
							return new Int3(j-negXRadius,space.getYLevel(),i-negZRadius);
						}
					}
				}
			}
		}else if(dir == ForgeDirection.SOUTH)
		{
			for(int i=negZRadius + posZRadius; i >= 0 ; i--)
			{
				for(int j=0; j<= negXRadius + posXRadius; j++)
				{
					GridSpace space = area[j][i];
					if(space.isRoadSegment())
					{
						index++;
						if(index >= amount)
						{
							return new Int3(j-negXRadius,space.getYLevel(),i-negZRadius);
						}
					}
				}
			}
		}else if(dir == ForgeDirection.EAST)
		{				
			for(int i=negXRadius + posXRadius; i >= 0; i--)
			{
				for(int j=0; j <= negZRadius + posZRadius ; j++)
				{
					GridSpace space = area[i][j];
					if(space.isRoadSegment())
					{
						index++;
						if(index >= amount)
						{
							return new Int3(i-negXRadius,space.getYLevel(),j-negZRadius);
						}
					}
				}
			}
		}else if(dir == ForgeDirection.WEST)
		{
			for(int i=0; i <= negXRadius + posXRadius; i++)
			{
				for(int j=0; j <= negZRadius + posZRadius ; j++)
				{
					GridSpace space = area[i][j];
					if(space.isRoadSegment())
					{
						index++;
						if(index >= amount)
						{
							return new Int3(i-negXRadius,space.getYLevel(),j-negZRadius);
						}
					}
				}
			}
		}
		
		return new Int3(0,0,0);
	}
	
	public void createGriddedRoad(int gridXi, int yi, int gridZi, ForgeDirection dir, int gridLength, boolean convertStarter) //Total grid length
	{	
		if(gridLength == 0 || gridLength == 1)
		{
			return;
		}
		
		if(convertStarter)
		{
			
		}
		
		int initGridX = gridXi;
		int initGridZ = gridZi;
		int initY = yi;
		
		if(convertStarter)
		{
			this.setGridSpace(initGridX, initGridZ, new GridSpace(GridSpace.CROSSROAD,initY));
			
			DemonCrosspath crosspath = new DemonCrosspath(xCoord + initGridX*5, initY, zCoord + initGridZ*5);
			crosspath.createCrosspath(worldObj);
		}
		
		for(int index=0; index<gridLength-1; index++)
		{
			DemonVillagePath path = new DemonVillagePath(xCoord + initGridX*5, initY, zCoord + initGridZ*5, dir, 6);
			
			Int3 next = path.constructFullPath(worldObj, this.getRoadStepClearance(), this.getRoadBlock(), this.getRoadMeta());
			
			if(next != null)
			{
				initY = next.yCoord;
			}
			
			initGridX += dir.offsetX;
			initGridZ += dir.offsetZ;
			
			if(!this.getGridSpace(initGridX, initGridZ).isRoadSegment())
			{
				this.setGridSpace(initGridX, initGridZ, new GridSpace(GridSpace.ROAD,initY));
			}	
		}
	}
	
	public void expandAreaInNegX()
	{
		GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 2][negZRadius + posZRadius + 1];
		for(int i=0; i<=negZRadius + posZRadius; i++)
		{
			newGrid[0][i] = new GridSpace();
		}
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			for(int j=0; j<=negZRadius + posZRadius; j++)
			{
				newGrid[i+1][j] = area[i][j];
			}
		}
		
		area = newGrid;
		negXRadius += 1;
	}
	
	public void expandAreaInPosX()
	{
		GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 2][negZRadius + posZRadius + 1];
		
		for(int i=0; i<=negZRadius + posZRadius; i++)
		{
			newGrid[negXRadius + posXRadius + 1][i] = new GridSpace();
		}
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			for(int j=0; j<=negZRadius + posZRadius; j++)
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
		
		System.out.println("x " + newGrid.length + "z " + newGrid[0].length);
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			newGrid[i][0] = new GridSpace();
		}
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			for(int j=0; j<=negZRadius + posZRadius; j++)
			{
				newGrid[i][j+1] = area[i][j];
			}
		}
		
		area = newGrid;
		negZRadius += 1;
	}
	
	public void expandAreaInPosZ()
	{
		GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 1][negZRadius + posZRadius + 2];
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			newGrid[i][negZRadius + posZRadius + 1] = new GridSpace();
		}
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			for(int j=0; j<=negZRadius + posZRadius; j++)
			{
				newGrid[i][j] = area[i][j];
			}
		}
		
		area = newGrid;
		posZRadius += 1;
	}
	
	public GridSpace getGridSpace(int x, int z)
	{
		if(x > posXRadius|| x < -negXRadius || z > posZRadius || z < -negZRadius)
		{
			return new GridSpace();
		}else
		{
			return (area[x + negXRadius][z + negZRadius]);
		}
	}
	
	public void setGridSpace(int x, int z, GridSpace space)
	{
		if(x > posXRadius)
		{
			this.expandAreaInPosX();
			this.setGridSpace(x, z, space);
			return;
		}else if(x < -negXRadius)
		{
			this.expandAreaInNegX();
			this.setGridSpace(x, z, space);
			return;
		}else if(z > posZRadius)
		{
			this.expandAreaInPosZ();
			this.setGridSpace(x, z, space);
			return;
		}else if(z < -negZRadius)
		{
			this.expandAreaInNegZ();
			this.setGridSpace(x, z, space);
			return;
		}else
		{
			area[x + negXRadius][z + negZRadius] = space;
		}
	}
	
	public void rightClickBlock(EntityPlayer player, int side)
	{
		this.testGson();
		Int3 roadMarker = this.getNextRoadMarker();
		
		this.initialize();
		
		this.createRandomRoad();
	}
	
	public void testGson()
	{
		boolean write = true;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(new BuildingSchematic());
		try {
			if(write)
			{
				Writer writer = new FileWriter("config/test3.json");
				writer.write(json);
				writer.close();
			}
			{
				BufferedReader br = new BufferedReader(new FileReader("config/test3.json"));
				BuildingSchematic schema = gson.fromJson(br, BuildingSchematic.class);
				String newJson = gson.toJson(schema);
				Writer writer = new FileWriter("config/test4.json");
				writer.write(newJson);
				writer.close();
			}
			{
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void createRoad(int xi, int yi, int zi, ForgeDirection dir, int length, boolean doesNotDrop)
	{
		int curX = xi;
		int curY = yi;
		int curZ = zi;
		int roadRadius = this.getRoadRadius();
		
		if(dir.offsetY != 0)
		{
			return;
		}
		
		DemonVillagePath path = new DemonVillagePath(xi, yi, zi, dir, length);
		
		path.constructFullPath(worldObj, this.getRoadStepClearance(), this.getRoadBlock(), this.getRoadMeta());
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
		return Blocks.nether_brick;
	}
	
	public int getRoadMeta()
	{
		return 0;
	}
	
	public int getRoadStepClearance()
	{
		return 10;
	}
	
	public Block getRoadMarker()
	{
		return ModBlocks.ritualStone;
	}
	
	public Int3 getNextRoadMarker()
	{
		int horizSearchMax = 25;
		int vertSearchMax = 10;
		
		for(int xPos=xCoord-horizSearchMax; xPos<=xCoord+horizSearchMax; xPos++)
		{
			for(int zPos=zCoord-horizSearchMax; zPos<=zCoord+horizSearchMax; zPos++)
			{
				for(int yPos=yCoord-vertSearchMax; yPos<=yCoord+vertSearchMax; yPos++)
				{
					Block block = worldObj.getBlock(xPos, yPos, zPos);
					if(block == this.getRoadMarker())
					{
						return new Int3(xPos,yPos,zPos);
					}
				}
			}
		}
		
		return null;
	}
	
	public int getRoadSpacer()
	{
		return 1;
	}
}
