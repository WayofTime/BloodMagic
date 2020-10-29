package wayoftime.bloodmagic.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.api.IBloodMagicRecipeRegistrar;
import wayoftime.bloodmagic.api.impl.recipe.RecipeARC;
import wayoftime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.api.impl.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;

public class BloodMagicRecipeRegistrar implements IBloodMagicRecipeRegistrar
{

//	private final Set<RecipeBloodAltar> altarRecipes;
//	private final Set<RecipeAlchemyTable> alchemyRecipes;
//	private final Set<RecipeTartaricForge> tartaricForgeRecipes;
//	private final Set<RecipeAlchemyArray> alchemyArrayRecipes;
//	private final Set<RecipeSacrificeCraft> sacrificeCraftRecipes;

	public BloodMagicRecipeRegistrar()
	{
//		this.altarRecipes = Sets.newHashSet();
//		this.alchemyRecipes = Sets.newHashSet();
//		this.tartaricForgeRecipes = Sets.newHashSet();
//		this.alchemyArrayRecipes = Sets.newHashSet();
//		this.sacrificeCraftRecipes = Sets.newHashSet();
	}

//	@Override
//	public void addBloodAltar(@Nonnull Ingredient input, @Nonnull ItemStack output, @Nonnegative int minimumTier,
//			@Nonnegative int syphon, @Nonnegative int consumeRate, @Nonnegative int drainRate)
//	{
//		Preconditions.checkNotNull(input, "input cannot be null.");
//		Preconditions.checkNotNull(output, "output cannot be null.");
//		Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");
//		Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
//		Preconditions.checkArgument(consumeRate >= 0, "consumeRate cannot be negative.");
//		Preconditions.checkArgument(drainRate >= 0, "drainRate cannot be negative.");
//
//		// TODO: Got to adda ResourceLocation argument.
//		altarRecipes.add(new IRecipeBloodAltar(null, input, output, minimumTier, syphon, consumeRate, drainRate));
//	}
//
//	@Override
//	public boolean removeBloodAltar(@Nonnull ItemStack input)
//	{
//		Preconditions.checkNotNull(input, "input cannot be null.");
//
//		return altarRecipes.remove(getBloodAltar(input));
//	}

//	@Override
//	public void addAlchemyTable(@Nonnull ItemStack output, @Nonnegative int syphon, @Nonnegative int ticks,
//			@Nonnegative int minimumTier, @Nonnull Ingredient... input)
//	{
//		Preconditions.checkNotNull(output, "output cannot be null.");
//		Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
//		Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative.");
//		Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");
//		Preconditions.checkNotNull(input, "input cannot be null.");
//
//		NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input);
//		alchemyRecipes.add(new RecipeAlchemyTable(inputs, output, syphon, ticks, minimumTier));
//	}
//
//	public void addAlchemyTable(@Nonnull ItemStack output, @Nonnegative int syphon, @Nonnegative int ticks,
//			@Nonnegative int minimumTier, @Nonnull Object... input)
//	{
//		Preconditions.checkNotNull(output, "output cannot be null.");
//		Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
//		Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative.");
//		Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");
//		Preconditions.checkNotNull(input, "input cannot be null.");
//
//		List<Ingredient> ingredients = Lists.newArrayList();
//		for (Object object : input)
//		{
//			if (object instanceof ItemStack && ((ItemStack) object).getItem() instanceof IBloodOrb)
//			{
//				ingredients.add(new IngredientBloodOrb(((IBloodOrb) ((ItemStack) object).getItem()).getOrb((ItemStack) object)));
//				continue;
//			}
//
//			ingredients.add(CraftingHelper.getIngredient(object));
//		}
//
//		addAlchemyTable(output, syphon, ticks, minimumTier, ingredients.toArray(new Ingredient[0]));
//	}
//
//	public void addAlchemyTable(RecipeAlchemyTable recipe)
//	{
//		alchemyRecipes.add(recipe);
//	}
//
//	@Override
//	public boolean removeAlchemyTable(@Nonnull ItemStack... input)
//	{
//		Preconditions.checkNotNull(input, "inputs cannot be null.");
//
//		for (ItemStack stack : input) Preconditions.checkNotNull(stack, "input cannot be null.");
//
//		return alchemyRecipes.remove(getAlchemyTable(Lists.newArrayList(input)));
//	}
//
//	@Override
//	public void addTartaricForge(@Nonnull ItemStack output, @Nonnegative double minimumSouls,
//			@Nonnegative double soulDrain, @Nonnull Ingredient... input)
//	{
//		Preconditions.checkNotNull(output, "output cannot be null.");
//		Preconditions.checkArgument(minimumSouls >= 0, "minimumSouls cannot be negative.");
//		Preconditions.checkArgument(soulDrain >= 0, "soulDrain cannot be negative.");
//		Preconditions.checkNotNull(input, "input cannot be null.");
//
//		NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input);
//		tartaricForgeRecipes.add(new RecipeTartaricForge(inputs, output, minimumSouls, soulDrain));
//	}
//
//	@Override
//	public boolean removeTartaricForge(@Nonnull ItemStack... input)
//	{
//		Preconditions.checkNotNull(input, "inputs cannot be null.");
//
//		for (ItemStack stack : input) Preconditions.checkNotNull(stack, "input cannot be null.");
//
//		return tartaricForgeRecipes.remove(getTartaricForge(Lists.newArrayList(input)));
//	}
//
//	public void addTartaricForge(@Nonnull ItemStack output, @Nonnegative double minimumSouls,
//			@Nonnegative double soulDrain, @Nonnull Object... input)
//	{
//		Preconditions.checkNotNull(output, "output cannot be null.");
//		Preconditions.checkArgument(minimumSouls >= 0, "minimumSouls cannot be negative.");
//		Preconditions.checkArgument(soulDrain >= 0, "soulDrain cannot be negative.");
//		Preconditions.checkNotNull(input, "input cannot be null.");
//
//		List<Ingredient> ingredients = Lists.newArrayList();
//		for (Object object : input)
//		{
//			if (object instanceof ItemStack && ((ItemStack) object).getItem() instanceof IBloodOrb)
//			{
//				ingredients.add(new IngredientBloodOrb(((IBloodOrb) ((ItemStack) object).getItem()).getOrb((ItemStack) object)));
//				continue;
//			}
//
//			ingredients.add(CraftingHelper.getIngredient(object));
//		}
//
//		addTartaricForge(output, minimumSouls, soulDrain, ingredients.toArray(new Ingredient[0]));
//	}
//
//	@Override
//	public void addAlchemyArray(@Nonnull Ingredient input, @Nonnull Ingredient catalyst, @Nonnull ItemStack output,
//			@Nullable ResourceLocation circleTexture)
//	{
//		Preconditions.checkNotNull(input, "input cannot be null.");
//		Preconditions.checkNotNull(catalyst, "catalyst cannot be null.");
//		Preconditions.checkNotNull(output, "output cannot be null.");
//
//		alchemyArrayRecipes.add(new RecipeAlchemyArray(input, catalyst, output, circleTexture));
//	}
//
//	@Override
//	public boolean removeAlchemyArray(@Nonnull ItemStack input, @Nonnull ItemStack catalyst)
//	{
//		Preconditions.checkNotNull(input, "input cannot be null.");
//		Preconditions.checkNotNull(catalyst, "catalyst cannot be null.");
//
//		return alchemyArrayRecipes.remove(getAlchemyArray(input, catalyst));
//	}
//
//	public void addAlchemyArray(@Nonnull ItemStack input, @Nonnull ItemStack catalyst, @Nonnull ItemStack output,
//			@Nullable ResourceLocation circleTexture)
//	{
//		Preconditions.checkNotNull(input, "input cannot be null.");
//		Preconditions.checkNotNull(catalyst, "catalyst cannot be null.");
//		Preconditions.checkNotNull(output, "output cannot be null.");
//
//		addAlchemyArray(Ingredient.fromStacks(input), Ingredient.fromStacks(catalyst), output, circleTexture);
//	}
//
//	public void addSacrificeCraft(@Nonnull ItemStack output, @Nonnegative double healthRequired,
//			@Nonnull Object... input)
//	{
//		Preconditions.checkNotNull(output, "output cannot be null.");
//		Preconditions.checkArgument(healthRequired >= 0, "healthRequired cannot be negative.");
//		Preconditions.checkNotNull(input, "input cannot be null.");
//
//		List<Ingredient> ingredients = Lists.newArrayList();
//		for (Object object : input)
//		{
//			if (object instanceof ItemStack && ((ItemStack) object).getItem() instanceof IBloodOrb)
//			{
//				ingredients.add(new IngredientBloodOrb(((IBloodOrb) ((ItemStack) object).getItem()).getOrb((ItemStack) object)));
//				continue;
//			}
//
//			ingredients.add(CraftingHelper.getIngredient(object));
//		}
//
//		addSacrificeCraft(output, healthRequired, ingredients.toArray(new Ingredient[0]));
//	}
//
//	@Override
//	public boolean removeSacrificeCraft(@Nonnull ItemStack... input)
//	{
//		Preconditions.checkNotNull(input, "inputs cannot be null.");
//
//		for (ItemStack stack : input) Preconditions.checkNotNull(stack, "input cannot be null.");
//
//		return sacrificeCraftRecipes.remove(getSacrificeCraft(Lists.newArrayList(input)));
//	}
//
//	@Override
//	public void addSacrificeCraft(@Nonnull ItemStack output, @Nonnegative double healthRequired,
//			@Nonnull Ingredient... input)
//	{
//		Preconditions.checkNotNull(output, "output cannot be null.");
//		Preconditions.checkArgument(healthRequired >= 0, "healthRequired cannot be negative.");
//		Preconditions.checkNotNull(input, "input cannot be null.");
//
//		NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input);
//		sacrificeCraftRecipes.add(new RecipeSacrificeCraft(inputs, output, healthRequired));
//	}

