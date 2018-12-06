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
                    2, 2),
            new InventoryCrafting(new Container() {
                public boolean canInteractWith(EntityPlayer player) {
                    return false;
                }
            },
                    1, 1)

    };

    private static ItemStack reversibleCheck;

    public static boolean isResultStackReversible(ItemStack stack, World world) {
        if (stack.isEmpty()) {
            return false;
        }

        inventoryCrafting[2].setInventorySlotContents(0, stack);
        ItemStack returnStack = getNNRecipeOutput(inventoryCrafting[2], world);

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

        if (isResultStackReversible(result, world)) {
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

    public ItemStack compressInventory(ItemStack[] inv, World world) {
        for (ItemStack invStack : inv) {
            if (invStack.isEmpty()) {
                continue;
            }

            for (int i = 3; i >= 2; i--) {
                reversibleCheck = invStack;
                ItemStack stack = getRecipe(invStack, world, i);
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
