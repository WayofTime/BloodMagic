package WayofTime.bloodmagic.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumPath implements IStringSerializable {
    WOOD,
    WOODTILE,
    STONE,
    STONETILE,
    WORNSTONE,
    WORNSTONETILE,
    OBSIDIAN,
    OBSIDIANTILE;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
