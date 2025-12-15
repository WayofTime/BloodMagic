package wayoftime.bloodmagic.api.sigil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SigilType(int airCost, int blockCost, int entityCost, int refreshCost, boolean isActivatable, SigilEffect effect) { // TODO arcane ash sigil beacon thingy?
    public static final Codec<SigilType> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("use_air_cost").forGetter(SigilType::airCost),
            Codec.INT.fieldOf("use_block_cost").forGetter(SigilType::blockCost),
            Codec.INT.fieldOf("use_entity_cost").forGetter(SigilType::entityCost),
            Codec.INT.fieldOf("refresh_cost").forGetter(SigilType::refreshCost),
            Codec.BOOL.fieldOf("is_activatable").forGetter(SigilType::isActivatable),
            SigilEffect.CODEC.fieldOf("sigil_effect").forGetter(SigilType::effect)
    ).apply(builder, SigilType::new));
}
