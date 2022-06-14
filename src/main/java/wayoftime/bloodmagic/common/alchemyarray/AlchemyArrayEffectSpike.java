package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

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
	public void onEntityCollidedWithBlock(TileAlchemyArray array, World world, BlockPos pos, BlockState state, Entity entity)
	{
		if (entity instanceof LivingEntity)
		{
			entity.hurt(DamageSource.CACTUS, 2);
		}
	}

	@Override
	public AlchemyArrayEffect getNewCopy()
	{
		return new AlchemyArrayEffectSpike();
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
