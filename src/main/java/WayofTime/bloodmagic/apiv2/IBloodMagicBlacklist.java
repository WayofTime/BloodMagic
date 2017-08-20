package WayofTime.bloodmagic.apiv2;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IBloodMagicBlacklist {

    void addTeleposer(@Nonnull IBlockState state);

    void addTeleposer(@Nonnull Block block);

    void addTeleposer(@Nonnull ResourceLocation entityId);

    void addTransposition(@Nonnull IBlockState state);

    void addTransposition(@Nonnull Block block);

    void addGreenGrove(@Nonnull IBlockState state);

    void addGreenGrove(@Nonnull Block block);

    void addWellOfSuffering(@Nonnull ResourceLocation entityId);
}
