package WayofTime.bloodmagic.compress;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

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
        int oldNeeded = needed;
        int i = -1;
        Set<Integer> consumeSet = new HashSet<>();

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

                kept -= used;

                if (kept <= 0 && needed > 0) {
                    int remainingFromStack = Math.max(stackSize - used - needed, 0);
                    needed -= (stackSize - used - remainingFromStack);
                    if (needed != 0 && needed < oldNeeded) {
                        consumeSet.add(i);
                    }

                    if (doDrain && (!(needed < oldNeeded) || needed == 0)) {
                        invStack.setCount(remainingFromStack + used);
                        for (Integer j : consumeSet) {
                            inv[j].setCount(0);
                            inv[j] = ItemStack.EMPTY;
                        }
                        consumeSet.clear();
                        if (invStack.isEmpty()) {
                            inv[i] = ItemStack.EMPTY;
                        }
                    }
                }
                if (needed <= 0) {
                    return 0;
                }
            }
        }
        return needed;


    }

}