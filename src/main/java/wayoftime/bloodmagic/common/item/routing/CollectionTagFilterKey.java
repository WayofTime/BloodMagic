package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.Tag;

public class CollectionTagFilterKey implements IFilterKey
{
	private List<Tag<Item>> itemTags;
	private int count;

	public CollectionTagFilterKey(List<Tag<Item>> itemTags, int count)
	{
		this.itemTags = itemTags;
		this.count = count;
	}

	@Override
	public boolean doesStackMatch(ItemStack testStack)
	{
		for (Tag<Item> tag : itemTags)
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
