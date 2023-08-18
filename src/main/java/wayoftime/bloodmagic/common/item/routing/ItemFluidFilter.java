package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;
import wayoftime.bloodmagic.util.Utils;

public class ItemFluidFilter extends ItemFluidRouterFilter
{
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide)
		{
			Utils.setUUID(stack);

			if (player instanceof ServerPlayer)
			{
				NetworkHooks.openGui((ServerPlayer) player, this, buf -> buf.writeItemStack(stack, false));
			}
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	public void appendHoverText(ItemStack filterStack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.basicfilter.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
		}
		int whitelistState = this.getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
		boolean isWhitelist = whitelistState == 0;

		if (isWhitelist)
		{
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.whitelist").withStyle(ChatFormatting.GRAY));
		} else
		{
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.blacklist").withStyle(ChatFormatting.GRAY));
		}

		ItemInventory inv = new InventoryFilter(filterStack);
		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty())
			{
				continue;
			}

			Component name = stack.getHoverName();

			LazyOptional<IFluidHandlerItem> potentialHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
			if (potentialHandler.isPresent())
			{
				IFluidHandlerItem handler = potentialHandler.resolve().get();
				FluidStack fluid = handler.getFluidInTank(0);
				name = fluid.getDisplayName();
			}

			if (isWhitelist)
			{
				int amount = GhostItemHelper.getItemGhostAmount(stack);
				if (amount > 0)
				{
					tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.count", amount, name));
				} else
				{
					tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.all", name));
				}
			} else
			{
				tooltip.add(name);
			}
		}
	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, FluidStack ghostStack, int amount)
	{
		return new FluidFilterKey(ghostStack, amount);
	}
}
