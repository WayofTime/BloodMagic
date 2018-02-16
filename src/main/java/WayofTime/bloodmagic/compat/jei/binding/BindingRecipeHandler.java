package WayofTime.bloodmagic.compat.jei.binding;

import WayofTime.bloodmagic.util.Constants;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class BindingRecipeHandler implements IRecipeHandler<BindingRecipeJEI> {
    @Nonnull
    @Override
    public Class<BindingRecipeJEI> getRecipeClass() {
        return BindingRecipeJEI.class;
    }

    @Override
    public String getRecipeCategoryUid(@Nonnull BindingRecipeJEI recipe) {
        return Constants.Compat.JEI_CATEGORY_BINDING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull BindingRecipeJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull BindingRecipeJEI recipe) {
        return true;
    }
}
