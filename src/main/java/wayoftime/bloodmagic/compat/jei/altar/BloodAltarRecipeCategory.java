package wayoftime.bloodmagic.compat.jei.altar;

import java.awt.Color;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NumeralHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class BloodAltarRecipeCategory implements IRecipeCategory<RecipeBloodAltar>
{
	private static final int INPUT_SLOT = 1;
	private static final int OUTPUT_SLOT = 0;
	public static final ResourceLocation UID = BloodMagic.rl(Constants.Compat.JEI_CATEGORY_ALTAR);

	@Nonnull
	private final IDrawable background;
	private final IDrawable icon;
//	@Nonnull
//	private final ICraftingGridHelper craftingGridHelper;

	public BloodAltarRecipeCategory(IGuiHelper guiHelper)
	{
		icon = guiHelper.createDrawableIngredient(new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()));
		background = guiHelper.createDrawable(BloodMagic.rl("gui/jei/altar.png"), 3, 4, 155, 65);
//		craftingGridHelper = guiHelper.createCraftingGridHelper(INPUT_SLOT);
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return UID;
	}

	@Override
	public List<ITextComponent> getTooltipStrings(RecipeBloodAltar recipe, double mouseX, double mouseY)
	{
		List<ITextComponent> tooltip = Lists.newArrayList();

		if (mouseX >= 13 && mouseX <= 64 && mouseY >= 27 && mouseY <= 58)
		{
			tooltip.add(new TranslationTextComponent("jei.bloodmagic.recipe.consumptionrate", ChatUtil.DECIMAL_FORMAT.format(recipe.getConsumeRate())));
			tooltip.add(new TranslationTextComponent("jei.bloodmagic.recipe.drainrate", ChatUtil.DECIMAL_FORMAT.format(recipe.getDrainRate())));
		}

		return tooltip;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return TextHelper.localize("jei.bloodmagic.recipe.altar");
	}

	@Nonnull
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Nullable
	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull RecipeBloodAltar recipe, @Nonnull IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(OUTPUT_SLOT, false, 125, 30);
		guiItemStacks.init(INPUT_SLOT, true, 31, 0);

		guiItemStacks.set(ingredients);
	}

	@Override
	public Class<? extends RecipeBloodAltar> getRecipeClass()
	{
		return RecipeBloodAltar.class;
	}

	@Override
	public void setIngredients(RecipeBloodAltar recipe, IIngredients ingredients)
	{
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
	}

	@Override
	public void draw(RecipeBloodAltar recipe, MatrixStack matrixStack, double mouseX, double mouseY)
	{
		Minecraft mc = Minecraft.getInstance();
		String[] infoString = new String[]
		{ TextHelper.localize("jei.bloodmagic.recipe.requiredtier", NumeralHelper.toRoman(recipe.getMinimumTier().toInt())),
				TextHelper.localize("jei.bloodmagic.recipe.requiredlp", recipe.getSyphon()) };
		mc.fontRenderer.drawString(matrixStack, infoString[0], 90
				- mc.fontRenderer.getStringWidth(infoString[0]) / 2, 0, Color.gray.getRGB());
		mc.fontRenderer.drawString(matrixStack, infoString[1], 90
				- mc.fontRenderer.getStringWidth(infoString[1]) / 2, 10, Color.gray.getRGB());
	}

}