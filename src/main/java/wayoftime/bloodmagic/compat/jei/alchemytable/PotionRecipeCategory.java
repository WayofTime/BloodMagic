package wayoftime.bloodmagic.compat.jei.alchemytable;

import com.google.common.collect.Lists;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlask;
import wayoftime.bloodmagic.core.registry.OrbRegistry;
import wayoftime.bloodmagic.recipe.EffectHolder;
import wayoftime.bloodmagic.recipe.flask.RecipePotionFlaskBase;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PotionRecipeCategory implements IRecipeCategory<RecipePotionFlaskBase>
{
	private static final int OUTPUT_SLOT = 0;
	private static final int ORB_SLOT = 1;
	private static final int INPUT_SLOT = 2;
	public static final ResourceLocation UID = BloodMagic.rl(Constants.Compat.JEI_CATEGORY_POTION);

	@Nonnull
	private final IDrawable background;
	private final IDrawable icon;
//	@Nonnull
//	private final ICraftingGridHelper craftingGridHelper;

	public PotionRecipeCategory(IGuiHelper guiHelper)
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
	public List<Component> getTooltipStrings(RecipePotionFlaskBase recipe, double mouseX, double mouseY)
	{
		List<Component> tooltip = Lists.newArrayList();

//		if (mouseX >= 13 && mouseX <= 64 && mouseY >= 27 && mouseY <= 58)
//		{
//			tooltip.add(new TranslationTextComponent("jei.bloodmagic.recipe.consumptionrate", ChatUtil.DECIMAL_FORMAT.format(recipe.getConsumeRate())));
//			tooltip.add(new TranslationTextComponent("jei.bloodmagic.recipe.drainrate", ChatUtil.DECIMAL_FORMAT.format(recipe.getDrainRate())));
//		}

		if (mouseX >= 58 && mouseX <= 78 && mouseY >= 21 && mouseY <= 34)
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.tier", ChatUtil.DECIMAL_FORMAT.format(recipe.getMinimumTier())));
			tooltip.add(Component.translatable("jei.bloodmagic.recipe.lpDrained", ChatUtil.DECIMAL_FORMAT.format(recipe.getSyphon())));
			tooltip.add(Component.translatable("jei.bloodmagic.recipe.ticksRequired", ChatUtil.DECIMAL_FORMAT.format(recipe.getTicks())));
		}

		return tooltip;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return Component.translatable("jei.bloodmagic.recipe.potionflask");
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
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RecipePotionFlaskBase recipe, @Nonnull IFocusGroup focuses)
	{
		List<ItemStack> validOrbs = OrbRegistry.getOrbsDownToTier(recipe.getMinimumTier());

		ItemStack[] validOrbStacks = new ItemStack[validOrbs.size()];
		for (int i = 0; i < validOrbStacks.length; i++)
		{
			validOrbStacks[i] = validOrbs.get(i);
		}

		Item[] flaskItems = new Item[] { BloodMagicItems.ALCHEMY_FLASK.get(),
				BloodMagicItems.ALCHEMY_FLASK_THROWABLE.get(), BloodMagicItems.ALCHEMY_FLASK_LINGERING.get() };

		ItemStack[] flaskStacks = new ItemStack[flaskItems.length];

		builder.addSlot(RecipeIngredientRole.INPUT, 61, 1).addIngredients(Ingredient.of(validOrbStacks)).setSlotName("orbs");

//		ingList.addAll(recipe.getInput());
		ItemStack flaskStack = recipe.getExamplePotionFlask();

		for (int i = 0; i < flaskItems.length; i++)
		{
			Item inputFlask = flaskItems[i];
			ItemStack copyFlaskStack = new ItemStack(inputFlask);
			copyFlaskStack.setTag(flaskStack.getTag());

			flaskStacks[i] = copyFlaskStack;
		}

//		ingList.add(Ingredient.of(flaskStacks));

//		List<MobEffectInstance> inputEffectInstances = PotionUtils.getMobEffects(flaskStack);
//		List<MobEffect> inputEffects = new ArrayList<>();
//		for (MobEffectInstance inst : inputEffectInstances)
//		{
//			inputEffects.add(inst.getEffect());
//		}
//
//		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(Ingredient.of(flaskStacks)).addIngredients(BloodMagicIngredientTypes.EFFECT, inputEffects).setSlotName("flask");
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(Ingredient.of(flaskStacks)).setSlotName("flask");
		List<Ingredient> inputList = recipe.getInput();

		for (int index = 0; index < inputList.size(); index++)
		{
			int x = (index + 1) % 3;
			int y = (index + 1) / 3;
			builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + 1, y * 18 + 1).addIngredients(inputList.get(index)).setSlotName("input");
		}

		List<EffectHolder> holderList = ((ItemAlchemyFlask) flaskStack.getItem()).getEffectHoldersOfFlask(flaskStack);
