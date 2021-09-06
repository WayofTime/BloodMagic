package wayoftime.bloodmagic.core.living;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Multimap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import wayoftime.bloodmagic.common.item.ItemLivingTrainer;
import wayoftime.bloodmagic.core.util.PlayerUtil;
import wayoftime.bloodmagic.event.LivingEquipmentEvent;

public class LivingUtil
{
	// @return Pair containing the LivingStats of the player, and if the LivingStats
	// upgraded due to the applied EXP.
	public static Pair<LivingStats, Boolean> applyNewExperience(PlayerEntity player, LivingUpgrade upgrade, double experience)
	{
		LivingStats stats = LivingStats.fromPlayer(player, true);
		if (stats == null)
			return Pair.of(null, false);

		if (!canTrain(player, upgrade, upgrade.getLevel((int) experience)))
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

			player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.living_upgrade_level_increase", new TranslationTextComponent(upgrade.getTranslationKey()), newLevel), true);
		}

//		System.out.println("Adding experience! Total experience is: " + currentExperience);

		stats.addExperience(upgrade.getKey(), experience);
		LivingStats.toPlayer(player, stats);
		return Pair.of(stats, didUpgrade);
	}

	// @return Pair containing the LivingStats of the player. Returned Double is the
	// effective amount of exp that was applied.
	public static Pair<LivingStats, Double> applyExperienceToUpgradeCap(PlayerEntity player, LivingUpgrade upgrade, double experience)
	{
//		System.out.println("Initial exp added: " + experience);
		LivingStats stats = LivingStats.fromPlayer(player, true);
		if (stats == null)
			return Pair.of(null, 0d);

		if (!canTrain(player, upgrade, upgrade.getLevel((int) experience)))
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

			player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.living_upgrade_level_increase", new TranslationTextComponent(upgrade.getTranslationKey()), newLevel), true);
		}

//			System.out.println("Adding experience! Total experience is: " + currentExperience);

		stats.addExperience(upgrade.getKey(), experience);
		LivingStats.toPlayer(player, stats);
		return Pair.of(stats, experience / multiplicationFactor);
	}

	public static double getDamageReceivedForArmour(PlayerEntity player, DamageSource source, double damage)
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

//		System.out.println("Final damage: " + damage);

		return damage;
	}

	public static double getAdditionalDamage(PlayerEntity player, ItemStack weapon, LivingEntity attackedEntity, double damage)
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

	public static boolean canTrain(PlayerEntity player, LivingUpgrade upgrade, int currentLevel)
	{
		ItemStack trainer = PlayerUtil.findItem(player, stack -> stack.getItem() instanceof ItemLivingTrainer && stack.hasTag() && stack.getTag().contains("livingStats"));
		if (trainer.isEmpty())
			return true;

		String mode = trainer.getTag().getString("livingLock");
		LivingStats stats = ((ILivingContainer) trainer.getItem()).getLivingStats(trainer);

		int levelLimit = stats.getLevel(upgrade.getKey());
		if (mode.equalsIgnoreCase("whitelist"))
		{
			return levelLimit != 0 && levelLimit > currentLevel;
		} else if (mode.equalsIgnoreCase("blacklist"))
		{
			return levelLimit == 0;
		}

		return true;
	}

	public static boolean hasFullSet(PlayerEntity player)
	{
		for (ItemStack stack : player.inventory.armorInventory)
			if (stack.isEmpty() || !(stack.getItem() instanceof ILivingContainer))
			return false;

		return true;
	}

	public static void applyAttributes(Multimap<Attribute, AttributeModifier> attributes, ItemStack stack, PlayerEntity player, EquipmentSlotType slot)
	{
		if (player == null || !hasFullSet(player))
			return;

		Multimap<Attribute, AttributeModifier> newAttributes = ((ArmorItem) stack.getItem()).getAttributeModifiers(slot, stack);
//		newAttributes.values().forEach(e -> e.setSerialize(false));
		attributes.putAll(newAttributes);
	}
}