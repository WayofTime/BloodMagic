package WayofTime.alchemicalWizardry.common.routing;

import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.items.routing.ILimitedRoutingFocus;

public class RoutingFocusLogicLimitGlobal extends RoutingFocusLogicLimit
{
	public int limit = 0;
	
	public RoutingFocusLogicLimitGlobal(ItemStack stack)
	{
		if(stack != null && stack.getItem() instanceof ILimitedRoutingFocus)
		{
			limit = ((ILimitedRoutingFocus)stack.getItem()).getRoutingFocusLimit(stack);
		}else
		{
			limit = 0;
		}
	}
	
	@Override
	public int getRoutingLimit()
	{
		return limit;
	}
	
	@Override
	public boolean getDefaultMatch(ItemStack keyStack, ItemStack checkedStack)
	{
		return true;
	}
}