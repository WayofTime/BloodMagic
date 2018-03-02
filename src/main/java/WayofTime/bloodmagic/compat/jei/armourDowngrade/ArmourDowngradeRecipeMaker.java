package WayofTime.bloodmagic.compat.jei.armourDowngrade;

import WayofTime.bloodmagic.recipe.LivingArmourDowngradeRecipe;
import WayofTime.bloodmagic.core.registry.LivingArmourDowngradeRecipeRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ArmourDowngradeRecipeMaker {
    @Nonnull
    public static List<ArmourDowngradeRecipeJEI> getRecipes() {
        List<LivingArmourDowngradeRecipe> recipeList = LivingArmourDowngradeRecipeRegistry.getRecipeList();
        ArrayList<ArmourDowngradeRecipeJEI> recipes = new ArrayList<>();

        for (LivingArmourDowngradeRecipe recipe : recipeList)
            recipes.add(new ArmourDowngradeRecipeJEI(recipe));

        return recipes;
    }
}
