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
import wayoftime.bloodmagic.api.living.LivingEntityEffect;

public record RemoveMobEffect(Holder<MobEffect> mobEffect, LevelBasedValue amplifier) implements LivingEntityEffect {
    public static final MapCodec<RemoveMobEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("mob_effect").forGetter(RemoveMobEffect::mobEffect),
            LevelBasedValue.CODEC.fieldOf("amplifier").forGetter(RemoveMobEffect::amplifier)
    ).apply(builder, RemoveMobEffect::new));

    @Override
    public void apply(int upgradeLevel, Entity entity) {
        LivingEntity living = (LivingEntity) entity;
        MobEffectInstance instance = living.getEffect(mobEffect);
        if (instance != null && instance.getAmplifier() <= amplifier.calculate(upgradeLevel)) {
            living.removeEffect(mobEffect);
        }
    }

    @Override
    public MapCodec<? extends LivingEntityEffect> codec() {
        return CODEC;
    }
}
