package wayoftime.bloodmagic.common.routing;

import net.minecraft.util.Direction;

public interface IItemRoutingNode extends IRoutingNode
{
	boolean isInventoryConnectedToSide(Direction side);

	int getPriority(Direction side);
}