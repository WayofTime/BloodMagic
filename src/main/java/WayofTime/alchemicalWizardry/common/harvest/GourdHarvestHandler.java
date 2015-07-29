package WayofTime.alchemicalWizardry.common.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;

public class GourdHarvestHandler implements IHarvestHandler
{
    public boolean canHandleBlock(Block block)
    {
        return block == Blocks.melon_block || block == Blocks.pumpkin;
    }

    @Override
    public boolean harvestAndPlant(World world, BlockPos pos, Block block, IBlockState state)
    {
        if (!this.canHandleBlock(block))
        {
            return false;
        }
        world.destroyBlock(pos, true);
        return true;
    }
}