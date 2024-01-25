package wayoftime.bloodmagic.common.routing;

import java.util.List;

import net.minecraft.core.Direction;

public interface IInputRoutingNode extends IItemRoutingNode
{
	boolean isInput(Direction side);

	List<IRoutingFilter> getInputFilterForSide(Direction side);
}
