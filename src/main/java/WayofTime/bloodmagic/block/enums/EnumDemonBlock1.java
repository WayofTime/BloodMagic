package WayofTime.bloodmagic.block.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public enum EnumDemonBlock1 implements IStringSerializable
{
    BRICK1_RAW,
    BRICK1_CORROSIVE,
    BRICK1_DESTRUCTIVE,
    BRICK1_VENGEFUL,
    BRICK1_STEADFAST;

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
