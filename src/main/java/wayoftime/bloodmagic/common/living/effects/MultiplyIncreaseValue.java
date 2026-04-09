package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.storage.loot.LootContext;
import wayoftime.bloodmagic.api.living.LivingValueEffect;

public record MultiplyIncreaseValue(LevelBasedValue amounts) implements LivingValueEffect {
    public static final MapCodec<MultiplyIncreaseValue> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("amounts").forGetter(MultiplyIncreaseValue::amounts)
    ).apply(builder, MultiplyIncreaseValue::new));

    @Override
    public float process(int level, LootContext lootContext, float value) {
        return value * (1 + amounts.calculate(level));
    }

    @Override
    public MapCodec<? extends LivingValueEffect> codec() {
        return CODEC;
    }
}
