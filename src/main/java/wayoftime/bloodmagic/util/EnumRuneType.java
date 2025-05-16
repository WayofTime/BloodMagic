package wayoftime.bloodmagic.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum EnumRuneType implements StringRepresentable {
    SPEED("speed"),
    SACRIFICE("sacrifice"),
    SELF_SACRIFICE("self_sacrifice"),
    DISPLACEMENT("displacement"),
    CAPACITY("capacity"),
    AUGMENTED_CAPACITY("augmented_capacity"),
    ORB("orb"),
    ACCELERATION("acceleration"),
    CHARGING("charging"),
    EFFICIENCY("efficiency");

    private final String name;
    EnumRuneType(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
