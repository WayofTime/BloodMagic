package WayofTime.alchemicalWizardry.common.harvest;

import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class CactusReedHarvestHandler implements IHarvestHandler
{
    public boolean canHandleBlock(Block block)
    {
        return block == Blocks.reeds || block == Blocks.cactus;
    }

    @Override
    public boolean harvestAndPlant(World world, int xCoord, int yCoord, int zCoord, Block block, int meta)
    {
        if (!this.canHandleBlock(block))
        {
            return false;
        }

        if (world.getBlock(xCoord, yCoord - 1, zCoord) != block || world.getBlock(xCoord, yCoord - 2, zCoord) != block)
        {
            return false;
        }

        world.func_147480_a(xCoord, yCoord, zCoord, true);

        return true;
    }
}