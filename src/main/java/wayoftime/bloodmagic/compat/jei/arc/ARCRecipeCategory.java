package wayoftime.bloodmagic.compat.jei.arc;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
		return Component.translatable("jei.bloodmagic.recipe.arc");
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
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RecipeARC recipe, @Nonnull IFocusGroup focuses)
	{
		NonNullList<Ingredient> inputList = recipe.getIngredients();

		int inputSize = recipe.getRequiredInputCount();
		List<ItemStack> inputStackList = List.of(inputList.get(0).getItems());
		if (inputSize > 1)
		{
			for (ItemStack stack : inputStackList)
			{
				stack.setCount(inputSize);
			}
		}

		builder.addSlot(RecipeIngredientRole.INPUT, 1, 6).addIngredients(VanillaTypes.ITEM, inputStackList).setSlotName("input");
		builder.addSlot(RecipeIngredientRole.INPUT, 22, 17).addIngredients(inputList.get(1)).setSlotName("tool");

		List<ItemStack> outputList = recipe.getAllListedOutputs();
		for (int i = 0; i < outputList.size(); i++)
		{
			builder.addSlot(RecipeIngredientRole.OUTPUT, 54 + i * 22, 17).addItemStack(outputList.get(i)).setSlotName("output");
		}
	}

	@Override
	public Class<? extends RecipeARC> getRecipeClass()
	{
		return RecipeARC.class;
	}

	@Override
	public void draw(RecipeARC recipe, PoseStack matrixStack, double mouseX, double mouseY)
	{
		Minecraft mc = Minecraft.getInstance();
		List<Pair<Double, Double>> chanceArray = recipe.getAllOutputChances();

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
