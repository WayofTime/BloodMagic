package wayoftime.bloodmagic.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record AltarTier(int tier, int distance, int sideRunes, int sideBlocks, int pillarHeight, int pillarOffset, ExtraCodecs.TagOrElementLocation capstone) implements Comparable<AltarTier> {
    public static final Codec<AltarTier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("tier").forGetter(AltarTier::tier),
            Codec.INT.optionalFieldOf("distance", 2).forGetter(AltarTier::distance),
            Codec.INT.fieldOf("sideRunes").forGetter(AltarTier::sideRunes),
            Codec.INT.fieldOf("sideBlocks").forGetter(AltarTier::sideBlocks),
            Codec.INT.fieldOf("pillarHeight").forGetter(AltarTier::pillarHeight),
            Codec.INT.fieldOf("pillarOffset").forGetter(AltarTier::pillarOffset),
            ExtraCodecs.TAG_OR_ELEMENT_ID.fieldOf("capstone").forGetter(AltarTier::capstone)
    ).apply(instance, AltarTier::new));

    @Override
    public int compareTo(@NotNull AltarTier altarTier) {
        return Integer.compare(this.tier, altarTier.tier);
    }
}
