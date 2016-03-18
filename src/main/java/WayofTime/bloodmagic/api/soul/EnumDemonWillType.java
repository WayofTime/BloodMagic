package WayofTime.bloodmagic.api.soul;

import lombok.Getter;
import net.minecraft.util.IStringSerializable;

@Getter
public enum EnumDemonWillType implements IStringSerializable
{
    DEFAULT("default"),
    CORROSIVE("corrosive"),
    DESTRUCTIVE("destructive"),
    VENGEFUL("vengeful"),
    STEADFAST("steadfast");

    public final String name;

    EnumDemonWillType(String name)
    {
        this.name = name;
    }
}
