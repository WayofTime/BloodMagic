package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class TEMasterStone extends TileEntity implements IMasterRitualStone
{
    private String currentRitualString;
    private boolean isActive;
    private String owner;
    private String varString1;
    private int cooldown;
    private int var1;
    private int direction;
    public boolean isRunning;
    public int runningTime;

    public TEMasterStone()
    {
        isActive = false;
        owner = "";
        cooldown = 0;
        var1 = 0;
        direction = 0;
        varString1 = "";
        currentRitualString = "";
        isRunning = false;
        runningTime = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        isActive = par1NBTTagCompound.getBoolean("isActive");
        owner = par1NBTTagCompound.getString("owner");
        cooldown = par1NBTTagCompound.getInteger("cooldown");
        var1 = par1NBTTagCompound.getInteger("var1");
        direction = par1NBTTagCompound.getInteger("direction");
        currentRitualString = par1NBTTagCompound.getString("currentRitualString");
        isRunning = par1NBTTagCompound.getBoolean("isRunning");
        runningTime = par1NBTTagCompound.getInteger("runningTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("isActive", isActive);
        par1NBTTagCompound.setString("owner", owner);
        par1NBTTagCompound.setInteger("cooldown", cooldown);
        par1NBTTagCompound.setInteger("var1", var1);
        par1NBTTagCompound.setInteger("direction", direction);
        par1NBTTagCompound.setString("currentRitualString", currentRitualString);
        par1NBTTagCompound.setBoolean("isRunning", isRunning);
        par1NBTTagCompound.setInteger("runningTime",runningTime);
    }

    public void activateRitual(World world, int crystalLevel, EntityPlayer player)
    {
    	if (world.isRemote)
        {
            return;
        }
    	
        String testRitual = Rituals.checkValidRitual(world, xCoord, yCoord, zCoord);

        if (testRitual.equals(""))
        {
        	player.addChatMessage(new ChatComponentText("Nothing appears to have happened..."));
            return;
        }

        boolean testLevel = Rituals.canCrystalActivate(testRitual, crystalLevel);

        if (!testLevel)
        {
        	player.addChatMessage(new ChatComponentText("Your crystal vibrates pathetically."));

            return;
        }

        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;

        if (currentEssence < Rituals.getCostForActivation(testRitual))
        {
        	player.addChatMessage(new ChatComponentText("You feel a pull, but you are too weak to push any further."));

            return;
        }

        if (!world.isRemote)
        {
            data.currentEssence = currentEssence - Rituals.getCostForActivation(testRitual);
            data.markDirty();
            
        	player.addChatMessage(new ChatComponentText("A rush of energy flows through the ritual!"));

            for (int i = 0; i < 12; i++)
            {
                SpellHelper.sendIndexedParticleToAllAround(world, xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, 1, xCoord, yCoord, zCoord);
            }
        }

        cooldown = Rituals.getInitialCooldown(testRitual);
        var1 = 0;
        currentRitualString = testRitual;
        isActive = true;
        isRunning = true;
        direction = Rituals.getDirectionOfRitual(world, xCoord, yCoord, zCoord, testRitual);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    @Override
    public void updateEntity()
    {
    	if(isRunning && runningTime < 100)
    	{
    		runningTime++;
    	}else if(!isRunning && runningTime > 0)
    	{
    		runningTime--;
    	}
    	
        if (!isActive)
        {
            return;
        }

        int worldTime = (int) (worldObj.getWorldTime() % 24000);

        if (worldObj.isRemote)
        {
            return;
        }

        if (worldTime % 100 == 0)
        {
            boolean testRunes = Rituals.checkDirectionOfRitualValid(worldObj, xCoord, yCoord, zCoord, currentRitualString, direction);
            SpellHelper.sendIndexedParticleToAllAround(worldObj, xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, 1, xCoord, yCoord, zCoord);

            if (!testRunes)
            {
                isActive = false;
                currentRitualString = "";
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return;
            }
        }

        if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0)
        {
        	if(isRunning)
        	{
        		isRunning = false;
        		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        	}
            return;
        }else
        {
        	if(!isRunning)
        	{
        		isRunning = true;
        		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        	}
        }

        performRitual(worldObj, xCoord, yCoord, zCoord, currentRitualString);
    }

    public void performRitual(World world, int x, int y, int z, String currentRitualString2)
    {
        Rituals.performEffect(this, currentRitualString2);
    }

    public String getOwner()
    {
        return owner;
    }

    public void setCooldown(int newCooldown)
    {
        this.cooldown = newCooldown;
    }

    public int getCooldown()
    {
        return this.cooldown;
    }

    public void setVar1(int newVar1)
    {
        this.var1 = newVar1;
    }

    public int getVar1()
    {
        return this.var1;
    }

    public void setActive(boolean active)
    {
        this.isActive = active;
        this.isRunning = active;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public int getDirection()
    {
        return this.direction;
    }

	@Override
	public World getWorld() 
	{
		return this.getWorldObj();
	}

	@Override
	public int getXCoord() 
	{
		return xCoord;
	}

	@Override
	public int getYCoord() 
	{
		return yCoord;
	}

	@Override
	public int getZCoord() 
	{
		return zCoord;
	}
	
	public String getCurrentRitual()
	{
		return this.currentRitualString;
	}
	
	public void setCurrentRitual(String str)
	{
		this.currentRitualString = str;
	}
	
	@Override
    public Packet getDescriptionPacket()
    {
        return NewPacketHandler.getPacket(this);
    }
	
	public AxisAlignedBB getRenderBoundingBox()
	{
		double renderExtention = 1.0d;
		AxisAlignedBB bb = AxisAlignedBB. getBoundingBox(xCoord-renderExtention, yCoord-renderExtention, zCoord-renderExtention, xCoord+1+renderExtention, yCoord+1+renderExtention, zCoord+1+renderExtention);
		return bb;
	}
}