package WayofTime.alchemicalWizardry.api.bindingRegistry;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;

public class BindingRegistry 
{
	public static List<BindingRecipe> bindingRecipes = new LinkedList();

	public static boolean isRequiredItemValid(ItemStack testItem, int currentTierAltar)
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
	
	public static ItemStack getItemForItemAndTier(ItemStack testItem, int currentTierAltar)
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
}
