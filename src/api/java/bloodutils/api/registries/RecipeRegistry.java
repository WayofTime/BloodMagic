package bloodutils.api.registries;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import WayofTime.alchemicalWizardry.api.items.ShapedBloodOrbRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeRegistry {
public static ArrayList<IRecipe> craftingRecipes = new ArrayList<IRecipe>();
public static ArrayList<AltarRecipe> altarRecipes = new ArrayList<AltarRecipe>();

	/** Used to register crafting recipes to the guide */
	public static IRecipe getLatestCraftingRecipe(){
		IRecipe rec = (IRecipe)CraftingManager.getInstance().getRecipeList().get(CraftingManager.getInstance().getRecipeList().size() -1);
		craftingRecipes.add(rec);
		return craftingRecipes.get(craftingRecipes.size() - 1);
	}
	
	/** Used to register items to the guide */
	public static AltarRecipe getLatestAltarRecipe(){
		AltarRecipe rec = (AltarRecipe)AltarRecipeRegistry.altarRecipes.get(AltarRecipeRegistry.altarRecipes.size() - 1);
		altarRecipes.add(rec);
		return altarRecipes.get(altarRecipes.size() - 1);
	}
	
	public static void addAltarRecipe(ItemStack result, ItemStack requiredItem, int minTier, int liquidRequired, int consumptionRate, int drainRate, boolean canBeFilled){
		AltarRecipeRegistry.registerAltarRecipe(result, requiredItem, minTier, liquidRequired, consumptionRate, drainRate, canBeFilled);
	}
	
	public static void addShapedRecipe(ItemStack output, Object[] obj){
		GameRegistry.addShapedRecipe(output, obj);
	}
	
	public static void addShapedOrbRecipe(ItemStack output, Object[] obj){
		GameRegistry.addRecipe(new ShapedBloodOrbRecipe(output, obj));
	}
	
	public static void addShapedOreRecipe(ItemStack output, Object[] obj){
		GameRegistry.addRecipe(new ShapedOreRecipe(output, obj));
	}
}