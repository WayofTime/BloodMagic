package WayofTime.alchemicalWizardry.common.routing;

import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.api.ILimitingLogic;
import WayofTime.alchemicalWizardry.common.items.routing.ILimitedRoutingFocus;

public class RoutingFocusLogicLimitMatchNBT extends RoutingFocusLogicMatchNBT implements ILimitingLogic
{
	public int limit = 0;
	
	public RoutingFocusLogicLimitMatchNBT(ItemStack stack)
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
}