	@Nullable
	public RecipeBloodAltar getBloodAltar(World world, @Nonnull ItemStack input)
	{
		Preconditions.checkNotNull(input, "input cannot be null.");
		if (input.isEmpty())
			return null;

		List<RecipeBloodAltar> altarRecipes = world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ALTAR);

		for (RecipeBloodAltar recipe : altarRecipes) if (recipe.getInput().test(input))
			return recipe;

		return null;
	}

	public RecipeARC getARC(World world, @Nonnull ItemStack input, @Nonnull ItemStack arcToolInput, @Nonnull FluidStack inputFluid)
	{
		Preconditions.checkNotNull(input, "input cannot be null.");
		Preconditions.checkNotNull(arcToolInput, "tool cannot be null.");
		if (input.isEmpty() || arcToolInput.isEmpty())
			return null;

		List<RecipeARC> arcRecipes = world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ARC);

		for (RecipeARC recipe : arcRecipes)
		{
			if (recipe.getInput().test(input) && recipe.getTool().test(arcToolInput))
			{
				if (recipe.getFluidIngredient() == null)
				{
					return recipe;
				} else if (recipe.getFluidIngredient().test(inputFluid))
				{
					return recipe;
				}
			}
		}

//		if (input.isEmpty())
//			return null;
//
//		List<RecipeBloodAltar> altarRecipes = world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ALTAR);
//
//		for (RecipeBloodAltar recipe : altarRecipes) if (recipe.getInput().test(input))
//			return recipe;

		return null;
	}

