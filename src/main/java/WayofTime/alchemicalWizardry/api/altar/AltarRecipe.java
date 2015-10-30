package WayofTime.alchemicalWizardry.api.altar;

import lombok.Getter;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

@Getter
public class AltarRecipe {

    public final int minTier, syphon, consumeRate, drainRate;
    public final boolean useTag;
    public final ItemStack input, output;

    /**
     * Allows creation of a recipe for the {@link WayofTime.alchemicalWizardry.block.BlockAltar} / {@link WayofTime.alchemicalWizardry.tile.TileAltar}.
     * The output ItemStack is allowed to be null as some recipes do not contain an output. (Blood Orbs)
     *
     * @param input       - The input ItemStack
     * @param output      - The ItemStack obtained from the recipe
     * @param minTier     - The minimum tier of Altar required
     * @param syphon      - The amount of LP to syphon from the Altar
     * @param consumeRate - The rate at which LP is consumed during crafting
     * @param drainRate   - The rate at which LP is drained during crafting
     * @param useTag      -
     */
    public AltarRecipe(ItemStack input, @Nullable ItemStack output, int minTier, int syphon, int consumeRate, int drainRate, boolean useTag) {
        this.input = input;
        this.output = output;
        this.minTier = minTier;
        this.syphon = syphon;
        this.consumeRate = consumeRate;
        this.drainRate = drainRate;
        this.useTag = useTag;
    }

    public AltarRecipe(ItemStack input, ItemStack output, int minTier, int syphon, int consumeRate, int drainRate) {
        this(input, output, minTier, syphon, consumeRate, drainRate, false);
    }

    public AltarRecipe (ItemStack input, int minTier, int consumeRate, int drainRate) {
        this(input, null, minTier, 0, consumeRate, drainRate);
    }
}
