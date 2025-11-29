package wayoftime.bloodmagic.common.recipe.alchemytable;

import com.google.common.base.Preconditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.recipe.BMRecipes;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;

public class AlchemyTableRecipe implements Recipe<AlchemyTableInput> {
    public static final String RECIPE_TYPE_NAME = "alchemytable";
    public static final int MAX_INPUTS = 6;

    @Nonnull
    protected final List<Ingredient> input;
    @Nonnull
    private final ItemStack output;
    @Nonnegative
    private final int syphon;
    @Nonnegative
    private final int ticks;
    @Nonnegative
    private final int minimumTier;

    public AlchemyTableRecipe(List<Ingredient> input, @Nonnull ItemStack output, int syphon, int ticks, int minimumTier) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative.");
        Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");

        this.input = input;
        this.output = output;
        this.syphon = syphon;
        this.ticks = ticks;
        this.minimumTier = minimumTier;
    }

    @Nonnull
    public List<Ingredient> getInput() {
        return input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(input);
        return list;
    }

    @Nonnull
    public ItemStack getOutput() {
        return output;
    }

    public int getSyphon() {
        return syphon;
    }

    public int getTicks() {
        return ticks;
    }

    public int getMinimumTier() {
        return minimumTier;
    }

    @Override
    public boolean matches(AlchemyTableInput container, Level level) {
        // Check if all ingredients are present in the container
        List<ItemStack> inputItems = container.items();

        // Create a mutable copy of ingredients to mark as matched
        List<Ingredient> remainingIngredients = new java.util.ArrayList<>(input);

        for (ItemStack stack : inputItems) {
            if (stack.isEmpty()) continue;

            // Try to match this stack with one of the remaining ingredients
            boolean matched = false;
            for (int i = 0; i < remainingIngredients.size(); i++) {
                if (remainingIngredients.get(i).test(stack)) {
                    remainingIngredients.remove(i);
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                // Extra item that doesn't match any ingredient
                return false;
            }
        }

        // All ingredients should be matched
        return remainingIngredients.isEmpty();
    }

    @Override
    public ItemStack assemble(AlchemyTableInput container, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BMRecipes.ALCHEMY_TABLE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BMRecipes.ALCHEMY_TABLE_TYPE.get();
    }
}
