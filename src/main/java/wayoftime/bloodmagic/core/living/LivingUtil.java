package wayoftime.bloodmagic.core.living;

import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;
import wayoftime.bloodmagic.common.item.ItemLivingTrainer;
import wayoftime.bloodmagic.core.util.PlayerUtil;
import wayoftime.bloodmagic.event.LivingEquipmentEvent;

import java.util.Map;
import java.util.Map.Entry;

public class LivingUtil
{
	// @return Pair containing the LivingStats of the player, and if the LivingStats
	// upgraded due to the applied EXP.
	public static Pair<LivingStats, Boolean> applyNewExperience(Player player, LivingUpgrade upgrade, double experience)
	{
		LivingStats stats = LivingStats.fromPlayer(player, true);
		if (stats == null)
			return Pair.of(null, false);

		double curExp = stats.getUpgrades().containsKey(upgrade) ? stats.getUpgrades().get(upgrade).doubleValue() : 0;

		if (!canTrain(player, upgrade, upgrade.getLevel((int) curExp), upgrade.getLevel((int) (curExp + experience))))
			return Pair.of(stats, false);

		LivingEquipmentEvent.GainExperience event = new LivingEquipmentEvent.GainExperience(player, stats, upgrade, experience);
//		EventResult result = LivingEquipmentEvent.EXPERIENCE_GAIN.invoker().gainExperience(event);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled())
			return Pair.of(stats, false);

		experience = event.getExperience();

		double currentExperience = stats.getUpgrades().getOrDefault(upgrade, 0d);
		double requiredForLevel = upgrade.getNextRequirement((int) currentExperience) - currentExperience;

		// If we're going to level up from this, check points
//		System.out.println("Required for level: " + requiredForLevel);
		if (requiredForLevel <= experience)
		{
//			System.out.println("Attempting to level up");
			int currentPoints = stats.getUsedPoints();
			// If we're already capped or somehow over the cap, we don't want to add
			// experience
			if (currentPoints >= stats.getMaxPoints())
				return Pair.of(stats, false);

			int currentPointCost = upgrade.getLevelCost(upgrade.getLevel((int) currentExperience));
			int nextPointCost = upgrade.getLevelCost(upgrade.getLevel((int) (currentExperience + experience)));

//			System.out.println("Current point cost: " + currentPointCost + ", Next point cost: " + nextPointCost);
			// If there's no more levels in this upgrade, we don't want to add experience
			if (nextPointCost == -1)
				return Pair.of(stats, false);

			int pointDif = nextPointCost - currentPointCost;
			if (pointDif < 0 && !upgrade.isNegative())
			{
				return Pair.of(stats, false);
			}

			// If applying this new level will go over our cap, we don't want to add
			// experience
			if (currentPoints + pointDif > stats.getMaxPoints())
				return Pair.of(stats, false);
		}

		int newLevel = upgrade.getLevel((int) (currentExperience + experience));
		boolean didUpgrade = false;
		if (upgrade.getLevel((int) currentExperience) != newLevel)
		{
			LivingEquipmentEvent.LevelUp levelUpEvent = new LivingEquipmentEvent.LevelUp(player, stats, upgrade);
//			LivingEquipmentEvent.LEVEL_UP.invoker().levelUp(levelUpEvent);
			MinecraftForge.EVENT_BUS.post(levelUpEvent);
			didUpgrade = true;

			player.displayClientMessage(Component.translatable("chat.bloodmagic.living_upgrade_level_increase", Component.translatable(upgrade.getTranslationKey()), newLevel), true);
		}

//		System.out.println("Adding experience! Total experience is: " + currentExperience);

		stats.addExperience(upgrade.getKey(), experience);
		LivingStats.toPlayer(player, stats);
		return Pair.of(stats, didUpgrade);
	}

