package WayofTime.bloodmagic.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface IBloodMagicRecipeRegistrar {

    void addBloodAltar(@Nonnull Ingredient input, @Nonnull ItemStack output, @Nonnegative int minimumTier, @Nonnegative int syphon, @Nonnegative int consumeRate, @Nonnegative int drainRate);

    boolean removeBloodAltar(@Nonnull ItemStack input);

    void addAlchemyTable(@Nonnull ItemStack output, @Nonnegative int syphon, @Nonnegative int ticks, @Nonnegative int minimumTier, @Nonnull Ingredient... input);

    boolean removeAlchemyTable(@Nonnull ItemStack... input);

    void addTartaricForge(@Nonnull ItemStack output, @Nonnegative double minimumSouls, @Nonnegative double soulDrain, @Nonnull Ingredient... input);

    boolean removeTartaricForge(@Nonnull ItemStack... input);
}