//	@Nullable
//	public RecipeAlchemyTable getAlchemyTable(@Nonnull List<ItemStack> input)
//	{
//		Preconditions.checkNotNull(input, "input cannot be null.");
//		if (input.isEmpty())
//			return null;
//
//		mainLoop: for (RecipeAlchemyTable recipe : alchemyRecipes)
//		{
//			if (recipe.getInput().size() != input.size())
//				continue;
//
//			List<Ingredient> recipeInput = new ArrayList<>(recipe.getInput());
//
//			for (int i = 0; i < input.size(); i++)
//			{
//				boolean matched = false;
//				for (int j = 0; j < recipeInput.size(); j++)
//				{
//					Ingredient ingredient = recipeInput.get(j);
//					if (ingredient.apply(input.get(i)))
//					{
//						matched = true;
//						recipeInput.remove(j);
//						break;
//					}
//				}
//
//				if (!matched)
//					continue mainLoop;
//			}
//
//			return recipe;
//		}
//
//		return null;
//	}
//
	@Nullable
	public RecipeTartaricForge getTartaricForge(World world, @Nonnull List<ItemStack> input)
	{
		Preconditions.checkNotNull(input, "input cannot be null.");
		if (input.isEmpty())
			return null;

		List<RecipeTartaricForge> tartaricForgeRecipes = world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.TARTARICFORGE);
		mainLoop: for (RecipeTartaricForge recipe : tartaricForgeRecipes)
		{
			if (recipe.getInput().size() != input.size())
				continue;

			List<Ingredient> recipeInput = new ArrayList<>(recipe.getInput());

			for (int i = 0; i < input.size(); i++)
			{
				boolean matched = false;
				for (int j = 0; j < recipeInput.size(); j++)
				{
					Ingredient ingredient = recipeInput.get(j);
					if (ingredient.test(input.get(i)))
					{
						matched = true;
						recipeInput.remove(j);
						break;
					}
				}

				if (!matched)
					continue mainLoop;
			}

			return recipe;
		}

		return null;
	}

