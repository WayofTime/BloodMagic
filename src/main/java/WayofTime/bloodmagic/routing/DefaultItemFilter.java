package WayofTime.bloodmagic.routing;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import WayofTime.bloodmagic.util.Utils;

/**
 * This particular implementation of IItemFilter allows any item to be drained
 * from or inputed to the connected inventory. Every stack is accepted here!
 * We're basically Olive Gardens.
 * 
 * @author WayofTime
 * 
 */
public class DefaultItemFilter implements IItemFilter
{
    protected TileEntity accessedTile;
    protected IItemHandler itemHandler;

    /**
     * Initializes the filter so that it knows what it wants to fulfill.
     * 
     * @param filteredList
     *        - The list of ItemStacks that the filter is set to.
     * @param inventory
     *        - The inventory that is being accessed. This inventory is either
     *        being pulled from or pushed to.
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
    }

    /**
     * This method is only called when the output inventory this filter is
     * managing receives an ItemStack. Should only really be called by the Input
     * filter via it's transfer method.
     * 
     * @param inputStack
     *        - The stack to transfer
     * 
     * @return - The remainder of the stack after it has been absorbed into the
     *         inventory.
     */
    @Override
    public ItemStack transferStackThroughOutputFilter(ItemStack inputStack)
    {
        int allowedAmount = inputStack.stackSize; //This is done to make the migration to a maximum amount transfered a lot easier

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

            int allowedAmount = Math.min(itemHandler.extractItem(slot, inputStack.stackSize, true).stackSize, maxTransfer);

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
        return true;
    }

    @Override
    public boolean doStacksMatch(ItemStack filterStack, ItemStack testStack)
    {
        return true;
    }
}
