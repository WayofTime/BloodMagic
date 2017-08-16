package WayofTime.bloodmagic.apiv2;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public interface IBloodMagicAPI {

    /**
     * Retrieves the instance of the blacklisting system that Blood Magic uses.
     *
     * @return the active blacklist instance
     */
    IBloodMagicBlacklist getBlacklist();

    void setSacrificialValue(ResourceLocation entityId, int value);

    void registerAltarComponent(IBlockState state, String componentType);
}
