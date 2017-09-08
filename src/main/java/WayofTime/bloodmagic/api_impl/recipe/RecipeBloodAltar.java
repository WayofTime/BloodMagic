package WayofTime.bloodmagic.api_impl.recipe;

import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class RecipeBloodAltar {

    @Nonnull
    private final Ingredient input;
    @Nonnull
    private final ItemStack output;
    @Nonnull
    private final EnumAltarTier minimumTier;
    @Nonnegative
    private final int syphon;
    @Nonnegative
    private final int consumeRate;
    @Nonnegative
    private final int drainRate;

    public RecipeBloodAltar(@Nonnull Ingredient input, @Nonnull ItemStack output, @Nonnegative int minimumTier, @Nonnegative int syphon, @Nonnegative int consumeRate, @Nonnegative int drainRate) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkNotNull(minimumTier, "minimumTier cannot be negative.");
        Preconditions.checkArgument(minimumTier <= EnumAltarTier.MAXTIERS, "minimumTier cannot be higher than max tier");
        Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
        Preconditions.checkArgument(consumeRate >= 0, "consumeRate cannot be negative.");
        Preconditions.checkArgument(drainRate >= 0, "drain cannot be negative.");

        this.input = input;
        this.output = output;
        this.minimumTier = EnumAltarTier.VALUES[minimumTier];
        this.syphon = syphon;
        this.consumeRate = consumeRate;
        this.drainRate = drainRate;
    }

    @Nonnull
    public final Ingredient getInput() {
        return input;
    }

    @Nonnull
    public final ItemStack getOutput() {
        return output;
    }

    @Nonnull
    public EnumAltarTier getMinimumTier() {
        return minimumTier;
    }

    @Nonnegative
    public final int getSyphon() {
        return syphon;
    }

    @Nonnegative
    public final int getConsumeRate() {
        return consumeRate;
    }

    @Nonnegative
    public final int getDrainRate() {
        return drainRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeBloodAltar)) return false;

        RecipeBloodAltar that = (RecipeBloodAltar) o;

        if (minimumTier != that.minimumTier) return false;
        if (syphon != that.syphon) return false;
        if (drainRate != that.drainRate) return false;
        if (!IngredientTester.compareIngredients(input, that.input)) return false;
        return ItemStack.areItemStacksEqualUsingNBTShareTag(output, that.output);
    }

    @Override
    public int hashCode() {
        int result = input.hashCode();
        result = 31 * result + output.hashCode();
        result = 31 * result + minimumTier.ordinal();
        result = 31 * result + syphon;
        result = 31 * result + drainRate;
        return result;
    }
}
