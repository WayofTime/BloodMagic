package WayofTime.bloodmagic.compat.jei.alchemyTable;

import WayofTime.bloodmagic.apibutnotreally.recipe.AlchemyTableRecipe;
import WayofTime.bloodmagic.apibutnotreally.registry.AlchemyTableRecipeRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AlchemyTableRecipeMaker {
    @Nonnull
    public static List<AlchemyTableRecipeJEI> getRecipes() {
        List<AlchemyTableRecipe> recipeList = AlchemyTableRecipeRegistry.getRecipeList();
        ArrayList<AlchemyTableRecipeJEI> recipes = new ArrayList<AlchemyTableRecipeJEI>();

        for (AlchemyTableRecipe recipe : recipeList)
            recipes.add(new AlchemyTableRecipeJEI(recipe));

        return recipes;
    }
}
