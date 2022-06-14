package wayoftime.bloodmagic.common.routing;

import net.minecraft.core.Direction;

public interface IInputItemRoutingNode extends IItemRoutingNode
{
	boolean isInput(Direction side);

	IItemFilter getInputFilterForSide(Direction side);
}
