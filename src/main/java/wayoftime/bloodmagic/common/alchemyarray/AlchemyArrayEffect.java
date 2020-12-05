package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public abstract class AlchemyArrayEffect
{
	public abstract AlchemyArrayEffect getNewCopy();

	public abstract void readFromNBT(CompoundNBT compound);

	public abstract void writeToNBT(CompoundNBT compound);

	public abstract boolean update(TileAlchemyArray array, int activeCounter);

	public void onEntityCollidedWithBlock(TileAlchemyArray tileAlchemyArray, World world, BlockPos pos, BlockState state, Entity entity)
	{
	};
}
