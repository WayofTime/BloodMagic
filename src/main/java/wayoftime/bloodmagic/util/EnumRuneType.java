package wayoftime.bloodmagic.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum EnumRuneType implements StringRepresentable {
    SPEED,
    SACRIFICE,
    SELF_SACRIFICE,
    DISPLACEMENT,
    CAPACITY,
    AUGMENTED_CAPACITY,
    ORB,
    ACCELERATION,
    CHARGING,
    EFFICIENCY;

    public static final Codec<EnumRuneType> CODEC = StringRepresentable.fromEnum(EnumRuneType::values);

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
