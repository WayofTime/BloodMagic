package WayofTime.alchemicalWizardry.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Cancelable;

@Cancelable
public class PlayerAddToNetworkEvent extends AddToNetworkEvent
{
	public final EntityPlayer player;
	public ItemStack itemStack;
	public PlayerAddToNetworkEvent(EntityPlayer player, ItemStack itemStack, String ownerNetwork, int addedAmount, int maximum) 
	{
		super(ownerNetwork, addedAmount, maximum);
		this.player = player;
		this.itemStack = itemStack;
	}
}
