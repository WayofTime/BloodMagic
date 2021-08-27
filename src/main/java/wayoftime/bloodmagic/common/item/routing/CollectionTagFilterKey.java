package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;

public class CollectionTagFilterKey implements IFilterKey
{
	private List<ITag<Item>> itemTags;
	private int count;

	public CollectionTagFilterKey(List<ITag<Item>> itemTags, int count)
	{
		this.itemTags = itemTags;
		this.count = count;
	}

	@Override
	public boolean doesStackMatch(ItemStack testStack)
	{
		for (ITag<Item> tag : itemTags)
		{
			if (tag.contains(testStack.getItem()))
			{
				return true;
			}
		}
		return false;
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
