package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeCategory;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeHandler;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeMaker;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeTransferHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BloodMagicPlugin implements IModPlugin {

    @Override
    public boolean isModLoaded() {
        return true;
    }

    @Override
    @Nonnull
    public Iterable<? extends IRecipeCategory> getRecipeCategories() {
        return Arrays.asList(
                new AltarRecipeCategory()
        );
    }

    @Override
    @Nonnull
    public Iterable<? extends IRecipeHandler> getRecipeHandlers() {
        return Arrays.asList(
                new AltarRecipeHandler()
        );
    }

    @Nonnull
    public Iterable<? extends IRecipeTransferHelper> getRecipeTransferHelpers() {
        return Arrays.asList(

        );
    }

    @Override
    @Nonnull
    public Iterable<Object> getRecipes() {
        List<Object> recipes = new ArrayList<Object>();

        recipes.addAll(AltarRecipeMaker.getAltarRecipes());

        return recipes;
    }
}
