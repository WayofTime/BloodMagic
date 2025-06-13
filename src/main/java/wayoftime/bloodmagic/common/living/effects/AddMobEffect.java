package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import wayoftime.bloodmagic.common.living.LivingEntityEffect;

public record AddMobEffect(Holder<MobEffect> mobEffect, LevelBasedValue amplifier, LevelBasedValue duration) implements LivingEntityEffect {
    public static final MapCodec<AddMobEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("mob_effect").forGetter(AddMobEffect::mobEffect),
            LevelBasedValue.CODEC.fieldOf("amplifier").forGetter(AddMobEffect::amplifier),
            LevelBasedValue.CODEC.fieldOf("duration").forGetter(AddMobEffect::duration)
    ).apply(builder, AddMobEffect::new));

    @Override
    public void apply(int upgradeLevel, Entity entity) {
        ((LivingEntity) entity).addEffect(new MobEffectInstance(mobEffect, (int) duration.calculate(upgradeLevel), (int) amplifier.calculate(upgradeLevel)));
    }

    @Override
    public MapCodec<? extends LivingEntityEffect> codec() {
        return CODEC;
    }
}
