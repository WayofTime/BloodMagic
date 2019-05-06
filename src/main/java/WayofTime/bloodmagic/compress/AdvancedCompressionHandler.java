package WayofTime.bloodmagic.compress;


import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class AdvancedCompressionHandler extends CompressionHandler {

    public ItemStack compressInventory(ItemStack[] inv, World world) {
        for (ItemStack invStack : inv) {
            if (invStack.isEmpty()) {
                continue;
            }

            for (int i = 3; i >= 2; i--) {
                ItemStack stack;
                ItemStack lastInvStack = ItemStack.EMPTY;
                if (!CompressionRegistry.areItemStacksEqual(invStack, lastInvStack)) {
                    StorageBlockCraftingManager.reversibleCheck = invStack;
                    stack = StorageBlockCraftingManager.getRecipe(invStack, world, i);
                } else {
                    stack = lastInvStack;
                }
                if (!stack.isEmpty()) {

                    int needed = (i == 2 ? 4 : 9);
                    int remaining = iterateThroughInventory(invStack, invStack.getMaxStackSize() - needed, inv, needed, true); // if more than needed gets consumed at any point, the simulate test was needed after all
                    if (remaining <= 0)
                        return stack;
                }
            }
        }

        return ItemStack.EMPTY;
    }


}
