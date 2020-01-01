package WayofTime.bloodmagic.routing;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public interface IItemFilter extends IRoutingFilter {
    void initializeFilter(List<ItemStack> filteredList, TileEntity tile, IItemHandler itemHandler, boolean isFilterOutput);

    /**
     * Tries to transfer items from this filter to outputFilter.
     * Potentially modifies outputFilter and this filter.
     * @param outputFilter the filter to transfer items to
     * @param maxTransfer the max amount of items to transfer
     * @return the amount of items actually transferred.
     */
    int transferThroughInputFilter(IItemFilter outputFilter, int maxTransfer);

    /**
     * Offers an inventory slot to this output filter. The output filter will
     * try to extract the stack and return how many items it actually extracted.
     * @param inv The inventory that has the offered ItemStack.
     *  inv may be modified by calling inv.extractItem(slot, ..., false)
     * @param slot The slot of inv to check.
     * @param maxTransfer The max amount of items that can be taken.
     * @return The number of items that were actually extracted and taken.
     */
    int offerStack(IItemHandler inv, int slot, int maxTransfer);

    /**
     * Returns true if this is an input filter that can accept no more items
     * or if this is an output filter that can output no items.
     * Filters may return false even if they have no work.
     * @return true if this filter can be skipped
     */
    boolean canSkip();
}
