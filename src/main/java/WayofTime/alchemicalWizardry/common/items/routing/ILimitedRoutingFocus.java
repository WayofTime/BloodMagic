package WayofTime.alchemicalWizardry.common.items.routing;

import net.minecraft.item.ItemStack;

public interface ILimitedRoutingFocus 
{
	public int getRoutingFocusLimit(ItemStack stack);
	
	public void setRoutingFocusLimit(ItemStack stack, int limit);
}
