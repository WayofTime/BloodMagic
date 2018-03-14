package WayofTime.bloodmagic.api.impl.recipe;

import WayofTime.bloodmagic.altar.AltarTier;
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
    private final AltarTier minimumTier;
    @Nonnegative
    private final int syphon;
    @Nonnegative
    private final int consumeRate;
    @Nonnegative
    private final int drainRate;

    public RecipeBloodAltar(@Nonnull Ingredient input, @Nonnull ItemStack output, @Nonnegative int minimumTier, @Nonnegative int syphon, @Nonnegative int consumeRate, @Nonnegative int drainRate) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(output, "output cannot be null.");
        Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");
        Preconditions.checkArgument(minimumTier <= AltarTier.MAXTIERS, "minimumTier cannot be higher than max tier");
        Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
        Preconditions.checkArgument(consumeRate >= 0, "consumeRate cannot be negative.");
        Preconditions.checkArgument(drainRate >= 0, "drain cannot be negative.");

        this.input = input;
        this.output = output;
        this.minimumTier = AltarTier.values()[minimumTier];
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
    public AltarTier getMinimumTier() {
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
}
