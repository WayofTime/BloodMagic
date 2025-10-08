package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;
import wayoftime.bloodmagic.util.Utils;

import java.util.List;

public class ItemStandardFilter extends ItemCompositeFilter
{
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide)
		{
			if (player instanceof ServerPlayer)
			{
				NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeItemStack(stack, false));
			}
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack filterStack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.basicfilter.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
		}

		List<ItemStack> nestedFilters = getNestedFilters(filterStack);
		if (nestedFilters.size() > 0)
		{
			boolean sneaking = Screen.hasShiftDown();
			if (!sneaking)
			{
				tooltip.add(Component.translatable("tooltip.bloodmagic.extraInfo").withStyle(ChatFormatting.BLUE));
			} else
			{
				tooltip.add(Component.translatable("tooltip.bloodmagic.contained_filters").withStyle(ChatFormatting.BLUE));
				for (ItemStack nestedStack : nestedFilters)
				{
					tooltip.add(nestedStack.getHoverName());
				}
			}
		}

		int whitelistState = this.getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
		boolean isWhitelist = whitelistState == 0;

		if (isWhitelist)
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.filter.whitelist").withStyle(ChatFormatting.GRAY));
		} else
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.filter.blacklist").withStyle(ChatFormatting.GRAY));
		}

		ItemInventory inv = new InventoryFilter(filterStack);
		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (isWhitelist)
			{
				int amount = GhostItemHelper.getItemGhostAmount(stack);
				if (amount > 0)
				{
					tooltip.add(Component.translatable("tooltip.bloodmagic.filter.count", amount, stack.getHoverName()));
				} else
				{
					tooltip.add(Component.translatable("tooltip.bloodmagic.filter.all", stack.getHoverName()));
				}
			} else
			{
				tooltip.add(stack.getHoverName());
			}
		}
	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount)
	{
		return new BasicFilterKey(ghostStack, amount);
	}
}