	// @return Pair containing the LivingStats of the player. Returned Double is the
	// effective amount of exp that was applied.
	public static Pair<LivingStats, Double> applyExperienceToUpgradeCap(Player player, LivingUpgrade upgrade, double experience)
	{
//		System.out.println("Initial exp added: " + experience);
		LivingStats stats = LivingStats.fromPlayer(player, true);
		if (stats == null)
			return Pair.of(null, 0d);

		double curExp = stats.getUpgrades().containsKey(upgrade) ? stats.getUpgrades().get(upgrade).doubleValue() : 0;

		if (!canTrain(player, upgrade, upgrade.getLevel((int) curExp), upgrade.getLevel((int) (curExp + experience))))
			return Pair.of(stats, 0d);

		LivingEquipmentEvent.GainExperience event = new LivingEquipmentEvent.GainExperience(player, stats, upgrade, experience);
//			EventResult result = LivingEquipmentEvent.EXPERIENCE_GAIN.invoker().gainExperience(event);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled())
			return Pair.of(stats, 0d);

		double multiplicationFactor = event.getExperience() / experience; // Multiplier from the GainExperience event.
																			// Used to keep track of the upgrade tome's
																			// exp syphoned.
		experience = event.getExperience();
		if (experience <= 0)
		{
			return Pair.of(stats, 0d);
		}

		double currentExperience = stats.getUpgrades().getOrDefault(upgrade, 0d);
		int currentLevel = upgrade.getLevel((int) currentExperience);
		double requiredForLevel = upgrade.getNextRequirement((int) currentExperience) - currentExperience;
		int potentialLevel = upgrade.getLevel((int) (currentExperience + experience));

		// If we're going to level up from this, check points
		if (requiredForLevel <= experience)
		{
			int currentPoints = stats.getUsedPoints();

			// If we're already capped or somehow over the cap, we don't want to add
			// experience
			if (currentPoints >= stats.getMaxPoints())
				return Pair.of(stats, 0d);

			int currentPointCost = upgrade.getLevelCost(currentLevel);

			// Do not want to add exp past a point cap. Check points at each level until the
			// exp can fit.
			while (potentialLevel > currentLevel)
			{
				int nextPointCost = upgrade.getLevelCost(potentialLevel);
				// If there's no more levels in this upgrade, we don't want to add experience
				if (nextPointCost == -1)
					return Pair.of(stats, 0d);

				int pointDif = nextPointCost - currentPointCost;
				if (pointDif < 0 && !upgrade.isNegative())
				{
					return Pair.of(stats, 0d);
				}

				// If applying this new level will go over our cap, we don't want to add
				// experience
				if (currentPoints + pointDif > stats.getMaxPoints())
				{
					// Decrease gained exp to below the next potential level's requirements so we
					// can try again.

					experience = upgrade.getLevelExp(potentialLevel) - currentExperience - 1;
					potentialLevel--;

					if (experience <= 0)
						return Pair.of(stats, 0d);
				} else
				{
					break;
				}
			}

			// If somehow the experience addition requirement is negative... can't have
			// that!
			if (experience < 0)
			{
				return Pair.of(stats, 0d);
			}

//				System.out.println("Current point cost: " + currentPointCost + ", Next point cost: " + nextPointCost);

		}

		// If we can't even get the upgrade to level 1, best not to add anything.
		if (potentialLevel == 0)
		{
			return Pair.of(stats, 0d);
		}

		int newLevel = upgrade.getLevel((int) (currentExperience + experience));
		boolean didUpgrade = false;
		if (upgrade.getLevel((int) currentExperience) != newLevel)
		{
			LivingEquipmentEvent.LevelUp levelUpEvent = new LivingEquipmentEvent.LevelUp(player, stats, upgrade);
//				LivingEquipmentEvent.LEVEL_UP.invoker().levelUp(levelUpEvent);
			MinecraftForge.EVENT_BUS.post(levelUpEvent);
			didUpgrade = true;

			player.displayClientMessage(Component.translatable("chat.bloodmagic.living_upgrade_level_increase", Component.translatable(upgrade.getTranslationKey()), newLevel), true);
		}

//			System.out.println("Adding experience! Total experience is: " + currentExperience);

		stats.addExperience(upgrade.getKey(), experience);
		LivingStats.toPlayer(player, stats);
		return Pair.of(stats, experience / multiplicationFactor);
	}

