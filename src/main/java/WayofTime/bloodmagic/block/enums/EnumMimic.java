package WayofTime.bloodmagic.block.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumMimic implements IStringSerializable {
    NOHITBOX,
    SOLIDOPAQUE,
    SOLIDCLEAR,
    SOLIDLIGHT,
    SENTIENT;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
