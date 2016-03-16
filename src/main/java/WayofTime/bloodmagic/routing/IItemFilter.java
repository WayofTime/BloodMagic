package WayofTime.bloodmagic.routing;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface IItemFilter
{
    void initializeFilter(List<ItemStack> filteredList, IInventory inventory, EnumFacing side, boolean isFilterOutput);

    /**
     * This method is only called when the output inventory this filter is
     * managing receives an ItemStack. Should only really be called by the Input
     * filter via it's transfer method.
     * 
     * @param inputStack
     *        - The stack to filter
     * 
     * @return - The remainder of the stack after it has been absorbed into the
     *         inventory.
     */
    ItemStack transferStackThroughOutputFilter(ItemStack inputStack);

    /**
     * This method is only called on an input filter to transfer ItemStacks from
     * the input inventory to the output inventory.
     */
    int transferThroughInputFilter(IItemFilter outputFilter, int maxTransfer);

    boolean doesStackMatchFilter(ItemStack testStack);

    boolean doStacksMatch(ItemStack filterStack, ItemStack testStack);
}
