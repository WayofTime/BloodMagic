package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;
import WayofTime.alchemicalWizardry.common.items.routing.OutputRoutingFocus;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicModItems;

public class OutputRoutingFocusModItems extends OutputRoutingFocus
{
	@Override
	public String getFocusDescription()
	{
		return "Only accepts items that are the same modID";
	}
	
	public RoutingFocusLogic getLogic()
	{
		return new RoutingFocusLogicModItems();
	}
}
