package WayofTime.alchemicalWizardry.api;

import net.minecraft.item.ItemStack;

public class RoutingFocusLogic 
{
	public boolean getDefaultMatch(ItemStack keyStack, ItemStack checkedStack)
	{
		return (keyStack != null ? checkedStack != null && keyStack.getItem() == checkedStack.getItem() && (keyStack.getItem().getHasSubtypes() ? keyStack.getItemDamage() == checkedStack.getItemDamage() : true) : false);
	}
	
	public boolean doesItemMatch(boolean previous, ItemStack keyStack, ItemStack checkedStack)
	{
		return previous || this.getDefaultMatch(keyStack, checkedStack);
	}
}
