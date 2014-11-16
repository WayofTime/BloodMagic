package WayofTime.alchemicalWizardry.api.event;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.eventhandler.Cancelable;

@Cancelable
public class PlayerDrainNetworkEvent extends SoulNetworkEvent
{
	public final EntityPlayer player; //Player that activated the event
	public PlayerDrainNetworkEvent(EntityPlayer player, String ownerNetwork, int drainAmount) 
	{
		super(ownerNetwork, drainAmount);
		this.player = player;
	}
	
	public EntityPlayer getPlayer()
	{
		return player;
	}
}
