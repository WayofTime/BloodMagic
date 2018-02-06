package WayofTime.bloodmagic.compat.jei.armourDowngrade;

import WayofTime.bloodmagic.apibutnotreally.recipe.LivingArmourDowngradeRecipe;
import WayofTime.bloodmagic.apibutnotreally.registry.LivingArmourDowngradeRecipeRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ArmourDowngradeRecipeMaker {
    @Nonnull
    public static List<ArmourDowngradeRecipeJEI> getRecipes() {
        List<LivingArmourDowngradeRecipe> recipeList = LivingArmourDowngradeRecipeRegistry.getRecipeList();
        ArrayList<ArmourDowngradeRecipeJEI> recipes = new ArrayList<ArmourDowngradeRecipeJEI>();

        for (LivingArmourDowngradeRecipe recipe : recipeList)
            recipes.add(new ArmourDowngradeRecipeJEI(recipe));

        return recipes;
    }
}
