package WayofTime.bloodmagic.routing;

import net.minecraft.util.Direction;

public interface IOutputItemRoutingNode extends IItemRoutingNode {
    boolean isOutput(Direction side);

    IItemFilter getOutputFilterForSide(Direction side);
}
