package WayofTime.alchemicalWizardry.common.routing;

import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;
import net.minecraft.item.ItemStack;

public class RoutingFocusLogicIgnMeta extends RoutingFocusLogic
{
	@Override
	public boolean getDefaultMatch(ItemStack keyStack, ItemStack checkedStack)
	{
		return (keyStack != null ? checkedStack != null && keyStack.getItem() == checkedStack.getItem() : false);
	}
}
