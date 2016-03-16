package WayofTime.bloodmagic.api.registry;

import javax.annotation.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class AltarRecipeRegistry
{
    private static BiMap<ItemStack, AltarRecipe> recipes = HashBiMap.create();

    public static void registerRecipe(AltarRecipe recipe)
    {
        if (!recipes.containsValue(recipe))
            recipes.put(recipe.input, recipe);
        else
            BloodMagicAPI.getLogger().error("Error adding altar recipe for %s. Recipe already exists.", recipe.input.getDisplayName(), recipe.output == null ? "" : " -> ");
    }

    public static void registerFillRecipe(ItemStack orbStack, EnumAltarTier tier, int maxForOrb, int consumeRate, int drainRate)
    {
        registerRecipe(new AltarRecipe(orbStack, orbStack, tier, maxForOrb, consumeRate, drainRate, true));
    }

    public static AltarRecipe getRecipeForInput(ItemStack input)
    {
        return recipes.get(input);
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class AltarRecipe
    {

        public final int syphon, consumeRate, drainRate;
        public final boolean fillable;
        public final ItemStack input, output;
        public final EnumAltarTier minTier;

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
        public AltarRecipe(ItemStack input, @Nullable ItemStack output, EnumAltarTier minTier, int syphon, int consumeRate, int drainRate, boolean fillable)
        {
            this.input = input;
            this.output = output;
            this.minTier = minTier;
            this.syphon = syphon < 0 ? -syphon : syphon;
            this.consumeRate = consumeRate < 0 ? -consumeRate : consumeRate;
            this.drainRate = drainRate < 0 ? -drainRate : drainRate;
            this.fillable = fillable;
        }

        public AltarRecipe(ItemStack input, ItemStack output, EnumAltarTier minTier, int syphon, int consumeRate, int drainRate)
        {
            this(input, output, minTier, syphon, consumeRate, drainRate, false);
        }

        public boolean doesRequiredItemMatch(ItemStack comparedStack, EnumAltarTier tierCheck)
        {
            if (comparedStack == null || this.input == null)
                return false;

            return tierCheck.ordinal() >= minTier.ordinal() && this.input.isItemEqual(comparedStack);// &&
            // (this.fillable this.areRequiredTagsEqual(comparedStack) : true);
        }
    }

    public static BiMap<ItemStack, AltarRecipe> getRecipes()
    {
        return HashBiMap.create(recipes);
    }
}
