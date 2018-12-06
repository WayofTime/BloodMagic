package WayofTime.bloodmagic.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Allows recipe addition and removal.
 */
public interface IBloodMagicRecipeRegistrar
{

    /**
     * Adds a new recipe to the Blood Altar.
     *
     * @param input       An input {@link Ingredient}.
     * @param output      An output {@link ItemStack}.
     * @param minimumTier The minimum Blood Altar tier required for this recipe.
     * @param syphon      The amount of Life Essence to syphon from the Blood Altar over the course of the craft.
     * @param consumeRate How quickly the Life Essence is syphoned.
     * @param drainRate   How quickly progress is lost if the Blood Altar runs out of Life Essence during the craft.
     */
    void addBloodAltar(@Nonnull Ingredient input, @Nonnull ItemStack output, @Nonnegative int minimumTier, @Nonnegative int syphon, @Nonnegative int consumeRate, @Nonnegative int drainRate);

    /**
     * Removes a Blood Altar recipe based on an input {@link ItemStack}.
     *
     * @param input The input item to remove the recipe of.
     * @return Whether or not a recipe was removed.
     */
    boolean removeBloodAltar(@Nonnull ItemStack input);

    /**
     * Adds a new recipe to the Alchemy Table.
     *
     * @param output      An output {@link ItemStack}.
     * @param syphon      The amount of Life Essence to syphon from the Blood Orb's bound network over the course of the craft.
     * @param ticks       The amount of ticks it takes to complete the craft.
     * @param minimumTier The minimum Blood Orb tier required for this recipe.
     * @param input       An array of {@link Ingredient}s to accept as inputs.
     */
    void addAlchemyTable(@Nonnull ItemStack output, @Nonnegative int syphon, @Nonnegative int ticks, @Nonnegative int minimumTier, @Nonnull Ingredient... input);

    /**
     * Removes an Alchemy Table recipe based on an input {@link ItemStack} array.
     *
     * @param input The input items to remove the recipe of.
     * @return Whether or not a recipe was removed.
     */
    boolean removeAlchemyTable(@Nonnull ItemStack... input);

    /**
     * Adds a new recipe to the Soul/Tartaric Forge.
     * 
     * @param output       An output {@link ItemStack}.
     * @param minimumSouls The minimum number of souls that must be contained in the Soul Gem.
     * @param soulDrain    The number of souls to drain from the Soul Gem.
     * @param input        An array of {@link Ingredient}s to accept as inputs.
     */
    void addTartaricForge(@Nonnull ItemStack output, @Nonnegative double minimumSouls, @Nonnegative double soulDrain, @Nonnull Ingredient... input);

    /**
     * Removes a Soul/Tartaric Forge recipe based on an input {@link ItemStack} array.
     *
     * @param input The input items to remove the recipe of. 
     * @return Whether or not a recipe was removed. 
     */
    boolean removeTartaricForge(@Nonnull ItemStack... input);

    /**
     * Adds a new recipe to the Alchemy Array.
     *
     * @param input         An input {@link Ingredient}. First item put into the Alchemy Array.
     * @param catalyst      A catalyst {@link Ingredient}. Second item put into the Alchemy Array.
     * @param output        An output {@link ItemStack}.
     * @param circleTexture The texture to render for the Alchemy Array circle.
     */
    void addAlchemyArray(@Nonnull Ingredient input, @Nonnull Ingredient catalyst, @Nonnull ItemStack output, @Nullable ResourceLocation circleTexture);

    /**
     * Removes an Alchemy Array recipe based on an input {@link ItemStack} and it's catalyst {@link ItemStack}.
     *
     * @param input    The input item to remove the recipe of.
     * @param catalyst The catalyst item to remove the recipe of.
     * @return Whether or not a recipe was removed.
     */
    boolean removeAlchemyArray(@Nonnull ItemStack input, @Nonnull ItemStack catalyst);

    void addSacrificeCraft(@Nonnull ItemStack output, @Nonnegative double healthRequired, @Nonnull Ingredient... input);

    boolean removeSacrificeCraft(@Nonnull ItemStack... input);
}
