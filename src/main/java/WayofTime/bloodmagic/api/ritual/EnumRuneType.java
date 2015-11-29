package WayofTime.bloodmagic.api.ritual;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumRuneType implements IStringSerializable {

    BLANK,
    WATER,
    FIRE,
    EARTH,
    AIR,
    DUSK,
    DAWN;

    public static EnumRuneType byMetadata(int meta) {
        if (meta < 0 || meta >= values().length)
            meta = 0;

        return values()[meta];
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
