package WayofTime.alchemicalWizardry.common.guide;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;

public class RecipeHolder 
{
	private static List recipeList;

	public static IRecipe bloodAltarRecipe;
	public static IRecipe knifeRecipe;
	public static IRecipe divinationSigilRecipe;
	public static IRecipe waterSigilRecipe;
	public static IRecipe lavaCrystalRecipe;
	public static IRecipe lavaSigilRecipe;
	public static IRecipe blankRuneRecipe;
	public static IRecipe speedRuneRecipe;
	public static IRecipe voidSigilRecipe;
	public static IRecipe airSigilRecipe;
	public static IRecipe sightSigilRecipe;
	public static IRecipe fastMinerRecipe;
	public static IRecipe greenGroveRecipe;
	public static IRecipe sacrificeRuneRecipe;
	public static IRecipe selfSacrificeRuneRecipe;
	public static IRecipe bloodPackRecipe;
	public static IRecipe capacityRuneRecipe;
	public static IRecipe dislocationRuneRecipe;
	public static IRecipe magnetismSigilRecipe;
	public static IRecipe phantomBridgeRecipe;
	public static IRecipe holdingSigilRecipe;
	public static IRecipe affinitySigilRecipe;
	public static IRecipe weakRitualStoneRecipe;
	public static IRecipe ritualStoneRecipe;
	public static IRecipe masterStoneRecipe;
	public static IRecipe bloodLampRecipe;
	public static IRecipe emptySocketRecipe;
	public static IRecipe soulForgeRecipe;
	public static IRecipe inhibitorRecipe;
	public static IRecipe ritualDiviner1Recipe;
	public static IRecipe ritualDiviner2Recipe;
	public static IRecipe ritualDiviner3Recipe;
	public static IRecipe bloodStoneRecipe;
	public static IRecipe whirlwindSigilRecipe;
	public static IRecipe compressionSigilRecipe;
	public static IRecipe enderSeveranceSigilRecipe;
	public static IRecipe teleposerRecipe;
	public static IRecipe suppressionSigilRecipe;
	public static IRecipe superiorCapacityRecipe;
	public static IRecipe orbRuneRecipe;
	public static IRecipe keyOfBindingRecipe;
	public static IRecipe energyBazookaRecipe;
	public static IRecipe accelerationRuneRecipe;
	public static IRecipe harvestSigilRecipe;
	public static IRecipe crystalCluserRecipe;
	public static IRecipe arcanePlinthRecipe;
	public static IRecipe arcanePedestalRecipe;
	public static IRecipe spellTableRecipe;
	public static IRecipe alchemySetRecipe;
	public static IRecipe crucibleRecipe;
	
	public static IRecipe woodAshRecipe;
	public static IRecipe byrrusRecipe;
	public static IRecipe livensRecipe;
	public static IRecipe virRecipe;
	public static IRecipe purpuraRecipe;
	
	public static IRecipe routerRecipe;
	public static IRecipe segmenterRecipe;
	public static IRecipe cleanserRecipe;
	public static IRecipe calcinatorRecipe;
	public static IRecipe belljarRecipe;
	public static IRecipe relayRecipe;
	
	public static AltarRecipe weakBloodOrbRecipe;
	public static AltarRecipe apprenticeBloodOrbRecipe;
	public static AltarRecipe magicianBloodOrbRecipe;
	public static AltarRecipe masterBloodOrbRecipe;
	public static AltarRecipe archmageBloodOrbRecipe;
	public static AltarRecipe transcendentBloodOrbRecipe;
	
	public static AltarRecipe blankSlateRecipe;
	public static AltarRecipe reinforcedSlateRecipe;
	public static AltarRecipe imbuedSlateRecipe;
	public static AltarRecipe demonicSlateRecipe;
	public static AltarRecipe etherealSlateRecipe;
	public static AltarRecipe daggerRecipe;
	public static AltarRecipe weakActivationRecipe;
	public static AltarRecipe filledSocketRecipe;
	public static AltarRecipe teleposerFocusRecipe1;
	public static AltarRecipe blankSpellRecipe;
	public static AltarRecipe waterScribeTool;
	public static AltarRecipe fireScribeTool;
	public static AltarRecipe earthScribeTool;
	public static AltarRecipe airScribeTool;
	public static AltarRecipe duskRecipe;
	public static AltarRecipe dawnRecipe;
	public static AltarRecipe flaskRecipe;
	
