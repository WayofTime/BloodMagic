package wayoftime.bloodmagic.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class SacrificeKnifeUsedEvent extends Event
{
	public final Player player;
	public final int healthDrained;
	public int lpAdded;
	public boolean shouldDrainHealth;
	public boolean shouldFillAltar;

	/**
	 * This event is called whenever a player attempts to use a
	 * {@link WayofTime.bloodmagic.item.ItemSacrificialDagger} to self-sacrifice
	 * near an altar.
	 *
	 * @param player            The player doing the sacrificing
	 * @param shouldDrainHealth Determines whether or not health is lost
	 * @param shouldFillAltar   Determines whether or not an altar should be filled
	 * @param hp                Amount of health lost
	 * @param lpAdded           Amount of LP added to the altar
	 *                          <p>
	 *                          This event is {@link Cancelable}.<br>
	 */
	public SacrificeKnifeUsedEvent(Player player, boolean shouldDrainHealth, boolean shouldFillAltar, int hp, int lpAdded)
	{
		this.player = player;
		this.shouldDrainHealth = shouldDrainHealth;
		this.shouldFillAltar = shouldFillAltar;
		this.healthDrained = hp;
		this.lpAdded = lpAdded;
	}
}
