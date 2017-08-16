package WayofTime.bloodmagic.routing;

import net.minecraft.util.EnumFacing;

public interface IFluidRoutingNode extends IRoutingNode {
    boolean isTankConnectedToSide(EnumFacing side);

    int getPriority(EnumFacing side);
}
