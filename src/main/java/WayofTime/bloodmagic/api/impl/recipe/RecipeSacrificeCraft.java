package WayofTime.bloodmagic.api.impl.recipe;

import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class RecipeSacrificeCraft {
    @Nonnull
    private final NonNullList<Ingredient> input;
    @Nonnull
    private final ItemStack output;
    @Nonnegative
    private final double healthRequired;

    public RecipeSacrificeCraft(@Nonnull NonNullList<Ingredient> input, @Nonnull ItemStack output, @Nonnegative double healthRequired) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(healthRequired >= 0, "healthRequired cannot be negative.");

        this.input = input;
        this.output = output;
        this.healthRequired = healthRequired;
    }

    @Nonnull
    public final NonNullList<Ingredient> getInput() {
        return input;
    }

    @Nonnull
    public final ItemStack getOutput() {
        return output;
    }

    @Nonnegative
    public final double getHealthRequired() {
        return healthRequired;
    }
}
