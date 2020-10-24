package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.nbt.CompoundNBT;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public abstract class AlchemyArrayEffect
{
	public abstract AlchemyArrayEffect getNewCopy();

	public abstract void readFromNBT(CompoundNBT compound);

	public abstract void writeToNBT(CompoundNBT compound);

	public abstract boolean update(TileAlchemyArray array, int activeCounter);
}
