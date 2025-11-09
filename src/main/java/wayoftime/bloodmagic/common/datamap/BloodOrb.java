package wayoftime.bloodmagic.common.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BloodOrb(int tier, int capacity, int fillRate) {
    public static Codec<BloodOrb> CODEC = RecordCodecBuilder.create(
            builder -> builder.group(
                    Codec.INT.fieldOf("tier").forGetter(BloodOrb::tier),
                    Codec.INT.fieldOf("capacity").forGetter(BloodOrb::capacity),
                    Codec.INT.fieldOf("fillRate").forGetter(BloodOrb::fillRate)
            ).apply(builder, BloodOrb::new)
    );
}
