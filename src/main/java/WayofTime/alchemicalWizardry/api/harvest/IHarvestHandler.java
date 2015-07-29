package WayofTime.alchemicalWizardry.api.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IHarvestHandler
{
    /**
     * A handler that is used to harvest and replant the block at the specified location
     *
     * @param world
     * @param block  block at this given location
     * @return true if successfully harvested, false if not
     */
    boolean harvestAndPlant(World world, BlockPos pos, Block block, IBlockState state);
}
