package WayofTime.bloodmagic.routing;

import net.minecraft.util.Direction;

public interface IFluidRoutingNode extends IRoutingNode {
    boolean isTankConnectedToSide(Direction side);

    int getPriority(Direction side);
}
