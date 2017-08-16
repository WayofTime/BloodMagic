package WayofTime.bloodmagic.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumDemonBlock3 implements IStringSerializable {
    STONE_RAW,
    STONE_CORROSIVE,
    STONE_DESTRUCTIVE,
    STONE_VENGEFUL,
    STONE_STEADFAST,
    POLISHED_RAW,
    POLISHED_CORROSIVE,
    POLISHED_DESTRUCTIVE,
    POLISHED_VENGEFUL,
    POLISHED_STEADFAST,
    METAL_RAW,
    METAL_CORROSIVE,
    METAL_DESTRUCTIVE,
    METAL_VENGEFUL,
    METAL_STEADFAST;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
