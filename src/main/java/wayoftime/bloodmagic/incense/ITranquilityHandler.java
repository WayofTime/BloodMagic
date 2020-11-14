package wayoftime.bloodmagic.incense;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITranquilityHandler
{
	TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, BlockState state);
}
