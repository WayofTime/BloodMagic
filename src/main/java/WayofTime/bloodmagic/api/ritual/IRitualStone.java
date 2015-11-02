package WayofTime.bloodmagic.api.ritual;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IRitualStone {

    boolean isRuneType(World world, BlockPos pos, int meta, EnumRuneType runeType);

    interface Tile {
        boolean isRuneType(EnumRuneType runeType);
    }
}
