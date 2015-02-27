package WayofTime.alchemicalWizardry.common.compress;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class StorageBlockCraftingManager 
{
    private static final StorageBlockCraftingManager instance = new StorageBlockCraftingManager();
    private List recipes = new ArrayList();

    public static StorageBlockCraftingManager getInstance()
    {
    	return instance;
    }
    
    public void addStorageBlockRecipes()
    {
    	List list = CraftingManager.getInstance().getRecipeList();
    	
		World world = DimensionManager.getWorld(0);
    	for(Object obj : list)
    	{
    		if(obj instanceof IRecipe)
    		{
    			IRecipe recipe = (IRecipe)obj;
    			ItemStack outputStack = recipe.getRecipeOutput();
    			if(outputStack == null || outputStack.getItem() == null)
    			{
    				continue;
    			}
    			    			    			
    			if(isResultStackReversible(outputStack, 2, world) || isResultStackReversible(outputStack, 3, world))
    			{
    				recipes.add(recipe);
    			}
    		}
    	}
    }
    
    public static boolean isResultStackReversible(ItemStack stack, int gridSize, World world)
	{
		if(stack == null)
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

		ItemStack returnStack = CraftingManager.getInstance().findMatchingRecipe(inventory, world);
		if(returnStack == null)
		{
			return false;
		}
				
		ItemStack compressedStack = null;
		switch(gridSize)
		{
		case 2:
			compressedStack = get22Recipe(returnStack, world);
			break;
		case 3:
			compressedStack = get33Recipe(returnStack, world);
			break;
		}
		
		if(compressedStack == null)
		{
			return false;
		}else
		{
			return SpellHelper.areItemStacksEqual(stack, compressedStack);
		}
	}
    
    public static ItemStack getRecipe(ItemStack stack, World world, int gridSize)
	{
		InventoryCrafting inventory = new InventoryCrafting(new Container()
	    {
	        public boolean canInteractWith(EntityPlayer player)
	        {
	            return false;
	        }
	    }, gridSize, gridSize);
		for(int i=0; i<inventory.getSizeInventory(); i++)
		{
			inventory.setInventorySlotContents(i, stack);
		}
		
		return CraftingManager.getInstance().findMatchingRecipe(inventory, world);
	}
	
	public static boolean has22Recipe(ItemStack stack, World world)
	{
		return get22Recipe(stack, world) != null;
	}
	
	public static ItemStack get22Recipe(ItemStack stack, World world)
	{
		return getRecipe(stack, world, 2);
	}
	
	public static boolean has33Recipe(ItemStack stack, World world)
	{
		return get33Recipe(stack, world) != null;
	}
	
	public static ItemStack get33Recipe(ItemStack stack, World world)
	{
		return getRecipe(stack, world, 3);
	}
    
	public ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World p_82787_2_)
    {
        int i = 0;
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        int j;

        for (j = 0; j < p_82787_1_.getSizeInventory(); ++j)
        {
            ItemStack itemstack2 = p_82787_1_.getStackInSlot(j);

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
            int j1 = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
            int k = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
            int l = j1 + k + item.getMaxDamage() * 5 / 100;
            int i1 = item.getMaxDamage() - l;

            if (i1 < 0)
            {
                i1 = 0;
            }

            return new ItemStack(itemstack.getItem(), 1, i1);
        }
        else
        {
            for (j = 0; j < this.recipes.size(); ++j)
            {
                IRecipe irecipe = (IRecipe)this.recipes.get(j);

                if (irecipe.matches(p_82787_1_, p_82787_2_))
                {
                    return irecipe.getCraftingResult(p_82787_1_);
                }
            }

            return null;
        }
    }
}
