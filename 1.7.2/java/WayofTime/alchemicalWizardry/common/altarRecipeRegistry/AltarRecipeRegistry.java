package WayofTime.alchemicalWizardry.common.altarRecipeRegistry;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;

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
		registerAltarRecipe(new ItemStack(ModItems.weakBloodOrb), new ItemStack(Items.diamond),1,2000,2,1,false);
		registerAltarRecipe(new ItemStack(ModItems.apprenticeBloodOrb), new ItemStack(Items.emerald),2,5000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.magicianBloodOrb), new ItemStack(Blocks.gold_block),3,25000,20,20,false);
		registerAltarRecipe(new ItemStack(ModItems.masterBloodOrb), new ItemStack(ModItems.weakBloodShard),4,40000,30,50,false);
		registerAltarRecipe(new ItemStack(ModItems.archmageBloodOrb), new ItemStack(ModItems.demonBloodShard),5,75000,50,100,false);

		registerAltarOrbRecipe(new ItemStack(ModItems.weakBloodOrb),1,2);
		registerAltarOrbRecipe(new ItemStack(ModItems.apprenticeBloodOrb),2,5);
		registerAltarOrbRecipe(new ItemStack(ModItems.magicianBloodOrb),3,15);
		registerAltarOrbRecipe(new ItemStack(ModItems.masterBloodOrb),4,25);
		registerAltarOrbRecipe(new ItemStack(ModItems.archmageBloodOrb),5,50);
		
		registerAltarRecipe(new ItemStack(ModItems.telepositionFocus), new ItemStack(Items.ender_pearl),4,2000,10,10,false);
		registerAltarRecipe(new ItemStack(ModItems.enhancedTelepositionFocus), new ItemStack(ModItems.telepositionFocus),4,10000,25,15,false);
		registerAltarRecipe(new ItemStack(ModItems.imbuedSlate), new ItemStack(ModItems.imbuedSlate),4,15000,20,20,false);
		registerAltarRecipe(new ItemStack(ModItems.duskScribeTool), new ItemStack(Blocks.coal_block),4,2000,20,10,false);
		registerAltarRecipe(new ItemStack(ModBlocks.bloodSocket), new ItemStack(ModBlocks.emptySocket),3,30000,40,10,false);
		registerAltarRecipe(new ItemStack(ModItems.earthScribeTool), new ItemStack(Blocks.obsidian),3,1000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.waterScribeTool), new ItemStack(Blocks.lapis_block),3,1000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.blankSpell), new ItemStack(Blocks.glass),2,1000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.blankSlate), new ItemStack(Blocks.stone),1,1000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.activationCrystal), new ItemStack(ModItems.lavaCrystal),3,10000,20,10,false);
		registerAltarRecipe(new ItemStack(ModItems.fireScribeTool), new ItemStack(Items.magma_cream),3,1000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.airScribeTool), new ItemStack(Items.ghast_tear),3,1000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.imbuedSlate), new ItemStack(ModItems.reinforcedSlate),3,5000,15,10,false);
		registerAltarRecipe(new ItemStack(ModItems.daggerOfSacrifice), new ItemStack(Items.iron_sword),2,3000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.alchemyFlask), new ItemStack(Items.glass_bottle),2,2000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.reinforcedSlate), new ItemStack(ModItems.blankSlate),2,2000,5,5,false);
		registerAltarRecipe(new ItemStack(ModItems.bucketLife), new ItemStack(Items.bucket),1,1000,5,0,false);
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
				return ItemStack.copyItemStack(recipe.getResult());
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
