package WayofTime.bloodmagic.compat.jei.alchemyTable;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import WayofTime.bloodmagic.api.Constants;

public class AlchemyTableRecipeHandler implements IRecipeHandler<AlchemyTableRecipeJEI>
{
    @Nonnull
    @Override
    public Class<AlchemyTableRecipeJEI> getRecipeClass()
    {
        return AlchemyTableRecipeJEI.class;
    }

    @Deprecated
    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE;
    }

    @Override
    public String getRecipeCategoryUid(@Nonnull AlchemyTableRecipeJEI recipe)
    {
        return Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull AlchemyTableRecipeJEI recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull AlchemyTableRecipeJEI recipe)
    {
        return recipe.getInputs().get(0).size() > 0 && recipe.getOutputs().size() > 0;
    }
}
