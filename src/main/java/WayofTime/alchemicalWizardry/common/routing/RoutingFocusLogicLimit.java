package WayofTime.alchemicalWizardry.common.routing;

import WayofTime.alchemicalWizardry.api.ILimitingLogic;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;

public class RoutingFocusLogicLimit extends RoutingFocusLogic implements ILimitingLogic
{
	public int getRoutingLimit()
	{
		return 0;
	}
}
