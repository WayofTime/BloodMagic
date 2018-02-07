package WayofTime.bloodmagic.incense;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IIncensePath {
    /**
     * Goes from 0 to however far this path block can be from the altar while
     * still functioning. 0 represents a block that can work when it is two
     * blocks horizontally away from the altar.
     */
    int getLevelOfPath(World world, BlockPos pos, IBlockState state);
}
