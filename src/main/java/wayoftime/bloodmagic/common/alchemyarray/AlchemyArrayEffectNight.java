package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public class AlchemyArrayEffectNight extends AlchemyArrayEffect
{
	private long startingTime = 0;

	public AlchemyArrayEffectNight()
	{

	}

	@Override
	public boolean update(TileAlchemyArray tile, int ticksActive)
	{
//		if (ticksActive < 200)
//		{
//			return false;
//		}

		World world = tile.getLevel();
		if (ticksActive == 100)
		{
			startingTime = world.getDayTime();
			tile.doDropIngredients(false);
		}

		if (ticksActive <= 100)
		{
			return false;
		}

		// TODO: Add recipe rechecking to verify nothing screwy is going on.

		if (world.isClientSide && world instanceof ClientWorld)
		{
			long finalTime = ((world.getDayTime() + 11000) / 24000) * 24000 + 13000;
			long time = (finalTime - startingTime) * (ticksActive - 100) / 100 + startingTime;

			((ClientWorld) world).getLevelData().setDayTime(time);

			return false;
		}

		if (world instanceof ServerWorld)
		{
//			world.getDayTime()
			long finalTime = ((world.getDayTime() + 11000) / 24000) * 24000 + 13000;
			long time = (finalTime - startingTime) * (ticksActive - 100) / 100 + startingTime;
			for (ServerWorld serverworld : world.getServer().getAllLevels())
			{
				serverworld.setDayTime((long) time);
			}

			if (ticksActive >= 200)
			{
				BlockPos pos = tile.getBlockPos();
				LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
//				LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
				lightningboltentity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				lightningboltentity.setVisualOnly(true);
				world.addFreshEntity(lightningboltentity);

				return true;
			}

			return false;
		}
		return false;
	}

	@Override
	public void writeToNBT(CompoundNBT tag)
	{

	}

	@Override
	public void readFromNBT(CompoundNBT tag)
	{

	}

	@Override
	public AlchemyArrayEffect getNewCopy()
	{
		return new AlchemyArrayEffectNight();
	}
}
