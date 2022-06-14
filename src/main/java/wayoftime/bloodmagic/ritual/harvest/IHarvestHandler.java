package wayoftime.bloodmagic.ritual.harvest;

import java.util.List;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Used to define a HarvestHandler for the Harvest Ritual.
 */
public interface IHarvestHandler
{

	/**
	 * Called whenever the Harvest Ritual attempts to harvest a block. <br>
	 * Use this to break the block and plant a new one. <br>
	 * Add the items to be dropped to the drops list. <br>
	 *
	 * @param world - The world
	 * @param pos   - The position of the {@link BlockState} being checked
	 * @param state - The {@link BlockState} being checked
	 * @param drops - The items to be dropped
	 * @return If the block was successfully harvested.
	 */
	boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops);

	/**
	 * Tests to see if the block is valid for harvest.
	 *
	 * @param world The world
	 * @param pos   The position in the world of the {@link BlockState} being
	 *              checked
	 * @param state The {@link BlockState} being checked
	 * @return if this block is valid for harvest.
	 */
	boolean test(Level world, BlockPos pos, BlockState state);
}
