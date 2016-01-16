package WayofTime.bloodmagic.tile.routing;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import WayofTime.bloodmagic.item.routing.IItemFilterProvider;
import WayofTime.bloodmagic.routing.IInputItemRoutingNode;
import WayofTime.bloodmagic.routing.IItemFilter;

public class TileInputRoutingNode extends TileFilteredRoutingNode implements IInputItemRoutingNode
{
    public TileInputRoutingNode()
    {
        super(7, "inputNode");
    }

    @Override
    public boolean isInput(EnumFacing side)
    {
        return true;
    }

    @Override
    public IItemFilter getInputFilterForSide(EnumFacing side)
    {
        ItemStack filterStack = this.getFilterStack(side);

        if (filterStack == null || !(filterStack.getItem() instanceof IItemFilterProvider))
        {
            return null;
        }

        IItemFilterProvider filter = (IItemFilterProvider) filterStack.getItem();

        TileEntity tile = worldObj.getTileEntity(pos.offset(side));
        if (tile instanceof IInventory)
        {
            return filter.getInputItemFilter(filterStack, (IInventory) tile, side.getOpposite());
        }

        return null;
    }
}
