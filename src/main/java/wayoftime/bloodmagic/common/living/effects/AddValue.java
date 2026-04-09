package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.storage.loot.LootContext;
import wayoftime.bloodmagic.api.living.LivingValueEffect;

public record AddValue(LevelBasedValue amounts) implements LivingValueEffect {
    public static final MapCodec<AddValue> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("amounts").forGetter(AddValue::amounts)
    ).apply(builder, AddValue::new));

    @Override
    public float process(int level, LootContext lootContext, float value) {
        return value + amounts.calculate(level);
    }

    @Override
    public MapCodec<? extends LivingValueEffect> codec() {
        return CODEC;
    }
}
