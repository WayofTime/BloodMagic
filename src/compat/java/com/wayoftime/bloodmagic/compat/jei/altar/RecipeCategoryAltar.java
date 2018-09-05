package com.wayoftime.bloodmagic.compat.jei.altar;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.compat.jei.BloodMagicJEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeCategoryAltar implements IRecipeCategory<RecipeWrapperAltar> {

    public static final String CATEGORY_ID = "bloodmagic:blood_altar";
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    @Nonnull
    private final IDrawable background = BloodMagicJEIPlugin.helper.getGuiHelper().createDrawable(new ResourceLocation(BloodMagic.MODID, "textures/gui/jei/blood_altar.png"), 3, 4, 155, 65);

    @Nonnull
    @Override
    public String getUid() {
        return CATEGORY_ID;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format("jei.bloodmagic:blood_altar");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull RecipeWrapperAltar recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 31, 0);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 125, 30);

        recipeLayout.getItemStacks().set(INPUT_SLOT, ingredients.getInputs(ItemStack.class).get(0));
        recipeLayout.getItemStacks().set(OUTPUT_SLOT, ingredients.getOutputs(ItemStack.class).get(0));
    }

    @Nonnull
    @Override
    public String getModName() {
        return BloodMagic.NAME;
    }
}