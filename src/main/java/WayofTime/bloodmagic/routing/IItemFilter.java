package WayofTime.bloodmagic.routing;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public interface IItemFilter extends IRoutingFilter {
    void initializeFilter(List<ItemStack> filteredList, TileEntity tile, IItemHandler itemHandler, boolean isFilterOutput);

    /**
     * This method is only called when the output inventory this filter is
     * managing receives an ItemStack. Should only really be called by the Input
     * filter via it's transfer method.
     *
     * @param inputStack - The stack to filter
     * @return - The remainder of the stack after it has been absorbed into the
     * inventory.
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
