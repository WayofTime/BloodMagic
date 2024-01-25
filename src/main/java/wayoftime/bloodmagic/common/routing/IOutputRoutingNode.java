package wayoftime.bloodmagic.common.routing;

import java.util.List;

import net.minecraft.core.Direction;

public interface IOutputRoutingNode extends IItemRoutingNode
{
	boolean isOutput(Direction side);

	List<IRoutingFilter> getOutputFilterForSide(Direction side);
}
