package WayofTime.bloodmagic.api.impl.recipe;

import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class RecipeTartaricForge {

    @Nonnull
    private final NonNullList<Ingredient> input;
    @Nonnull
    private final ItemStack output;
    @Nonnegative
    private final double minimumSouls;
    @Nonnegative
    private final double soulDrain;

    public RecipeTartaricForge(@Nonnull NonNullList<Ingredient> input, @Nonnull ItemStack output, @Nonnegative double minimumSouls, @Nonnegative double soulDrain) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(minimumSouls >= 0, "minimumSouls cannot be negative.");
        Preconditions.checkArgument(soulDrain >= 0, "soulDrain cannot be negative.");

        this.input = input;
        this.output = output;
        this.minimumSouls = minimumSouls;
        this.soulDrain = soulDrain;
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
    public final double getMinimumSouls() {
        return minimumSouls;
    }

    @Nonnegative
    public final double getSoulDrain() {
        return soulDrain;
    }
}
