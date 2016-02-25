package WayofTime.bloodmagic.compat.jei.orb;

import java.util.Arrays;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;

public class ShapedOrbRecipeHandler implements IRecipeHandler<ShapedBloodOrbRecipe>
{

    @Nonnull
    @Override
    public Class<ShapedBloodOrbRecipe> getRecipeClass()
    {
        return ShapedBloodOrbRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull ShapedBloodOrbRecipe recipe)
    {
        return new ShapedOrbRecipeJEI(Arrays.asList(recipe.getInput()), recipe.getTier(), recipe.getRecipeOutput());
    }

    @Override
    public boolean isRecipeValid(@Nonnull ShapedBloodOrbRecipe recipe)
    {
        return recipe.getInput().length > 0;
    }
}
