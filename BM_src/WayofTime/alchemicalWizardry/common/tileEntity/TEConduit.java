package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import WayofTime.alchemicalWizardry.common.PacketHandler;

public class TEConduit extends TEOrientable
{
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
	}

	//Logic for the actual block is under here
	@Override
	public void updateEntity()
	{
		//this.capacity=(int) (10000*this.capacityMultiplier);
		if (!worldObj.isRemote && worldObj.getWorldTime() % 20 == 0)
		{
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketHandler.getBlockOrientationPacket(this);
	}
}
