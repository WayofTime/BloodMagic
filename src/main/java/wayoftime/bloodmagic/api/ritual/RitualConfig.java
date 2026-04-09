package wayoftime.bloodmagic.api.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record RitualConfig(HashMap<String, Range> ranges, Map<EnumWillType, Boolean> activeTypes) {
    public static final Codec<RitualConfig> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.unboundedMap(Codec.STRING, Range.CODEC).fieldOf("ranges").xmap(HashMap::new, Function.identity()).forGetter(RitualConfig::ranges),
            Codec.unboundedMap(EnumWillType.CODEC, Codec.BOOL).fieldOf("activeTypes").forGetter(RitualConfig::activeTypes)
    ).apply(builder, RitualConfig::new));
}
