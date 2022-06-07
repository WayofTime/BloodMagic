package wayoftime.bloodmagic.tile.routing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.item.routing.IItemFilterProvider;
import wayoftime.bloodmagic.common.routing.IItemFilter;
import wayoftime.bloodmagic.common.routing.IOutputItemRoutingNode;
import wayoftime.bloodmagic.tile.container.ContainerItemRoutingNode;
import wayoftime.bloodmagic.util.Utils;

public class TileOutputRoutingNode extends TileFilteredRoutingNode implements IOutputItemRoutingNode, INamedContainerProvider
{
	@ObjectHolder("bloodmagic:outputroutingnode")
	public static TileEntityType<TileOutputRoutingNode> TYPE;

	public TileOutputRoutingNode(TileEntityType<?> type)
	{
		super(type, 6, "outputnode");
	}

	public TileOutputRoutingNode()
	{
		this(TYPE);
	}

	@Override
	public boolean isOutput(Direction side)
	{
		return true;
	}

	@Override
	public IItemFilter getOutputFilterForSide(Direction side)
	{
		TileEntity tile = getWorld().getTileEntity(pos.offset(side));
		if (tile != null)
		{
			IItemHandler handler = Utils.getInventory(tile, side.getOpposite());
			if (handler != null)
			{
				ItemStack filterStack = this.getFilterStack(side);

				if (filterStack.isEmpty() || !(filterStack.getItem() instanceof IItemFilterProvider))
				{
					return null;
				}

				IItemFilterProvider filter = (IItemFilterProvider) filterStack.getItem();
				return filter.getOutputItemFilter(filterStack, tile, handler);
			}
		}

		return null;
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
	{
		assert world != null;
		return new ContainerItemRoutingNode(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("Output Routing Node");
	}

//	@Override
//	public boolean isFluidOutput(Direction side)
//	{
//		return true;
//	}
//
//	@Override
//	public IFluidFilter getOutputFluidFilterForSide(Direction side)
//	{
//		TileEntity tile = getWorld().getTileEntity(pos.offset(side));
//		if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side))
//		{
//			IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
//			ItemStack filterStack = this.getFilterStack(side);
//			if (filterStack == null || !(filterStack.getItem() instanceof IFluidFilterProvider))
//			{
//				return null;
//			}
//
//			return ((IFluidFilterProvider) filterStack.getItem()).getOutputFluidFilter(filterStack, tile, handler);
//		}
//
//		return null;
//	}
//
//	@Override
//	public boolean isTankConnectedToSide(Direction side)
//	{
//		return true;
//	}
}
