package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public class AlchemyArrayEffectDay extends AlchemyArrayEffect
{
	public AlchemyArrayEffectDay()
	{

	}

	@Override
	public boolean update(TileAlchemyArray tile, int ticksActive)
	{
		// TODO: Add recipe rechecking to verify nothing screwy is going on.
		if (tile.getWorld().isRemote)
		{
			return false;
		}

		if (tile.getWorld() instanceof ServerWorld)
		{
			long time = (tile.getWorld().getGameTime() / 24000) * 24000;
			for (ServerWorld serverworld : tile.getWorld().getServer().getWorlds())
			{
				serverworld.func_241114_a_((long) time);
			}

			return true;
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
		return new AlchemyArrayEffectDay();
	}
}