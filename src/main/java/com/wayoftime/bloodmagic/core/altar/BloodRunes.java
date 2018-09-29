package com.wayoftime.bloodmagic.core.altar;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum BloodRunes implements IStringSerializable {

    BLANK,
    SPEED,
    EFFICIENCY,
    SACRIFICE,
    SELF_SACRIFICE,
    DISPLACEMENT,
    CAPACITY,
    AUGMENTED_CAPACITY,
    ORB,
    ACCELERATION,
    CHARGING,
    ;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
