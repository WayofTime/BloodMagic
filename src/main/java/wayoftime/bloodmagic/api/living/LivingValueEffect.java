package wayoftime.bloodmagic.api.living;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import wayoftime.bloodmagic.common.living.LivingValueEffects;

import java.util.function.Function;

public interface LivingValueEffect {
    Codec<LivingValueEffect> CODEC = Codec.lazyInitialized(() -> LivingValueEffects.VALUE_BASED_EFFECT_TYPE
            .getRegistry()
            .get()
            .byNameCodec()
            .dispatch(LivingValueEffect::codec, Function.identity())
    );

    float process(int level, LootContext lootContext, float value);

    MapCodec<? extends LivingValueEffect> codec();
}
