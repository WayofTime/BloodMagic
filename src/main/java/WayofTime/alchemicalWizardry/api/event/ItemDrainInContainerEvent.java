package WayofTime.alchemicalWizardry.api.event;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Cancelable;

@Cancelable
public class ItemDrainInContainerEvent extends SoulNetworkEvent
{
	public ItemStack stack;
	public ItemDrainInContainerEvent(ItemStack stack, String ownerNetwork, int drainAmount) 
	{
		super(ownerNetwork, drainAmount);
		this.stack = stack;
	}
}
