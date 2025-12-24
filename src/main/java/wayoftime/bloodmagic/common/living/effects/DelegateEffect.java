package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import wayoftime.bloodmagic.api.living.LivingEntityEffect;
import wayoftime.bloodmagic.api.living.LivingValueEffect;

public record DelegateEffect(LivingEntityEffect effect) implements LivingValueEffect {
    public static final MapCodec<DelegateEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LivingEntityEffect.CODEC.fieldOf("delegate_effect").forGetter(DelegateEffect::effect)
    ).apply(builder, DelegateEffect::new));

    @Override
    public float process(int level, LootContext lootContext, float value) {
        effect.apply(level, lootContext.getParam(LootContextParams.ATTACKING_ENTITY));
        return value;
    }

    @Override
    public MapCodec<? extends LivingValueEffect> codec() {
        return CODEC;
    }
}
