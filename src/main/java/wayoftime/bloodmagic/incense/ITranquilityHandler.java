package wayoftime.bloodmagic.incense;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * This is a functional interface to return the TranquilityStack of a certain Block type
 */
public interface ITranquilityHandler
{
	TranquilityStack getTranquilityOfBlock(Level world, BlockPos pos, Block block, BlockState state);
}