//
//	@Nullable
//	public RecipeSacrificeCraft getSacrificeCraft(@Nonnull List<ItemStack> input)
//	{
//		Preconditions.checkNotNull(input, "input cannot be null.");
//		if (input.isEmpty())
//			return null;
//
//		mainLoop: for (RecipeSacrificeCraft recipe : sacrificeCraftRecipes)
//		{
//			if (recipe.getInput().size() != input.size())
//				continue;
//
//			List<Ingredient> recipeInput = new ArrayList<>(recipe.getInput());
//
//			for (int i = 0; i < input.size(); i++)
//			{
//				boolean matched = false;
//				for (int j = 0; j < recipeInput.size(); j++)
//				{
//					Ingredient ingredient = recipeInput.get(j);
//					if (ingredient.apply(input.get(i)))
//					{
//						matched = true;
//						recipeInput.remove(j);
//						break;
//					}
//				}
//
//				if (!matched)
//					continue mainLoop;
//			}
//
//			return recipe;
//		}
//
//		return null;
//	}
//
	/**
	 * 
	 * @param world
	 * @param input
	 * @param catalyst
	 * @return If false and the recipe is nonnull, it is a partial match. If true,
	 *         the returned recipe is a full match.
	 */
	@Nullable
	public Pair<Boolean, RecipeAlchemyArray> getAlchemyArray(World world, @Nonnull ItemStack input, @Nonnull ItemStack catalyst)
	{
		Preconditions.checkNotNull(input, "input cannot be null.");
		if (input.isEmpty())
			return null;

		List<RecipeAlchemyArray> altarRecipes = world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ARRAY);

		RecipeAlchemyArray partialMatch = null;
		for (RecipeAlchemyArray recipe : altarRecipes)
		{
			if (recipe.getBaseInput().test(input))
			{
				if (recipe.getAddedInput().test(catalyst))
				{
					return Pair.of(true, recipe);
				} else if (partialMatch == null)
				{
					partialMatch = recipe;
				}
			}
		}

		return Pair.of(false, partialMatch);
	}

	public Set<RecipeBloodAltar> getAltarRecipes(World world)
	{
		return ImmutableSet.copyOf(world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ALTAR));
	}

	public Set<RecipeTartaricForge> getTartaricForgeRecipes(World world)
	{
		return ImmutableSet.copyOf(world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.TARTARICFORGE));
	}

	public Set<RecipeAlchemyArray> getAlchemyArrayRecipes(World world)
	{
		return ImmutableSet.copyOf(world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ARRAY));
	}

	public Set<RecipeAlchemyArray> getCraftingAlchemyArrayRecipes(World world)
	{
		Set<RecipeAlchemyArray> recipes = Set.copyOf(world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ARRAY));
		Set<RecipeAlchemyArray> copyRecipes = Set.of();
		for (RecipeAlchemyArray recipe : recipes)
		{
			if (!recipe.getOutput().isEmpty())
			{
				copyRecipes.add(recipe);
			}
		}

		return copyRecipes;
	}

//	public Set<RecipeAlchemyTable> getAlchemyRecipes()
//	{
//		return ImmutableSet.copyOf(alchemyRecipes);
//	}
//
//	public Set<RecipeTartaricForge> getTartaricForgeRecipes()
//	{
//		return ImmutableSet.copyOf(tartaricForgeRecipes);
//	}
//
//	public Set<RecipeAlchemyArray> getAlchemyArrayRecipes()
//	{
//		return ImmutableSet.copyOf(alchemyArrayRecipes);
//	}
}
