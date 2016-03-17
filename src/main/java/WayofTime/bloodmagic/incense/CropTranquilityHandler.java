package WayofTime.bloodmagic.incense;

import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.TranquilityHandler;
import WayofTime.bloodmagic.api.incense.TranquilityStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CropTranquilityHandler extends TranquilityHandler
{
    @Override
    public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
    {
        if (block == Blocks.potatoes || block == Blocks.carrots || block == Blocks.wheat || block == Blocks.nether_wart)
        {
            return new TranquilityStack(EnumTranquilityType.CROP, 1);
        }

        return null;
    }
}
