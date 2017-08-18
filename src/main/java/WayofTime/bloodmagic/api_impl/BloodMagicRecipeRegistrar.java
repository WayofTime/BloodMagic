package WayofTime.bloodmagic.api_impl;

import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api_impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.api_impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.api_impl.recipe.RecipeTartaricForge;
import WayofTime.bloodmagic.apiv2.IBloodMagicRecipeRegistrar;
import WayofTime.bloodmagic.core.recipe.IngredientBloodOrb;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class BloodMagicRecipeRegistrar implements IBloodMagicRecipeRegistrar {

    private final Map<Ingredient, RecipeBloodAltar> altarRecipes;
    private final Map<List<Ingredient>, RecipeAlchemyTable> alchemyRecipes;
    private final Map<List<Ingredient>, RecipeTartaricForge> tartaricForgeRecipes;

    public BloodMagicRecipeRegistrar() {
        this.altarRecipes = Maps.newHashMap();
        this.alchemyRecipes = Maps.newHashMap();
        this.tartaricForgeRecipes = Maps.newHashMap();
    }

    @Override
    public void addBloodAltar(@Nonnull Ingredient input, @Nonnull ItemStack output, @Nonnegative int minimumTier, @Nonnegative int syphon, @Nonnegative int consumeRate, @Nonnegative int drainRate) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");
        Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
        Preconditions.checkArgument(consumeRate >= 0, "consumeRate cannot be negative.");
        Preconditions.checkArgument(drainRate >= 0, "drainRate cannot be negative.");

        altarRecipes.put(input, new RecipeBloodAltar(input, output, minimumTier, syphon, consumeRate, drainRate));
    }

    @Override
    public void addAlchemyTable(@Nonnull ItemStack output, @Nonnegative int syphon, @Nonnegative int ticks, @Nonnegative int minimumTier, @Nonnull Ingredient... input) {
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative.");
        Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");
        Preconditions.checkNotNull(input, "input cannot be null.");

        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input);
        alchemyRecipes.put(inputs, new RecipeAlchemyTable(inputs, output, syphon, ticks, minimumTier));
    }

    @Override
    public void addTartaricForge(@Nonnull ItemStack output, @Nonnegative double minimumSouls, @Nonnegative double soulDrain, @Nonnull Ingredient... input) {
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(minimumSouls >= 0, "minimumSouls cannot be negative.");
        Preconditions.checkArgument(soulDrain >= 0, "soulDrain cannot be negative.");
        Preconditions.checkNotNull(input, "input cannot be null.");

        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input);
        tartaricForgeRecipes.put(inputs, new RecipeTartaricForge(inputs, output, minimumSouls, soulDrain));
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

        for (Map.Entry<Ingredient, RecipeBloodAltar> entry : altarRecipes.entrySet())
            if (entry.getKey().test(input))
                return entry.getValue();

        return null;
    }

    @Nullable
    public RecipeAlchemyTable getAlchemyTable(@Nonnull List<ItemStack> input) {
        Preconditions.checkNotNull(input, "input cannot be null.");

        mainLoop:
        for (Map.Entry<List<Ingredient>, RecipeAlchemyTable> entry : alchemyRecipes.entrySet()) {
            if (entry.getKey().size() != input.size())
                continue;

            for (int i = 0; i > input.size(); i++) {
                Ingredient ingredient = entry.getKey().get(i);
                if (!ingredient.apply(input.get(i)))
                    continue mainLoop;
            }

            return entry.getValue();
        }

        return null;
    }

    @Nullable
    public RecipeTartaricForge getTartaricForge(@Nonnull List<ItemStack> input) {
        Preconditions.checkNotNull(input, "input cannot be null.");

        mainLoop:
        for (Map.Entry<List<Ingredient>, RecipeTartaricForge> entry : tartaricForgeRecipes.entrySet()) {
            if (entry.getKey().size() != input.size())
                continue;

            for (int i = 0; i > input.size(); i++) {
                Ingredient ingredient = entry.getKey().get(i);
                if (!ingredient.apply(input.get(i)))
                    continue mainLoop;
            }

            return entry.getValue();
        }

        return null;
    }

    public Map<Ingredient, RecipeBloodAltar> getAltarRecipes() {
        return ImmutableMap.copyOf(altarRecipes);
    }

    public Map<List<Ingredient>, RecipeAlchemyTable> getAlchemyRecipes() {
        return ImmutableMap.copyOf(alchemyRecipes);
    }

    public Map<List<Ingredient>, RecipeTartaricForge> getTartaricForgeRecipes() {
        return ImmutableMap.copyOf(tartaricForgeRecipes);
    }
}
