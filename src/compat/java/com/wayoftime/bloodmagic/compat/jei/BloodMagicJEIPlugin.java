package com.wayoftime.bloodmagic.compat.jei;

import com.wayoftime.bloodmagic.api.impl.BloodMagicAPI;
import com.wayoftime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import com.wayoftime.bloodmagic.compat.jei.altar.RecipeCategoryAltar;
import com.wayoftime.bloodmagic.compat.jei.altar.RecipeWrapperAltar;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagicBlocks;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class BloodMagicJEIPlugin implements IModPlugin {

    public static IJeiHelpers helper;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        helper = registry.getJeiHelpers();

        registry.addRecipeCategories(
                new RecipeCategoryAltar()
        );
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAltarRecipes(), RecipeCategoryAltar.CATEGORY_ID);
        registry.handleRecipes(RecipeBloodAltar.class, RecipeWrapperAltar::new, RecipeCategoryAltar.CATEGORY_ID);
        registry.addRecipeCatalyst(new ItemStack(RegistrarBloodMagicBlocks.BLOOD_ALTAR), RecipeCategoryAltar.CATEGORY_ID);
    }
}
