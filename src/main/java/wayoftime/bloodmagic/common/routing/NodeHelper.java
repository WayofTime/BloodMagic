package wayoftime.bloodmagic.common.routing;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class NodeHelper
{
	public static boolean isNodeConnectionEnabled(Level world, IRoutingNode node, BlockPos testPos)
	{
		if (!node.isConnectionEnabled(testPos))
		{
			return false;
		}
		BlockEntity tile = world.getBlockEntity(testPos);
		if (!(tile instanceof IRoutingNode))
		{
			return false;
		}

		IRoutingNode testNode = (IRoutingNode) tile;

		return testNode.isConnectionEnabled(node.getCurrentBlockPos());
	}
}