package WayofTime.alchemicalWizardry.api.event;

import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualBreakMethod;

public class RitualStopEvent extends RitualEvent
{
	public final RitualBreakMethod method;
	public RitualStopEvent(IMasterRitualStone mrs, String ownerKey, String ritualKey, RitualBreakMethod method) 
	{
		super(mrs, ownerKey, ritualKey);
		
		this.method = method;
	}
}
