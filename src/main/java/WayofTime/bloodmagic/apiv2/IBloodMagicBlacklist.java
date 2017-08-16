package WayofTime.bloodmagic.apiv2;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public interface IBloodMagicBlacklist {

    void addTeleposer(IBlockState state);

    void addTeleposer(Block block);

    void addTeleposer(ResourceLocation entityId);

    void addTransposition(IBlockState state);

    void addTransposition(Block block);

    void addGreenGrove(IBlockState state);

    void addGreenGrove(Block block);

    void addSacrifice(ResourceLocation entityId);
}
