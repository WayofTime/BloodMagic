package WayofTime.bloodmagic.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumSubWillType1 implements IStringSerializable {
    RAW,
    CORROSIVE;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
