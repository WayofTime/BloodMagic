package WayofTime.bloodmagic.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumDecorative implements IStringSerializable {
    BLOODSTONE_TILE,
    BLOODSTONE_BRICK,
    CRYSTAL_TILE,
    CRYSTAL_BRICK;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getName() {
        return toString();
    }
}
