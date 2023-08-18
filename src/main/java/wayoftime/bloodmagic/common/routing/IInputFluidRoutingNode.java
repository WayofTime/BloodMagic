package wayoftime.bloodmagic.common.routing;

import net.minecraft.core.Direction;

public interface IInputFluidRoutingNode extends IFluidRoutingNode {
	boolean isFluidInput(Direction side);

	IFluidFilter getInputFluidFilterForSide(Direction side);
}
