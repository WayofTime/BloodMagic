package WayofTime.bloodmagic.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumBloodRune implements IStringSerializable {
    BLANK,
    SPEED,
    EFFICIENCY,
    SACRIFICE,
    SELFSACRIFICE,
    DISPLACEMENT,
    CAPACITY,
    AUGCAPACITY,
    ORB,
    ACCELERATION,
    CHARGING;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
