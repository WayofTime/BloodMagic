package WayofTime.bloodmagic.compat.jei.armourDowngrade;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import WayofTime.bloodmagic.api.recipe.LivingArmourDowngradeRecipe;
import WayofTime.bloodmagic.api.registry.LivingArmourDowngradeRecipeRegistry;

public class ArmourDowngradeRecipeMaker
{
    @Nonnull
    public static List<ArmourDowngradeRecipeJEI> getRecipes()
    {
        List<LivingArmourDowngradeRecipe> recipeList = LivingArmourDowngradeRecipeRegistry.getRecipeList();
        ArrayList<ArmourDowngradeRecipeJEI> recipes = new ArrayList<ArmourDowngradeRecipeJEI>();

        for (LivingArmourDowngradeRecipe recipe : recipeList)
            recipes.add(new ArmourDowngradeRecipeJEI(recipe));

        return recipes;
    }
}
