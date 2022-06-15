package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.common.tile.TileAlchemyArray;
import net.minecraft.world.level.Level;

public class AlchemyArrayEffectUpdraft extends AlchemyArrayEffect
{
	public AlchemyArrayEffectUpdraft()
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
		double motionY = 1;
		double motionYGlowstoneMod = 0.1;
		double motionYFeatherMod = 0.05;

		TileAlchemyArray tileArray = (TileAlchemyArray) array;

		motionY += motionYGlowstoneMod * (tileArray.getItem(0).getCount() - 1); // Glowstone Dust
		motionY += motionYFeatherMod * (tileArray.getItem(1).getCount() - 1); // Feathers

//		entity.getMotion().y = motionY;
		entity.fallDistance = 0;

		entity.setDeltaMovement(new Vec3(0, motionY, 0));

	}

	@Override
	public AlchemyArrayEffect getNewCopy()
	{
		return new AlchemyArrayEffectUpdraft();
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
