package wayoftime.bloodmagic.incense;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class IncenseTranquilityRegistry
{
	public static List<ITranquilityHandler> handlerList = new ArrayList<>();

	public static void registerTranquilityHandler(ITranquilityHandler handler)
	{
		handlerList.add(handler);
	}

	public static TranquilityStack getTranquilityOfBlock(Level world, BlockPos pos, Block block, BlockState state)
	{
		for (ITranquilityHandler handler : handlerList)
		{
			TranquilityStack tranq = handler.getTranquilityOfBlock(world, pos, block, state);
			if (tranq != null)
			{
				return tranq;
			}
		}

		return null;
	}
}
