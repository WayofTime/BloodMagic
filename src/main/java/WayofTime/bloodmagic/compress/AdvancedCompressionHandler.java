package WayofTime.bloodmagic.compress;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;


public class AdvancedCompressionHandler extends CompressionHandler {
    private static ItemStack reversibleCheck;
    @Override
    public ItemStack compressInventory(ItemStack[] inv, World world) {
        return test(inv, true, world);

    }

    public ItemStack test(ItemStack[] inv, boolean doDrain, World world) {
        for (ItemStack invStack : inv) {
            if (invStack.isEmpty() || invStack.equals(ItemStack.EMPTY)) {
                continue;
            }

            for (int i = 3; i >= 2; i--) {
                reversibleCheck = invStack;
                ItemStack stack = getRecipe(invStack, world, i);
                if (!stack.isEmpty() && !invStack.equals(ItemStack.EMPTY) && invStack != ItemStack.EMPTY) {
                    //int threshold = CompressionRegistry.getItemThreshold(invStack); //currently set to a full stack at all times

                    int needed = (i == 2 ? 4 : 9);
                    int remaining = iterateThroughInventory(invStack, /*threshold +*/ invStack.getMaxStackSize() - needed, inv, needed, false);
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

            if (invStack == null || invStack.isEmpty() || invStack.equals(ItemStack.EMPTY)) {
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
        ItemStack returnStack;
        if (stack.isEmpty()) {
            return false;
        }
        InventoryCrafting inventory = new InventoryCrafting(new Container() {
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 2, 2);

        inventory.setInventorySlotContents(0, stack);
        try {
            returnStack = CraftingManager.findMatchingRecipe(inventory, world).getRecipeOutput();
        }catch(NullPointerException e){
            return false;
        }


       /* ItemStack compressedStack = ItemStack.EMPTY;
        switch (gridSize) {
            case 2:
                compressedStack = get22Recipe(returnStack, world);
                break;
            case 3:
                compressedStack = get33Recipe(returnStack, world);
                break;
        }*/

        return !returnStack.isEmpty() && CompressionRegistry.areItemStacksEqual(reversibleCheck, returnStack);
    }

    public static ItemStack getRecipe(ItemStack stack, World world, int gridSize) {
        ItemStack result;
        InventoryCrafting inventory = new InventoryCrafting(new Container() {
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, gridSize, gridSize);
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            inventory.setInventorySlotContents(i, stack);
        }
        if(!StorageBlockCraftingManager.getInstance().findMatchingRecipe(inventory, world).isEmpty()){
            return StorageBlockCraftingManager.getInstance().findMatchingRecipe(inventory, world);
        }

        try{result = CraftingManager.findMatchingRecipe(inventory, world).getRecipeOutput();
        }catch(NullPointerException e){
            return ItemStack.EMPTY;
        }
        if(isResultStackReversible(result, gridSize, world)){
            StorageBlockCraftingManager.getInstance().addRecipe(CraftingManager.findMatchingRecipe(inventory, world));
            return result; //StorageBlockCraftingManager.getInstance().findMatchingRecipe(inventory, world);
        }
        return ItemStack.EMPTY;


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
