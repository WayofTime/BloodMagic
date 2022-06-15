package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.common.tile.TileAlchemyArray;
import net.minecraft.world.level.Level;

public class AlchemyArrayEffectBounce extends AlchemyArrayEffect
{
	public AlchemyArrayEffectBounce()
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
		if (entity.isShiftKeyDown())
		{
			entity.fallDistance = 0;
		} else if (entity.getDeltaMovement().y < 0.0D)
		{
			Vec3 motion = entity.getDeltaMovement();
			motion = motion.multiply(1, -1, 1);

			if (!(entity instanceof LivingEntity))
			{
				motion = motion.multiply(1, 0.8, 1);
			}

			entity.setDeltaMovement(motion);

			entity.fallDistance = 0;
		}
	}

	@Override
	public AlchemyArrayEffect getNewCopy()
	{
		return new AlchemyArrayEffectBounce();
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
