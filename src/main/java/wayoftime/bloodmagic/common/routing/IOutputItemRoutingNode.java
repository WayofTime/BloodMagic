package wayoftime.bloodmagic.common.routing;

import net.minecraft.core.Direction;

public interface IOutputItemRoutingNode extends IItemRoutingNode
{
	boolean isOutput(Direction side);

	IItemFilter getOutputFilterForSide(Direction side);
}
