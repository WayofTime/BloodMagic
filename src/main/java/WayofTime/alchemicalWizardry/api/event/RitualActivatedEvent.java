package WayofTime.alchemicalWizardry.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;

@Cancelable
public class RitualActivatedEvent extends RitualEvent
{
	public final EntityPlayer player;
	public final ItemStack crystalStack;
	public int crystalTier;
	
	public RitualActivatedEvent(IMasterRitualStone mrs, String ownerKey, String ritualKey, EntityPlayer player, ItemStack activationCrystal, int crystalTier) 
	{
		super(mrs, ownerKey, ritualKey);
		
		this.player = player;
		this.crystalStack = activationCrystal;
		this.crystalTier = crystalTier;
	}
}
