package wayoftime.bloodmagic.compat.jei.arc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;

public class ARCRecipeCategory implements IRecipeCategory<RecipeARC>
{
	private static final int OUTPUT_SLOT = 0;
	private static final int INPUT_SLOT = 4;
	private static final int CATALYST_SLOT = 5;
	public static final ResourceLocation UID = BloodMagic.rl(Constants.Compat.JEI_CATEGORY_ARC);
	public static final ResourceLocation BACKGROUNDRL = BloodMagic.rl("gui/jei/arc.png");

	@Nonnull
	private final IDrawable background;
	private final IDrawable icon;
//	@Nonnull
//	private final ICraftingGridHelper craftingGridHelper;

	public ARCRecipeCategory(IGuiHelper guiHelper)
	{
		icon = guiHelper.createDrawableIngredient(new ItemStack(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get()));
		background = guiHelper.createDrawable(BACKGROUNDRL, 0, 0, 157, 43);
//		craftingGridHelper = guiHelper.createCraftingGridHelper(INPUT_SLOT);
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return UID;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return new TranslatableComponent("jei.bloodmagic.recipe.arc");
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
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull RecipeARC recipe, @Nonnull IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 53, 16);
		recipeLayout.getItemStacks().init(OUTPUT_SLOT + 1, false, 53 + 22 * 1, 16);
		recipeLayout.getItemStacks().init(OUTPUT_SLOT + 2, false, 53 + 22 * 2, 16);
		recipeLayout.getItemStacks().init(OUTPUT_SLOT + 3, false, 53 + 22 * 3, 16);
		recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 5);
		recipeLayout.getItemStacks().init(CATALYST_SLOT, true, 21, 16);

		guiItemStacks.set(ingredients);
	}

	@Override
	public Class<? extends RecipeARC> getRecipeClass()
	{
		return RecipeARC.class;
	}

	@Override
	public void setIngredients(RecipeARC recipe, IIngredients ingredients)
	{
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.getAllListedOutputs());
	}

	@Override
	public void draw(RecipeARC recipe, PoseStack matrixStack, double mouseX, double mouseY)
	{
		Minecraft mc = Minecraft.getInstance();
		List<Pair<Double, Double>> chanceArray = recipe.getAllOutputChances();

//		double mainChance = recipe.getMainOutputChance();
//		if (mainChance > 0)
//		{
//			String infoString = "+1";
//			if (mainChance >= 1)
//			{
//				infoString = "+1";
//			} else if (mainChance < 0.01)
//			{
//				infoString = "+<1%";
//			} else
//			{
//				infoString = "+" + (int) (Math.round(mainChance * 100)) + "%";
//			}
//
//			mc.font.drawShadow(matrixStack, infoString, 86 - 24 - mc.font.width(infoString) / 2, 5 + 0 * 32, Color.white.getRGB());
//		}

		String[] infoString = new String[chanceArray.size()];
		for (int i = 0; i < infoString.length; i++)
		{
			Pair<Double, Double> chance = chanceArray.get(i);
			double totalChance = chance.getLeft() + chance.getRight();
			if (totalChance >= 1)
			{
				infoString[i] = "";
			} else if (totalChance < 0.01)
			{
				infoString[i] = "<1%";
			} else
			{
				infoString[i] = "" + (int) (Math.round(totalChance * 100)) + "%";
			}

			mc.font.drawShadow(matrixStack, infoString[i], 86 + 22 * i - mc.font.width(infoString[i]) / 2, 5, Color.white.getRGB());
		}

//		if (recipe.getFluidOutput() != null && !recipe.getFluidOutput().isEmpty())
		{
			FluidStack outputStack = recipe.getFluidOutput();
			ClientHandler.handleGuiTank(matrixStack, outputStack, outputStack.getAmount(), 140, 7, 16, 36, 157, 6, 18, 38, (int) mouseX, (int) mouseY, BACKGROUNDRL.toString(), null);
		}

		if (recipe.getFluidIngredient() != null)
		{
			List<FluidStack> inputFluids = recipe.getFluidIngredient().getRepresentations();
			FluidStack inputStack = inputFluids.get(0);
			ClientHandler.handleGuiTank(matrixStack, inputStack, inputStack.getAmount(), 1, 26, 16, 16, 175, 26, 18, 18, (int) mouseX, (int) mouseY, BACKGROUNDRL.toString(), null);
		}

//		{ TextHelper.localize("jei.bloodmagic.recipe.requiredtier", NumeralHelper.toRoman(recipe.getMinimumTier().toInt())),
//				TextHelper.localize("jei.bloodmagic.recipe.requiredlp", recipe.getSyphon()) };
//		mc.fontRenderer.drawString(matrixStack, infoString[0], 90
//				- mc.fontRenderer.getStringWidth(infoString[0]) / 2, 0, Color.gray.getRGB());
//		mc.fontRenderer.drawString(matrixStack, infoString[1], 90
//				- mc.fontRenderer.getStringWidth(infoString[1]) / 2, 10, Color.gray.getRGB());
	}

	@Override
	public List<Component> getTooltipStrings(RecipeARC recipe, double mouseX, double mouseY)
	{
		List<Component> tooltip = new ArrayList<>();
		FluidStack outputStack = recipe.getFluidOutput();
		if (!outputStack.isEmpty())
		{
			ClientHandler.handleGuiTank(null, outputStack, -1, 140, 8, 16, 34, 157, 7, 18, 36, (int) mouseX, (int) mouseY, BACKGROUNDRL.toString(), tooltip);
		}

		if (recipe.getFluidIngredient() != null)
		{
			List<FluidStack> inputFluids = recipe.getFluidIngredient().getRepresentations();
			FluidStack inputStack = inputFluids.get(0);
			ClientHandler.handleGuiTank(null, inputStack, -1, 1, 26, 16, 16, 175, 26, 18, 18, (int) mouseX, (int) mouseY, BACKGROUNDRL.toString(), tooltip);
		}

		return tooltip;
	}
}
