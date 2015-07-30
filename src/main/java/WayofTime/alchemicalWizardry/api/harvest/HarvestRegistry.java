package WayofTime.alchemicalWizardry.api.harvest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class HarvestRegistry
{
    public static List<IHarvestHandler> handlerList = new ArrayList<IHarvestHandler>();

    public static void registerHarvestHandler(IHarvestHandler handler)
    {
        handlerList.add(handler);
    }

    public static boolean harvestBlock(World world, BlockPos pos)
    {
    	IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        for (IHarvestHandler handler : handlerList)
        {
            if (handler.harvestAndPlant(world, pos, block, state))
            {
                return true;
            }
        }

        return false;
    }
}
