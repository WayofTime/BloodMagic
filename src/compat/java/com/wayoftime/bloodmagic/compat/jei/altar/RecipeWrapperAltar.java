package com.wayoftime.bloodmagic.compat.jei.altar;

import com.google.common.collect.Lists;
import com.wayoftime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.List;

public class RecipeWrapperAltar implements IRecipeWrapper {

    private final RecipeBloodAltar recipe;

    public RecipeWrapperAltar(RecipeBloodAltar recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, NonNullList.from(ItemStack.EMPTY, recipe.getInput().getMatchingStacks()));
        ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }

    @Nonnull
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        List<String> tooltip = Lists.newArrayList();
        if (mouseX >= 13 && mouseX <= 64 && mouseY >= 27 && mouseY <= 58) {
            tooltip.add(I18n.format("jei.bloodmagic:consumption_rate", recipe.getConsumeRate()));
            tooltip.add(I18n.format("jei.bloodmagic:drain_rate", recipe.getDrainRate()));
        }
        return tooltip;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        String line1 = I18n.format("jei.bloodmagic:required_tier", I18n.format("enchantment.level." + (recipe.getMinimumTier().ordinal() + 1)));
        minecraft.fontRenderer.drawString(line1, 90 - minecraft.fontRenderer.getStringWidth(line1) / 2, 0, Color.GRAY.getRGB());

        String line2 = I18n.format("jei.bloodmagic:required_lp", recipe.getSyphon());
        minecraft.fontRenderer.drawString(line2, 90 - minecraft.fontRenderer.getStringWidth(line2) / 2, 10, Color.GRAY.getRGB());
    }
}
