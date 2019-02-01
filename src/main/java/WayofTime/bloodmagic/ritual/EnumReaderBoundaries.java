package WayofTime.bloodmagic.ritual;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumReaderBoundaries implements IStringSerializable {
    SUCCESS,
    VOLUME_TOO_LARGE,
    NOT_WITHIN_BOUNDARIES;


    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getName() {
        return toString();
    }
}
