package WayofTime.bloodmagic.block.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public enum EnumBloodRune implements IStringSerializable
{
    BLANK,
    SPEED,
    EFFICIENCY,
    SACRIFICE,
    SELFSACRIFICE,
    DISPLACEMENT,
    CAPACITY,
    AUGCAPACITY,
    ORB,
    ACCELERATION,
    CHARGING;

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
