package WayofTime.bloodmagic.compat.jei.alchemyTable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyTableRecipeRegistry;

public class AlchemyTableRecipeMaker
{
    @Nonnull
    public static List<AlchemyTableRecipeJEI> getRecipes()
    {
        List<AlchemyTableRecipe> recipeList = AlchemyTableRecipeRegistry.getRecipeList();
        ArrayList<AlchemyTableRecipeJEI> recipes = new ArrayList<AlchemyTableRecipeJEI>();

        for (AlchemyTableRecipe recipe : recipeList)
            recipes.add(new AlchemyTableRecipeJEI(recipe));

        return recipes;
    }
}
