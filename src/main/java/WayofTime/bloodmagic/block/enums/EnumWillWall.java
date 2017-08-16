package WayofTime.bloodmagic.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumWillWall implements IStringSerializable {
    BRICK_RAW,
    BRICK_CORROSIVE,
    BRICK_DESTRUCTIVE,
    BRICK_VENGEFUL,
    BRICK_STEADFAST,
    SMALLBRICK_RAW,
    SMALLBRICK_CORROSIVE,
    SMALLBRICK_DESTRUCTIVE,
    SMALLBRICK_VENGEFUL,
    SMALLBRICK_STEADFAST,
    LARGE_RAW,
    LARGE_CORROSIVE,
    LARGE_DESTRUCTIVE,
    LARGE_VENGEFUL,
    LARGE_STEADFAST;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
