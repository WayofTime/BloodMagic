package WayofTime.bloodmagic.tile.routing;

import WayofTime.bloodmagic.item.routing.IItemFilterProvider;
import WayofTime.bloodmagic.routing.DefaultItemFilter;
import WayofTime.bloodmagic.routing.IInputItemRoutingNode;
import WayofTime.bloodmagic.routing.IItemFilter;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

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
        TileEntity tile = worldObj.getTileEntity(pos.offset(side));
        if (tile != null)
        {
            IItemHandler handler = Utils.getInventory(tile, side.getOpposite());
            if (handler != null)
            {
                ItemStack filterStack = this.getFilterStack(side);

                if (filterStack == null || !(filterStack.getItem() instanceof IItemFilterProvider))
                {
                    IItemFilter filter = new DefaultItemFilter();
                    filter.initializeFilter(null, tile, handler, false);
                    return filter;
                }

                IItemFilterProvider filter = (IItemFilterProvider) filterStack.getItem();
                return filter.getInputItemFilter(filterStack, tile, handler);
            }
        }

        return null;
    }
}
