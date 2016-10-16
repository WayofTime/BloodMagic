package WayofTime.bloodmagic.block.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public enum EnumPath implements IStringSerializable
{
    WOOD,
    WOODTILE,
    STONE,
    STONETILE,
    WORNSTONE,
    WORNSTONETILE,
    OBSIDIAN,
    OBSIDIANTILE;

    @Override
    public String toString()
    {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName()
    {
        return this.toString();
    }
}
