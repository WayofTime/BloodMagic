package WayofTime.alchemicalWizardry.api.rituals;

import net.minecraft.nbt.NBTTagCompound;
import WayofTime.alchemicalWizardry.api.Int3;

/**
 * This class is used to pass ritual-specific data into the RitualEffect from the containing Master Ritual Stone. This is basically used as auxillarary storage,
 * for when simply storing to NBT becomes... difficult.
 *
 */
public class LocalRitualStorage 
{
	public int xCoord;
	public int yCoord;
	public int zCoord;
		
	public void writeToNBT(NBTTagCompound tag) 
	{
		tag.setInteger("xCoord", xCoord);
		tag.setInteger("yCoord", yCoord);
		tag.setInteger("zCoord", zCoord);
	}

	public void readFromNBT(NBTTagCompound tag) 
	{
		this.xCoord = tag.getInteger("xCoord");
		this.yCoord = tag.getInteger("yCoord");
		this.zCoord = tag.getInteger("zCoord");
	}
	
	public Int3 getLocation()
	{
		return new Int3(xCoord, yCoord, zCoord);
	}
	
	public void setLocation(Int3 location)
	{
		this.xCoord = location.xCoord;
		this.yCoord = location.yCoord;
		this.zCoord = location.zCoord;
	}
}
