package WayofTime.bloodmagic.routing;

import net.minecraft.util.EnumFacing;

public interface IOutputItemRoutingNode extends IItemRoutingNode
{
    public boolean isOutput(EnumFacing side);

    public IItemFilter getOutputFilterForSide(EnumFacing side);
}
