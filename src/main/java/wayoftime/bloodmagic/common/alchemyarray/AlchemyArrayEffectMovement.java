package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.common.tile.TileAlchemyArray;
import net.minecraft.world.level.Level;

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
	public void onEntityCollidedWithBlock(TileAlchemyArray array, Level world, BlockPos pos, BlockState state, Entity entity)
	{
		double motionY = 0.5;
		double motionYGlowstoneMod = 0.05;
		double speed = 1.5;
		double speedRedstoneMod = 0.15;

		Direction direction = array.getRotation();
		TileAlchemyArray tileArray = (TileAlchemyArray) array;

		motionY += motionYGlowstoneMod * (tileArray.getItem(0).getCount() - 1);
		speed += speedRedstoneMod * (tileArray.getItem(1).getCount() - 1);

//		entity.getMotion().y = motionY;
		entity.fallDistance = 0;

		switch (direction)
		{
		case NORTH:
//                entity.motionX = 0;
//                entity.motionY = motionY;
//                entity.motionZ = -speed;
			entity.setDeltaMovement(new Vec3(0, motionY, -speed));
			break;

		case SOUTH:
//                entity.motionX = 0;
//                entity.motionY = motionY;
//                entity.motionZ = speed;
			entity.setDeltaMovement(new Vec3(0, motionY, speed));
			break;

		case WEST:
//                entity.motionX = -speed;
//                entity.motionY = motionY;
//                entity.motionZ = 0;
			entity.setDeltaMovement(new Vec3(-speed, motionY, 0));
			break;

		case EAST:
//                entity.motionX = speed;
//                entity.motionY = motionY;
//                entity.motionZ = 0;
			entity.setDeltaMovement(new Vec3(speed, motionY, 0));
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
