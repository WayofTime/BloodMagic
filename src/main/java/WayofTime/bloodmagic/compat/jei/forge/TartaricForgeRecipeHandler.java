package WayofTime.bloodmagic.compat.jei.forge;

import WayofTime.bloodmagic.apibutnotreally.Constants;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class TartaricForgeRecipeHandler implements IRecipeHandler<TartaricForgeRecipeJEI> {
    @Nonnull
    @Override
    public Class<TartaricForgeRecipeJEI> getRecipeClass() {
        return TartaricForgeRecipeJEI.class;
    }

    @Override
    public String getRecipeCategoryUid(@Nonnull TartaricForgeRecipeJEI recipe) {
        return Constants.Compat.JEI_CATEGORY_SOULFORGE;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull TartaricForgeRecipeJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull TartaricForgeRecipeJEI recipe) {
        return true;
    }
}
