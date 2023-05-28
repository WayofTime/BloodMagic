package wayoftime.bloodmagic.compat.jei.forge;

import com.google.common.collect.Lists;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TartaricForgeRecipeCategory implements IRecipeCategory<RecipeTartaricForge>
{
	private static final int OUTPUT_SLOT = 0;
	private static final int GEM_SLOT = 1;
	private static final int INPUT_SLOT = 2;
	public static final ResourceLocation UID = BloodMagic.rl(Constants.Compat.JEI_CATEGORY_SOULFORGE);

	@Nonnull
	private final IDrawable background;
	private final IDrawable icon;
//	@Nonnull
//	private final ICraftingGridHelper craftingGridHelper;

	public TartaricForgeRecipeCategory(IGuiHelper guiHelper)
	{
		icon = guiHelper.createDrawableIngredient(new ItemStack(BloodMagicBlocks.SOUL_FORGE.get()));
		background = guiHelper.createDrawable(BloodMagic.rl("gui/jei/soulforge.png"), 0, 0, 100, 40);
//		craftingGridHelper = guiHelper.createCraftingGridHelper(INPUT_SLOT);
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return UID;
	}

	@Override
	public List<Component> getTooltipStrings(RecipeTartaricForge recipe, double mouseX, double mouseY)
	{
		List<Component> tooltip = Lists.newArrayList();
		if (mouseX >= 40 && mouseX <= 60 && mouseY >= 21 && mouseY <= 34)
		{
			tooltip.add(Component.translatable("jei.bloodmagic.recipe.minimumsouls", ChatUtil.DECIMAL_FORMAT.format(recipe.getMinimumSouls())));
			tooltip.add(Component.translatable("jei.bloodmagic.recipe.soulsdrained", ChatUtil.DECIMAL_FORMAT.format(recipe.getSoulDrain())));
		}
		return tooltip;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return Component.translatable("jei.bloodmagic.recipe.soulforge");
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
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull RecipeTartaricForge recipe, @Nonnull IIngredients ingredients)
	{
		List<ItemStack> validGems = Lists.newArrayList();
		for (DefaultWill will : DefaultWill.values())
		{
			if (will.minSouls >= recipe.getMinimumSouls())
			{
				validGems.add(will.willStack);
			}
		}

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(OUTPUT_SLOT, false, 73, 13);

		guiItemStacks.init(GEM_SLOT, true, 42, 0);

		for (int y = 0; y < 2; ++y)
		{
			for (int x = 0; x < 2; ++x)
			{
				int index = INPUT_SLOT + x + (y * 2);
				guiItemStacks.init(index, true, x * 18, y * 18);
			}
		}

		guiItemStacks.set(GEM_SLOT, validGems);
		guiItemStacks.set(OUTPUT_SLOT, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		guiItemStacks.set(ingredients);
	}

	@Override
	public Class<? extends RecipeTartaricForge> getRecipeClass()
	{
		return RecipeTartaricForge.class;
	}

	@Override
	public void setIngredients(RecipeTartaricForge recipe, IIngredients ingredients)
	{
		List<ItemStack> validGems = Lists.newArrayList();
		for (DefaultWill will : DefaultWill.values())
		{
			if (will.minSouls >= recipe.getMinimumSouls())
			{
				validGems.add(will.willStack);
			}
		}

		ItemStack[] validGemStacks = new ItemStack[validGems.size()];
		for (int i = 0; i < validGemStacks.length; i++)
		{
			validGemStacks[i] = validGems.get(i);
		}

		List<Ingredient> ingList = Lists.newArrayList();
		ingList.add(Ingredient.of(validGemStacks));
		ingList.addAll(recipe.getInput());

		ingredients.setInputIngredients(ingList);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
	}

	public enum DefaultWill
	{
		SOUL(new ItemStack(BloodMagicItems.MONSTER_SOUL_RAW.get()), 16),
		PETTY(new ItemStack(BloodMagicItems.PETTY_GEM.get()), 64),
		LESSER(new ItemStack(BloodMagicItems.LESSER_GEM.get()), 256),
		COMMON(new ItemStack(BloodMagicItems.COMMON_GEM.get()), 1024),
		GREATER(new ItemStack(BloodMagicItems.GREATER_GEM.get()), 4096);
//		GRAND(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 4), 16384);

		public final ItemStack willStack;
		public final double minSouls;

		DefaultWill(ItemStack willStack, double minSouls)
		{
			this.willStack = willStack;
			this.minSouls = minSouls;
		}
	}
}
