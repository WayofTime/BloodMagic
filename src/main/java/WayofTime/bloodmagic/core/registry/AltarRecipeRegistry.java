package WayofTime.bloodmagic.core.registry;

import WayofTime.bloodmagic.util.BMLog;
import WayofTime.bloodmagic.util.ItemStackWrapper;
import WayofTime.bloodmagic.altar.AltarTier;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.List;

public class AltarRecipeRegistry {
    private static BiMap<List<ItemStackWrapper>, AltarRecipe> recipes = HashBiMap.create();

    /**
     * Registers an {@link AltarRecipe} for the Blood Altar. This can be a
     * {@code ItemStack}, {@code List<Itemstack>}, or {@code String}
     * OreDictionary entry.
     * <p>
     * If the OreDictionary entry does not exist or is empty, it will not be
     * registered.
     *
     * @param altarRecipe - The AltarRecipe to register
     */
    public static void registerRecipe(AltarRecipe altarRecipe) {
        if (!recipes.containsValue(altarRecipe) && altarRecipe.getInput().size() > 0)
            recipes.put(altarRecipe.getInput(), altarRecipe);
        else
            BMLog.DEFAULT.error("Error adding altar recipe for input [{}].", altarRecipe.toString());
    }

    public static void registerFillRecipe(ItemStack orbStack, AltarTier tier, int maxForOrb, int consumeRate, int drainRate) {
        registerRecipe(new AltarRecipe(orbStack, orbStack, tier, maxForOrb, consumeRate, drainRate, true));
    }

    public static void removeRecipe(AltarRecipe altarRecipe) {
        recipes.remove(altarRecipe.getInput());
    }

    /**
     * Gets the recipe that the provided input is registered to.
     *
     * @param input - The input ItemStack to get the recipe for
     * @return - The recipe that the provided input is registered to.
     */
    public static AltarRecipe getRecipeForInput(List<ItemStack> input) {
        List<ItemStackWrapper> wrapperList = ItemStackWrapper.toWrapperList(input);
        if (recipes.keySet().contains(wrapperList))
            return recipes.get(wrapperList);

        return null;
    }

    //TODO: Determine a more time-effective method
    public static AltarRecipe getRecipeForInput(ItemStack input) {
        for (AltarRecipe recipe : recipes.values()) {
            if (recipe.doesRequiredItemMatch(input, recipe.getMinTier())) {
                return recipe;
            }
        }

        return null;
    }

    public static AltarRecipe getRecipeForInput(String input) {
        return getRecipeForInput(OreDictionary.getOres(input));
    }

    public static BiMap<List<ItemStackWrapper>, AltarRecipe> getRecipes() {
        return HashBiMap.create(recipes);
    }

    public static class AltarRecipe {
        private final List<ItemStackWrapper> input;
        private final ItemStack output;
        private final AltarTier minTier;
        private final int syphon, consumeRate, drainRate;
        private final boolean fillable;

        /**
         * Allows creation of a recipe for the
         * {@link WayofTime.bloodmagic.block.BlockAltar} /
         * {@link WayofTime.bloodmagic.tile.TileAltar}. The output ItemStack is
         * allowed to be null as some recipes do not contain an output. (Blood
         * Orbs)
         *
         * @param input       - The input ItemStack
         * @param output      - The ItemStack obtained from the recipe
         * @param minTier     - The minimum tier of Altar required
         * @param syphon      - The amount of LP to syphon from the Altar
         * @param consumeRate - The rate at which LP is consumed during crafting
         * @param drainRate   - The rate at which LP is drained during crafting
         * @param fillable    - Whether the input item can be filled with LP. IE: Orbs
         */
        public AltarRecipe(List<ItemStack> input, ItemStack output, AltarTier minTier, int syphon, int consumeRate, int drainRate, boolean fillable) {
            this.input = ItemStackWrapper.toWrapperList(input);
            this.output = output;
            this.minTier = minTier;
            this.syphon = syphon < 0 ? -syphon : syphon;
            this.consumeRate = consumeRate < 0 ? -consumeRate : consumeRate;
            this.drainRate = drainRate < 0 ? -drainRate : drainRate;
            this.fillable = fillable;
        }

