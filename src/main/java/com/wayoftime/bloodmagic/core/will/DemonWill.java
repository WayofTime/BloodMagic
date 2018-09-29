package com.wayoftime.bloodmagic.core.will;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum DemonWill implements IStringSerializable {

    RAW,
    CORROSIVE,
    DESTRUCTIVE,
    VENGEFUL,
    STEADFAST,
    ;

    public static final DemonWill[] VALUES = values();

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
