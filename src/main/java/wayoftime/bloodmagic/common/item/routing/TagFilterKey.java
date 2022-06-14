package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.Tag;

public class TagFilterKey implements IFilterKey
{
	private Tag<Item> itemTag;
	private int count;

	public TagFilterKey(Tag<Item> itemTag, int count)
	{
		this.itemTag = itemTag;
		this.count = count;
	}

	@Override
	public boolean doesStackMatch(ItemStack testStack)
	{
		return itemTag.contains(testStack.getItem());
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