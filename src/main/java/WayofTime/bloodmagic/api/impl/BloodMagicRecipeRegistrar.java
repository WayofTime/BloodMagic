package WayofTime.bloodmagic.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import WayofTime.bloodmagic.api.IBloodMagicRecipeRegistrar;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.api.impl.recipe.RecipeSacrificeCraft;
import WayofTime.bloodmagic.api.impl.recipe.RecipeTartaricForge;
import WayofTime.bloodmagic.core.recipe.IngredientBloodOrb;
import WayofTime.bloodmagic.orb.IBloodOrb;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class BloodMagicRecipeRegistrar implements IBloodMagicRecipeRegistrar {

    private final Set<RecipeBloodAltar> altarRecipes;
    private final Set<RecipeAlchemyTable> alchemyRecipes;
    private final Set<RecipeTartaricForge> tartaricForgeRecipes;
    private final Set<RecipeAlchemyArray> alchemyArrayRecipes;
    private final Set<RecipeSacrificeCraft> sacrificeCraftRecipes;

    public BloodMagicRecipeRegistrar() {
        this.altarRecipes = Sets.newHashSet();
        this.alchemyRecipes = Sets.newHashSet();
        this.tartaricForgeRecipes = Sets.newHashSet();
        this.alchemyArrayRecipes = Sets.newHashSet();
        this.sacrificeCraftRecipes = Sets.newHashSet();
    }

    @Override
    public void addBloodAltar(@Nonnull Ingredient input, @Nonnull ItemStack output, @Nonnegative int minimumTier, @Nonnegative int syphon, @Nonnegative int consumeRate, @Nonnegative int drainRate) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");
        Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
        Preconditions.checkArgument(consumeRate >= 0, "consumeRate cannot be negative.");
        Preconditions.checkArgument(drainRate >= 0, "drainRate cannot be negative.");

        altarRecipes.add(new RecipeBloodAltar(input, output, minimumTier, syphon, consumeRate, drainRate));
    }

    @Override
    public boolean removeBloodAltar(@Nonnull ItemStack input) {
        Preconditions.checkNotNull(input, "input cannot be null.");

        return altarRecipes.remove(getBloodAltar(input));
    }

    @Override
    public void addAlchemyTable(@Nonnull ItemStack output, @Nonnegative int syphon, @Nonnegative int ticks, @Nonnegative int minimumTier, @Nonnull Ingredient... input) {
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative.");
        Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");
        Preconditions.checkNotNull(input, "input cannot be null.");

        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input);
        alchemyRecipes.add(new RecipeAlchemyTable(inputs, output, syphon, ticks, minimumTier));
    }

    public void addAlchemyTable(@Nonnull ItemStack output, @Nonnegative int syphon, @Nonnegative int ticks, @Nonnegative int minimumTier, @Nonnull Object... input) {
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative.");
        Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");
        Preconditions.checkNotNull(input, "input cannot be null.");

        List<Ingredient> ingredients = Lists.newArrayList();
        for (Object object : input) {
            if (object instanceof ItemStack && ((ItemStack) object).getItem() instanceof IBloodOrb) {
                ingredients.add(new IngredientBloodOrb(((IBloodOrb) ((ItemStack) object).getItem()).getOrb((ItemStack) object)));
                continue;
            }

            ingredients.add(CraftingHelper.getIngredient(object));
        }

        addAlchemyTable(output, syphon, ticks, minimumTier, ingredients.toArray(new Ingredient[0]));
    }

    public void addAlchemyTable(RecipeAlchemyTable recipe) {
        alchemyRecipes.add(recipe);
    }

    @Override
    public boolean removeAlchemyTable(@Nonnull ItemStack... input) {
        Preconditions.checkNotNull(input, "inputs cannot be null.");

        for (ItemStack stack : input)
            Preconditions.checkNotNull(stack, "input cannot be null.");

        return alchemyRecipes.remove(getAlchemyTable(Lists.newArrayList(input)));
    }

    @Override
    public void addTartaricForge(@Nonnull ItemStack output, @Nonnegative double minimumSouls, @Nonnegative double soulDrain, @Nonnull Ingredient... input) {
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(minimumSouls >= 0, "minimumSouls cannot be negative.");
        Preconditions.checkArgument(soulDrain >= 0, "soulDrain cannot be negative.");
        Preconditions.checkNotNull(input, "input cannot be null.");

        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input);
        tartaricForgeRecipes.add(new RecipeTartaricForge(inputs, output, minimumSouls, soulDrain));
    }

    @Override
    public boolean removeTartaricForge(@Nonnull ItemStack... input) {
        Preconditions.checkNotNull(input, "inputs cannot be null.");

        for (ItemStack stack : input)
            Preconditions.checkNotNull(stack, "input cannot be null.");

        return tartaricForgeRecipes.remove(getTartaricForge(Lists.newArrayList(input)));
    }

    public void addTartaricForge(@Nonnull ItemStack output, @Nonnegative double minimumSouls, @Nonnegative double soulDrain, @Nonnull Object... input) {
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(minimumSouls >= 0, "minimumSouls cannot be negative.");
        Preconditions.checkArgument(soulDrain >= 0, "soulDrain cannot be negative.");
        Preconditions.checkNotNull(input, "input cannot be null.");

        List<Ingredient> ingredients = Lists.newArrayList();
        for (Object object : input) {
            if (object instanceof ItemStack && ((ItemStack) object).getItem() instanceof IBloodOrb) {
                ingredients.add(new IngredientBloodOrb(((IBloodOrb) ((ItemStack) object).getItem()).getOrb((ItemStack) object)));
                continue;
            }

            ingredients.add(CraftingHelper.getIngredient(object));
        }

        addTartaricForge(output, minimumSouls, soulDrain, ingredients.toArray(new Ingredient[0]));
    }

    @Override
    public void addAlchemyArray(@Nonnull Ingredient input, @Nonnull Ingredient catalyst, @Nonnull ItemStack output, @Nullable ResourceLocation circleTexture) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(catalyst, "catalyst cannot be null.");
        Preconditions.checkNotNull(output, "output cannot be null.");

        alchemyArrayRecipes.add(new RecipeAlchemyArray(input, catalyst, output, circleTexture));
    }

    @Override
    public boolean removeAlchemyArray(@Nonnull ItemStack input, @Nonnull ItemStack catalyst) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(catalyst, "catalyst cannot be null.");

        return alchemyArrayRecipes.remove(getAlchemyArray(input, catalyst));
    }

    public void addAlchemyArray(@Nonnull ItemStack input, @Nonnull ItemStack catalyst, @Nonnull ItemStack output, @Nullable ResourceLocation circleTexture) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(catalyst, "catalyst cannot be null.");
        Preconditions.checkNotNull(output, "output cannot be null.");

        addAlchemyArray(Ingredient.fromStacks(input), Ingredient.fromStacks(catalyst), output, circleTexture);
    }

    public void addSacrificeCraft(@Nonnull ItemStack output, @Nonnegative double healthRequired, @Nonnull Object... input) {
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(healthRequired >= 0, "healthRequired cannot be negative.");
        Preconditions.checkNotNull(input, "input cannot be null.");

        List<Ingredient> ingredients = Lists.newArrayList();
        for (Object object : input) {
            if (object instanceof ItemStack && ((ItemStack) object).getItem() instanceof IBloodOrb) {
                ingredients.add(new IngredientBloodOrb(((IBloodOrb) ((ItemStack) object).getItem()).getOrb((ItemStack) object)));
                continue;
            }

            ingredients.add(CraftingHelper.getIngredient(object));
        }

        addSacrificeCraft(output, healthRequired, ingredients.toArray(new Ingredient[0]));
    }

    @Override
    public boolean removeSacrificeCraft(@Nonnull ItemStack... input) {
        Preconditions.checkNotNull(input, "inputs cannot be null.");

        for (ItemStack stack : input)
            Preconditions.checkNotNull(stack, "input cannot be null.");

        return sacrificeCraftRecipes.remove(getSacrificeCraft(Lists.newArrayList(input)));
    }

    @Override
    public void addSacrificeCraft(@Nonnull ItemStack output, @Nonnegative double healthRequired, @Nonnull Ingredient... input) {
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(healthRequired >= 0, "healthRequired cannot be negative.");
        Preconditions.checkNotNull(input, "input cannot be null.");

        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input);
        sacrificeCraftRecipes.add(new RecipeSacrificeCraft(inputs, output, healthRequired));
    }

    @Nullable
    public RecipeBloodAltar getBloodAltar(@Nonnull ItemStack input) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        if (input.isEmpty())
            return null;

        for (RecipeBloodAltar recipe : altarRecipes)
            if (recipe.getInput().test(input))
                return recipe;

        return null;
    }

    @Nullable
    public RecipeAlchemyTable getAlchemyTable(@Nonnull List<ItemStack> input) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        if (input.isEmpty())
            return null;

        mainLoop:
        for (RecipeAlchemyTable recipe : alchemyRecipes) {
            if (recipe.getInput().size() != input.size())
                continue;

            List<Ingredient> recipeInput = new ArrayList<>(recipe.getInput());

            for (int i = 0; i < input.size(); i++) {
                boolean matched = false;
                for (int j = 0; j < recipeInput.size(); j++) {
                    Ingredient ingredient = recipeInput.get(j);
                    if (ingredient.apply(input.get(i))) {
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
    public RecipeTartaricForge getTartaricForge(@Nonnull List<ItemStack> input) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        if (input.isEmpty())
            return null;

        mainLoop:
        for (RecipeTartaricForge recipe : tartaricForgeRecipes) {
            if (recipe.getInput().size() != input.size())
                continue;

            List<Ingredient> recipeInput = new ArrayList<>(recipe.getInput());

            for (int i = 0; i < input.size(); i++) {
                boolean matched = false;
                for (int j = 0; j < recipeInput.size(); j++) {
                    Ingredient ingredient = recipeInput.get(j);
                    if (ingredient.apply(input.get(i))) {
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
    public RecipeSacrificeCraft getSacrificeCraft(@Nonnull List<ItemStack> input) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        if (input.isEmpty())
            return null;

        mainLoop:
        for (RecipeSacrificeCraft recipe : sacrificeCraftRecipes) {
            if (recipe.getInput().size() != input.size())
                continue;

            List<Ingredient> recipeInput = new ArrayList<>(recipe.getInput());

            for (int i = 0; i < input.size(); i++) {
                boolean matched = false;
                for (int j = 0; j < recipeInput.size(); j++) {
                    Ingredient ingredient = recipeInput.get(j);
                    if (ingredient.apply(input.get(i))) {
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
    public RecipeAlchemyArray getAlchemyArray(@Nonnull ItemStack input, @Nonnull ItemStack catalyst) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        if (input.isEmpty())
            return null;

        for (RecipeAlchemyArray recipe : alchemyArrayRecipes)
            if (recipe.getInput().test(input) && recipe.getCatalyst().test(catalyst))
                return recipe;

        return null;
    }

    public Set<RecipeBloodAltar> getAltarRecipes() {
        return ImmutableSet.copyOf(altarRecipes);
    }

    public Set<RecipeAlchemyTable> getAlchemyRecipes() {
        return ImmutableSet.copyOf(alchemyRecipes);
    }

    public Set<RecipeTartaricForge> getTartaricForgeRecipes() {
        return ImmutableSet.copyOf(tartaricForgeRecipes);
    }

    public Set<RecipeAlchemyArray> getAlchemyArrayRecipes() {
        return ImmutableSet.copyOf(alchemyArrayRecipes);
    }
}
