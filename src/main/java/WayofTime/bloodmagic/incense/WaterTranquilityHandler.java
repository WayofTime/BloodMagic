package WayofTime.bloodmagic.incense;

import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.TranquilityHandler;
import WayofTime.bloodmagic.api.incense.TranquilityStack;
import WayofTime.bloodmagic.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WaterTranquilityHandler extends TranquilityHandler
{
    @Override
    public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
    {
        if (block == Blocks.water || block == Blocks.flowing_water)
        {
            return new TranquilityStack(EnumTranquilityType.WATER, 1);
        }

        if (block == ModBlocks.lifeEssence)
        {
            return new TranquilityStack(EnumTranquilityType.WATER, 1.5);
        }

        return null;
    }
}
