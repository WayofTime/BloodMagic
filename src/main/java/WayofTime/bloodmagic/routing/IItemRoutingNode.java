package WayofTime.bloodmagic.routing;

import net.minecraft.util.EnumFacing;

public interface IItemRoutingNode extends IRoutingNode
{
    IItemFilter generateFilterForSide(EnumFacing side); //Will later return an IItemFilter once fully implemented.

    boolean isInventoryConnectedToSide(EnumFacing side);
}
