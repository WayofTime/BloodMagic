package wayoftime.bloodmagic.common.routing;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.common.item.routing.IFilterKey;

public interface IItemFilter extends IRoutingFilter
{
	void initializeFilter(List<IFilterKey> filteredList, TileEntity tile, IItemHandler itemHandler, boolean isFilterOutput);

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
}
