package WayofTime.bloodmagic.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Allows blacklisting of various objects from different Blood Magic systems.
 */
public interface IBloodMagicBlacklist {

    /**
     * Blacklists a given {@link IBlockState} from being teleposed.
     *
     * @param state The {@link IBlockState} to blacklist.
     */
    void addTeleposer(@Nonnull IBlockState state);

    /**
     * Blacklists a {@link net.minecraft.entity.Entity} from being teleposed based on the given registry name.
     *
     * @param entityId The registry name to blacklist.
     */
    void addTeleposer(@Nonnull ResourceLocation entityId);

    /**
     * Blacklists a given {@link IBlockState} from being transposed.
     *
     * @param state The {@link IBlockState} to blacklist.
     */
    void addTransposition(@Nonnull IBlockState state);

    /**
     * Blacklists a given {@link IBlockState} from being accelerated by the growth enhancement ritual and sigil.
     *
     * @param state The {@link IBlockState} to blacklist.
     */
    void addGreenGrove(@Nonnull IBlockState state);

    /**
     * Blacklists a {@link net.minecraft.entity.Entity} from being sacrificed via the Well of Suffering ritual.
     *
     * @param entityId The registry name to blacklist.
     */
    void addWellOfSuffering(@Nonnull ResourceLocation entityId);
}
