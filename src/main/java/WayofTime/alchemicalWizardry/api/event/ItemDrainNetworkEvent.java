package WayofTime.alchemicalWizardry.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Cancelable;

@Cancelable
public class ItemDrainNetworkEvent extends PlayerDrainNetworkEvent
{
	public final ItemStack itemStack;
	public boolean shouldDamage; //If true, will damage regardless of if the network had enough inside it
	public float damageAmount; //Amount of damage that would incur if the network could not drain properly
	
	/**
	 * Set result to deny the action i.e. damage/drain anyways. Cancelling event prevents action without penalties
	 * 
	 * @param player		Player using the item
	 * @param ownerNetwork	Network that the item is tied to
	 * @param itemStack		Item used
	 * @param drainAmount	Original drain amount - change to alter cost
	 */
	public ItemDrainNetworkEvent(EntityPlayer player, String ownerNetwork, ItemStack itemStack, int drainAmount) 
	{
		super(player, ownerNetwork, drainAmount);
		this.itemStack = itemStack;
		this.shouldDamage = false;
		this.damageAmount = (float)(drainAmount) / 100.0f;
	}
}