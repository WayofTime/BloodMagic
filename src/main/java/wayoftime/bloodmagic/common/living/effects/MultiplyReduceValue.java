package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.storage.loot.LootContext;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.living.LivingValueEffect;

public record MultiplyReduceValue(LevelBasedValue amounts) implements LivingValueEffect {
    public static final MapCodec<MultiplyReduceValue> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("amounts").forGetter(MultiplyReduceValue::amounts)
    ).apply(builder, MultiplyReduceValue::new));

    @Override
    public float process(int level, LootContext lootContext, float value) {
        float multi = amounts.calculate(level);
        float ret = value * (1 - multi);
        BloodMagic.LOGGER.info("multi: {} for level {}, ret: {}", multi, level, ret);
        return ret;
    }

    @Override
    public MapCodec<? extends LivingValueEffect> codec() {
        return CODEC;
    }
}
