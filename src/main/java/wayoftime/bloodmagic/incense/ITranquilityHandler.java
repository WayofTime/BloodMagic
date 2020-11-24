package wayoftime.bloodmagic.incense;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is a functional interface to return the TranquilityStack of a certain Block type
 */
public interface ITranquilityHandler
{
	TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, BlockState state);
}
