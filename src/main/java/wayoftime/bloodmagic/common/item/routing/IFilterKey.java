package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.world.item.ItemStack;

public interface IFilterKey
{
	public boolean doesStackMatch(ItemStack testStack);

	public int getCount();

	public void setCount(int count);

	public void grow(int changeAmount);

	public boolean isEmpty();

	public void shrink(int changeAmount);
}
