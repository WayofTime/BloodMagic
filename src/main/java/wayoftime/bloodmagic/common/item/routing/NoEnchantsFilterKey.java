package wayoftime.bloodmagic.common.item.routing;

import java.util.Map;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;

public class NoEnchantsFilterKey implements IFilterKey<ItemStack>
{
	private int count;

	public NoEnchantsFilterKey(int count)
	{
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
		Map<Enchantment, Integer> enchantMap = EnchantmentHelper.getEnchantments(testStack);
		return enchantMap.size() <= 0;
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