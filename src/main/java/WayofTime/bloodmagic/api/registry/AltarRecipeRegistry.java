package WayofTime.bloodmagic.api.registry;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;
import java.util.List;

public class AltarRecipeRegistry
{
    private static BiMap<List<ItemStackWrapper>, AltarRecipe> recipes = HashBiMap.create();

    /**
     * Registers an {@link AltarRecipe} for the Blood Altar. This can be a
     * {@code ItemStack}, {@code List<Itemstack>}, or {@code String}
     * OreDictionary entry.
     * 
     * If the OreDictionary entry does not exist or is empty, it will not be
     * registered.
     * 
     * @param altarRecipe
     *        - The AltarRecipe to register
     */
    public static void registerRecipe(AltarRecipe altarRecipe)
    {
        if (!recipes.containsValue(altarRecipe) && altarRecipe.getInput().size() > 0)
            recipes.put(altarRecipe.getInput(), altarRecipe);
        else
            BloodMagicAPI.getLogger().error("Error adding altar recipe for input [{}].", altarRecipe.toString());
    }

    public static void registerFillRecipe(ItemStack orbStack, EnumAltarTier tier, int maxForOrb, int consumeRate, int drainRate)
    {
        registerRecipe(new AltarRecipe(orbStack, orbStack, tier, maxForOrb, consumeRate, drainRate, true));
    }

    /**
     * Gets the recipe that the provided input is registered to.
     * 
     * @param input
     *        - The input ItemStack to get the recipe for
     * @return - The recipe that the provided input is registered to.
     */
    public static AltarRecipe getRecipeForInput(List<ItemStack> input)
    {
        List<ItemStackWrapper> wrapperList = ItemStackWrapper.toWrapperList(input);
        if (recipes.keySet().contains(wrapperList))
            return recipes.get(wrapperList);

        return null;
    }

    public static AltarRecipe getRecipeForInput(ItemStack input)
    {
        return getRecipeForInput(Collections.singletonList(input));
    }

    public static AltarRecipe getRecipeForInput(String input)
    {
        return getRecipeForInput(OreDictionary.getOres(input));
    }

    public static BiMap<List<ItemStackWrapper>, AltarRecipe> getRecipes()
    {
        return HashBiMap.create(recipes);
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class AltarRecipe
    {
        private final List<ItemStackWrapper> input;
        private final ItemStack output;
        private final EnumAltarTier minTier;
        private final int syphon, consumeRate, drainRate;
        private final boolean fillable;

        /**
         * Allows creation of a recipe for the
         * {@link WayofTime.bloodmagic.block.BlockAltar} /
         * {@link WayofTime.bloodmagic.tile.TileAltar}. The output ItemStack is
         * allowed to be null as some recipes do not contain an output. (Blood
         * Orbs)
         * 
         * @param input
         *        - The input ItemStack
         * @param output
         *        - The ItemStack obtained from the recipe
         * @param minTier
         *        - The minimum tier of Altar required
         * @param syphon
         *        - The amount of LP to syphon from the Altar
         * @param consumeRate
         *        - The rate at which LP is consumed during crafting
         * @param drainRate
         *        - The rate at which LP is drained during crafting
         * @param fillable
         *        - Whether the input item can be filled with LP. IE: Orbs
         */
        public AltarRecipe(List<ItemStack> input, ItemStack output, EnumAltarTier minTier, int syphon, int consumeRate, int drainRate, boolean fillable)
        {
            this.input = ItemStackWrapper.toWrapperList(input);
            this.output = output;
            this.minTier = minTier;
            this.syphon = syphon < 0 ? -syphon : syphon;
            this.consumeRate = consumeRate < 0 ? -consumeRate : consumeRate;
            this.drainRate = drainRate < 0 ? -drainRate : drainRate;
            this.fillable = fillable;
        }

        public AltarRecipe(List<ItemStack> input, ItemStack output, EnumAltarTier minTier, int syphon, int consumeRate, int drainRate)
        {
            this(input, output, minTier, syphon, consumeRate, drainRate, false);
        }

        public AltarRecipe(ItemStack input, ItemStack output, EnumAltarTier minTier, int syphon, int consumeRate, int drainRate, boolean fillable)
        {
            this(Collections.singletonList(input), output, minTier, syphon, consumeRate, drainRate, fillable);
        }

        public AltarRecipe(ItemStack input, ItemStack output, EnumAltarTier minTier, int syphon, int consumeRate, int drainRate)
        {
            this(Collections.singletonList(input), output, minTier, syphon, consumeRate, drainRate, false);
        }

        public AltarRecipe(String inputEntry, ItemStack output, EnumAltarTier minTier, int syphon, int consumeRate, int drainRate, boolean fillable)
        {
            this(OreDictionary.doesOreNameExist(inputEntry) && OreDictionary.getOres(inputEntry).size() > 0 ? OreDictionary.getOres(inputEntry) : Collections.<ItemStack>emptyList(), output, minTier, syphon, consumeRate, drainRate, fillable);
        }

        public AltarRecipe(String inputEntry, ItemStack output, EnumAltarTier minTier, int syphon, int consumeRate, int drainRate)
        {
            this(OreDictionary.doesOreNameExist(inputEntry) && OreDictionary.getOres(inputEntry).size() > 0 ? OreDictionary.getOres(inputEntry) : Collections.<ItemStack>emptyList(), output, minTier, syphon, consumeRate, drainRate, false);
        }

        public boolean doesRequiredItemMatch(ItemStack comparedStack, EnumAltarTier tierCheck)
        {
            if (comparedStack == null || this.input == null)
                return false;

            if (tierCheck.ordinal() < minTier.ordinal())
                return false;

            for (ItemStackWrapper stack : input)
                if (comparedStack.isItemEqual(stack.toStack()))
                    return true;

            return false;
        }
    }
}