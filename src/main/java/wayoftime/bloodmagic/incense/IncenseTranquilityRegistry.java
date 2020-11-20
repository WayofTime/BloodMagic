package wayoftime.bloodmagic.incense;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IncenseTranquilityRegistry
{
	public static List<ITranquilityHandler> handlerList = new ArrayList<>();

	public static void registerTranquilityHandler(ITranquilityHandler handler)
	{
		handlerList.add(handler);
	}

	public static TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, BlockState state)
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
