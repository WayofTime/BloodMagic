package WayofTime.bloodmagic.compat.jei.forge;

import WayofTime.bloodmagic.api.Constants;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class SoulForgeRecipeHandler implements IRecipeHandler<SoulForgeRecipeJEI>
{
    @Nonnull
    @Override
    public Class<SoulForgeRecipeJEI> getRecipeClass()
    {
        return SoulForgeRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return Constants.Compat.JEI_CATEGORY_SOULFORGE;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull SoulForgeRecipeJEI recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull SoulForgeRecipeJEI recipe)
    {
        return recipe.getInputs().size() > 0 && recipe.getOutputs().size() > 0;
    }
}
