package WayofTime.alchemicalWizardry.api.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;

@Cancelable
public class RitualRunEvent extends RitualEvent
{
	public RitualRunEvent(IMasterRitualStone mrs, String ownerKey, String ritualKey) 
	{
		super(mrs, ownerKey, ritualKey);
	}
}
