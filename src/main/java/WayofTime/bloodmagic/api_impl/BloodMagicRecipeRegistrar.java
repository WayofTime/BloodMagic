package WayofTime.bloodmagic.api_impl;

import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api_impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.api_impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.api_impl.recipe.RecipeTartaricForge;
import WayofTime.bloodmagic.apiv2.IBloodMagicRecipeRegistrar;
import WayofTime.bloodmagic.core.recipe.IngredientBloodOrb;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class BloodMagicRecipeRegistrar implements IBloodMagicRecipeRegistrar {

    private final Set<RecipeBloodAltar> altarRecipes;
    private final Set<RecipeAlchemyTable> alchemyRecipes;
    private final Set<RecipeTartaricForge> tartaricForgeRecipes;

    public BloodMagicRecipeRegistrar() {
        this.altarRecipes = Sets.newHashSet();
        this.alchemyRecipes = Sets.newHashSet();
        this.tartaricForgeRecipes = Sets.newHashSet();
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

            for (int i = 0; i < input.size(); i++) {
                Ingredient ingredient = recipe.getInput().get(i);
                if (!ingredient.apply(input.get(i)))
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

            for (int i = 0; i < input.size(); i++) {
                Ingredient ingredient = recipe.getInput().get(i);
                if (!ingredient.apply(input.get(i)))
                    continue mainLoop;
            }

            return recipe;
        }

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
}
