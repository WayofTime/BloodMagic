package WayofTime.bloodmagic.util.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;

public class RecipeHelper
{
    public static IRecipe getRecipeForOutput(ItemStack stack)
    {
        for (IRecipe recipe : CraftingManager.getInstance().getRecipeList())
        {
            if (recipe != null)
            {
                ItemStack resultStack = recipe.getRecipeOutput();
                if (resultStack != null && resultStack.getItem() != null)
                {
                    if (resultStack.getItem() == stack.getItem() && resultStack.getItemDamage() == stack.getItemDamage())
                    {
                        return recipe;
                    }
                }
            }
        }

        return null;
    }

    public static AltarRecipeRegistry.AltarRecipe getAltarRecipeForOutput(ItemStack stack)
    {
        for (AltarRecipeRegistry.AltarRecipe recipe : AltarRecipeRegistry.getRecipes().values())
        {
            if (recipe != null && !recipe.isFillable())
            {
                ItemStack resultStack = recipe.getOutput();
                if (resultStack != null && resultStack.getItem() != null)
                {
                    if (resultStack.getItem() == stack.getItem() && resultStack.getItemDamage() == stack.getItemDamage())
                    {
                        return recipe;
                    }
                }
            }
        }

        return null;
    }

    public static TartaricForgeRecipe getForgeRecipeForOutput(ItemStack stack)
    {
        for (TartaricForgeRecipe recipe : TartaricForgeRecipeRegistry.getRecipeList())
        {
            if (recipe != null)
            {
                ItemStack resultStack = recipe.getRecipeOutput();
                if (resultStack != null && resultStack.getItem() != null)
                {
                    if (resultStack.getItem() == stack.getItem() && resultStack.getItemDamage() == stack.getItemDamage())
                    {
                        return recipe;
                    }
                }
            }
        }

        return null;
    }
}
