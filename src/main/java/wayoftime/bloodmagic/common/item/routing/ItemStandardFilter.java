package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;
import wayoftime.bloodmagic.util.Utils;

public class ItemStandardFilter extends ItemCompositeFilter
{
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide)
		{
			Utils.setUUID(stack);

			if (player instanceof ServerPlayerEntity)
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, this, buf -> buf.writeItemStack(stack, false));
			}
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack filterStack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.basicfilter.desc").withStyle(TextFormatting.ITALIC).withStyle(TextFormatting.GRAY));

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
				tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.extraInfo").withStyle(TextFormatting.BLUE));
			} else
			{
				tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.contained_filters").withStyle(TextFormatting.BLUE));
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
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.whitelist").withStyle(TextFormatting.GRAY));
		} else
		{
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.blacklist").withStyle(TextFormatting.GRAY));
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
					tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.count", amount, stack.getHoverName()));
				} else
				{
					tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.all", stack.getHoverName()));
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
