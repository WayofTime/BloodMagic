package WayofTime.bloodmagic.compress;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;


public class AdvancedCompressionHandler extends CompressionHandler {

    @Override
    public void compressInventory(InventoryPlayer inv, World world) {
        ItemStack stack;
        ItemStack lastInvStack = ItemStack.EMPTY;
        NonNullList<ItemStack> inventory = inv.mainInventory;
        for (ItemStack invStack : inventory) {
            if (invStack.isEmpty()) {
                continue;
            }

            for (int i = 3; i >= 2; i--) {
                if (!CompressionRegistry.areItemStacksEqual(invStack, lastInvStack)) {
                    StorageBlockCraftingManager.reversibleCheck = invStack;
                    stack = StorageBlockCraftingManager.getRecipe(invStack, world, i);
                } else {
                    stack = lastInvStack;
                }
                if (!stack.isEmpty()) {

                    int needed = (i == 2 ? 4 : 9);
                    int remaining = iterateThroughInventory(invStack, invStack.getMaxStackSize() - needed, inventory.toArray(new ItemStack[inventory.size()]), needed, true); // if more than needed gets consumed at any point, the simulate test was needed after all
                    if (remaining <= 0)
                        inv.addItemStackToInventory(stack);
                }
            }
        }
    }
}
