package WayofTime.bloodmagic.compat.jei.binding;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import WayofTime.bloodmagic.api.Constants;

public class BindingRecipeHandler implements IRecipeHandler<BindingRecipeJEI>
{
    @Nonnull
    @Override
    public Class<BindingRecipeJEI> getRecipeClass()
    {
        return BindingRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return Constants.Compat.JEI_CATEGORY_BINDING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull BindingRecipeJEI recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull BindingRecipeJEI recipe)
    {
        return recipe.getInputs().size() > 0 && recipe.getOutputs().size() > 0;
    }
}
