package WayofTime.alchemicalWizardry.api.event;

import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import cpw.mods.fml.common.eventhandler.Cancelable;

@Cancelable
public class RitualRunEvent extends RitualEvent
{
	
	
	public RitualRunEvent(IMasterRitualStone mrs, String ownerKey, String ritualKey) 
	{
		super(mrs, ownerKey, ritualKey);
	}

}
