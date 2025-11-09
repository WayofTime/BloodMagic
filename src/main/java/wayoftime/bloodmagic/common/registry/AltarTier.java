package wayoftime.bloodmagic.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record AltarTier(int tier, List<AltarComponent> components) implements Comparable<AltarTier> {
    public static final Codec<AltarTier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("tier").forGetter(AltarTier::tier),
            Codec.list(AltarComponent.CODEC).fieldOf("components").forGetter(AltarTier::components)
    ).apply(instance, AltarTier::new));

    @Override
    public int compareTo(@NotNull AltarTier altarTier) {
        return Integer.compare(this.tier, altarTier.tier);
    }
}
