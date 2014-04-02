package WayofTime.alchemicalWizardry.api.altarRecipeRegistry;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AltarRecipeRegistry 
{
	public static List<AltarRecipe> altarRecipes = new LinkedList();
	
	public static void registerAltarRecipe(ItemStack result, ItemStack requiredItem, int minTier, int liquidRequired, int consumptionRate, int drainRate, boolean canBeFilled)
	{
		altarRecipes.add(new AltarRecipe(result, requiredItem, minTier, liquidRequired, consumptionRate, drainRate, canBeFilled));
		//EXAMPLE: registerAltarRecipe(new ItemStack(ModItems.telepositionFocus), new ItemStack(Item.enderPearl),4,2000,10,10,false);
	}
	
	public static void registerAltarOrbRecipe(ItemStack orbStack, int minTier, int consumptionRate)
	{
		registerAltarRecipe(null, orbStack, minTier, 0, consumptionRate, 0, true);
		//EXAMPLE: registerAltarOrbRecipe(new ItemStack(ModItems.weakBloodOrb),1,2);
	}
	
	public static boolean isRequiredItemValid(ItemStack testItem, int currentTierAltar)
	{
		for(AltarRecipe recipe : altarRecipes)
		{
			if(recipe.doesRequiredItemMatch(testItem, currentTierAltar))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static ItemStack getItemForItemAndTier(ItemStack testItem, int currentTierAltar)
	{
		for(AltarRecipe recipe : altarRecipes)
		{
			if(recipe.doesRequiredItemMatch(testItem, currentTierAltar))
			{
				return recipe.getResult().copy();
			}
		}
		
		return null;
	}
	
	public static AltarRecipe getAltarRecipeForItemAndTier(ItemStack testItem, int currentTierAltar)
	{
		for(AltarRecipe recipe : altarRecipes)
		{
			if(recipe.doesRequiredItemMatch(testItem, currentTierAltar))
			{
				return recipe;
			}
		}
		
		return null;
	}
}
