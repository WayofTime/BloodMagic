package wayoftime.bloodmagic.tile.routing;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.item.routing.IItemFilterProvider;
import wayoftime.bloodmagic.common.routing.BasicItemFilter;
import wayoftime.bloodmagic.common.routing.IInputItemRoutingNode;
import wayoftime.bloodmagic.common.routing.IItemFilter;
import wayoftime.bloodmagic.util.Utils;

public class TileInputRoutingNode extends TileFilteredRoutingNode implements IInputItemRoutingNode
{
	@ObjectHolder("bloodmagic:inputroutingnode")
	public static TileEntityType<TileInputRoutingNode> TYPE;

	public TileInputRoutingNode(TileEntityType<?> type)
	{
		super(type, 6, "inputnode");
	}

	public TileInputRoutingNode()
	{
		this(TYPE);
	}

	@Override
	public boolean isInput(Direction side)
	{
		return true;
	}

	@Override
	public IItemFilter getInputFilterForSide(Direction side)
	{
		TileEntity tile = getWorld().getTileEntity(pos.offset(side));
		if (tile != null)
		{
			IItemHandler handler = Utils.getInventory(tile, side.getOpposite());
			if (handler != null)
			{
				ItemStack filterStack = this.getFilterStack(side);

				if (filterStack.isEmpty())
				{
					IItemFilter filter = new BasicItemFilter();
					filter.initializeFilter(null, tile, handler, false);
					return filter;
				} else if (!(filterStack.getItem() instanceof IItemFilterProvider))
				{
					return null;
				}

				IItemFilterProvider filter = (IItemFilterProvider) filterStack.getItem();
				return filter.getInputItemFilter(filterStack, tile, handler);
			}
		}

		return null;
	}

//    @Override
//    public boolean isFluidInput(Direction side) {
//        return true;
//    }
//
//    @Override
//    public IFluidFilter getInputFluidFilterForSide(Direction side) {
//        TileEntity tile = getWorld().getTileEntity(pos.offset(side));
//        if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
//            IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
//            ItemStack filterStack = this.getFilterStack(side);
//            if (filterStack == null || !(filterStack.getItem() instanceof IFluidFilterProvider)) {
//                return null;
//            }
//
//            return ((IFluidFilterProvider) filterStack.getItem()).getInputFluidFilter(filterStack, tile, handler);
//        }
//
//        return null;
//    }
//
//    @Override
//    public boolean isTankConnectedToSide(Direction side) {
//        return true;
//    }
}
