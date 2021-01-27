package wayoftime.bloodmagic.compat.jei.alchemytable;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.core.registry.OrbRegistry;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class AlchemyTableRecipeCategory implements IRecipeCategory<RecipeAlchemyTable>
{
	private static final int OUTPUT_SLOT = 0;
	private static final int ORB_SLOT = 1;
	private static final int INPUT_SLOT = 2;
	public static final ResourceLocation UID = BloodMagic.rl(Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE);

	@Nonnull
	private final IDrawable background;
	private final IDrawable icon;
//	@Nonnull
//	private final ICraftingGridHelper craftingGridHelper;

	public AlchemyTableRecipeCategory(IGuiHelper guiHelper)
	{
		icon = guiHelper.createDrawableIngredient(new ItemStack(BloodMagicBlocks.ALCHEMY_TABLE.get()));
		background = guiHelper.createDrawable(BloodMagic.rl("gui/jei/alchemytable.png"), 0, 0, 118, 40);
//		craftingGridHelper = guiHelper.createCraftingGridHelper(INPUT_SLOT);
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return UID;
	}

	@Override
	public List<ITextComponent> getTooltipStrings(RecipeAlchemyTable recipe, double mouseX, double mouseY)
	{
		List<ITextComponent> tooltip = Lists.newArrayList();

//		if (mouseX >= 13 && mouseX <= 64 && mouseY >= 27 && mouseY <= 58)
//		{
//			tooltip.add(new TranslationTextComponent("jei.bloodmagic.recipe.consumptionrate", ChatUtil.DECIMAL_FORMAT.format(recipe.getConsumeRate())));
//			tooltip.add(new TranslationTextComponent("jei.bloodmagic.recipe.drainrate", ChatUtil.DECIMAL_FORMAT.format(recipe.getDrainRate())));
//		}

		if (mouseX >= 58 && mouseX <= 78 && mouseY >= 21 && mouseY <= 34)
		{
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.tier", ChatUtil.DECIMAL_FORMAT.format(recipe.getMinimumTier())));
			tooltip.add(new TranslationTextComponent("jei.bloodmagic.recipe.lpDrained", ChatUtil.DECIMAL_FORMAT.format(recipe.getSyphon())));
			tooltip.add(new TranslationTextComponent("jei.bloodmagic.recipe.ticksRequired", ChatUtil.DECIMAL_FORMAT.format(recipe.getTicks())));
		}

		return tooltip;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return TextHelper.localize("jei.bloodmagic.recipe.alchemytable");
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
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull RecipeAlchemyTable recipe, @Nonnull IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(OUTPUT_SLOT, false, 91, 13);
		guiItemStacks.init(ORB_SLOT, true, 60, 0);

		for (int y = 0; y < 2; ++y)
		{
			for (int x = 0; x < 3; ++x)
			{
				int index = INPUT_SLOT + x + (y * 3);
				guiItemStacks.init(index, true, x * 18, y * 18);
			}
		}
//		guiItemStacks.set(ORB_SLOT, OrbRegistry.getOrbsDownToTier(recipe.getMinimumTier()));
//		guiItemStacks.set(OUTPUT_SLOT, ingredients.getOutputs(ItemStack.class).get(0));

//		guiItemStacks.init(OUTPUT_SLOT, false, 125, 30);
//		guiItemStacks.init(INPUT_SLOT, true, 31, 0);
//        craftingGridHelper.setInputs(guiItemStacks, ingredients.getInputs(ItemStack.class), 3, 2);

		guiItemStacks.set(ingredients);
	}

	@Override
	public Class<? extends RecipeAlchemyTable> getRecipeClass()
	{
		return RecipeAlchemyTable.class;
	}

	@Override
	public void setIngredients(RecipeAlchemyTable recipe, IIngredients ingredients)
	{
		List<ItemStack> validOrbs = OrbRegistry.getOrbsDownToTier(recipe.getMinimumTier());

		ItemStack[] validOrbStacks = new ItemStack[validOrbs.size()];
		for (int i = 0; i < validOrbStacks.length; i++)
		{
			validOrbStacks[i] = validOrbs.get(i);
		}

		List<Ingredient> ingList = Lists.newArrayList();
		ingList.add(Ingredient.fromStacks(validOrbStacks));
		ingList.addAll(recipe.getInput());
		ingredients.setInputIngredients(ingList);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
	}

//	@Override
//	public void draw(RecipeBloodAltar recipe, MatrixStack matrixStack, double mouseX, double mouseY)
//	{
//		Minecraft mc = Minecraft.getInstance();
//		String[] infoString = new String[]
//		{ TextHelper.localize("jei.bloodmagic.recipe.requiredtier", NumeralHelper.toRoman(recipe.getMinimumTier().toInt())),
//				TextHelper.localize("jei.bloodmagic.recipe.requiredlp", recipe.getSyphon()) };
//		mc.fontRenderer.drawString(matrixStack, infoString[0], 90
//				- mc.fontRenderer.getStringWidth(infoString[0]) / 2, 0, Color.gray.getRGB());
//		mc.fontRenderer.drawString(matrixStack, infoString[1], 90
//				- mc.fontRenderer.getStringWidth(infoString[1]) / 2, 10, Color.gray.getRGB());
//	}

}
