package WayofTime.bloodmagic.tile.routing;

import WayofTime.bloodmagic.item.routing.IFluidFilterProvider;
import WayofTime.bloodmagic.item.routing.IItemFilterProvider;
import WayofTime.bloodmagic.routing.*;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class TileOutputRoutingNode extends TileFilteredRoutingNode implements IOutputItemRoutingNode, IOutputFluidRoutingNode {
    public TileOutputRoutingNode() {
        super(6, "outputNode");
    }

    @Override
    public boolean isOutput(EnumFacing side) {
        return true;
    }

    @Override
    public IItemFilter getOutputFilterForSide(EnumFacing side) {
        TileEntity tile = getWorld().getTileEntity(pos.offset(side));
        if (tile != null) {
            IItemHandler handler = Utils.getInventory(tile, side.getOpposite());
            if (handler != null) {
                ItemStack filterStack = this.getFilterStack(side);

                if (filterStack.isEmpty()) {
                    IItemFilter filter = new DefaultItemFilter();
                    filter.initializeFilter(null, tile, handler, true);
                    return filter;
                } else if (!(filterStack.getItem() instanceof IItemFilterProvider)) {
                    return null;
                }

                IItemFilterProvider filter = (IItemFilterProvider) filterStack.getItem();
                return filter.getOutputItemFilter(filterStack, tile, handler);
            }
        }

        return null;
    }

    @Override
    public boolean isFluidOutput(EnumFacing side) {
        return true;
    }

    @Override
    public IFluidFilter getOutputFluidFilterForSide(EnumFacing side) {
        TileEntity tile = getWorld().getTileEntity(pos.offset(side));
        if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
            IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            ItemStack filterStack = this.getFilterStack(side);
            if (filterStack == null || !(filterStack.getItem() instanceof IFluidFilterProvider)) {
                return null;
            }

            return ((IFluidFilterProvider) filterStack.getItem()).getOutputFluidFilter(filterStack, tile, handler);
        }

        return null;
    }

    @Override
    public boolean isTankConnectedToSide(EnumFacing side) {
        return true;
    }
}
