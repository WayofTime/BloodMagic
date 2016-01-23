package WayofTime.bloodmagic.api.incense;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class IncenseTranquilityRegistry
{
    public static List<TranquilityHandler> handlerList = new ArrayList<TranquilityHandler>();

    public static void registerTranquilityHandler(TranquilityHandler handler)
    {
        handlerList.add(handler);
    }

    public static TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
    {
        for (TranquilityHandler handler : handlerList)
        {
            TranquilityStack tranq = handler.getTranquilityOfBlock(world, pos, block, state);
            if (tranq != null)
            {
                return tranq;
            }
        }

        return null;
    }
}
