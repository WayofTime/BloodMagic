package WayofTime.bloodmagic.block.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public enum EnumMimic implements IStringSerializable
{
    NOHITBOX,
    SOLIDOPAQUE,
    SOLIDCLEAR,
    SOLIDLIGHT,
    SENTIENT;

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
