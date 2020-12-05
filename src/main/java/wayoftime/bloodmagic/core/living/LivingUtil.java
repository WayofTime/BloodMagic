package wayoftime.bloodmagic.core.living;

import java.util.Map;
import java.util.Map.Entry;

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
	public static LivingStats applyNewExperience(PlayerEntity player, LivingUpgrade upgrade, double experience)
	{
		LivingStats stats = LivingStats.fromPlayer(player, true);
		if (stats == null)
			return null;

		if (!canTrain(player, upgrade, upgrade.getLevel((int) experience)))
			return stats;

		LivingEquipmentEvent.GainExperience event = new LivingEquipmentEvent.GainExperience(player, stats, upgrade, experience);
//		EventResult result = LivingEquipmentEvent.EXPERIENCE_GAIN.invoker().gainExperience(event);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled())
			return stats;

		experience = event.getExperience();

		double currentExperience = stats.getUpgrades().getOrDefault(upgrade, 0d);
		double requiredForLevel = upgrade.getNextRequirement((int) currentExperience) - currentExperience;

		// If we're going to level up from this, check points
		if (requiredForLevel <= experience)
		{
			int currentPoints = stats.getUsedPoints();
			// If we're already capped or somehow over the cap, we don't want to add
			// experience
			if (currentPoints >= stats.getMaxPoints())
				return stats;

			int nextPointCost = upgrade.getLevelCost(upgrade.getLevel((int) currentExperience) + 1);
			// If there's no more levels in this upgrade, we don't want to add experience
			if (nextPointCost == -1)
				return stats;

			// If applying this new level will go over our cap, we don't want to add
			// experience
			if (currentPoints + nextPointCost > stats.getMaxPoints())
				return stats;
		}

		int newLevel = upgrade.getLevel((int) (currentExperience + experience));
		if (upgrade.getLevel((int) currentExperience) != newLevel)
		{
			LivingEquipmentEvent.LevelUp levelUpEvent = new LivingEquipmentEvent.LevelUp(player, stats, upgrade);
//			LivingEquipmentEvent.LEVEL_UP.invoker().levelUp(levelUpEvent);
			MinecraftForge.EVENT_BUS.post(levelUpEvent);

			player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.living_upgrade_level_increase", new TranslationTextComponent(upgrade.getTranslationKey()), newLevel), true);
		}

//		System.out.println("Adding experience! Total experience is: " + currentExperience);

		stats.addExperience(upgrade.getKey(), experience);
		LivingStats.toPlayer(player, stats);
		return stats;
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