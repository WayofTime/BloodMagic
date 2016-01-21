package WayofTime.bloodmagic.tile.routing;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import WayofTime.bloodmagic.item.routing.IItemFilterProvider;
import WayofTime.bloodmagic.routing.DefaultItemFilter;
import WayofTime.bloodmagic.routing.IItemFilter;
import WayofTime.bloodmagic.routing.IOutputItemRoutingNode;

public class TileOutputRoutingNode extends TileFilteredRoutingNode implements IOutputItemRoutingNode
{
    public TileOutputRoutingNode()
    {
        super(7, "outputNode");
    }

    @Override
    public boolean isOutput(EnumFacing side)
    {
        return true;
    }

    @Override
    public IItemFilter getOutputFilterForSide(EnumFacing side)
    {
        TileEntity tile = worldObj.getTileEntity(pos.offset(side));
        if (tile instanceof IInventory)
        {
            ItemStack filterStack = this.getFilterStack(side);

            if (filterStack == null || !(filterStack.getItem() instanceof IItemFilterProvider))
            {
                IItemFilter filter = new DefaultItemFilter();
                filter.initializeFilter(null, (IInventory) tile, side.getOpposite(), true);
                return filter;
            }

            IItemFilterProvider filter = (IItemFilterProvider) filterStack.getItem();

            return filter.getOutputItemFilter(filterStack, (IInventory) tile, side.getOpposite());
        }

        return null;
    }
}
