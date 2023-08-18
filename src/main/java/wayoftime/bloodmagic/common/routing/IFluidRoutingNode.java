package wayoftime.bloodmagic.common.routing;

import net.minecraft.core.Direction;

public interface IFluidRoutingNode extends IRoutingNode {
	boolean isTankConnectedToSide(Direction side);

	int getPriority(Direction side);
}