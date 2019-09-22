package WayofTime.bloodmagic.routing;

import net.minecraft.util.Direction;

public interface IInputFluidRoutingNode extends IFluidRoutingNode {
    boolean isFluidInput(Direction side);

    IFluidFilter getInputFluidFilterForSide(Direction side);
}
