package WayofTime.bloodmagic.compress;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.compress.CompressionRegistry;

public class StorageBlockCraftingManager
{
    private static final StorageBlockCraftingManager instance = new StorageBlockCraftingManager();
    private List recipes = new LinkedList();

    public static StorageBlockCraftingManager getInstance()
    {
        return instance;
    }

    public void addStorageBlockRecipes()
    {
        this.recipes = new StorageBlockCraftingRecipeAssimilator().getPackingRecipes();

        BloodMagic.instance.getLogger().info("Total number of compression recipes: " + this.recipes.size());
    }

    private static boolean isResultStackReversible(ItemStack stack, int gridSize, World world, List list)
    {
        if (stack == null)
        {
            return false;
        }
        InventoryCrafting inventory = new InventoryCrafting(new Container()
        {
            public boolean canInteractWith(EntityPlayer player)
            {
                return false;
            }
        }, 2, 2);

        inventory.setInventorySlotContents(0, stack);

        ItemStack returnStack = StorageBlockCraftingManager.getInstance().findMatchingRecipe(inventory, world, list);
        if (returnStack == null || returnStack.getItem() == null)
        {
            return false;
        }

        ItemStack compressedStack = null;
        switch (gridSize)
        {
        case 2:
            compressedStack = get22Recipe(returnStack, world, list);
            break;
        case 3:
            compressedStack = get33Recipe(returnStack, world, list);
            break;
        }

        return compressedStack != null && CompressionRegistry.areItemStacksEqual(stack, compressedStack);
    }

    private static ItemStack getRecipe(ItemStack stack, World world, int gridSize, List list)
    {
        InventoryCrafting inventory = new InventoryCrafting(new Container()
        {
            public boolean canInteractWith(EntityPlayer player)
            {
                return false;
            }
        }, gridSize, gridSize);
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            inventory.setInventorySlotContents(i, stack);
        }

        return StorageBlockCraftingManager.getInstance().findMatchingRecipe(inventory, world, list);
    }

    private static boolean has22Recipe(ItemStack stack, World world, List list)
    {
        return get22Recipe(stack, world, list) != null;
    }

    private static ItemStack get22Recipe(ItemStack stack, World world, List list)
    {
        return getRecipe(stack, world, 2, list);
    }

    private static boolean has33Recipe(ItemStack stack, World world, List list)
    {
        return get33Recipe(stack, world, list) != null;
    }

    private static ItemStack get33Recipe(ItemStack stack, World world, List list)
    {
        return getRecipe(stack, world, 3, list);
    }

    public ItemStack findMatchingRecipe(InventoryCrafting craftingInventory, World world)
    {
        return this.findMatchingRecipe(craftingInventory, world, this.recipes);
    }

    private ItemStack findMatchingRecipe(InventoryCrafting craftingInventory, World world, List list)
    {
        int i = 0;
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        int j;

        for (j = 0; j < craftingInventory.getSizeInventory(); ++j)
        {
            ItemStack itemstack2 = craftingInventory.getStackInSlot(j);

            if (itemstack2 != null)
            {
                if (i == 0)
                {
                    itemstack = itemstack2;
                }

                if (i == 1)
                {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && itemstack.getItem().isRepairable())
        {
            Item item = itemstack.getItem();
            int j1 = item.getMaxDamage() - itemstack.getItemDamage();
            int k = item.getMaxDamage() - itemstack1.getItemDamage();
            int l = j1 + k + item.getMaxDamage() * 5 / 100;
            int i1 = item.getMaxDamage() - l;

            if (i1 < 0)
            {
                i1 = 0;
            }

            return new ItemStack(itemstack.getItem(), 1, i1);
        } else
        {
            for (j = 0; j < list.size(); ++j)
            {
                IRecipe irecipe = (IRecipe) list.get(j);

                if (irecipe.matches(craftingInventory, world))
                {
                    return irecipe.getCraftingResult(craftingInventory);
                }
            }

            return null;
        }
    }
}
