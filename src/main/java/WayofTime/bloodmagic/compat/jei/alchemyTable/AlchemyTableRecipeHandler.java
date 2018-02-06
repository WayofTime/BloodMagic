package WayofTime.bloodmagic.compat.jei.alchemyTable;

import WayofTime.bloodmagic.apibutnotreally.Constants;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class AlchemyTableRecipeHandler implements IRecipeHandler<AlchemyTableRecipeJEI> {
    @Nonnull
    @Override
    public Class<AlchemyTableRecipeJEI> getRecipeClass() {
        return AlchemyTableRecipeJEI.class;
    }

    @Override
    public String getRecipeCategoryUid(@Nonnull AlchemyTableRecipeJEI recipe) {
        return Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull AlchemyTableRecipeJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull AlchemyTableRecipeJEI recipe) {
        return true;
    }
}
