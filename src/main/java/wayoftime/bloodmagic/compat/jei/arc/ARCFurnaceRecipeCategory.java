package wayoftime.bloodmagic.compat.jei.arc;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;

public class ARCFurnaceRecipeCategory implements IRecipeCategory<SmeltingRecipe>
{
	private static final int OUTPUT_SLOT = 0;
	private static final int INPUT_SLOT = 1;
	private static final int CATALYST_SLOT = 2;
	public static final ResourceLocation UID = BloodMagic.rl(Constants.Compat.JEI_CATEGORY_ARC + "furnace");
	public static final ResourceLocation BACKGROUNDRL = BloodMagic.rl("gui/jei/arc.png");

	@Nonnull
	private final IDrawable background;
	private final IDrawable icon;
//	@Nonnull
//	private final ICraftingGridHelper craftingGridHelper;

	public ARCFurnaceRecipeCategory(IGuiHelper guiHelper)
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
		return new TranslatableComponent("jei.bloodmagic.recipe.arcfurnace");
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
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull SmeltingRecipe recipe, @Nonnull IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 53, 16);
		recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 5);
		recipeLayout.getItemStacks().init(CATALYST_SLOT, true, 21, 16);

		guiItemStacks.set(ingredients);
	}

	@Override
	public Class<? extends SmeltingRecipe> getRecipeClass()
	{
		return SmeltingRecipe.class;
	}

	@Override
	public void setIngredients(SmeltingRecipe recipe, IIngredients ingredients)
	{
		List<Ingredient> inputIngList = recipe.getIngredients();
		inputIngList.add(Ingredient.of(BloodMagicTags.ARC_TOOL_FURNACE));
		ingredients.setInputIngredients(inputIngList);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void draw(SmeltingRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY)
	{
		FluidStack outputStack = FluidStack.EMPTY;
		ClientHandler.handleGuiTank(matrixStack, outputStack, outputStack.getAmount(), 140, 7, 16, 36, 157, 6, 18, 38, (int) mouseX, (int) mouseY, BACKGROUNDRL.toString(), null);

		FluidStack inputStack = FluidStack.EMPTY;
		ClientHandler.handleGuiTank(matrixStack, inputStack, inputStack.getAmount(), 1, 26, 16, 16, 175, 26, 18, 18, (int) mouseX, (int) mouseY, BACKGROUNDRL.toString(), null);
	}
}
