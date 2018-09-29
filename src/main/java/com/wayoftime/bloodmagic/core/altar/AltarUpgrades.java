package com.wayoftime.bloodmagic.core.altar;

import com.google.common.collect.Maps;
import net.minecraftforge.fluids.Fluid;

import java.util.EnumMap;

import static com.wayoftime.bloodmagic.core.altar.BloodRunes.*;

public class AltarUpgrades {

    private final EnumMap<BloodRunes, Integer> upgrades;

    public AltarUpgrades() {
        this.upgrades = Maps.newEnumMap(BloodRunes.class);
    }

    public AltarUpgrades upgrade(BloodRunes runeType) {
        upgrades.compute(runeType, (k, v) -> v == null ? 1 : v + 1);
        return this;
    }

    public int getAccelerationCount() {
        return getCount(ACCELERATION);
    }

    public float getCapacityModifier() {
        return (float) ((1 * Math.pow(1.10, getCount(AUGMENTED_CAPACITY))) + 0.20 * getCount(CAPACITY));
    }

    public int getChargingFrequency() {
        return Math.max(20 - getCount(ACCELERATION), 1);
    }

    public int getChargingRate() {
        return (int) (10 * getCount(CHARGING) * (1 + getConsumptionModifier() / 2));
    }

    public float getConsumptionModifier() {
        return (float) (0.20 * getCount(SPEED));
    }

    public float getDislocationModifier() {
        return (float) (Math.pow(1.2, getCount(DISPLACEMENT)));
    }

    public float getEfficiencyModifier() {
        return (float) Math.pow(0.85, getCount(EFFICIENCY));
    }

    public int getMaxCharge() {
        return (int) (Fluid.BUCKET_VOLUME * Math.max(0.5 * getCapacityModifier(), 1) * getCount(CHARGING));
    }

    public float getOrbCapacityModifier() {
        return (float) (1.02 * getCount(ORB));
    }

    public float getSacrificeModifier() {
        return (float) (0.10 * getCount(SACRIFICE));
    }

    public float getSelfSacrificeModifier() {
        return (float) (0.10 * getCount(SELF_SACRIFICE));
    }

    public int getCount(BloodRunes rune) {
        return upgrades.getOrDefault(rune, 0);
    }
}
