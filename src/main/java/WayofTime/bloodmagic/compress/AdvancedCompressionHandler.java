package WayofTime.bloodmagic.compress;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;


public class AdvancedCompressionHandler extends CompressionHandler {

    private static InventoryCrafting[] inventoryCrafting = {
            new InventoryCrafting(new Container() {
                public boolean canInteractWith(EntityPlayer player) {
                    return false;
                }
            },
                    3, 3),
            new InventoryCrafting(new Container() {
                public boolean canInteractWith(EntityPlayer player) {
                    return false;
                }
            },
                    2, 2)

    };

    private static ItemStack reversibleCheck;

    public static boolean isResultStackReversible(ItemStack stack, World world, InventoryCrafting inventory) {
        if (stack.isEmpty()) {
            return false;
        }

        inventory.setInventorySlotContents(0, stack);
        ItemStack returnStack = getNNRecipeOutput(inventory, world);

        return !returnStack.isEmpty() && CompressionRegistry.areItemStacksEqual(reversibleCheck, returnStack);
    }

    public static ItemStack getRecipe(ItemStack stack, World world, int gridSize) {
        StorageBlockCraftingManager craftingManagerSB = StorageBlockCraftingManager.getInstance();
        InventoryCrafting inventory = inventoryCrafting[3 - gridSize];
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            inventory.setInventorySlotContents(i, stack);
        }
        ItemStack notEmptyRecipe = craftingManagerSB.findMatchingRecipe(inventory, world);
        if (!notEmptyRecipe.isEmpty()) {
            return notEmptyRecipe;
        }
        ItemStack result = getNNRecipeOutput(inventory, world);

        if (isResultStackReversible(result, world, inventory)) {
            craftingManagerSB.addRecipe(CraftingManager.findMatchingRecipe(inventory, world));
            return result;
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getNNRecipeOutput(InventoryCrafting inventory, World world) {
        IRecipe checkForNull = CraftingManager.findMatchingRecipe(inventory, world);
        if (checkForNull != null) {
            return checkForNull.getRecipeOutput();
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack get22Recipe(ItemStack stack, World world) {
        return getRecipe(stack, world, 2);
    }

    public static ItemStack get33Recipe(ItemStack stack, World world) {
        return getRecipe(stack, world, 3);
    }

    @Override
    public ItemStack compressInventory(ItemStack[] inv, World world) {
        return test(inv, true, world);

    }

    public ItemStack test(ItemStack[] inv, boolean doDrain, World world) {
        for (ItemStack invStack : inv) {
            if (invStack.isEmpty()) {
                continue;
            }

            for (int i = 3; i >= 2; i--) {
                reversibleCheck = invStack;
                ItemStack stack = getRecipe(invStack, world, i);
                if (!stack.isEmpty()) {

                    int needed = (i == 2 ? 4 : 9);
                    int remaining = iterateThroughInventory(invStack, invStack.getMaxStackSize() - needed, inv, needed, false);
                    if (remaining <= 0) {
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
}
