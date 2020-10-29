package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public class AlchemyArrayEffectCrafting extends AlchemyArrayEffect
{
	public final ItemStack outputStack;
	public int tickLimit;

	public AlchemyArrayEffectCrafting(ItemStack outputStack)
	{
		this(outputStack, 200);
	}

	public AlchemyArrayEffectCrafting(ItemStack outputStack, int tickLimit)
	{
		this.outputStack = outputStack;
		this.tickLimit = tickLimit;
	}

	@Override
	public boolean update(TileAlchemyArray tile, int ticksActive)
	{
		// TODO: Add recipe rechecking to verify nothing screwy is going on.
		if (tile.getWorld().isRemote)
		{
			return false;
		}

		if (ticksActive >= tickLimit)
		{
			BlockPos pos = tile.getPos();

			ItemStack output = outputStack.copy();

			ItemEntity outputEntity = new ItemEntity(tile.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ()
					+ 0.5, output);

			tile.getWorld().addEntity(outputEntity);
//			tile.getWorld().spawnEntity(outputEntity);

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
		return new AlchemyArrayEffectCrafting(outputStack, tickLimit);
	}
}
