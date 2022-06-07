package wayoftime.bloodmagic.impl;

import java.util.ArrayList;
import java.util.HashSet;
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
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.EffectHolder;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.recipe.RecipeLivingDowngrade;
import wayoftime.bloodmagic.recipe.RecipeMeteor;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.recipe.flask.RecipePotionFlaskBase;

public class BloodMagicRecipeRegistrar
{
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

	@Nullable
	public RecipeAlchemyTable getAlchemyTable(World world, @Nonnull List<ItemStack> input)
	{
		Preconditions.checkNotNull(input, "input cannot be null.");
		if (input.isEmpty())
			return null;

		List<RecipeAlchemyTable> tartaricForgeRecipes = world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ALCHEMYTABLE);
		mainLoop: for (RecipeAlchemyTable recipe : tartaricForgeRecipes)
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

	@Nullable
	public RecipeMeteor getMeteor(World world, @Nonnull ItemStack input)
	{
		Preconditions.checkNotNull(input, "input cannot be null.");
		if (input.isEmpty())
			return null;

		List<RecipeMeteor> meteorRecipes = world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.METEOR);
//		System.out.println("Number of recipes: " + meteorRecipes.size());

		for (RecipeMeteor recipe : meteorRecipes) if (recipe.getInput().test(input))
			return recipe;

		return null;
	}

	@Nullable
	public RecipePotionFlaskBase getPotionFlaskRecipe(World world, ItemStack flaskStack, @Nonnull List<EffectHolder> holderList, @Nonnull List<ItemStack> input)
	{
		Preconditions.checkNotNull(input, "input cannot be null.");
		if (input.isEmpty())
			return null;

//		List<EffectHolder> holderList = ((ItemAlchemyFlask) flaskStack.getItem()).getEffectHoldersOfFlask(flaskStack);
//		Collection<EffectInstance> instanceList = PotionUtils.getEffectsFromStack(flaskStack);

		List<RecipePotionFlaskBase> potionRecipes = world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.POTIONFLASK);
//		System.out.println("Number of recipes: " + potionRecipes.size());

		RecipePotionFlaskBase validRecipe = null;
		int recipePriority = Integer.MIN_VALUE;

		mainLoop: for (RecipePotionFlaskBase recipe : potionRecipes)
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

//			System.out.println("Passed ingredient check");

			// Now check if recipe works with flask's current effects.
			if (recipe.canModifyFlask(flaskStack, holderList))
			{
				int prio = recipe.getPriority(holderList);
				if (prio > recipePriority)
				{
					validRecipe = recipe;
					recipePriority = prio;
				}
			}
		}

		return validRecipe;
	}

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

	@Nullable
	public RecipeLivingDowngrade getLivingDowngrade(World world, @Nonnull ItemStack input)
	{
		Preconditions.checkNotNull(input, "input cannot be null.");
		if (input.isEmpty())
			return null;

		List<RecipeLivingDowngrade> downgradeRecipes = world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.LIVINGDOWNGRADE);

		for (RecipeLivingDowngrade recipe : downgradeRecipes) if (recipe.getInput().test(input))
			return recipe;

		return null;
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

	public Set<RecipeARC> getARCRecipes(World world)
	{
		return ImmutableSet.copyOf(world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ARC));
	}

	public Set<RecipeAlchemyTable> getAlchemyTableRecipes(World world)
	{
		return ImmutableSet.copyOf(world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ALCHEMYTABLE));
	}

	public Set<RecipePotionFlaskBase> getPotionFlaskRecipes(World world)
	{
		return ImmutableSet.copyOf(world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.POTIONFLASK));
	}

	public Set<RecipeAlchemyArray> getCraftingAlchemyArrayRecipes(World world)
	{
		Set<RecipeAlchemyArray> recipes = new HashSet<RecipeAlchemyArray>(world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ARRAY));

		Set<RecipeAlchemyArray> copyRecipes = new HashSet<RecipeAlchemyArray>();
		for (RecipeAlchemyArray recipe : recipes)
		{
			if (!recipe.getOutput().isEmpty())
			{
				copyRecipes.add(recipe);
			}
		}

		return copyRecipes;
	}
}
