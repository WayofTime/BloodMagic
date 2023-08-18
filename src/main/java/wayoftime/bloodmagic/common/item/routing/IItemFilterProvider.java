package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.common.routing.IItemFilter;

public interface IItemFilterProvider extends IRoutingFilterProvider
{
	IItemFilter getInputItemFilter(ItemStack stack, BlockEntity tile, IItemHandler handler);

	IItemFilter getOutputItemFilter(ItemStack stack, BlockEntity tile, IItemHandler handler);

	// Only used for filters that check ItemStacks and do not transfer items to/from
	// a connected inventory.
	IItemFilter getUninitializedItemFilter(ItemStack stack);

	IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount);
}
