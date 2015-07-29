package WayofTime.alchemicalWizardry.api.rituals;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

/**
 * This class is used to pass ritual-specific data into the RitualEffect from the containing Master Ritual Stone. This is basically used as auxillarary storage,
 * for when simply storing to NBT becomes... difficult.
 *
 */
public class LocalRitualStorage 
{
	public BlockPos coords;
		
	public void writeToNBT(NBTTagCompound tag) 
	{
		tag.setInteger("xCoord", coords.getX());
		tag.setInteger("yCoord", coords.getY());
		tag.setInteger("zCoord", coords.getZ());
	}

	public void readFromNBT(NBTTagCompound tag) 
	{
		this.coords = new BlockPos(tag.getInteger("xCoord"), tag.getInteger("yCoord"), tag.getInteger("zCoord"));
	}
	
	public BlockPos getLocation()
	{
		return coords;
	}
	
	public void setLocation(BlockPos location)
	{
		this.coords = location;
	}
}
