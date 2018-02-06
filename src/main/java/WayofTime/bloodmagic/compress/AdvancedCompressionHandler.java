package WayofTime.bloodmagic.compress;

import WayofTime.bloodmagic.apibutnotreally.compress.CompressionHandler;
import WayofTime.bloodmagic.apibutnotreally.compress.CompressionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AdvancedCompressionHandler extends CompressionHandler {
    @Override
    public ItemStack compressInventory(ItemStack[] inv, World world) {
        return test(inv, true, world);
    }

    public ItemStack test(ItemStack[] inv, boolean doDrain, World world) {
        for (ItemStack invStack : inv) {
            if (invStack.isEmpty()) {
                continue;
            }

            for (int i = 2; i <= 3; i++) {
                ItemStack stack = getRecipe(invStack, world, i);
                if (!stack.isEmpty()) {
                    int threshold = CompressionRegistry.getItemThreshold(invStack);

                    int needed = i * i;
                    int neededLeft = iterateThroughInventory(invStack, threshold + invStack.getMaxStackSize() - needed, inv, needed, false);
                    if (neededLeft <= 0) {
                        iterateThroughInventory(invStack, 0, inv, needed, true);
                        return stack;
                    }
                }
            }
        }

        return ItemStack.EMPTY;
    }

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

                kept -= used;

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

    public static boolean isResultStackReversible(ItemStack stack, int gridSize, World world) {
        if (stack.isEmpty()) {
            return false;
        }
        InventoryCrafting inventory = new InventoryCrafting(new Container() {
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 2, 2);

        inventory.setInventorySlotContents(0, stack);

        ItemStack returnStack = StorageBlockCraftingManager.getInstance().findMatchingRecipe(inventory, world);
        if (returnStack.isEmpty()) {
            return false;
        }

        ItemStack compressedStack = ItemStack.EMPTY;
        switch (gridSize) {
            case 2:
                compressedStack = get22Recipe(returnStack, world);
                break;
            case 3:
                compressedStack = get33Recipe(returnStack, world);
                break;
        }

        return !compressedStack.isEmpty() && CompressionRegistry.areItemStacksEqual(stack, compressedStack);
    }

    public static ItemStack getRecipe(ItemStack stack, World world, int gridSize) {
        InventoryCrafting inventory = new InventoryCrafting(new Container() {
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, gridSize, gridSize);
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            inventory.setInventorySlotContents(i, stack);
        }

        return StorageBlockCraftingManager.getInstance().findMatchingRecipe(inventory, world);
    }

    public static boolean has22Recipe(ItemStack stack, World world) {
        return !get22Recipe(stack, world).isEmpty();
    }

    public static ItemStack get22Recipe(ItemStack stack, World world) {
        return getRecipe(stack, world, 2);
    }

    public static boolean has33Recipe(ItemStack stack, World world) {
        return !get33Recipe(stack, world).isEmpty();
    }

    public static ItemStack get33Recipe(ItemStack stack, World world) {
        return getRecipe(stack, world, 3);
    }
}
