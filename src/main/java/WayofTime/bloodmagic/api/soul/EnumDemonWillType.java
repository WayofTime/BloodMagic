package WayofTime.bloodmagic.api.soul;

import net.minecraft.util.IStringSerializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumDemonWillType implements IStringSerializable
{
    DEFAULT("Default"),
    CORROSIVE("Corrosive"),
    DESTRUCTIVE("Destructive"),
    VENGEFUL("Vengeful"),
    STEADFAST("Steadfast");

    public final String name;
}
