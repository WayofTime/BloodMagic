package WayofTime.alchemicalWizardry.common.book;

import java.util.HashMap;
import java.util.Map;

import WayofTime.alchemicalWizardry.book.registries.RecipeRegistry;
import net.minecraft.item.crafting.IRecipe;

public class SpecialEntryRegistry 
{
	public static Map<String, IRecipe> recipeStringMap = new HashMap();
	
	public static void registerIRecipeKey(IRecipe recipe, String key)
	{
		recipeStringMap.put(key, recipe);
	}
	
	public static IRecipe getIRecipeForKey(String str)
	{
		return recipeStringMap.get(str);
	}
	
	public static void registerLatestIRecipe(String key)
	{
        SpecialEntryRegistry.registerIRecipeKey(RecipeRegistry.getLatestCraftingRecipe(), key);
	}
}
