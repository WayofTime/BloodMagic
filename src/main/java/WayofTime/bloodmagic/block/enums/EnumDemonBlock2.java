package WayofTime.bloodmagic.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumDemonBlock2 implements IStringSerializable {
    SMALLBRICK_RAW,
    SMALLBRICK_CORROSIVE,
    SMALLBRICK_DESTRUCTIVE,
    SMALLBRICK_VENGEFUL,
    SMALLBRICK_STEADFAST,
    TILE_RAW,
    TILE_CORROSIVE,
    TILE_DESTRUCTIVE,
    TILE_VENGEFUL,
    TILE_STEADFAST,
    TILESPECIAL_RAW,
    TILESPECIAL_CORROSIVE,
    TILESPECIAL_DESTRUCTIVE,
    TILESPECIAL_VENGEFUL,
    TILESPECIAL_STEADFAST;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
