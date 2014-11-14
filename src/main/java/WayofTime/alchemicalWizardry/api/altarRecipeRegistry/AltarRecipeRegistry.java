package WayofTime.alchemicalWizardry.api.altarRecipeRegistry;

import net.minecraft.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class AltarRecipeRegistry
{
    public static List<AltarRecipe> altarRecipes = new LinkedList();

    public static void registerAltarRecipe(ItemStack result, ItemStack requiredItem, int minTier, int liquidRequired, int consumptionRate, int drainRate, boolean canBeFilled)
    {
        altarRecipes.add(new AltarRecipe(result, requiredItem, minTier, liquidRequired, consumptionRate, drainRate, canBeFilled));
    }
    
    public static void registerNBTAltarRecipe(ItemStack result, ItemStack requiredItem, int minTier, int liquidRequired, int consumptionRate, int drainRate, boolean canBeFilled)
    {
    	altarRecipes.add(new AltarRecipe(result, requiredItem, minTier, liquidRequired, consumptionRate, drainRate, canBeFilled, true));
    }

    public static void registerAltarOrbRecipe(ItemStack orbStack, int minTier, int consumptionRate)
    {
        registerAltarRecipe(null, orbStack, minTier, 0, consumptionRate, 0, true);
    }

    public static boolean isRequiredItemValid(ItemStack testItem, int currentTierAltar)
    {
        for (AltarRecipe recipe : altarRecipes)
        {
            if (recipe.doesRequiredItemMatch(testItem, currentTierAltar))
            {
                return true;
            }
        }

        return false;
    }

    public static ItemStack getItemForItemAndTier(ItemStack testItem, int currentTierAltar)
    {
        for (AltarRecipe recipe : altarRecipes)
        {
            if (recipe.doesRequiredItemMatch(testItem, currentTierAltar))
            {
                return ItemStack.copyItemStack(recipe.getResult());
            }
        }

        return null;
    }

    public static AltarRecipe getAltarRecipeForItemAndTier(ItemStack testItem, int currentTierAltar)
    {
        for (AltarRecipe recipe : altarRecipes)
        {
            if (recipe.doesRequiredItemMatch(testItem, currentTierAltar))
            {
                return recipe;
            }
        }

        return null;
    }
}
