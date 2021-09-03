package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ItemModFilter extends ItemRouterFilter implements INestableItemFilterProvider
{
	public ItemModFilter()
	{
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack filterStack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.modfilter.desc").mergeStyle(TextFormatting.ITALIC).mergeStyle(TextFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
		}

		boolean sneaking = Screen.hasShiftDown();
		if (!sneaking)
		{
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.extraInfo").mergeStyle(TextFormatting.BLUE));
		} else
		{
			int whitelistState = this.getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
			boolean isWhitelist = whitelistState == 0;

			if (isWhitelist)
			{
				tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.whitelist").mergeStyle(TextFormatting.GRAY));
			} else
			{
				tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.blacklist").mergeStyle(TextFormatting.GRAY));
			}
			ItemInventory inv = new InventoryFilter(filterStack);
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if (stack.isEmpty())
				{
					continue;
				}

				TranslationTextComponent modText = new TranslationTextComponent("tooltip.bloodmagic.filter.from_mod", stack.getItem().getRegistryName().getNamespace());

				if (isWhitelist)
				{
					int amount = GhostItemHelper.getItemGhostAmount(stack);
					if (amount > 0)
					{
						tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.count", amount, modText));
					} else
					{
						tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.all", modText));
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
		String namespace = ghostStack.getItem().getRegistryName().getNamespace();

		return new ModFilterKey(namespace, amount);
	}
}
