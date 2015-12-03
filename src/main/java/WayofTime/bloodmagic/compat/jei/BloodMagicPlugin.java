package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeCategory;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeHandler;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeMaker;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeCategory;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeHandler;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeMaker;
<<<<<<< HEAD
import WayofTime.bloodmagic.registry.ModBlocks;
import mezz.jei.api.*;
import net.minecraft.item.ItemStack;
=======
import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeTransferHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
>>>>>>> parent of d51a908... Update JEI compat

public class BloodMagicPlugin implements IModPlugin {

    @Override
    public boolean isModLoaded() {
        return true;
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCategories(
                new AltarRecipeCategory(),
                new BindingRecipeCategory()
        );

        registry.addRecipeHandlers(
                new AltarRecipeHandler(),
                new BindingRecipeHandler()
        );

        registry.addRecipes(AltarRecipeMaker.getRecipes());
        registry.addRecipes(BindingRecipeMaker.getRecipes());
    }

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
        jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.bloodLight));
    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {

    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

    }
}
