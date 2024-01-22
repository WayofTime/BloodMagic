package wayoftime.bloodmagic.compat.jei.altar;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
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
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NumeralHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class BloodAltarRecipeCategory implements IRecipeCategory<RecipeBloodAltar> {
    public static final RecipeType<RecipeBloodAltar> RECIPE_TYPE = RecipeType.create(BloodMagic.MODID, Constants.Compat.JEI_CATEGORY_ALTAR, RecipeBloodAltar.class);

    @Nonnull
    private final IDrawable background;
    private final IDrawable icon;

    public BloodAltarRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()));
        background = guiHelper.createDrawable(BloodMagic.rl("gui/jei/altar.png"), 3, 4, 155, 65);
    }


    @Override
    public List<Component> getTooltipStrings(RecipeBloodAltar recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltip = Lists.newArrayList();

        if (mouseX >= 13 && mouseX <= 64 && mouseY >= 27 && mouseY <= 58) {
            tooltip.add(Component.translatable("jei.bloodmagic.recipe.consumptionrate", ChatUtil.DECIMAL_FORMAT.format(recipe.getConsumeRate())));
            tooltip.add(Component.translatable("jei.bloodmagic.recipe.drainrate", ChatUtil.DECIMAL_FORMAT.format(recipe.getDrainRate())));
        }

        return tooltip;
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeBloodAltar recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 126, 31);
        output.addItemStack(recipe.getOutput());

        IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, 32, 1);
        input.addIngredients(recipe.getInput());
    }


    @Override
    public RecipeType<RecipeBloodAltar> getRecipeType() {
        return RECIPE_TYPE;
    }


    @Override
    public void draw(RecipeBloodAltar recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        String[] infoString = new String[]{
                TextHelper.localize("jei.bloodmagic.recipe.requiredtier", NumeralHelper.toRoman(recipe.getMinimumTier() + 1)),
                TextHelper.localize("jei.bloodmagic.recipe.requiredlp", recipe.getSyphon())};
        guiGraphics.drawString(mc.font, infoString[0], 90 - mc.font.width(infoString[0]) / 2, 0, Color.gray.getRGB());
        guiGraphics.drawString(mc.font, infoString[1], 90 - mc.font.width(infoString[1]) / 2, 10, Color.gray.getRGB());
    }

}