package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

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
	public void onEntityCollidedWithBlock(TileAlchemyArray array, World world, BlockPos pos, BlockState state, Entity entity)
	{
		if (entity.isShiftKeyDown())
		{
			entity.fallDistance = 0;
		} else if (entity.getDeltaMovement().y < 0.0D)
		{
			Vector3d motion = entity.getDeltaMovement();
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
	public void readFromNBT(CompoundNBT compound)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToNBT(CompoundNBT compound)
	{
		// TODO Auto-generated method stub

	}
}
