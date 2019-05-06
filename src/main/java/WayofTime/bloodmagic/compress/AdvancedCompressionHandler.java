package WayofTime.bloodmagic.compress;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;


public class AdvancedCompressionHandler extends CompressionHandler {

    public ItemStack compressInventory(ItemStack[] inv, World world) {
        for (ItemStack invStack : inv) {
            if (invStack.isEmpty()) {
                continue;
            }

            for (int i = 3; i >= 2; i--) {
                ItemStack invStackCopy = invStack.copy();
                invStackCopy.setCount(1);
                Tuple<ItemStack, Integer> stackTuple = CompressionRegistry.compressionMap.get(invStackCopy);
                ItemStack stack;
                if (stackTuple == null) {
                    StorageBlockCraftingManager.reversibleCheck = invStack;
                    stack = StorageBlockCraftingManager.getRecipe(invStack, world, i);
                    if (stack.isEmpty())
                        continue;
                    CompressionRegistry.compressionMap.put(invStackCopy, new Tuple<>(stack, i * i));
                } else {
                    stack = stackTuple.getFirst();
                    if (stackTuple.getSecond() != i * i)
                        return ItemStack.EMPTY;
                }

                if (!stack.isEmpty()) {

                    int needed = (i == 2 ? 4 : 9);
                    int remaining = iterateThroughInventory(invStack, CompressionRegistry.getItemThreshold(invStack, needed), inv, needed, true);
                    if (remaining <= 0)
                        return stack;
                }
            }
        }

        return ItemStack.EMPTY;
    }


}
