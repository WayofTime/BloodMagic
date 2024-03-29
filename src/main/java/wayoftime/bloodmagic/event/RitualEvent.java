package wayoftime.bloodmagic.event;

import java.util.UUID;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.imperfect.IImperfectRitualStone;
import wayoftime.bloodmagic.ritual.imperfect.ImperfectRitual;

public class RitualEvent extends Event
{
	private final IMasterRitualStone mrs;
	private final UUID ownerId;
	private final Ritual ritual;

	private RitualEvent(IMasterRitualStone mrs, UUID ownerId, Ritual ritual)
	{
		this.mrs = mrs;
		this.ownerId = ownerId;
		this.ritual = ritual;
	}

	public IMasterRitualStone getRitualStone()
	{
		return mrs;
	}

	public UUID getOwnerId()
	{
		return ownerId;
	}

	public Ritual getRitual()
	{
		return ritual;
	}

	/**
	 * This event is called when a ritual is activated. If cancelled, it will not
	 * activate.
	 * <p>
	 * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#activateRitual(ItemStack, PlayerEntity, Ritual)}
	 */
	@Cancelable
	public static class RitualActivatedEvent extends RitualEvent
	{

		private final Player player;
		private final ItemStack crystalStack;
		private final int crystalTier;

		public RitualActivatedEvent(IMasterRitualStone mrs, UUID ownerId, Ritual ritual, Player player, ItemStack activationCrystal, int crystalTier)
		{
			super(mrs, ownerId, ritual);

			this.player = player;
			this.crystalStack = activationCrystal;
			this.crystalTier = crystalTier;
		}

		public Player getPlayer()
		{
			return player;
		}

		public ItemStack getCrystalStack()
		{
			return crystalStack;
		}

		public int getCrystalTier()
		{
			return crystalTier;
		}
	}

	/**
	 * This event is called when a Ritual effect is performed. If cancelled, the
	 * effect will not happen.
	 * <p>
	 * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#performRitual(World, net.minecraft.util.math.BlockPos)}
	 */
	@Cancelable
	public static class RitualRunEvent extends RitualEvent
	{

		public RitualRunEvent(IMasterRitualStone mrs, UUID ownerId, Ritual ritual)
		{
			super(mrs, ownerId, ritual);
		}
	}

	/**
	 * This event is called when a Ritual is stopped by a {@link Ritual.BreakType}.
	 * <p>
	 * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#stopRitual(Ritual.BreakType)}
	 */
	public static class RitualStopEvent extends RitualEvent
	{

		private final Ritual.BreakType method;

		public RitualStopEvent(IMasterRitualStone mrs, UUID ownerId, Ritual ritual, Ritual.BreakType method)
		{
			super(mrs, ownerId, ritual);

			this.method = method;
		}

		public Ritual.BreakType getMethod()
		{
			return method;
		}
	}

	@Cancelable
	public static class ImperfectRitualActivatedEvent extends Event
	{

		private final IImperfectRitualStone ims;
		private final Player activator;
		private final ImperfectRitual imperfectRitual;

		public ImperfectRitualActivatedEvent(IImperfectRitualStone ims, Player activator, ImperfectRitual imperfectRitual)
		{
			this.ims = ims;
			this.activator = activator;
			this.imperfectRitual = imperfectRitual;
		}

		public IImperfectRitualStone getRitualStone()
		{
			return ims;
		}

		public Player getActivator()
		{
			return activator;
		}

		public ImperfectRitual getImperfectRitual()
		{
			return imperfectRitual;
		}
	}
}