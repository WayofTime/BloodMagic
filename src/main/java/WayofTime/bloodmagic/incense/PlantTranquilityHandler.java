package WayofTime.bloodmagic.incense;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.TranquilityHandler;
import WayofTime.bloodmagic.api.incense.TranquilityStack;

public class PlantTranquilityHandler extends TranquilityHandler
{
    @Override
    public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
    {
        if (block instanceof IGrowable || block instanceof IPlantable)
        {
            return new TranquilityStack(EnumTranquilityType.PLANT, 1);
        }

        if (block instanceof BlockLog || block instanceof BlockLeaves)
        {
            return new TranquilityStack(EnumTranquilityType.PLANT, 0.5);
        }

        return null;
    }
}
