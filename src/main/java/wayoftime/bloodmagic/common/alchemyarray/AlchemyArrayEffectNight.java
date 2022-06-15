package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.tile.TileAlchemyArray;
import net.minecraft.server.level.ServerLevel;

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

		Level world = tile.getLevel();
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

		if (world.isClientSide && world instanceof ClientLevel)
		{
			long finalTime = ((world.getDayTime() + 11000) / 24000) * 24000 + 13000;
			long time = (finalTime - startingTime) * (ticksActive - 100) / 100 + startingTime;

			((ClientLevel) world).getLevelData().setDayTime(time);

			return false;
		}

		if (world instanceof ServerLevel)
		{
//			world.getDayTime()
			long finalTime = ((world.getDayTime() + 11000) / 24000) * 24000 + 13000;
			long time = (finalTime - startingTime) * (ticksActive - 100) / 100 + startingTime;
			for (ServerLevel serverworld : world.getServer().getAllLevels())
			{
				serverworld.setDayTime((long) time);
			}

			if (ticksActive >= 200)
			{
				BlockPos pos = tile.getBlockPos();
				LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
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
	public void writeToNBT(CompoundTag tag)
	{

	}

	@Override
	public void readFromNBT(CompoundTag tag)
	{

	}

	@Override
	public AlchemyArrayEffect getNewCopy()
	{
		return new AlchemyArrayEffectNight();
	}
}
