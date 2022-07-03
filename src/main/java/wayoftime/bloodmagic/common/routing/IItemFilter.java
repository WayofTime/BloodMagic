package wayoftime.bloodmagic.common.routing;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.common.item.routing.IFilterKey;

public interface IItemFilter extends IRoutingFilter
{
	void initializeFilter(List<IFilterKey> filteredList, BlockEntity tile, IItemHandler itemHandler, boolean isFilterOutput);

	void initializeFilter(List<IFilterKey> filteredList);

	/**
	 * This method is only called when the output inventory this filter is managing
	 * receives an ItemStack. Should only really be called by the Input filter via
	 * it's transfer method.
	 *
	 * @param inputStack - The stack to filter
	 * @return - The remainder of the stack after it has been absorbed into the
	 *         inventory.
	 */
	ItemStack transferStackThroughOutputFilter(ItemStack inputStack);

	/**
	 * This method is only called on an input filter to transfer ItemStacks from the
	 * input inventory to the output inventory.
	 */
	int transferThroughInputFilter(IItemFilter outputFilter, int maxTransfer);

	boolean doesStackPassFilter(ItemStack testStack);

	boolean doStacksMatch(IFilterKey filterStack, ItemStack testStack);

	/**
	 * Called to get the filter list directly for testing from outside sources. No
	 * items within this list, nor the list itself, may be modified!
	 */
	List<IFilterKey> getFilterList();
}
