package wayoftime.bloodmagic.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import wayoftime.bloodmagic.common.item.IBindable;

@Cancelable
public class ItemBindEvent extends Event
{
	private final Player player;
	private final ItemStack itemStack;

	/**
	 * This event is called whenever a player attempts to bind a
	 * {@link IBindable} item.
	 *
	 * @param player    The player doing the binding
	 * @param itemStack The {@link ItemStack} that the player is binding
	 *                  <p>
	 *                  This event is {@link Cancelable}.<br>
	 */
	public ItemBindEvent(Player player, ItemStack itemStack)
	{
		this.player = player;
		this.itemStack = itemStack;
	}

	public Player getNewOwner()
	{
		return player;
	}

	public ItemStack getBindingStack()
	{
		return itemStack;
	}
}