package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import wayoftime.bloodmagic.common.routing.IFluidFilter;

public interface IFluidFilterProvider extends IRoutingFilterProvider
{
	IFluidFilter getInputFluidFilter(ItemStack stack, BlockEntity tile, IFluidHandler handler);

	IFluidFilter getOutputFluidFilter(ItemStack stack, BlockEntity tile, IFluidHandler handler);

	// Only used for filters that check ItemStacks and do not transfer items to/from
	// a connected inventory.
	IFluidFilter getUninitializedFluidFilter(ItemStack stack);

	IFilterKey getFilterKey(ItemStack filterStack, int slot, FluidStack ghostStack, int amount);
}
