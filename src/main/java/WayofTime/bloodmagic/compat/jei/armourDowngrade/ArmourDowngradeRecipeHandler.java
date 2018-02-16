package WayofTime.bloodmagic.compat.jei.armourDowngrade;

import WayofTime.bloodmagic.util.Constants;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class ArmourDowngradeRecipeHandler implements IRecipeHandler<ArmourDowngradeRecipeJEI> {
    @Nonnull
    @Override
    public Class<ArmourDowngradeRecipeJEI> getRecipeClass() {
        return ArmourDowngradeRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(ArmourDowngradeRecipeJEI recipe) {
        return Constants.Compat.JEI_CATEGORY_ARMOURDOWNGRADE;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull ArmourDowngradeRecipeJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull ArmourDowngradeRecipeJEI recipe) {
        return true;
    }
}
