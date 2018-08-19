package WayofTime.bloodmagic.compress;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class CompressionHandler {
    /**
     * Called to look at the inventory and syphons the required stack. Returns
     * resultant stack if successful, and null if not.
     *
     * @param inv The inventory iterated through
     * @return The result of the compression
     */
    public abstract ItemStack compressInventory(ItemStack[] inv, World world);

    public int iterateThroughInventory(ItemStack required, int kept, ItemStack[] inv, int needed, boolean doDrain) {
        int i = -1;

        for (ItemStack invStack : inv) {
            i++;

            if (invStack.isEmpty()) {
                continue;
            }

            if (invStack.isItemEqual(required) && (invStack.getTagCompound() == null ? required.getTagCompound() == null : invStack.getTagCompound().equals(required.getTagCompound()))) {
                int stackSize = invStack.getCount();
                int used = 0;
                if (kept > 0) {
                    int remainingFromStack = Math.max(stackSize - kept, 0);
                    used += stackSize - remainingFromStack;
                }

                kept -= used; // 0

                if (kept <= 0 && needed > 0) {
                    int remainingFromStack = Math.max(stackSize - used - needed, 0);
                    if (doDrain) {
                        invStack.setCount(remainingFromStack + used);
                        if (invStack.isEmpty()) {
                            inv[i] = ItemStack.EMPTY;
                        }
                    }

                    needed -= (stackSize - used - remainingFromStack);
                }

                if (needed <= 0) {
                    return 0;
                }
            }
        }

        return needed;


    }

}