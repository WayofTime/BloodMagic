package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public class AlchemyArrayEffectMovement extends AlchemyArrayEffect
{
	public AlchemyArrayEffectMovement()
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
		double motionY = 0.5;
		double motionYGlowstoneMod = 0.05;
		double speed = 1.5;
		double speedRedstoneMod = 0.15;

		Direction direction = array.getRotation();
		TileAlchemyArray tileArray = (TileAlchemyArray) array;

		motionY += motionYGlowstoneMod * (tileArray.getStackInSlot(0).getCount() - 1);
		speed += speedRedstoneMod * (tileArray.getStackInSlot(1).getCount() - 1);

//		entity.getMotion().y = motionY;
		entity.fallDistance = 0;

		switch (direction)
		{
		case NORTH:
//                entity.motionX = 0;
//                entity.motionY = motionY;
//                entity.motionZ = -speed;
			entity.setMotion(new Vector3d(0, motionY, -speed));
			break;

		case SOUTH:
//                entity.motionX = 0;
//                entity.motionY = motionY;
//                entity.motionZ = speed;
			entity.setMotion(new Vector3d(0, motionY, speed));
			break;

		case WEST:
//                entity.motionX = -speed;
//                entity.motionY = motionY;
//                entity.motionZ = 0;
			entity.setMotion(new Vector3d(-speed, motionY, 0));
			break;

		case EAST:
//                entity.motionX = speed;
//                entity.motionY = motionY;
//                entity.motionZ = 0;
			entity.setMotion(new Vector3d(speed, motionY, 0));
			break;
		default:
			break;
		}
	}

	@Override
	public AlchemyArrayEffect getNewCopy()
	{
		return new AlchemyArrayEffectMovement();
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
