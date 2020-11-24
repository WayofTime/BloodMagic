package wayoftime.bloodmagic.api.compat;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Any Block that implements this will be considered to be a valid path block for the Incense Altar
 */
public interface IIncensePath
{
	/**
	 * Goes from 0 to however far this path block can be from the altar while still
	 * functioning. 0 represents a block that can work when it is two blocks
	 * horizontally away from the altar.
	 */
	int getLevelOfPath(World world, BlockPos pos, BlockState state);
}
