package WayofTime.bloodmagic.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum BloodRuneType implements IStringSerializable {
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