//		ingredients.setInputIngredients(ingList);

		ItemStack outputStack = recipe.getOutput(flaskStack, holderList);
		((ItemAlchemyFlask) flaskStack.getItem()).resyncEffectInstances(outputStack);
//		ingredients.setOutput(VanillaTypes.ITEM, outputStack);

//		List<MobEffectInstance> outputEffectInstances = PotionUtils.getMobEffects(flaskStack);
//		List<MobEffect> outputEffects = new ArrayList<>();
//		for (MobEffectInstance inst : outputEffectInstances)
//		{
//			outputEffects.add(inst.getEffect());
//		}
//		
//		builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 14).addItemStack(outputStack).addIngredients(BloodMagicIngredientTypes.EFFECT, outputEffects).setSlotName("output");
		builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 14).addItemStack(outputStack).setSlotName("output");

//		ItemStack[] outputFlaskStacks = new ItemStack[flaskItems.length];
//		for (int i = 0; i < flaskItems.length; i++)
//		{
//			ItemStack outputStack = recipe.getOutput(flaskStacks[i], holderList);
//			((ItemAlchemyFlask) flaskStacks[i].getItem()).resyncEffectInstances(outputStack);
//			outputFlaskStacks[i] = outputStack;
//		}
//		
//		builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 14).addIngredients(Ingredient.of(outputFlaskStacks)).setSlotName("output");

	}

//	@Override
//	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull RecipePotionFlaskBase recipe, @Nonnull IIngredients ingredients)
//	{
//		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
//
//		guiItemStacks.init(OUTPUT_SLOT, false, 91, 13);
//		guiItemStacks.init(ORB_SLOT, true, 60, 0);
//
//		for (int y = 0; y < 2; ++y)
//		{
//			for (int x = 0; x < 3; ++x)
//			{
//				int index = INPUT_SLOT + x + (y * 3);
//				guiItemStacks.init(index, true, x * 18, y * 18);
//			}
//		}
////		guiItemStacks.set(ORB_SLOT, OrbRegistry.getOrbsDownToTier(recipe.getMinimumTier()));
////		guiItemStacks.set(OUTPUT_SLOT, ingredients.getOutputs(ItemStack.class).get(0));
//
////		guiItemStacks.init(OUTPUT_SLOT, false, 125, 30);
////		guiItemStacks.init(INPUT_SLOT, true, 31, 0);
////        craftingGridHelper.setInputs(guiItemStacks, ingredients.getInputs(ItemStack.class), 3, 2);
//
//		guiItemStacks.set(ingredients);
//	}

	@Override
	public Class<? extends RecipePotionFlaskBase> getRecipeClass()
	{
		return RecipePotionFlaskBase.class;
	}

	@Override
	public void setIngredients(RecipePotionFlaskBase recipe, IIngredients ingredients)
	{
		List<ItemStack> validOrbs = OrbRegistry.getOrbsDownToTier(recipe.getMinimumTier());

		ItemStack[] validOrbStacks = new ItemStack[validOrbs.size()];
		for (int i = 0; i < validOrbStacks.length; i++)
		{
			validOrbStacks[i] = validOrbs.get(i);
		}

		Item[] flaskItems = new Item[] { BloodMagicItems.ALCHEMY_FLASK.get(),
				BloodMagicItems.ALCHEMY_FLASK_THROWABLE.get(), BloodMagicItems.ALCHEMY_FLASK_LINGERING.get() };

		ItemStack[] flaskStacks = new ItemStack[flaskItems.length];

		List<Ingredient> ingList = Lists.newArrayList();
		ingList.add(Ingredient.of(validOrbStacks));
		ingList.addAll(recipe.getInput());
		ItemStack flaskStack = recipe.getExamplePotionFlask();

		for (int i = 0; i < flaskItems.length; i++)
		{
			Item inputFlask = flaskItems[i];
			ItemStack copyFlaskStack = new ItemStack(inputFlask);
			copyFlaskStack.setTag(flaskStack.getTag());

			flaskStacks[i] = copyFlaskStack;
		}

		ingList.add(Ingredient.of(flaskStacks));
		List<EffectHolder> holderList = ((ItemAlchemyFlask) flaskStack.getItem()).getEffectHoldersOfFlask(flaskStack);
		ingredients.setInputIngredients(ingList);
		ItemStack outputStack = recipe.getOutput(flaskStack, holderList);
		((ItemAlchemyFlask) flaskStack.getItem()).resyncEffectInstances(outputStack);
		ingredients.setOutput(VanillaTypes.ITEM, outputStack);
	}
}
