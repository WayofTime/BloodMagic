package wayoftime.bloodmagic.common.item.routing;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class CollectionEnchantFilterKey implements IFilterKey
{
	private Map<Enchantment, Integer> enchantMap;
	private boolean isFuzzy;
	private boolean matchAll;
	private int count;

	public CollectionEnchantFilterKey(Map<Enchantment, Integer> enchantMap, boolean isFuzzy, boolean matchAll, int count)
	{
		this.enchantMap = enchantMap;
		this.isFuzzy = isFuzzy;
		this.matchAll = matchAll;
		this.count = count;
	}

	@Override
	public boolean doesStackMatch(ItemStack testStack)
	{
		for (Entry<Enchantment, Integer> entry : enchantMap.entrySet())
		{
			int level = 0;
			if (testStack.getItem() == Items.ENCHANTED_BOOK)
			{
				Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(testStack);
				if (enchants.containsKey(entry.getKey()))
				{
					level = enchants.get(entry.getKey());
				} else
				{
					return false;
				}
			} else
			{
				level = EnchantmentHelper.getItemEnchantmentLevel(entry.getKey(), testStack);
			}

			// Got a match!
			if (isFuzzy ? level > 0 : level == entry.getValue())
			{
				if (!matchAll)
				{
					return true;
				}
			} else if (matchAll)
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