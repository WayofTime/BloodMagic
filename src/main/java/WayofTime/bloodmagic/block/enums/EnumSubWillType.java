package WayofTime.bloodmagic.block.enums;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumSubWillType implements IStringSerializable {
    RAW,
    CORROSIVE,
    DESTRUCTIVE,
    VENGEFUL,
    STEADFAST;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }

    public EnumDemonWillType getType() {
        String name = name();

        if (this == RAW)
            name = EnumDemonWillType.DEFAULT.name();

        return EnumDemonWillType.valueOf(name);
    }
}
