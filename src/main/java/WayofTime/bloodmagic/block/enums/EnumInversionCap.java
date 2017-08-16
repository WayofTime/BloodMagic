package WayofTime.bloodmagic.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumInversionCap implements IStringSerializable {
    RAW_BOTTOM,
    RAW_TOP,
    CORROSIVE_BOTTOM,
    CORROSIVE_TOP,
    DESTRUCTIVE_BOTTOM,
    DESTRUCTIVE_TOP,
    VENGEFUL_BOTTOM,
    VENGEFUL_TOP,
    STEADFAST_BOTTOM,
    STEADFAST_TOP;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
