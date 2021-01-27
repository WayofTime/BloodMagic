package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

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
	public void onEntityCollidedWithBlock(TileAlchemyArray array, World world, BlockPos pos, BlockState state, Entity entity)
	{
		double motionY = 1;
		double motionYGlowstoneMod = 0.1;
		double motionYFeatherMod = 0.05;

		TileAlchemyArray tileArray = (TileAlchemyArray) array;

		motionY += motionYGlowstoneMod * (tileArray.getStackInSlot(0).getCount() - 1); // Glowstone Dust
		motionY += motionYFeatherMod * (tileArray.getStackInSlot(1).getCount() - 1); // Feathers

//		entity.getMotion().y = motionY;
		entity.fallDistance = 0;

		entity.setMotion(new Vector3d(0, motionY, 0));

	}

	@Override
	public AlchemyArrayEffect getNewCopy()
	{
		return new AlchemyArrayEffectUpdraft();
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
