package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.TileAlchemyArray;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class AlchemyArrayEffectSpike extends AlchemyArrayEffect
{
	public AlchemyArrayEffectSpike()
	{
		super();
	}

	@Override
	public boolean update(TileAlchemyArray tile, int ticksActive)
	{
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(TileAlchemyArray array, Level world, BlockPos pos, BlockState state, Entity entity)
	{
		if (entity instanceof LivingEntity)
		{
			entity.hurt(entity.damageSources().cactus(), 2);
		}
	}

	@Override
	public AlchemyArrayEffect getNewCopy()
	{
		return new AlchemyArrayEffectSpike();
	}

	@Override
	public void readFromNBT(CompoundTag compound)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToNBT(CompoundTag compound)
	{
		// TODO Auto-generated method stub

	}
}
