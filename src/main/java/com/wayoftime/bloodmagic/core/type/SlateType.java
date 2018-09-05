package com.wayoftime.bloodmagic.core.type;

import java.util.Locale;

public enum SlateType {

    BLANK,
    REINFORCED,
    IMBUED,
    DEMONIC,
    ETHEREAL,
    ;

    private final String name;

    SlateType() {
        this.name = name().toLowerCase(Locale.ROOT);
    }

    public String getName() {
        return name;
    }
}
