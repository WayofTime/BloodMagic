package WayofTime.alchemicalWizardry.api.bindingRegistry;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class BindingRegistry 
{
	public static List<BindingRecipe> bindingRecipes = new LinkedList();

	public static void registerRecipe(ItemStack output, ItemStack input)
	{
		bindingRecipes.add(new BindingRecipe(output, input));
	}
	
	public static boolean isRequiredItemValid(ItemStack testItem)
	{
		for(BindingRecipe recipe : bindingRecipes)
		{
			if(recipe.doesRequiredItemMatch(testItem))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static ItemStack getItemForItemAndTier(ItemStack testItem)
	{
		for(BindingRecipe recipe : bindingRecipes)
		{
			if(recipe.doesRequiredItemMatch(testItem))
			{
				return recipe.getResult().copy();
			}
		}
		
		return null;
	}
	
	public static int getIndexForItem(ItemStack testItem)
	{
		int i=0;
		for(BindingRecipe recipe : bindingRecipes)
		{
			if(recipe.doesRequiredItemMatch(testItem))
			{
				return i;
			}
			i++;
		}
		
		return -1;
	}
	
	public static ItemStack getOutputForIndex(int index)
	{
		if(bindingRecipes.size()<=index)
		{
			return null;
		}
		
		return bindingRecipes.get(index).getResult();
	}
}
