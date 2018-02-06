package WayofTime.bloodmagic.compat.jei.forge;

import WayofTime.bloodmagic.apibutnotreally.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.apibutnotreally.registry.TartaricForgeRecipeRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TartaricForgeRecipeMaker {
    @Nonnull
    public static List<TartaricForgeRecipeJEI> getRecipes() {
        List<TartaricForgeRecipe> recipeList = TartaricForgeRecipeRegistry.getRecipeList();
        ArrayList<TartaricForgeRecipeJEI> recipes = new ArrayList<TartaricForgeRecipeJEI>();

        for (TartaricForgeRecipe recipe : recipeList)
            recipes.add(new TartaricForgeRecipeJEI(recipe));

        return recipes;
    }
}
