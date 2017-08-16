package WayofTime.bloodmagic.routing;

import net.minecraft.util.EnumFacing;

public interface IInputFluidRoutingNode extends IFluidRoutingNode {
    boolean isFluidInput(EnumFacing side);

    IFluidFilter getInputFluidFilterForSide(EnumFacing side);
}
