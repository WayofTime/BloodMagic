package WayofTime.bloodmagic.routing;

import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import WayofTime.bloodmagic.util.Utils;

/**
 * This particular implementation of IItemFilter checks to make sure that a) as
 * an output filter it will fill until the requested amount and b) as an input
 * filter it will only syphon until the requested amount.
 * 
 * @author WayofTime
 * 
 */
public class TestItemFilter implements IItemFilter
{
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
     * @param filteredList
     *        - The list of ItemStacks that the filter is set to.
     * @param tile
     *        - The inventory that is being accessed. This inventory is either
     *        being pulled from or pushed to.
     * @param itemHandler
     *        - The item handler
     * @param side
     *        - The side that the inventory is being accessed from. Used for
     *        pulling/pushing from/to the inventory.
     * @param isFilterOutput
     *        - Tells the filter what actions to expect. If true, it should be
     *        initialized as an output filter. If false, it should be
     *        initialized as an input filter.
     */
    @Override
    public void initializeFilter(List<ItemStack> filteredList, TileEntity tile, IItemHandler itemHandler, boolean isFilterOutput)
    {
        this.accessedTile = tile;
        this.itemHandler = itemHandler;
        if (isFilterOutput)
        {
            requestList = filteredList;

            for (int slot = 0; slot < itemHandler.getSlots(); slot++)
            {
                ItemStack checkedStack = itemHandler.getStackInSlot(slot);
                if (checkedStack == null)
                {
                    continue;
                }

                int stackSize = checkedStack.stackSize;

                for (ItemStack filterStack : requestList)
                {
                    if (filterStack.stackSize == 0)
                    {
                        continue;
                    }

                    if (doStacksMatch(filterStack, checkedStack))
                    {
                        filterStack.stackSize = Math.max(filterStack.stackSize - stackSize, 0);
                    }
                }
            }
        } else
        {
            requestList = filteredList;
            for (ItemStack filterStack : requestList)
            {
                filterStack.stackSize *= -1; //Invert the stack size so that 
            }

            for (int slot = 0; slot < itemHandler.getSlots(); slot++)
            {
                ItemStack checkedStack = itemHandler.getStackInSlot(slot);
                if (checkedStack == null)
                {
                    continue;
                }

                int stackSize = checkedStack.stackSize;

                for (ItemStack filterStack : filteredList)
                {
                    if (doStacksMatch(filterStack, checkedStack))
                    {
                        filterStack.stackSize += stackSize;
                    }
                }
            }
        }

        Iterator<ItemStack> iterator = requestList.iterator();
        while (iterator.hasNext())
        {
            ItemStack filterStack = iterator.next();
            if (filterStack.stackSize <= 0)
            {
                iterator.remove();
            }
        }
    }

    /**
     * This method is only called when the output inventory this filter is
     * managing receives an ItemStack. Should only really be called by the Input
     * filter via it's transfer method.
     * 
     * @param inputStack
     *        - The stack to transfer
     * @return - The remainder of the stack after it has been absorbed into the
     *         inventory.
     */
    @Override
    public ItemStack transferStackThroughOutputFilter(ItemStack inputStack)
    {
        int allowedAmount = 0;
        for (ItemStack filterStack : requestList)
        {
            if (doStacksMatch(filterStack, inputStack))
            {
                allowedAmount = Math.min(filterStack.stackSize, inputStack.stackSize);
                break;
            }
        }

        if (allowedAmount <= 0)
        {
            return inputStack;
        }

        ItemStack testStack = inputStack.copy();
        testStack.stackSize = allowedAmount;
        ItemStack remainderStack = Utils.insertStackIntoTile(testStack, itemHandler);

        int changeAmount = allowedAmount - (remainderStack == null ? 0 : remainderStack.stackSize);
        testStack = inputStack.copy();
        testStack.stackSize -= changeAmount;

        Iterator<ItemStack> itr = requestList.iterator();
        while (itr.hasNext())
        {
            ItemStack filterStack = itr.next();
            if (doStacksMatch(filterStack, inputStack))
            {
                filterStack.stackSize -= changeAmount;
                if (filterStack.stackSize <= 0)
                {
                    itr.remove();
                }
            }
        }

        World world = accessedTile.getWorld();
        BlockPos pos = accessedTile.getPos();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

        return testStack;
    }

    /**
     * This method is only called on an input filter to transfer ItemStacks from
     * the input inventory to the output inventory.
     */
    @Override
    public int transferThroughInputFilter(IItemFilter outputFilter, int maxTransfer)
    {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++)
        {
            ItemStack inputStack = itemHandler.getStackInSlot(slot);
            if (inputStack == null || itemHandler.extractItem(slot, inputStack.stackSize, true) == null)//(accessedInventory instanceof ISidedInventory && !((ISidedInventory) accessedInventory).canExtractItem(slot, inputStack, accessedSide)))
            {
                continue;
            }

            int allowedAmount = 0;
            for (ItemStack filterStack : requestList)
            {
                if (doStacksMatch(filterStack, inputStack))
                {
                    allowedAmount = Math.min(maxTransfer, Math.min(filterStack.stackSize, itemHandler.extractItem(slot, inputStack.stackSize, true).stackSize));
                    break;
                }
            }

            if (allowedAmount <= 0)
            {
                continue;
            }

            ItemStack testStack = inputStack.copy();
            testStack.stackSize = allowedAmount;
            ItemStack remainderStack = outputFilter.transferStackThroughOutputFilter(testStack);
            int changeAmount = allowedAmount - (remainderStack == null ? 0 : remainderStack.stackSize);

            if (remainderStack != null && remainderStack.stackSize == allowedAmount)
            {
                //Nothing has changed. Moving on!
                continue;
            }

            maxTransfer -= changeAmount;

            itemHandler.extractItem(slot, changeAmount, false);

            Iterator<ItemStack> itr = requestList.iterator();
            while (itr.hasNext())
            {
                ItemStack filterStack = itr.next();
                if (doStacksMatch(filterStack, inputStack))
                {
                    filterStack.stackSize -= changeAmount;
                    if (filterStack.stackSize <= 0)
                    {
                        itr.remove();
                    }
                }
            }

            World world = accessedTile.getWorld();
            BlockPos pos = accessedTile.getPos();
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

            return changeAmount;
        }

        return 0;
    }

    @Override
    public boolean doesStackMatchFilter(ItemStack testStack)
    {
        for (ItemStack filterStack : requestList)
        {
            if (doStacksMatch(filterStack, testStack))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean doStacksMatch(ItemStack filterStack, ItemStack testStack)
    {
        return Utils.canCombine(filterStack, testStack);
    }
}
