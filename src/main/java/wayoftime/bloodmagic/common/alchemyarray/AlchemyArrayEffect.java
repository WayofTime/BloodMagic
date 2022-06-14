package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public abstract class AlchemyArrayEffect
{
	public abstract AlchemyArrayEffect getNewCopy();

	public abstract void readFromNBT(CompoundTag compound);

	public abstract void writeToNBT(CompoundTag compound);

	public abstract boolean update(TileAlchemyArray array, int activeCounter);

	public void onEntityCollidedWithBlock(TileAlchemyArray tileAlchemyArray, Level world, BlockPos pos, BlockState state, Entity entity)
	{
	};
}
