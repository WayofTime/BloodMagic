package WayofTime.bloodmagic.block.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public enum EnumBrickSize implements IStringSerializable
{
    LARGE,
    BRICK;

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
