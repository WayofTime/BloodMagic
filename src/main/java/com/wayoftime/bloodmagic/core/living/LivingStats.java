package com.wayoftime.bloodmagic.core.living;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagicLivingArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Map;

public class LivingStats implements INBTSerializable<NBTTagCompound> {

    public static final int DEFAULT_UPGRADE_POINTS = 100;

    private final Map<LivingUpgrade, Integer> upgrades;
    private int maxPoints = DEFAULT_UPGRADE_POINTS;

    public LivingStats(Map<LivingUpgrade, Integer> upgrades) {
        this.upgrades = upgrades;
    }

    public LivingStats() {
        this(Maps.newHashMap());
    }

    public Map<LivingUpgrade, Integer> getUpgrades() {
        return ImmutableMap.copyOf(upgrades);
    }

    public LivingStats addExperience(ResourceLocation key, int experience) {
        LivingUpgrade upgrade = RegistrarBloodMagicLivingArmor.UPGRADES.get(key);
        int current = upgrades.getOrDefault(upgrade, 0);
        if (upgrade.getNextRequirement(current) == 0)
            return this;

        upgrades.put(upgrade, current + experience);
        return this;
    }

    public int getLevel(ResourceLocation key) {
        LivingUpgrade upgrade = RegistrarBloodMagicLivingArmor.UPGRADES.get(key);
        return upgrade.getLevel(upgrades.getOrDefault(upgrade, 0));
    }

    public int getUsedPoints() {
        int total = 0;
        for (Map.Entry<LivingUpgrade, Integer> applied : upgrades.entrySet()) {
            int experience = applied.getValue();
            int level = applied.getKey().getLevel(experience);
            int cost = applied.getKey().getLevelCost(level);
            total += cost;
        }

        return total;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public LivingStats setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
        return this;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList statList = new NBTTagList();
        upgrades.forEach((k, v) -> {
            NBTTagCompound upgrade = new NBTTagCompound();
            upgrade.setString("key", k.getKey().toString());
            upgrade.setInteger("exp", v);
            statList.appendTag(upgrade);
        });
        compound.setTag("upgrades", statList);

        compound.setInteger("maxPoints", maxPoints);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList statList = nbt.getTagList("upgrades", 10);

        for (NBTBase base : statList) {
            if (!(base instanceof NBTTagCompound))
                continue;

            LivingUpgrade upgrade = RegistrarBloodMagicLivingArmor.UPGRADES.get(new ResourceLocation(((NBTTagCompound) base).getString("key")));
            if (upgrade == null)
                continue;
            int experience = ((NBTTagCompound) base).getInteger("exp");
            upgrades.put(upgrade, experience);
        }

        maxPoints = nbt.getInteger("maxPoints");
    }

    public static LivingStats fromNBT(NBTTagCompound statTag) {
        LivingStats stats = new LivingStats();
        stats.deserializeNBT(statTag);
        return stats;
    }

    @Nullable
    public static LivingStats fromPlayer(EntityPlayer player) {
        return fromPlayer(player, false);
    }

    @Nullable
    public static LivingStats fromPlayer(EntityPlayer player, boolean createNew) {
        if (!LivingUtil.hasFullSet(player))
            return null;

        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        LivingStats stats = ((ILivingContainer) chest.getItem()).getLivingStats(chest);
        return stats == null && createNew ? new LivingStats() : stats;
    }

    public static void toPlayer(EntityPlayer player, LivingStats stats) {
        if (!LivingUtil.hasFullSet(player))
            return;

        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ((ILivingContainer) chest.getItem()).updateLivingStates(chest, stats);
    }
}
