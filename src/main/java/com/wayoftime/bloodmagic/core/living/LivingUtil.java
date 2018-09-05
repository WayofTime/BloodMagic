package com.wayoftime.bloodmagic.core.living;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public class LivingUtil {

    @Nullable
    public static LivingStats applyNewExperience(EntityPlayer player, LivingUpgrade upgrade, int experience) {
        LivingStats stats = LivingStats.fromPlayer(player, true);
        if (stats == null)
            return null;

        LivingEquipmentEvent.GainExperience event = new LivingEquipmentEvent.GainExperience(player, stats, upgrade, experience);
        if (MinecraftForge.EVENT_BUS.post(event))
            return stats;

        experience = event.getExperience();

        int currentExperience = stats.getUpgrades().getOrDefault(upgrade, 0);
        int requiredForLevel = upgrade.getNextRequirement(currentExperience) - currentExperience;

        // If we're going to level up from this, check points
        if (requiredForLevel <= experience) {
            int currentPoints = stats.getUsedPoints();
            // If we're already capped or somehow over the cap, we don't want to add experience
            if (currentPoints >= stats.getMaxPoints())
                return stats;

            int nextPointCost = upgrade.getLevelCost(upgrade.getLevel(currentExperience) + 1);
            // If there's no more levels in this upgrade, we don't want to add experience
            if (nextPointCost == -1)
                return stats;

            // If applying this new level will go over our cap, we don't want to add experience
            if (currentPoints + nextPointCost > stats.getMaxPoints())
                return stats;
        }

        int newLevel = upgrade.getLevel(currentExperience + experience);
        if (upgrade.getLevel(currentExperience) != newLevel) {
            MinecraftForge.EVENT_BUS.post(new LivingEquipmentEvent.LevelUp(player, stats, upgrade));
            player.sendStatusMessage(new TextComponentTranslation("chat.bloodmagic:living_upgrade_level_increase", new TextComponentTranslation(upgrade.getUnlocalizedName()), newLevel), true);
        }

        stats.addExperience(upgrade.getKey(), experience);
        LivingStats.toPlayer(player, stats);
        return stats;
    }

    public static boolean hasFullSet(EntityPlayer player) {
        for (ItemStack stack : player.inventory.armorInventory)
            if (stack.isEmpty() || !(stack.getItem() instanceof ILivingContainer))
                return false;

        return true;
    }
}
