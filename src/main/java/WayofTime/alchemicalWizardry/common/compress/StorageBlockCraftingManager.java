package WayofTime.alchemicalWizardry.common.compress;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

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
    	
    	System.out.println("Total number of compression recipes: " + this.recipes.size());
//    	List<IRecipe> tempRecipeList = new LinkedList();
//    	
//		World world = DimensionManager.getWorld(0);
//		
//		for(Object obj : this.recipes)
//		{
//			if(obj instanceof IRecipe)
//			{
//				IRecipe recipe = (IRecipe)obj;
//				ItemStack outputStack = recipe.getRecipeOutput();
//				if(outputStack == null || outputStack.getItem() == null)
//				{
//					continue;
//				}
//				
//				if(recipe instanceof ShapedRecipes)
//				{
//					ShapedRecipes sRecipe = (ShapedRecipes)recipe;
//					ItemStack[] input = sRecipe.recipeItems;
//					
//					if(outputStack.stackSize == 1 && (input.length == 9 || input.length == 4))
//					{
//						tempRecipeList.add(recipe);
//					}else if((outputStack.stackSize == 9 || outputStack.stackSize == 4) && input.length == 1)
//					{
//						tempRecipeList.add(recipe);
//					}
//				}
//				else if(recipe instanceof ShapelessRecipes)
//				{
//					ShapelessRecipes sRecipe = (ShapelessRecipes)recipe;
//					List input = sRecipe.recipeItems;
//					
//					if(outputStack.stackSize == 1 && (input.size() == 9 || input.size() == 4))
//					{
//						Object obj1 = input.get(0);
//						if(obj1 != null)
//						{
//							boolean allMatch = true;
//							for(Object obj2 : input)
//							{
//								if(obj2 == null || !obj2.equals(obj1))
//								{
//									allMatch = false;
//									break;
//								}
//							}
//							if(allMatch)
//							{
//								tempRecipeList.add(recipe);
//							}
//						}
//						
//					}else if((outputStack.stackSize == 9 || outputStack.stackSize == 4) && input.size() == 1)
//					{
//						tempRecipeList.add(recipe);
//					}
//				}
//				else if((outputStack.stackSize == 1 && (recipe.getRecipeSize() == 9 || recipe.getRecipeSize() == 4)) || ((outputStack.stackSize == 9 || outputStack.stackSize == 4) && recipe.getRecipeSize() == 1))
//				{
//					tempRecipeList.add(recipe);
//					continue;
//				}
//			}
//		}
//						
//		List<IRecipe> tempRecipeList2 = new LinkedList();
//		
//    	for(Object obj : tempRecipeList)
//    	{
//    		if(obj instanceof IRecipe)
//    		{
//    			IRecipe recipe = (IRecipe)obj;
//    			ItemStack outputStack = recipe.getRecipeOutput();
//    			if(outputStack == null || outputStack.getItem() == null)
//    			{
//    				continue;
//    			}
//    			    			    			
//    			if(isResultStackReversible(outputStack, 2, world, tempRecipeList) || isResultStackReversible(outputStack, 3, world, tempRecipeList))
//    			{
//    				tempRecipeList2.add(recipe);
//    				AlchemicalWizardry.logger.info("Now adding recipe for " + outputStack + " to the compression handler.");
//    			}
//    		}
//    	}
//    	
//    	this.recipes = tempRecipeList2;
    }
    
    private static boolean isResultStackReversible(ItemStack stack, int gridSize, World world, List list)
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

		ItemStack returnStack = StorageBlockCraftingManager.getInstance().findMatchingRecipe(inventory, world, list);
		if(returnStack == null || returnStack.getItem() == null)
		{
			return false;
		}
				
		ItemStack compressedStack = null;
		switch(gridSize)
		{
		case 2:
			compressedStack = get22Recipe(returnStack, world, list);
			break;
		case 3:
			compressedStack = get33Recipe(returnStack, world, list);
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
    
    private static ItemStack getRecipe(ItemStack stack, World world, int gridSize, List list)
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
    
	public ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World p_82787_2_)
	{
		return this.findMatchingRecipe(p_82787_1_, p_82787_2_, this.recipes);
	}
	
	private ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World p_82787_2_, List list)
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
            int j1 = item.getMaxDamage() - itemstack.getItemDamage();
            int k = item.getMaxDamage() - itemstack1.getItemDamage();
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
            for (j = 0; j < list.size(); ++j)
            {
                IRecipe irecipe = (IRecipe)list.get(j);

                if (irecipe.matches(p_82787_1_, p_82787_2_))
                {
                    return irecipe.getCraftingResult(p_82787_1_);
                }
            }

            return null;
        }
    }
}
