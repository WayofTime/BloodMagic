package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.world.item.ItemStack;

public interface ICompositeItemFilterProvider extends IItemFilterProvider
{
	ItemStack nestFilter(ItemStack mainFilterStack, ItemStack nestedFilterStack);

	boolean canReceiveNestedFilter(ItemStack mainFilterStack, ItemStack nestedFilterStack);
}
