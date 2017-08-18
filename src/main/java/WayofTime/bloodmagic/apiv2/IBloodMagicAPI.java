package WayofTime.bloodmagic.apiv2;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface IBloodMagicAPI {

    /**
     * Retrieves the instance of the blacklisting system that Blood Magic uses.
     *
     * @return the active blacklist instance
     */
    @Nonnull
    IBloodMagicBlacklist getBlacklist();

    @Nonnull
    IBloodMagicRecipeRegistrar getRecipeRegistrar();

    void setSacrificialValue(@Nonnull ResourceLocation entityId, @Nonnegative int value);

    void registerAltarComponent(@Nonnull IBlockState state, @Nonnull String componentType);
}
