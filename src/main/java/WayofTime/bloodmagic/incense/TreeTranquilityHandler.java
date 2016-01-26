package WayofTime.bloodmagic.incense;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.TranquilityHandler;
import WayofTime.bloodmagic.api.incense.TranquilityStack;

public class TreeTranquilityHandler extends TranquilityHandler
{
    @Override
    public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
    {
        if (block instanceof BlockLog)
        {
            return new TranquilityStack(EnumTranquilityType.TREE, 1);
        }

        return null;
    }
}
