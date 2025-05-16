package wayoftime.bloodmagic.common.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import wayoftime.bloodmagic.util.EnumRuneType;

public record BloodRune(EnumRuneType type, int amount) {
    public static final Codec<BloodRune> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            StringRepresentable.fromEnum(EnumRuneType::values).fieldOf("type").forGetter(BloodRune::type),
            Codec.INT.fieldOf("amount").forGetter(BloodRune::amount)
    ).apply(builder, BloodRune::new));
}
