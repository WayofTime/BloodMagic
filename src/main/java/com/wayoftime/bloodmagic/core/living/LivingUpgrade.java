package com.wayoftime.bloodmagic.core.living;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class LivingUpgrade {

    private final ResourceLocation key;
    private final Set<ResourceLocation> incompatible;
    private final TreeMap<Integer, Integer> experienceToLevel;
    private final Map<Integer, Integer> levelToCost;
    private final boolean isNegative;
    private String unlocalizedName = null;
    private IAttributeProvider attributeProvider;
    private IArmorProvider armorProvider;

    public LivingUpgrade(ResourceLocation key, Consumer<List<UpgradeLevel>> experienceMapper /* xp needed -> level */, boolean isNegative) {
        this.key = key;
        this.incompatible = Sets.newHashSet();
        this.experienceToLevel = Maps.newTreeMap();
        this.levelToCost = Maps.newHashMap();
        this.isNegative = isNegative;

        List<UpgradeLevel> levels = Lists.newArrayList();
        experienceMapper.accept(levels);

        for (int i = 0; i < levels.size(); i++) {
            UpgradeLevel level = levels.get(i);
            experienceToLevel.put(level.experienceNeeded, i + 1);
            levelToCost.put(i + 1, level.upgradeCost);
        }
    }

    public LivingUpgrade(ResourceLocation key, Consumer<List<UpgradeLevel>> experienceMapper /* xp needed -> level */) {
        this(key, experienceMapper, false);
    }

    public LivingUpgrade withAttributeProvider(IAttributeProvider attributeProvider) {
        this.attributeProvider = attributeProvider;
        return this;
    }

    @Nullable
    public IAttributeProvider getAttributeProvider() {
        return attributeProvider;
    }

    public LivingUpgrade withArmorProvider(IArmorProvider armorProvider) {
        this.armorProvider = armorProvider;
        return this;
    }

    @Nullable
    public IArmorProvider getArmorProvider() {
        return armorProvider;
    }

    public String getUnlocalizedName() {
        return unlocalizedName == null ? unlocalizedName = "upgrade." + key.getNamespace() + ":" + key.getPath() + ".name" : unlocalizedName;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public boolean isCompatible(ResourceLocation otherUpgrade) {
        return !incompatible.contains(otherUpgrade);
    }

    public LivingUpgrade addIncompatibility(ResourceLocation... otherKeys) {
        Collections.addAll(incompatible, otherKeys);
        return this;
    }

    public int getLevel(int experience) {
        Map.Entry<Integer, Integer> floor = experienceToLevel.floorEntry(experience);
        return floor == null ? 0 : floor.getValue();
    }

    public int getNextRequirement(int experience) {
        Integer ret = experienceToLevel.ceilingKey(experience + 1);
        return ret == null ? 0 : ret;
    }

    public int getLevelCost(int level) {
        return levelToCost.getOrDefault(level, 0);
    }

    public ResourceLocation getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key.toString();
    }

    public interface IAttributeProvider {
        void handleAttributes(LivingStats stats, Multimap<String, AttributeModifier> attributeMap, int level);
    }

    public interface IArmorProvider {
        double getProtection(EntityPlayer player, DamageSource source, int level);
    }

    public static class UpgradeLevel {
        private final int experienceNeeded;
        private final int upgradeCost;

        public UpgradeLevel(int experienceNeeded, int upgradeCost) {
            this.experienceNeeded = experienceNeeded;
            this.upgradeCost = upgradeCost;
        }
    }
}
