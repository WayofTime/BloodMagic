package WayofTime.bloodmagic.routing;

import WayofTime.bloodmagic.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

/**
 * This particular implementation of IItemFilter allows any item to be drained
 * from or inputed to the connected inventory. Every stack is accepted here!
 * We're basically Olive Gardens.
 *
 * @author WayofTime
 */
public class DefaultItemFilter implements IItemFilter {
    protected TileEntity accessedTile;
    protected IItemHandler itemHandler;

    /**
     * Initializes the filter so that it knows what it wants to fulfill.
     *
     * @param filteredList   - The list of ItemStacks that the filter is set to.
     * @param itemHandler    - The inventory that is being accessed. This inventory is either
     *                       being pulled from or pushed to.
     * @param isFilterOutput - Tells the filter what actions to expect. If true, it should be
     *                       initialized as an output filter. If false, it should be
     *                       initialized as an input filter.
     */
    @Override
    public void initializeFilter(List<ItemStack> filteredList, TileEntity tile, IItemHandler itemHandler, boolean isFilterOutput) {
        this.accessedTile = tile;
        this.itemHandler = itemHandler;
    }

    /**
     * This method is only called on an input filter to transfer ItemStacks from
     * the input inventory to the output inventory.
     */
    @Override
    public int transferThroughInputFilter(IItemFilter outputFilter, int maxTransfer) {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            int taken = outputFilter.offerStack(itemHandler, slot, maxTransfer);
            if (taken == 0) {
                continue;
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
        if (inv.getStackInSlot(slot).isEmpty()) {
            return 0;
        }

        ItemStack extracted = inv.extractItem(slot, maxTransfer, true);
        if (extracted.isEmpty()) {
            return 0;
        }
        ItemStack remainderStack = Utils.insertStackIntoTile(extracted, itemHandler);
        int taken = extracted.getCount() - remainderStack.getCount();
        inv.extractItem(slot, taken, false);

        World world = accessedTile.getWorld();
        BlockPos pos = accessedTile.getPos();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

        return taken;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
