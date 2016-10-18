package WayofTime.bloodmagic.block.enums;

import java.util.Locale;

import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import net.minecraft.util.IStringSerializable;

public enum EnumSubWillType implements IStringSerializable
{
    RAW,
    CORROSIVE,
    DESTRUCTIVE,
    VENGEFUL,
    STEADFAST;

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

    public EnumDemonWillType getType() {
        return EnumDemonWillType.valueOf(name());
    }
}
