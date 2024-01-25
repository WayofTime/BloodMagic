package wayoftime.bloodmagic.common.tile.routing;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.common.container.tile.ContainerItemRoutingNode;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.common.routing.IInputRoutingNode;
import wayoftime.bloodmagic.common.routing.IRoutingFilter;
import wayoftime.bloodmagic.common.tile.BloodMagicTileEntities;
import wayoftime.bloodmagic.util.Utils;

public class TileInputRoutingNode extends TileFilteredRoutingNode implements IInputRoutingNode, MenuProvider
{
	public TileInputRoutingNode(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, 6, "inputnode", pos, state);
	}

	public TileInputRoutingNode(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.INPUT_ROUTING_NODE_TYPE.get(), pos, state);
	}

	@Override
	public boolean isInput(Direction side)
	{
		return true;
	}

	@Override
	public List<IRoutingFilter> getInputFilterForSide(Direction side)
	{
		BlockEntity tile = getLevel().getBlockEntity(worldPosition.relative(side));
		if (tile != null)
		{
			IItemHandler handler = Utils.getInventory(tile, side.getOpposite());
			IFluidHandler fluidHandler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).resolve().orElse(null);
			if (handler != null || fluidHandler != null)
			{
				ItemStack filterStack = this.getFilterStack(side);

				if (filterStack.isEmpty() || !(filterStack.getItem() instanceof IRoutingFilterProvider))
				{
					return null;
				}

				IRoutingFilterProvider filter = (IRoutingFilterProvider) filterStack.getItem();
				return filter.getInputFilter(filterStack, tile, side);
			}
		}

		return null;
	}

	@Override
	public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_)
	{
		assert level != null;
		return new ContainerItemRoutingNode(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public Component getDisplayName()
	{
		return new TextComponent("Input Routing Node");
	}

	// @Override
	// public boolean isFluidInput(Direction side) {
	// return true;
	// }
	//
	// @Override
	// public IFluidFilter getInputFluidFilterForSide(Direction side) {
	// TileEntity tile = getWorld().getTileEntity(pos.offset(side));
	// if (tile != null &&
	// tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
	// IFluidHandler handler =
	// tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
	// ItemStack filterStack = this.getFilterStack(side);
	// if (filterStack == null || !(filterStack.getItem() instanceof
	// IFluidFilterProvider)) {
	// return null;
	// }
	//
	// return ((IFluidFilterProvider)
	// filterStack.getItem()).getInputFluidFilter(filterStack, tile, handler);
	// }
	//
	// return null;
	// }
	//
	// @Override
	// public boolean isTankConnectedToSide(Direction side) {
	// return true;
	// }
}
