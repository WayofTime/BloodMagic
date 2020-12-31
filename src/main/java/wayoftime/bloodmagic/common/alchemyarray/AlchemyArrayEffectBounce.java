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
		if (entity.isSneaking())
		{
			entity.fallDistance = 0;
		} else if (entity.getMotion().y < 0.0D)
		{
			Vector3d motion = entity.getMotion();
			motion = motion.mul(1, -1, 1);

			if (!(entity instanceof LivingEntity))
			{
				motion = motion.mul(1, 0.8, 1);
			}

			entity.setMotion(motion);

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
