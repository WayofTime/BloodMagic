package WayofTime.bloodmagic.routing;

import net.minecraft.util.EnumFacing;

public interface IInputItemRoutingNode extends IItemRoutingNode {
    boolean isInput(EnumFacing side);

    IItemFilter getInputFilterForSide(EnumFacing side);
}
