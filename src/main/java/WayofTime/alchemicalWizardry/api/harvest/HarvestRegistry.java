package WayofTime.alchemicalWizardry.api.harvest;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class HarvestRegistry
{
    public static List<IHarvestHandler> handlerList = new ArrayList();

    public static void registerHarvestHandler(IHarvestHandler handler)
    {
        handlerList.add(handler);
    }

    public static boolean harvestBlock(World world, int xCoord, int yCoord, int zCoord)
    {
        Block block = world.getBlock(xCoord, yCoord, zCoord);
        int meta = world.getBlockMetadata(xCoord, yCoord, zCoord);

        for (IHarvestHandler handler : handlerList)
        {
            if (handler.harvestAndPlant(world, xCoord, yCoord, zCoord, block, meta))
            {
                return true;
            }
        }

        return false;
    }
}