	public static double getDamageReceivedForArmour(Player player, DamageSource source, double damage)
	{
//		System.out.println("Initial damage from " + source + ": " + damage);
		LivingStats stats = LivingStats.fromPlayer(player, true);
		if (stats == null)
			return damage;

		Map<LivingUpgrade, Double> upgrades = stats.getUpgrades();
		for (Entry<LivingUpgrade, Double> entry : upgrades.entrySet())
		{
			LivingUpgrade upgrade = entry.getKey();
			if (upgrade.getArmorProvider() == null)
			{
				continue;
			}

			int level = upgrade.getLevel(entry.getValue().intValue());
			damage *= 1 - upgrade.getArmorProvider().getProtection(player, stats, source, upgrade, level);
		}

		return damage;
	}

	public static double getAdditionalDamage(Player player, ItemStack weapon, LivingEntity attackedEntity, double damage)
	{
//		System.out.println("Initial damage from " + source + ": " + damage);
		LivingStats stats = LivingStats.fromPlayer(player, true);
		if (stats == null)
			return 0;

		double additionalDamage = 0;

		Map<LivingUpgrade, Double> upgrades = stats.getUpgrades();
		for (Entry<LivingUpgrade, Double> entry : upgrades.entrySet())
		{
			LivingUpgrade upgrade = entry.getKey();
			if (upgrade.getArmorProvider() == null)
			{
				continue;
			}

			int level = upgrade.getLevel(entry.getValue().intValue());
			if (upgrade.getDamageProvider() == null)
			{
				continue;
			}
			additionalDamage += upgrade.getDamageProvider().getAdditionalDamage(player, weapon, damage, stats, attackedEntity, upgrade, level);
		}

//		System.out.println("Final damage: " + damage);

		return additionalDamage;
	}

	public static boolean canTrain(Player player, LivingUpgrade upgrade, int currentLevel, int nextLevel)
	{
		ItemStack trainer = PlayerUtil.findItem(player, stack -> stack.getItem() instanceof ItemLivingTrainer && stack.hasTag() && stack.getTag().contains("livingStats"));

		if (trainer.isEmpty())
			return true;

//		String mode = trainer.getTag().getString("livingLock");
		LivingStats stats = ((ILivingContainer) trainer.getItem()).getLivingStats(trainer);
		boolean isWhitelist = ((ItemLivingTrainer) trainer.getItem()).getIsWhitelist(trainer);

		int levelLimit = stats.getLevel(upgrade.getKey());

		if (isWhitelist)
		{
			return levelLimit != 0 && (levelLimit > currentLevel || (nextLevel != currentLevel && levelLimit >= nextLevel));
		} else
		{
			Map<LivingUpgrade, Double> upgradeMap = stats.getUpgrades();

			return (levelLimit == 0 && !upgradeMap.containsKey(upgrade)) || (levelLimit != 0 && (levelLimit > currentLevel || (nextLevel != currentLevel && levelLimit >= nextLevel)));
		}

//		return true;
	}

	public static boolean hasFullSet(Player player)
	{
		for (ItemStack stack : player.getInventory().armor)
		{
			if (stack.isEmpty() || !(stack.getItem() instanceof ILivingContainer))
				return false;

			if (stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getType() == ArmorItem.Type.CHESTPLATE)
			{
				if (stack.getMaxDamage() - stack.getDamageValue() <= 1)
				{
					return false;
				}
			}
		}

		return true;
	}

	public static void applyAttributes(Multimap<Attribute, AttributeModifier> attributes, ItemStack stack, Player player, EquipmentSlot slot)
	{
		if (player == null || !hasFullSet(player))
			return;

		Multimap<Attribute, AttributeModifier> newAttributes = ((ArmorItem) stack.getItem()).getAttributeModifiers(slot, stack);
//		newAttributes.values().forEach(e -> e.setSerialize(false));
		attributes.putAll(newAttributes);
	}
}