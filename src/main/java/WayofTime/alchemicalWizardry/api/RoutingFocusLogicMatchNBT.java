package WayofTime.alchemicalWizardry.api;

import net.minecraft.item.ItemStack;

public class RoutingFocusLogicMatchNBT extends RoutingFocusLogic
{
	public boolean doesItemMatch(boolean previous, ItemStack keyStack, ItemStack checkedStack)
	{
		return previous && (keyStack != null ? checkedStack != null && keyStack.getItem() == checkedStack.getItem() && (keyStack.getItem().getHasSubtypes() ? keyStack.getItemDamage() == checkedStack.getItemDamage() : true) && keyStack.areItemStackTagsEqual(keyStack, checkedStack) : false);
	}
}
