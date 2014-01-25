package WayofTime.alchemicalWizardry.common.altarRecipeRegistry;

import java.util.LinkedList;
import java.util.List;

import WayofTime.alchemicalWizardry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AltarRecipeRegistry 
{
	public static List<AltarRecipe> altarRecipes = new LinkedList();
	
	public static void registerAltarRecipe(ItemStack result, ItemStack requiredItem, int minTier, int liquidRequired, int consumptionRate, int drainRate, boolean canBeFilled)
	{
		altarRecipes.add(new AltarRecipe(result, requiredItem, minTier, liquidRequired, consumptionRate, drainRate, canBeFilled));
	}
	
	public static void registerAltarOrbRecipe(ItemStack orbStack, int minTier, int consumptionRate)
	{
		registerAltarRecipe(null, orbStack, minTier, 0, consumptionRate, 0, true);
	}
	
	public static void initRecipes()
	{
		registerAltarRecipe(new ItemStack(ModItems.weakBloodOrb), new ItemStack(Item.diamond),1,2000,2,1,false);
		registerAltarRecipe(new ItemStack(ModItems.apprenticeBloodOrb), new ItemStack(Item.emerald),2,5000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.magicianBloodOrb), new ItemStack(Block.blockGold),3,5000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.masterBloodOrb), new ItemStack(ModItems.weakBloodShard),4,40000,30,50,false);
		registerAltarRecipe(new ItemStack(ModItems.archmageBloodOrb), new ItemStack(ModItems.demonBloodShard),5,50000,5,5,false);

		registerAltarOrbRecipe(new ItemStack(ModItems.weakBloodOrb),1,2);
		registerAltarOrbRecipe(new ItemStack(ModItems.apprenticeBloodOrb),2,5);
		registerAltarOrbRecipe(new ItemStack(ModItems.magicianBloodOrb),3,5);
		registerAltarOrbRecipe(new ItemStack(ModItems.masterBloodOrb),4,25);
		registerAltarOrbRecipe(new ItemStack(ModItems.archmageBloodOrb),5,50);
		
		registerAltarRecipe(new ItemStack(ModItems.telepositionFocus), new ItemStack(Item.enderPearl),4,2000,10,10,false);
		registerAltarRecipe(new ItemStack(ModItems.enhancedTelepositionFocus), new ItemStack(ModItems.telepositionFocus),4,10000,25,15,false);
		registerAltarRecipe(new ItemStack(ModItems.imbuedSlate), new ItemStack(ModItems.imbuedSlate),4,15000,20,20,false);

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
				return recipe.getResult();
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
