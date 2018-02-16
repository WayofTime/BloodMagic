package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.core.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.core.registry.TartaricForgeRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class RecipeHelper {
    public static IRecipe getRecipeForOutput(ItemStack stack) {
        for (IRecipe recipe : ForgeRegistries.RECIPES.getValues()) {
            if (recipe != null) {
                ItemStack resultStack = recipe.getRecipeOutput();
                if (!resultStack.isEmpty()) {
                    if (resultStack.getItem() == stack.getItem() && resultStack.getItemDamage() == stack.getItemDamage()) {
                        return recipe;
                    }
                }
            }
        }

        return null;
    }

    public static AltarRecipeRegistry.AltarRecipe getAltarRecipeForOutput(ItemStack stack) {
        for (AltarRecipeRegistry.AltarRecipe recipe : AltarRecipeRegistry.getRecipes().values()) {
            if (recipe != null && !recipe.isFillable()) {
                ItemStack resultStack = recipe.getOutput();
                if (!resultStack.isEmpty()) {
                    if (resultStack.getItem() == stack.getItem() && resultStack.getItemDamage() == stack.getItemDamage()) {
                        return recipe;
                    }
                }
            }
        }

        return null;
    }

    public static TartaricForgeRecipe getForgeRecipeForOutput(ItemStack stack) {
        for (TartaricForgeRecipe recipe : TartaricForgeRecipeRegistry.getRecipeList()) {
            if (recipe != null) {
                ItemStack resultStack = recipe.getRecipeOutput();
                if (!resultStack.isEmpty()) {
                    if (resultStack.getItem() == stack.getItem() && resultStack.getItemDamage() == stack.getItemDamage()) {
                        return recipe;
                    }
                }
            }
        }

        return null;
    }
}
