package com.wayoftime.bloodmagic.core.type;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum DemonWillType implements IStringSerializable {

    RAW,
    CORROSIVE,
    DESTRUCTIVE,
    VENGEFUL,
    STEADFAST,
    ;

    public static final DemonWillType[] VALUES = values();

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
