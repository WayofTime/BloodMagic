package WayofTime.bloodmagic.compat.jei.forge;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;

public class TartaricForgeRecipeMaker
{
    @Nonnull
    public static List<TartaricForgeRecipeJEI> getRecipes()
    {
        List<TartaricForgeRecipe> recipeList = TartaricForgeRecipeRegistry.getRecipeList();
        ArrayList<TartaricForgeRecipeJEI> recipes = new ArrayList<TartaricForgeRecipeJEI>();

        for (TartaricForgeRecipe recipe : recipeList)
            recipes.add(new TartaricForgeRecipeJEI(recipe));

        return recipes;
    }
}
