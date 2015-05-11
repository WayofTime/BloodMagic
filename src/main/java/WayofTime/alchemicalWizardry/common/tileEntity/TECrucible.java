package WayofTime.alchemicalWizardry.common.tileEntity;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.sacrifice.IIncense;
import WayofTime.alchemicalWizardry.api.sacrifice.PlayerSacrificeHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class TECrucible extends TEInventory
{
	public float rColour;
	public float gColour;
	public float bColour;
	
	public int ticksRemaining = 0;
	public int minValue = 0;
	public int maxValue = 0;
<<<<<<< HEAD
=======
	public float incrementValue = 0;
>>>>>>> origin/master
	
	public int state = 0; //0 is when it gives off gray particles, 1 is when it gives off white particles (player can't use this incense anymore), 2 is the normal colour of the incense, 3 means no particles (it is out)
	
	public TECrucible() 
	{
		super(1);
		float f = (float) 1.0F;
        float f1 = f * 0.6F + 0.4F;
        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;
        rColour = f1;
        gColour = f2;
        bColour = f3;
	}
	
	@Override
	public void updateEntity()
	{
		if(worldObj.isRemote)
			return;
		
		if(ticksRemaining <= 0)
		{
			ItemStack stack = this.getStackInSlot(0);
			if(stack != null && stack.getItem() instanceof IIncense)
			{
				IIncense incense = (IIncense)stack.getItem();
				
				rColour = incense.getRedColour(stack);
				gColour = incense.getGreenColour(stack);
				bColour = incense.getBlueColour(stack);
				ticksRemaining = incense.getIncenseDuration(stack);
				
				minValue = incense.getMinLevel(stack);
				maxValue = incense.getMaxLevel(stack);
				
<<<<<<< HEAD
=======
				incrementValue = incense.getTickRate(stack);
				
>>>>>>> origin/master
				stack.stackSize--;
				if(stack.stackSize <= 0)
				{
					this.setInventorySlotContents(0, null);
				}
			}
		}

		if(ticksRemaining > 0)
		{
			List<EntityPlayer> playerList = SpellHelper.getPlayersInRange(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 3, 3);
			
			if(playerList != null && !playerList.isEmpty())
			{
				boolean stateChanged = false;
				boolean allAreGood = true;
				
				for(EntityPlayer player : playerList)
				{
<<<<<<< HEAD
					if(ticksRemaining > 0 && PlayerSacrificeHandler.incrementIncense(player, minValue, maxValue))
=======
					if(ticksRemaining > 0 && PlayerSacrificeHandler.incrementIncense(player, minValue, maxValue, incrementValue))
>>>>>>> origin/master
					{
						ticksRemaining--;
						if(state != 2)
						{
							state = 2;
							stateChanged = true;
						}
						
						allAreGood = false;
					}
				}
				
				if(allAreGood && state != 1)
				{
					state = 1;
					stateChanged = true;
				}
				
				if(stateChanged)
				{
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}else
			{
				if(state != 0)
				{
					state = 0;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
		}else
		{
			if(state != 0)
			{
				state = 0;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
	}
	
	public void spawnClientParticle(World world, int x, int y, int z, Random rand)
	{
		switch(state)
		{
		case 0:
	        world.spawnParticle("reddust", x + 0.5D + rand.nextGaussian() / 8, y + 0.5D, z + 0.5D + rand.nextGaussian() / 8, 0.15, 0.15, 0.15);
			break;
			
		case 1:
	        world.spawnParticle("reddust", x + 0.5D + rand.nextGaussian() / 8, y + 0.5D, z + 0.5D + rand.nextGaussian() / 8, 0.9, 0.9, 0.9);
			break;
			
		case 2:
	        world.spawnParticle("reddust", x + 0.5D + rand.nextGaussian() / 8, y + 0.5D, z + 0.5D + rand.nextGaussian() / 8, rColour, gColour, bColour);
			break;
			
		case 3:
			//No particles - it is out
			break;
		}
	}

	@Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        
    	tag.setInteger("ticksRemaining", ticksRemaining);
    	tag.setInteger("minValue", minValue);
    	tag.setInteger("maxValue", maxValue);
        
        this.writeClientNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        
    	ticksRemaining = tag.getInteger("ticksRemaining");
    	minValue = tag.getInteger("minValue");
    	maxValue = tag.getInteger("maxValue");

        this.readClientNBT(tag);
    }

    public void writeClientNBT(NBTTagCompound tag)
    {
    	tag.setFloat("rColour", rColour);
    	tag.setFloat("gColour", gColour);
    	tag.setFloat("bColour", bColour);
    	tag.setInteger("state", state);
    }
    
    public void readClientNBT(NBTTagCompound tag)
    {
    	rColour = tag.getFloat("rColour");
    	gColour = tag.getFloat("gColour");
    	bColour = tag.getFloat("bColour");
    	state = tag.getInteger("state");
    }

    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeClientNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 90210, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        super.onDataPacket(net, packet);
        readClientNBT(packet.func_148857_g());        
    }

	@Override
	public String getInventoryName() 
	{
		return "TECrucible";
	}
	
	@Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return stack != null ? stack.getItem() instanceof IIncense : false;
    }
}
