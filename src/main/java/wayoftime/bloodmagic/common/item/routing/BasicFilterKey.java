package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.world.item.ItemStack;

public class BasicFilterKey implements IFilterKey<ItemStack>
{
	private ItemStack keyStack;
	private int count;

	public BasicFilterKey(ItemStack keyStack, int count)
	{
		this.keyStack = keyStack;
		this.count = count;
	}

	@Override
	public boolean doesStackMatch(ItemStack testStack)
	{
		return !keyStack.isEmpty() && !testStack.isEmpty() && keyStack.getItem() == testStack.getItem();
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

	@Override
	public ItemStack getType()
	{
		return ItemStack.EMPTY;
	}
}
