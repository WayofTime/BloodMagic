package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ItemFluidModFilter extends ItemFluidRouterFilter
{
	public ItemFluidModFilter()
	{
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack filterStack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.modfilter.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
		}

		boolean sneaking = Screen.hasShiftDown();
		if (!sneaking)
		{
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.extraInfo").withStyle(ChatFormatting.BLUE));
		} else
		{
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

				FluidStack fluidStack = FluidUtil.getFluidContained(stack).get();
				TranslatableComponent modText = new TranslatableComponent("tooltip.bloodmagic.filter.from_mod", fluidStack.getFluid().getRegistryName().getNamespace());

				if (isWhitelist)
				{
					int amount = GhostItemHelper.getItemGhostAmount(stack);
					if (amount > 0)
					{
						tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.count", amount, modText));
					} else
					{
						tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.all", modText));
					}

				} else
				{
					tooltip.add(modText);
				}
			}
		}
	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount)
	{
		String namespace = FluidUtil.getFluidContained(ghostStack).get().getFluid().getRegistryName().getNamespace();

		return new FluidModFilterKey(namespace, amount);
	}
}
