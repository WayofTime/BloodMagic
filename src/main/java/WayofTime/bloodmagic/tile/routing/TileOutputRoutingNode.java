package WayofTime.bloodmagic.tile.routing;

import WayofTime.bloodmagic.item.routing.IItemFilterProvider;
import WayofTime.bloodmagic.routing.DefaultItemFilter;
import WayofTime.bloodmagic.routing.IItemFilter;
import WayofTime.bloodmagic.routing.IOutputItemRoutingNode;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

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
        if (tile != null)
        {
            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite()))
            {
                IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
                ItemStack filterStack = this.getFilterStack(side);

                if (filterStack == null || !(filterStack.getItem() instanceof IItemFilterProvider))
                {
                    IItemFilter filter = new DefaultItemFilter();
                    filter.initializeFilter(null, tile, handler, true);
                    return filter;
                }

                IItemFilterProvider filter = (IItemFilterProvider) filterStack.getItem();
                return filter.getOutputItemFilter(filterStack, tile, handler);
            }
        }

        return null;
    }
}
