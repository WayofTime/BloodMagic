package WayofTime.bloodmagic.incense;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.TranquilityHandler;
import WayofTime.bloodmagic.api.incense.TranquilityStack;

public class FireTranquilityHandler extends TranquilityHandler
{
    @Override
    public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
    {
        if (block instanceof BlockFire)
        {
            return new TranquilityStack(EnumTranquilityType.FIRE, 1);
        }

        if (block == Blocks.netherrack)
        {
            return new TranquilityStack(EnumTranquilityType.FIRE, 0.5);
        }

        return null;
    }
}
