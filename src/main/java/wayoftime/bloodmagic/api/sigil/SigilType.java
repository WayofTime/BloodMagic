package wayoftime.bloodmagic.api.sigil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SigilType(int airCost, int blockCost, int entityCost, int refreshCost, SigilEffect effect) { // TODO arcane ash sigil beacon thingy?
    public static final Codec<SigilType> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("click_air_cost").forGetter(SigilType::airCost),
            Codec.INT.fieldOf("click_block_cost").forGetter(SigilType::blockCost),
            Codec.INT.fieldOf("click_entity_cost").forGetter(SigilType::entityCost),
            Codec.INT.fieldOf("refresh_cost").forGetter(SigilType::refreshCost),
            SigilEffect.CODEC.fieldOf("sigil_effect").forGetter(SigilType::effect)
    ).apply(builder, SigilType::new));
}
