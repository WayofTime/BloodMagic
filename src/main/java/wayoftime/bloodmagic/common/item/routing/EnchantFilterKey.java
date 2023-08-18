package wayoftime.bloodmagic.common.item.routing;

import java.util.Map;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class EnchantFilterKey implements IFilterKey
{
	private Enchantment enchantment;
	private int enchantLevel;
	private boolean isFuzzy;
	private int count;

	public EnchantFilterKey(Enchantment enchantment, int enchantLevel, boolean isFuzzy, int count)
	{
		this.enchantment = enchantment;
		this.enchantLevel = enchantLevel;
		this.isFuzzy = isFuzzy;
		this.count = count;
	}

	@Override
	public boolean doesStackMatch(ItemStack testStack)
	{
		int level = 0;

		if (testStack.getItem() == Items.ENCHANTED_BOOK)
		{
			Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(testStack);
			if (enchants.containsKey(enchantment))
			{
				level = enchants.get(enchantment);
			} else
			{
				return false;
			}
		} else
		{
			level = EnchantmentHelper.getItemEnchantmentLevel(enchantment, testStack);
		}

		return isFuzzy ? level > 0 : level == enchantLevel;
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