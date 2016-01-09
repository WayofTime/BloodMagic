package WayofTime.bloodmagic.compat.jei.forge;

import WayofTime.bloodmagic.api.recipe.SoulForgeRecipe;
import WayofTime.bloodmagic.api.registry.SoulForgeRecipeRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SoulForgeRecipeMaker
{
    @Nonnull
    public static List<SoulForgeRecipeJEI> getRecipes()
    {
        List<SoulForgeRecipe> recipeList = SoulForgeRecipeRegistry.getRecipeList();
        ArrayList<SoulForgeRecipeJEI> recipes = new ArrayList<SoulForgeRecipeJEI>();

        for (SoulForgeRecipe recipe : recipeList)
            recipes.add(new SoulForgeRecipeJEI(recipe));

        return recipes;
    }
}
