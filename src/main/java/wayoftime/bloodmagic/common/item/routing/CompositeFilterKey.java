package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class CompositeFilterKey implements IFilterKey
{
	private List<IFilterKey> keyList = new ArrayList<IFilterKey>();
	private int count;

	public CompositeFilterKey(int count)
	{
		this.count = count;
	}

	public void addFilterKey(IFilterKey key)
	{
		if (!(key instanceof CompositeFilterKey))
		{
			keyList.add(key);
		}
	}

	@Override
	public boolean doesStackMatch(ItemStack testStack)
	{
		if (testStack.isEmpty())
		{
			return false;
		}

		for (IFilterKey key : keyList)
		{
			if (!key.doesStackMatch(testStack))
			{
				return false;
			}
		}

		return true;
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
