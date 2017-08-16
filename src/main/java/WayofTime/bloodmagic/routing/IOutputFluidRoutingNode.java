package WayofTime.bloodmagic.routing;

import net.minecraft.util.EnumFacing;

public interface IOutputFluidRoutingNode extends IFluidRoutingNode {
    boolean isFluidOutput(EnumFacing side);

    IFluidFilter getOutputFluidFilterForSide(EnumFacing side);
}
