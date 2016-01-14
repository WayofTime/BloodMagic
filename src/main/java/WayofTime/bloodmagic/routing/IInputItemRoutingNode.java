package WayofTime.bloodmagic.routing;

import net.minecraft.util.EnumFacing;

public interface IInputItemRoutingNode extends IItemRoutingNode
{
    public boolean isInput(EnumFacing side);

    public IItemFilter getInputFilterForSide(EnumFacing side);
}
