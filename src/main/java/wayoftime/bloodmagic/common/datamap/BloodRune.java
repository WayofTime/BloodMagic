package wayoftime.bloodmagic.common.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import wayoftime.bloodmagic.api.altar.EnumRuneType;

public record BloodRune(EnumRuneType type, int amount) {
    public static final Codec<BloodRune> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            EnumRuneType.CODEC.fieldOf("type").forGetter(BloodRune::type),
            Codec.INT.fieldOf("amount").forGetter(BloodRune::amount)
    ).apply(builder, BloodRune::new));
}
