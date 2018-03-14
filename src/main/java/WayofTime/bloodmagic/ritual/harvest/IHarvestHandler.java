package WayofTime.bloodmagic.ritual.harvest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Used to define a HarvestHandler for the Harvest Ritual.
 */
public interface IHarvestHandler {

    /**
     * Called whenever the Harvest Ritual attempts to harvest a block. <br>
     * Use this to break the block and plant a new one. <br>
     * Add the items to be dropped to the drops list. <br>
     *
     * @param world - The world
     * @param pos   - The position of the {@link IBlockState} being checked
     * @param state - The {@link IBlockState} being checked
     * @param drops - The items to be dropped
     * @return If the block was successfully harvested.
     */
    boolean harvest(World world, BlockPos pos, IBlockState state, List<ItemStack> drops);

    /**
     * Tests to see if the block is valid for harvest.
     *
     * @param world The world
     * @param pos   The position in the world of the {@link IBlockState} being checked
     * @param state The {@link IBlockState} being checked
     * @return if this block is valid for harvest.
     */
    boolean test(World world, BlockPos pos, IBlockState state);
}
