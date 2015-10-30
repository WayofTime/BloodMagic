package WayofTime.alchemicalWizardry.api.enumeration;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum  EnumStoneType implements IStringSerializable {

    BLANK,
    WATER,
    FIRE,
    EARTH,
    AIR,
    DUSK,
    DAWN;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }

    public static EnumStoneType byMetadata(int meta) {
        if (meta < 0 || meta >= values().length)
            meta = 0;

        return values()[meta];
    }
}
