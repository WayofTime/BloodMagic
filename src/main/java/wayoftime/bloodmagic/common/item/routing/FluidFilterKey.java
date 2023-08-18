package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FluidFilterKey implements IFilterKey
{
	private FluidStack keyStack;
	private int count;

	public FluidFilterKey(FluidStack keyStack, int count)
	{
		this.keyStack = keyStack;
		this.count = count;
	}

	@Override
	public boolean doesStackMatch(ItemStack testStack)
	{
		return keyStack.isFluidEqual(testStack);
	}

	@Override
	public int getCount()
	{
		return count;
	}

	@Override
	public void setCount(int count)
	{
		this.count = count;
	}

	@Override
	public void shrink(int changeAmount)
	{
		this.count -= changeAmount;
	}

	@Override
	public void grow(int changeAmount)
	{
		this.count += changeAmount;
	}

	@Override
	public boolean isEmpty()
	{
		return count == 0 || keyStack.isEmpty();
	}
}
