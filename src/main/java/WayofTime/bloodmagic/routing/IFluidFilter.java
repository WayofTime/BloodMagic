package WayofTime.bloodmagic.routing;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public interface IFluidFilter extends IRoutingFilter {
    void initializeFilter(List<ItemStack> filteredList, TileEntity tile, IFluidHandler fluidHandler, boolean isFilterOutput);

    /**
     * This method is only called when the output tank this filter is managing
     * receives an ItemStack. Should only really be called by the Input filter
     * via it's transfer method.
     *
     * @param fluidStack - The stack to filter
     * @return - The remainder of the stack after it has been absorbed into the
     * tank.
     */
    FluidStack transferStackThroughOutputFilter(FluidStack fluidStack);

    /**
     * This method is only called on an input filter to transfer FluidStacks
     * from the input tank to the output tank.
     */
    int transferThroughInputFilter(IFluidFilter outputFilter, int maxTransfer);

    boolean doesStackMatchFilter(FluidStack testStack);

    boolean doStacksMatch(FluidStack filterStack, FluidStack testStack);
}
