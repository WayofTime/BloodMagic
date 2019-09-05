package WayofTime.bloodmagic.compress;

import WayofTime.bloodmagic.util.BMLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class StorageBlockCraftingManager {
    private static final StorageBlockCraftingManager instance = new StorageBlockCraftingManager();
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
    static ItemStack reversibleCheck;
    private HashSet<IRecipe> recipes = new HashSet<>(); // TODO: Clear when recipes are reloaded in 1.14
    private Set<ItemStack> blacklist = new HashSet<>();

    public static boolean isResultStackReversible(ItemStack stack, World world) {
        if (stack.isEmpty()) {
            return false;
        }

        inventoryCrafting[2].setInventorySlotContents(0, stack);
        ItemStack returnStack = getNNRecipeOutput(inventoryCrafting[2], world);

        return !returnStack.isEmpty() && CompressionRegistry.areItemStacksEqual(reversibleCheck, returnStack);
    }

    public static ItemStack getRecipe(ItemStack stack, World world, int gridSize) {
        StorageBlockCraftingManager craftingManagerSB = getInstance();
        if (craftingManagerSB.blacklist.contains(stack)) {
            return ItemStack.EMPTY;
        }
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
        craftingManagerSB.blacklist.add(stack);
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

    public void addRecipe(IRecipe recipe) {
        this.recipes.add(recipe);
    }

    //recipes are currently added during runtime, this will only return recipes specifically added in init
    //through BaseCompressionHandler
    public void addStorageBlockRecipes() {

        //this.recipes = new StorageBlockCraftingRecipeAssimilator().getPackingRecipes();
        BMLog.DEBUG.info("Total number of compression recipes: " + this.recipes.size());
    }

    public ItemStack findMatchingRecipe(InventoryCrafting craftingInventory, World world) {
        return this.findMatchingRecipe(craftingInventory, world, this.recipes);
    }

    private ItemStack findMatchingRecipe(InventoryCrafting craftingInventory, World world, HashSet<IRecipe> list) {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;
        int j;

        for (j = 0; j < craftingInventory.getSizeInventory(); ++j) {
            ItemStack itemstack2 = craftingInventory.getStackInSlot(j);

            if (!itemstack2.isEmpty()) {
                if (i == 0) {
                    itemstack = itemstack2;
                }

                if (i == 1) {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.getCount() == 1 && itemstack1.getCount() == 1 && itemstack.getItem().isRepairable()) {
            Item item = itemstack.getItem();
            int j1 = item.getMaxDamage(itemstack) - itemstack.getItemDamage();
            int k = item.getMaxDamage(itemstack) - itemstack1.getItemDamage();
            int l = j1 + k + item.getMaxDamage(itemstack) * 5 / 100;
            int i1 = item.getMaxDamage(itemstack) - l;

            if (i1 < 0) {
                i1 = 0;
            }

            return new ItemStack(itemstack.getItem(), 1, i1);
        } else {
            for (IRecipe iRecipe : list) {

                if (iRecipe.matches(craftingInventory, world)) {
                    return iRecipe.getCraftingResult(craftingInventory);
                }
            }

            return ItemStack.EMPTY;
        }
    }

    public static StorageBlockCraftingManager getInstance() {
        return instance;
    }


}
