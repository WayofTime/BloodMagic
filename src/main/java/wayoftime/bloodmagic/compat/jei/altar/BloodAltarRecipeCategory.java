package wayoftime.bloodmagic.compat.jei.altar;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.recipe.bloodaltar.BloodAltarRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class BloodAltarRecipeCategory implements IRecipeCategory<BloodAltarRecipe> {

    public static final RecipeType<BloodAltarRecipe> RECIPE_TYPE = RecipeType.create(BloodMagic.MODID, "blood_altar", BloodAltarRecipe.class);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    private static final String[] ROMAN_NUMERALS = {"I", "II", "III", "IV", "V", "VI"};

    @Nonnull
    private final IDrawable background;
    private final IDrawable icon;

    public BloodAltarRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(BMBlocks.BLOOD_ALTAR.block().get()));
        background = guiHelper.createDrawable(BloodMagic.rl("gui/jei/altar.png"), 3, 4, 155, 65);
    }

    @Override
    public RecipeType<BloodAltarRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("jei.bloodmagic.recipe.altar");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public List<Component> getTooltipStrings(BloodAltarRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltip = Lists.newArrayList();
        if (mouseX >= 85 && mouseX <= 104 && mouseY >= 30 && mouseY <= 44) {
            tooltip.add(Component.translatable("jei.bloodmagic.recipe.consumptionrate", DECIMAL_FORMAT.format(recipe.getCraftSpeed())));
            tooltip.add(Component.translatable("jei.bloodmagic.recipe.drainrate", DECIMAL_FORMAT.format(recipe.getDrainSpeed())));
        }
        return tooltip;
    }

    @Override
    public void draw(BloodAltarRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        String tierText = "Tier " + toRoman(recipe.getMinTier() + 1);
        String lpText = recipe.getTotalBlood() + " LP";

        guiGraphics.drawString(mc.font, tierText, 90 - mc.font.width(tierText) / 2, 0, Color.gray.getRGB(), false);
        guiGraphics.drawString(mc.font, lpText, 90 - mc.font.width(lpText) / 2, 10, Color.gray.getRGB(), false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BloodAltarRecipe recipe, IFocusGroup focuses) {
        // Output
        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 126, 31);
        output.addItemStack(recipe.getResult());

        // Input
        IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, 32, 1);
        input.addIngredients(recipe.getInput());
    }

    private static String toRoman(int number) {
        if (number >= 1 && number <= ROMAN_NUMERALS.length) {
            return ROMAN_NUMERALS[number - 1];
        }
        return String.valueOf(number);
    }
}
