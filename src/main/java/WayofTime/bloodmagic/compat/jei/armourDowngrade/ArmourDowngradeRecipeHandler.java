package WayofTime.bloodmagic.compat.jei.armourDowngrade;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import WayofTime.bloodmagic.api.Constants;

public class ArmourDowngradeRecipeHandler implements IRecipeHandler<ArmourDowngradeRecipeJEI>
{
    @Nonnull
    @Override
    public Class<ArmourDowngradeRecipeJEI> getRecipeClass()
    {
        return ArmourDowngradeRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return Constants.Compat.JEI_CATEGORY_ARMOURDOWNGRADE;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull ArmourDowngradeRecipeJEI recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull ArmourDowngradeRecipeJEI recipe)
    {
        return recipe.getInputs().get(0).size() > 0 && recipe.getOutputs().size() > 0;
    }
}
