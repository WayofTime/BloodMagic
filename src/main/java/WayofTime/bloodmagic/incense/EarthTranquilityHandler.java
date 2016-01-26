package WayofTime.bloodmagic.incense;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.TranquilityHandler;
import WayofTime.bloodmagic.api.incense.TranquilityStack;

public class EarthTranquilityHandler extends TranquilityHandler
{
    @Override
    public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
    {
        if (block == Blocks.dirt)
        {
            return new TranquilityStack(EnumTranquilityType.EARTHEN, 0.25);
        }

        if (block instanceof BlockGrass)
        {
            return new TranquilityStack(EnumTranquilityType.EARTHEN, 0.5);
        }

        if (block == Blocks.farmland)
        {
            return new TranquilityStack(EnumTranquilityType.EARTHEN, 1);
        }

        return null;
    }
}
