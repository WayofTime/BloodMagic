package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CollectionTagFilterKey implements IFilterKey<ItemStack>
{
	private List<TagKey<Item>> itemTags;
	private int count;

	public CollectionTagFilterKey(List<TagKey<Item>> tagList, int count)
	{
		this.itemTags = tagList;
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
		for (TagKey<Item> tag : itemTags)
		{
			if (testStack.is(tag))
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
