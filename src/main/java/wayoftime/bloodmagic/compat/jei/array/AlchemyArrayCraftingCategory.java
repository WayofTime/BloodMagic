package wayoftime.bloodmagic.compat.jei.array;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class AlchemyArrayCraftingCategory implements IRecipeCategory<RecipeAlchemyArray>
{
	private static final int OUTPUT_SLOT = 0;
	private static final int INPUT_SLOT = 1;
	private static final int CATALYST_SLOT = 2;
	public static final ResourceLocation UID = BloodMagic.rl(Constants.Compat.JEI_CATEGORY_ALCHEMYARRAY);

	@Nonnull
	private final IDrawable background;
	private final IDrawable icon;
//	@Nonnull
//	private final ICraftingGridHelper craftingGridHelper;

	public AlchemyArrayCraftingCategory(IGuiHelper guiHelper)
	{
		icon = guiHelper.createDrawableIngredient(new ItemStack(BloodMagicItems.ARCANE_ASHES.get()));
		background = guiHelper.createDrawable(BloodMagic.rl("gui/jei/binding.png"), 0, 0, 100, 30);
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
	public String getTitle()
	{
		return TextHelper.localize("jei.bloodmagic.recipe.alchemyarraycrafting");
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
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull RecipeAlchemyArray recipe, @Nonnull IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 73, 5);
		recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 5);
		recipeLayout.getItemStacks().init(CATALYST_SLOT, true, 29, 3);

		guiItemStacks.set(ingredients);
	}

	@Override
	public Class<? extends RecipeAlchemyArray> getRecipeClass()
	{
		return RecipeAlchemyArray.class;
	}

	@Override
	public void setIngredients(RecipeAlchemyArray recipe, IIngredients ingredients)
	{
		ingredients.setInputIngredients(recipe.getIngredients());
		if (recipe.getOutput().isEmpty())
		{
			ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(BloodMagicItems.ARCANE_ASHES.get()));
//			ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(BloodMagicBlocks.ALCHEMY_ARRAY.get()));
		} else
		{
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
		}

	}
}
