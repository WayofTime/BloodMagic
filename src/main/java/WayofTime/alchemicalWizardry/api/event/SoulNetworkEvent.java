package WayofTime.alchemicalWizardry.api.event;

import cpw.mods.fml.common.eventhandler.Event;

public class SoulNetworkEvent extends Event
{
	public String ownerNetwork;
	public int drainAmount;
	
	public SoulNetworkEvent(String ownerNetwork, int drainAmount) 
	{
		super();
		this.ownerNetwork = ownerNetwork;
		this.drainAmount = drainAmount;
	}
	
	public String getOwnerNetwork()
	{
		return this.ownerNetwork;
	}
	
	public int getDrainAmount()
	{
		return this.drainAmount;
	}
}
