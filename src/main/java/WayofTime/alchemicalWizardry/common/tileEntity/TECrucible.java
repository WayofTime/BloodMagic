package WayofTime.alchemicalWizardry.common.tileEntity;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.sacrifice.IIncense;

public class TECrucible extends TEInventory
{
	public float rColour;
	public float gColour;
	public float bColour;
	
	public int ticksRemaining = 0;
	
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
				
				stack.stackSize--;
				if(stack.stackSize <= 0)
				{
					this.setInventorySlotContents(0, null);
				}
			}
		}else
		{
			ticksRemaining--;
		}
		
	}
	
	public void spawnClientParticle(World world, int x, int y, int z, Random rand)
	{
        world.spawnParticle("reddust", x + 0.5D + rand.nextGaussian() / 8, y + 0.5D, z + 0.5D + rand.nextGaussian() / 8, rColour, gColour, bColour);
	}

	@Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        this.writeClientNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.readClientNBT(tag);
    }

    public void readClientNBT(NBTTagCompound tag)
    {
    	rColour = tag.getFloat("rColour");
    	gColour = tag.getFloat("gColour");
    	bColour = tag.getFloat("bColour");
    	
    	ticksRemaining = tag.getInteger("ticksRemaining");
    }

    public void writeClientNBT(NBTTagCompound tag)
    {
    	tag.setFloat("rColour", rColour);
    	tag.setFloat("gColour", gColour);
    	tag.setFloat("bColour", bColour);
    	
    	tag.setInteger("ticksRemaining", ticksRemaining);
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
}
