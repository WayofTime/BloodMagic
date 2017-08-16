package WayofTime.bloodmagic.routing;

import net.minecraft.util.EnumFacing;

public interface IOutputItemRoutingNode extends IItemRoutingNode {
    boolean isOutput(EnumFacing side);

    IItemFilter getOutputFilterForSide(EnumFacing side);
}
