package WayofTime.alchemicalWizardry.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Event;

public class ItemBindEvent extends Event
{
	public final EntityPlayer player;
	public String key;
	public ItemStack itemStack;
	
	public ItemBindEvent(EntityPlayer player, String key, ItemStack itemStack)
	{
		super();
		this.player = player;
		this.key = key;
		this.itemStack = itemStack;
	}
}
