package wayoftime.bloodmagic.common.ritual.weak;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

// "ApplyMobEffectEffect" would be more "correct" but eh. This conveys the meaning anyway
public record ApplyPotionEffect(int cost, Holder<MobEffect> effectType, int effectTime, Optional<Integer> amplifier, Optional<Boolean> showIcon) implements ImperfectRitualEffect {

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void perform(Player player, ServerLevel level, BlockPos ritualPos) {
        new MobEffectInstance(effectType, effectTime, amplifier.isPresent() ? amplifier().get() : 0, false, showIcon.isPresent() ? showIcon().get() : true);
    }

    public static final MapCodec<ApplyPotionEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.INT.fieldOf("cost").forGetter(ApplyPotionEffect::cost),
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect_type").forGetter(ApplyPotionEffect::effectType),
            Codec.INT.fieldOf("effect_time").forGetter(ApplyPotionEffect::effectTime),
            Codec.INT.optionalFieldOf("amplifier").forGetter(ApplyPotionEffect::amplifier),
            Codec.BOOL.optionalFieldOf("showIcon").forGetter(ApplyPotionEffect::showIcon)
    ).apply(builder, ApplyPotionEffect::new));

    @Override
    public MapCodec<? extends ImperfectRitualEffect> codec() {
        return CODEC;
    }
}
