package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.world.item.ItemStack;

public class ModFilterKey implements IFilterKey<ItemStack>
{
	private String namespace;
	private int count;

	public ModFilterKey(String namespace, int count)
	{
		this.namespace = namespace;
		this.count = count;
	}

	@Override
	public ItemStack getType()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean doesStackMatch(ItemStack testStack)
	{
		return !testStack.isEmpty() && testStack.getItem().getRegistryName().getNamespace().equals(namespace);
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
		return count == 0;
	}
}