	public static void init()
	{
		recipeList = CraftingManager.getInstance().getRecipeList();
		bloodAltarRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockAltar));
		knifeRecipe = getRecipeForItemStack(new ItemStack(ModItems.sacrificialDagger));
		divinationSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.divinationSigil));
		waterSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.waterSigil));
		lavaCrystalRecipe = getRecipeForItemStack(new ItemStack(ModItems.lavaCrystal));
		lavaSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.lavaSigil));
		blankRuneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.bloodRune));
		speedRuneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.speedRune));
		
		voidSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.voidSigil));
		airSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.airSigil));
		sightSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemSeerSigil));
		fastMinerRecipe = getRecipeForItemStack(new ItemStack(ModItems.sigilOfTheFastMiner));
		greenGroveRecipe = getRecipeForItemStack(new ItemStack(ModItems.growthSigil));
		sacrificeRuneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.runeOfSacrifice));
		selfSacrificeRuneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.runeOfSelfSacrifice));
		bloodPackRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemBloodPack));
		capacityRuneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.bloodRune, 1, 1));
		dislocationRuneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.bloodRune, 1, 2));
		magnetismSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.sigilOfMagnetism));
		phantomBridgeRecipe = getRecipeForItemStack(new ItemStack(ModItems.sigilOfTheBridge));
		holdingSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.sigilOfHolding));
		affinitySigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.sigilOfElementalAffinity));
		ritualStoneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.ritualStone));
		masterStoneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockMasterStone));
		bloodLampRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemBloodLightSigil));
		emptySocketRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.emptySocket));
		soulForgeRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.armourForge));
		inhibitorRecipe = getRecipeForItemStack(new ItemStack(ModItems.armourInhibitor));
		ritualDiviner1Recipe = getRecipeForItemStack(new ItemStack(ModItems.itemRitualDiviner));
		ritualDiviner2Recipe = getRecipeForItemStack(new ItemStack(ModItems.itemRitualDiviner, 1, 1));
		ritualDiviner3Recipe = getRecipeForItemStack(new ItemStack(ModItems.itemRitualDiviner, 1, 2));
		bloodStoneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.largeBloodStoneBrick));
		whirlwindSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.sigilOfWind));
		compressionSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemCompressionSigil));
		enderSeveranceSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemSigilOfEnderSeverance));
		teleposerRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockTeleposer));
		suppressionSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemSigilOfSupression));
		superiorCapacityRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.bloodRune, 1, 4));
		orbRuneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.bloodRune, 1, 3));
		keyOfBindingRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemKeyOfDiablo));
		energyBazookaRecipe = getRecipeForItemStack(new ItemStack(ModItems.energyBazooka));
		accelerationRuneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.bloodRune, 1, 5));
		harvestSigilRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemHarvestSigil));
		crystalCluserRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockCrystal));	
		weakRitualStoneRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.imperfectRitualStone));
		
		arcanePlinthRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockPlinth));
		arcanePedestalRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockPedestal));
		spellTableRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockHomHeart));
		
		alchemySetRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockWritingTable));
		crucibleRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockCrucible));
		woodAshRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemIncense, 1, 0));
		byrrusRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemIncense, 1, 1));
		livensRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemIncense, 1, 2));
		virRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemIncense, 1, 3));
		purpuraRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemIncense, 1, 4));
		
		routerRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemAttunedCrystal));
		segmenterRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemTankSegmenter));
		cleanserRecipe = getRecipeForItemStack(new ItemStack(ModItems.itemDestinationClearer));
		calcinatorRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockAlchemicCalcinator));
		belljarRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockCrystalBelljar));
		relayRecipe = getRecipeForItemStack(new ItemStack(ModBlocks.blockReagentConduit));
		
		weakBloodOrbRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.weakBloodOrb));
		apprenticeBloodOrbRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.apprenticeBloodOrb));
		magicianBloodOrbRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.magicianBloodOrb));
		masterBloodOrbRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.masterBloodOrb));
		archmageBloodOrbRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.archmageBloodOrb));
		transcendentBloodOrbRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.transcendentBloodOrb));
		
		blankSlateRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.blankSlate));
		reinforcedSlateRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.reinforcedSlate));
		imbuedSlateRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.imbuedSlate));
		demonicSlateRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.demonicSlate));
		etherealSlateRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.baseItems, 1, 27));
		daggerRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.daggerOfSacrifice));
		weakActivationRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.activationCrystal, 1, 0));
		filledSocketRecipe = getAltarRecipeForItemStack(new ItemStack(ModBlocks.bloodSocket));
		teleposerFocusRecipe1 = getAltarRecipeForItemStack(new ItemStack(ModItems.telepositionFocus));
		blankSpellRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.blankSpell));
		waterScribeTool = getAltarRecipeForItemStack(new ItemStack(ModItems.waterScribeTool));
		fireScribeTool = getAltarRecipeForItemStack(new ItemStack(ModItems.fireScribeTool));
		earthScribeTool = getAltarRecipeForItemStack(new ItemStack(ModItems.earthScribeTool));
		airScribeTool = getAltarRecipeForItemStack(new ItemStack(ModItems.airScribeTool));
		duskRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.duskScribeTool));
		dawnRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.dawnScribeTool));
		flaskRecipe = getAltarRecipeForItemStack(new ItemStack(ModItems.alchemyFlask));
	}
	
	private static IRecipe getRecipeForItemStack(ItemStack stack)
	{		
		for(Object obj : recipeList)
		{
			IRecipe recipe = (IRecipe)obj;
			if(recipe.getRecipeOutput() != null && stack.isItemEqual(recipe.getRecipeOutput()))
			{
				return recipe;
			}
		}
		
		return null;
	}
	
	private static AltarRecipe getAltarRecipeForItemStack(ItemStack stack)
	{
		for(AltarRecipe recipe : AltarRecipeRegistry.altarRecipes)
		{
			if(recipe.getResult() != null && stack.isItemEqual(recipe.getResult()))
			{
				return recipe;
			}
		}
		
		return null;
	}
}
