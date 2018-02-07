package WayofTime.bloodmagic.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

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

    @Nonnull
    IBloodMagicValueManager getValueManager();

    void registerAltarComponent(@Nonnull IBlockState state, @Nonnull String componentType);
}
