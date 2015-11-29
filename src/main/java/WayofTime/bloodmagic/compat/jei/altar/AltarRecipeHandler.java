package WayofTime.bloodmagic.compat.jei.altar;

import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class AltarRecipeHandler implements IRecipeHandler<AltarRecipeJEI> {

    @Nonnull
    @Override
    public Class<AltarRecipeJEI> getRecipeClass() {
        return AltarRecipeJEI.class;
    }

    @Nonnull
    @Override
    public Class<? extends IRecipeCategory> getRecipeCategoryClass() {
        return AltarRecipeCategory.class;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull AltarRecipeJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull AltarRecipeJEI recipe) {
        return recipe.getInputs().size() > 0 && recipe.getOutputs().size() > 0;
    }
}
