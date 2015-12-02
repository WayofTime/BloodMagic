package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeCategory;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeHandler;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeMaker;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BloodMagicPlugin implements IModPlugin {

    @Override
    public boolean isModLoaded() {
        return true;
    }

    @Override
    public Iterable<? extends IRecipeCategory> getRecipeCategories() {
        return Arrays.asList(
                new AltarRecipeCategory()
        );
    }

    @Override
    public Iterable<? extends IRecipeHandler> getRecipeHandlers() {
        return Arrays.asList(
                new AltarRecipeHandler()
        );
    }

    @Override
    public Iterable<Object> getRecipes() {
        List<Object> recipes = new ArrayList<Object>();

        recipes.addAll(AltarRecipeMaker.getAltarRecipes());

        return recipes;
    }
}
