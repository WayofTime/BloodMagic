package WayofTime.bloodmagic.routing;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
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
    private IInventory accessedInventory;
    private EnumFacing accessedSide;

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
    public void initializeFilter(List<ItemStack> filteredList, IInventory inventory, EnumFacing side, boolean isFilterOutput)
    {
        accessedInventory = inventory;
        accessedSide = side;
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
        ItemStack remainderStack = Utils.insertStackIntoInventory(testStack, accessedInventory, accessedSide);

        int changeAmount = allowedAmount - (remainderStack == null ? 0 : remainderStack.stackSize);
        testStack = inputStack.copy();
        testStack.stackSize -= changeAmount;

        TileEntity tile = (TileEntity) accessedInventory;
        World world = tile.getWorld();
        BlockPos pos = tile.getPos();
        world.markBlockForUpdate(pos);

        return testStack;
    }

    /**
     * This method is only called on an input filter to transfer ItemStacks from
     * the input inventory to the output inventory.
     */
    @Override
    public int transferThroughInputFilter(IItemFilter outputFilter, int maxTransfer)
    {
        boolean[] canAccessSlot = new boolean[accessedInventory.getSizeInventory()];
        if (accessedInventory instanceof ISidedInventory)
        {
            int[] slots = ((ISidedInventory) accessedInventory).getSlotsForFace(accessedSide);
            for (int slot : slots)
            {
                canAccessSlot[slot] = true;
            }
        } else
        {
            for (int slot = 0; slot < accessedInventory.getSizeInventory(); slot++)
            {
                canAccessSlot[slot] = true;
            }
        }

        for (int slot = 0; slot < accessedInventory.getSizeInventory(); slot++)
        {
            if (!canAccessSlot[slot])
            {
                continue;
            }

            ItemStack inputStack = accessedInventory.getStackInSlot(slot);
            if (inputStack == null || (accessedInventory instanceof ISidedInventory && !((ISidedInventory) accessedInventory).canExtractItem(slot, inputStack, accessedSide)))
            {
                continue;
            }

            int allowedAmount = Math.min(inputStack.stackSize, maxTransfer);

            ItemStack testStack = inputStack.copy();
            testStack.stackSize = allowedAmount;
            ItemStack remainderStack = outputFilter.transferStackThroughOutputFilter(testStack);
            int changeAmount = allowedAmount - (remainderStack == null ? 0 : remainderStack.stackSize);

            if (remainderStack != null && remainderStack.stackSize == allowedAmount)
            {
                //Nothing has changed. Moving on!
                continue;
            }

            TileEntity tile = (TileEntity) accessedInventory;
            World world = tile.getWorld();
            BlockPos pos = tile.getPos();
            world.markBlockForUpdate(pos);

            inputStack.stackSize -= changeAmount;
            maxTransfer -= changeAmount;

            accessedInventory.setInventorySlotContents(slot, inputStack.stackSize <= 0 ? null : inputStack); //Sets the slot in the inventory

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
