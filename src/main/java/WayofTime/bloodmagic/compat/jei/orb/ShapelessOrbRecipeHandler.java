package WayofTime.bloodmagic.compat.jei.orb;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import WayofTime.bloodmagic.api.recipe.ShapelessBloodOrbRecipe;

public class ShapelessOrbRecipeHandler implements IRecipeHandler<ShapelessBloodOrbRecipe>
{

    @Nonnull
    @Override
    public Class<ShapelessBloodOrbRecipe> getRecipeClass()
    {
        return ShapelessBloodOrbRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull ShapelessBloodOrbRecipe recipe)
    {
        return new ShapelessOrbRecipeJEI(recipe.getInput(), recipe.getTier(), recipe.getRecipeOutput());
    }

    @Override
    public boolean isRecipeValid(@Nonnull ShapelessBloodOrbRecipe recipe)
    {
        return recipe.getInput().size() > 0;
    }
}
