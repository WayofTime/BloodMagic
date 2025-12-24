package wayoftime.bloodmagic.api.living;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;
import wayoftime.bloodmagic.common.living.LivingEntityEffects;

import java.util.function.Function;

public interface LivingEntityEffect {
    Codec<LivingEntityEffect> CODEC = Codec.lazyInitialized(() -> LivingEntityEffects.ENTITY_EFFECT_TYPE
            .getRegistry()
            .get()
            .byNameCodec()
            .dispatch(LivingEntityEffect::codec, Function.identity())
    );

    void apply(int upgradeLevel, Entity entity);

    MapCodec<? extends LivingEntityEffect> codec();
}
