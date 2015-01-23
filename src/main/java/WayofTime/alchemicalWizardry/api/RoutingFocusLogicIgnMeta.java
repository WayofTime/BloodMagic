package WayofTime.alchemicalWizardry.api;

import net.minecraft.item.ItemStack;

public class RoutingFocusLogicIgnMeta extends RoutingFocusLogic
{
	@Override
	public boolean doesItemMatch(boolean previous, ItemStack keyStack, ItemStack checkedStack)
	{
		return previous || (keyStack != null ? checkedStack != null && keyStack.getItem() == checkedStack.getItem() : false);
	}
}
