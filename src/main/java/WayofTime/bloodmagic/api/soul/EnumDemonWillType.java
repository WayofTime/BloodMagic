package WayofTime.bloodmagic.api.soul;

import lombok.Getter;
import net.minecraft.util.IStringSerializable;

@Getter
public enum EnumDemonWillType implements IStringSerializable
{
    DEFAULT("Default"),
    CORROSIVE("Corrosive"),
    DESTRUCTIVE("Destructive"),
    VENGEFUL("Vengeful"),
    STEADFAST("Steadfast");

    public final String name;

    EnumDemonWillType(String name)
    {
        this.name = name;
    }
}
