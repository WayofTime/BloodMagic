package WayofTime.alchemicalWizardry.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;

public class RitualEvent extends Event
{
	public final IMasterRitualStone mrs;
	public String ownerKey;
	public final String ritualKey;
	
	public RitualEvent(IMasterRitualStone mrs, String ownerKey, String ritualKey)
	{
		this.mrs = mrs;
		this.ownerKey = ownerKey;
		this.ritualKey = ritualKey;
	}
}
