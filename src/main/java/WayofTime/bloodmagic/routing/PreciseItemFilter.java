package WayofTime.bloodmagic.routing;

import WayofTime.bloodmagic.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Iterator;
import java.util.List;

/**
 * This particular implementation of IItemFilter checks to make sure that a) as
 * an output filter it will fill until the requested amount and b) as an input
 * filter it will only syphon until the requested amount.
 *
 * @author WayofTime
 */
public class PreciseItemFilter implements IItemFilter {
    /*
     * This list acts as the way the filter keeps track of its contents. For the
     * case of an output filter, it holds a list of ItemStacks that needs to be
     * inserted in the inventory to finish its request. For the case of an input
     * filter, it keeps track of how many can be removed.
     */
    protected List<ItemStack> requestList;
    protected TileEntity accessedTile;
    protected IItemHandler itemHandler;

    /**
     * Initializes the filter so that it knows what it wants to fulfill.
     *
     * @param filteredList   - The list of ItemStacks that the filter is set to.
     * @param tile           - The inventory that is being accessed. This inventory is either
     *                       being pulled from or pushed to.
     * @param itemHandler    - The item handler
     * @param isFilterOutput - Tells the filter what actions to expect. If true, it should be
     *                       initialized as an output filter. If false, it should be
     *                       initialized as an input filter.
     */
    @Override
    public void initializeFilter(List<ItemStack> filteredList, TileEntity tile, IItemHandler itemHandler, boolean isFilterOutput) {
        this.accessedTile = tile;
        this.itemHandler = itemHandler;
        if (isFilterOutput) {
            requestList = filteredList;

            for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                ItemStack checkedStack = itemHandler.getStackInSlot(slot);
                if (checkedStack.isEmpty()) {
                    continue;
                }

                int stackSize = checkedStack.getCount();

                for (ItemStack filterStack : requestList) {
                    if (filterStack.getCount() == 0) {
                        continue;
                    }

                    if (doStacksMatch(filterStack, checkedStack)) {
                        filterStack.setCount(Math.max(filterStack.getCount() - stackSize, 0));
                    }
                }
            }
        } else {
            requestList = filteredList;
            for (ItemStack filterStack : requestList) {
                // Invert the stack size so that after adding the items in the
                // inventory filterStack's count will be the amount we can
                // remove.
                // Negative stacks count as empty, so this is safe.
                filterStack.setCount(filterStack.getCount() * -1);
            }

            for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                ItemStack checkedStack = itemHandler.getStackInSlot(slot);
                if (checkedStack.isEmpty()) {
                    continue;
                }

                int stackSize = checkedStack.getCount();

                for (ItemStack filterStack : filteredList) {
                    if (doStacksMatch(filterStack, checkedStack)) {
                        filterStack.grow(stackSize);
                    }
                }
            }
        }

        requestList.removeIf(ItemStack::isEmpty);
    }

    @Override
    public int transferThroughInputFilter(IItemFilter outputFilter, int maxTransfer) {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack inputStack = itemHandler.getStackInSlot(slot);
            if (inputStack.isEmpty()) {
                continue;
            }

            Iterator<ItemStack> itr = requestList.iterator();
            ItemStack match = null;
            while (itr.hasNext()) {
                ItemStack filterStack = itr.next();
                if (doStacksMatch(filterStack, inputStack)) {
                    match = filterStack;
                    break;
                }
            }

            if (match == null) {
                continue;
            }

            int allowedAmount = Math.min(maxTransfer, match.getCount());
            int taken = outputFilter.offerStack(itemHandler, slot, allowedAmount);
            if (taken == 0) {
                continue;
            }

            match.shrink(taken);
            if (match.isEmpty()) {
                itr.remove();
            }

            World world = accessedTile.getWorld();
            BlockPos pos = accessedTile.getPos();
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

            return taken;
        }
        return 0;
    }

    @Override
    public int offerStack(IItemHandler inv, int slot, int maxTransfer) {
        ItemStack invStack = inv.getStackInSlot(slot);
        ItemStack match = null;
        Iterator<ItemStack> itr = requestList.iterator();
        while (itr.hasNext()) {
            ItemStack filterStack = itr.next();
            if (doStacksMatch(filterStack, invStack)) {
                match = filterStack;
                break;
            }
        }

        if (match == null) {
            return 0;
        }

        int allowed = Math.min(maxTransfer, match.getCount());
        ItemStack extracted = inv.extractItem(slot, allowed, true);
        ItemStack remainderStack = Utils.insertStackIntoTile(extracted, itemHandler);
        int taken = extracted.getCount() - remainderStack.getCount();
        inv.extractItem(slot, taken, false);

        match.shrink(taken);
        if (match.isEmpty()) {
            itr.remove();
        }

        World world = accessedTile.getWorld();
        BlockPos pos = accessedTile.getPos();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

        return taken;
    }

    @Override
    public boolean canSkip() {
        return requestList.isEmpty();
    }

    /**
     * @return True iff testStack matches this filter's conditions.
     */
    public boolean doesStackMatchFilter(ItemStack testStack) {
        for (ItemStack filterStack : requestList) {
            if (doStacksMatch(filterStack, testStack)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param filterStack
     * @param testStack
     * @return True iff filterStack and testStack are equivalent using
     *  this filter's conditions.
     */
    public boolean doStacksMatch(ItemStack filterStack, ItemStack testStack) {
        return ItemHandlerHelper.canItemStacksStack(filterStack, testStack);
    }
}
