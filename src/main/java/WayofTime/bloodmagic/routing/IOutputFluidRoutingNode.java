package WayofTime.bloodmagic.routing;

import net.minecraft.util.Direction;

public interface IOutputFluidRoutingNode extends IFluidRoutingNode {
    boolean isFluidOutput(Direction side);

    IFluidFilter getOutputFluidFilterForSide(Direction side);
}
