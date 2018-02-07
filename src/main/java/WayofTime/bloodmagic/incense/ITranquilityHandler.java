package WayofTime.bloodmagic.incense;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITranquilityHandler {
    TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state);
}
