package wayoftime.bloodmagic.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;

public class LivingEquipmentEvent extends Event
{

//	public static final Event<OnExperienceGain> EXPERIENCE_GAIN = EventFactory.createArrayBacked(OnExperienceGain.class, handlers -> e -> {
//		for (OnExperienceGain gain : handlers) if (gain.gainExperience(e) == EventResult.CANCEL)
//			return EventResult.CANCEL;
//
//		return EventResult.PASS;
//	});
//	public static final Event<OnLevelUp> LEVEL_UP = EventFactory.createArrayBacked(OnLevelUp.class, handlers -> e -> {
//		for (OnLevelUp levelUp : handlers) levelUp.levelUp(e);
//	});
//
//	public interface OnExperienceGain
//	{
//		EventResult gainExperience(GainExperience event);
//	}
//
//	public interface OnLevelUp
//	{
//		void levelUp(LevelUp event);
//	}

	private final Player player;
	private final LivingStats stats;

	private LivingEquipmentEvent(Player player, LivingStats stats)
	{
		this.player = player;
		this.stats = stats;
	}

	public Player getPlayer()
	{
		return player;
	}

	public LivingStats getStats()
	{
		return stats;
	}

	public static class GainExperience extends LivingEquipmentEvent
	{

		private final LivingUpgrade upgrade;
		private double experience;

		public GainExperience(Player player, LivingStats stats, LivingUpgrade upgrade, double experience)
		{
			super(player, stats);
			this.upgrade = upgrade;
			this.experience = experience;
		}

		public LivingUpgrade getUpgrade()
		{
			return upgrade;
		}

		public double getExperience()
		{
			return experience;
		}

		public void setExperience(double experience)
		{
			this.experience = experience;
		}
	}

	public static class LevelUp extends LivingEquipmentEvent
	{

		private final LivingUpgrade upgrade;

		public LevelUp(Player player, LivingStats stats, LivingUpgrade upgrade)
		{
			super(player, stats);

			this.upgrade = upgrade;
		}

		public LivingUpgrade getUpgrade()
		{
			return upgrade;
		}
	}
}
