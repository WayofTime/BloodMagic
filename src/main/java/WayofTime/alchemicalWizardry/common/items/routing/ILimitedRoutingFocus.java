package WayofTime.alchemicalWizardry.common.items.routing;

import net.minecraft.item.ItemStack;

public interface ILimitedRoutingFocus 
{
	int getRoutingFocusLimit(ItemStack stack);
	
	void setRoutingFocusLimit(ItemStack stack, int limit);
}
