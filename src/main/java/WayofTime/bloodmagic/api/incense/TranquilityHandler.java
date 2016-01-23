package WayofTime.bloodmagic.api.incense;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class TranquilityHandler
{
    public abstract TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state);
}
