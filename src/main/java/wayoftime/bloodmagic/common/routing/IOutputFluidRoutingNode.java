package wayoftime.bloodmagic.common.routing;

import net.minecraft.core.Direction;

public interface IOutputFluidRoutingNode extends IFluidRoutingNode {
	boolean isFluidOutput(Direction side);

	IFluidFilter getOutputFluidFilterForSide(Direction side);
}
