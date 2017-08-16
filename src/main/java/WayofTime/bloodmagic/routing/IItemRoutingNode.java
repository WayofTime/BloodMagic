package WayofTime.bloodmagic.routing;

import net.minecraft.util.EnumFacing;

public interface IItemRoutingNode extends IRoutingNode {
    boolean isInventoryConnectedToSide(EnumFacing side);

    int getPriority(EnumFacing side);
}
