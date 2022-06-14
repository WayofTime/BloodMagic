package wayoftime.bloodmagic.tile.routing;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.item.routing.IItemFilterProvider;
import wayoftime.bloodmagic.common.routing.IItemFilter;
import wayoftime.bloodmagic.common.routing.IOutputItemRoutingNode;
import wayoftime.bloodmagic.tile.container.ContainerItemRoutingNode;
import wayoftime.bloodmagic.util.Utils;

public class TileOutputRoutingNode extends TileFilteredRoutingNode implements IOutputItemRoutingNode, MenuProvider
{
	@ObjectHolder("bloodmagic:outputroutingnode")
	public static BlockEntityType<TileOutputRoutingNode> TYPE;

	public TileOutputRoutingNode(BlockEntityType<?> type)
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
		BlockEntity tile = getLevel().getBlockEntity(worldPosition.relative(side));
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
	public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_)
	{
		assert level != null;
		return new ContainerItemRoutingNode(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public Component getDisplayName()
	{
		return new TextComponent("Output Routing Node");
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
