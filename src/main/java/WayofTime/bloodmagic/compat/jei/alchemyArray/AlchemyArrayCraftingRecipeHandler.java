package WayofTime.bloodmagic.compat.jei.alchemyArray;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import WayofTime.bloodmagic.api.Constants;

public class AlchemyArrayCraftingRecipeHandler implements IRecipeHandler<AlchemyArrayCraftingRecipeJEI>
{
    @Nonnull
    @Override
    public Class<AlchemyArrayCraftingRecipeJEI> getRecipeClass()
    {
        return AlchemyArrayCraftingRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return Constants.Compat.JEI_CATEGORY_ALCHEMYARRAY;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull AlchemyArrayCraftingRecipeJEI recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull AlchemyArrayCraftingRecipeJEI recipe)
    {
        return recipe.getInputs().size() > 0 && recipe.getOutputs().size() > 0;
    }
}