        public AltarRecipe(List<ItemStack> input, ItemStack output, AltarTier minTier, int syphon, int consumeRate, int drainRate) {
            this(input, output, minTier, syphon, consumeRate, drainRate, false);
        }

        public AltarRecipe(ItemStack input, ItemStack output, AltarTier minTier, int syphon, int consumeRate, int drainRate, boolean fillable) {
            this(Collections.singletonList(input), output, minTier, syphon, consumeRate, drainRate, fillable);
        }

        public AltarRecipe(ItemStack input, ItemStack output, AltarTier minTier, int syphon, int consumeRate, int drainRate) {
            this(Collections.singletonList(input), output, minTier, syphon, consumeRate, drainRate, false);
        }

        public AltarRecipe(String inputEntry, ItemStack output, AltarTier minTier, int syphon, int consumeRate, int drainRate, boolean fillable) {
            this(OreDictionary.doesOreNameExist(inputEntry) && OreDictionary.getOres(inputEntry).size() > 0 ? OreDictionary.getOres(inputEntry) : Collections.emptyList(), output, minTier, syphon, consumeRate, drainRate, fillable);
        }

        public AltarRecipe(String inputEntry, ItemStack output, AltarTier minTier, int syphon, int consumeRate, int drainRate) {
            this(OreDictionary.doesOreNameExist(inputEntry) && OreDictionary.getOres(inputEntry).size() > 0 ? OreDictionary.getOres(inputEntry) : Collections.emptyList(), output, minTier, syphon, consumeRate, drainRate, false);
        }

        public boolean doesRequiredItemMatch(ItemStack comparedStack, AltarTier tierCheck) {
            if (comparedStack == null || this.input == null)
                return false;

            if (tierCheck.ordinal() < minTier.ordinal())
                return false;

            for (ItemStackWrapper stack : input) {
                if (comparedStack.isItemEqual(stack.toStack()))
                    return true;

                if (comparedStack.getItem() == stack.item && stack.meta == OreDictionary.WILDCARD_VALUE)
                    return true;
            }

            return false;
        }

        public List<ItemStackWrapper> getInput() {
            return input;
        }

        public ItemStack getOutput() {
            return output;
        }

        public AltarTier getMinTier() {
            return minTier;
        }

        public int getSyphon() {
            return syphon;
        }

        public int getConsumeRate() {
            return consumeRate;
        }

        public int getDrainRate() {
            return drainRate;
        }

        public boolean isFillable() {
            return fillable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AltarRecipe)) return false;

            AltarRecipe that = (AltarRecipe) o;

            if (syphon != that.syphon) return false;
            if (consumeRate != that.consumeRate) return false;
            if (drainRate != that.drainRate) return false;
            if (fillable != that.fillable) return false;
            if (input != null ? !input.equals(that.input) : that.input != null) return false;
            if (output != null ? !output.equals(that.output) : that.output != null) return false;
            return minTier == that.minTier;
        }

        @Override
        public int hashCode() {
            int result = input != null ? input.hashCode() : 0;
            result = 31 * result + (output != null ? output.hashCode() : 0);
            result = 31 * result + (minTier != null ? minTier.hashCode() : 0);
            result = 31 * result + syphon;
            result = 31 * result + consumeRate;
            result = 31 * result + drainRate;
            result = 31 * result + (fillable ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("input", input)
                    .append("output", output)
                    .append("minTier", minTier)
                    .append("syphon", syphon)
                    .append("consumeRate", consumeRate)
                    .append("drainRate", drainRate)
                    .append("fillable", fillable)
                    .toString();
        }
    }
}