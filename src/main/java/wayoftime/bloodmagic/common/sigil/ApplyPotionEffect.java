package wayoftime.bloodmagic.common.sigil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.api.sigil.SigilEffect;

import java.util.Optional;

public record ApplyPotionEffect(Holder<MobEffect> effect, int selfAmplifier, int selfTicks, int upkeep, Optional<Integer> otherAmplifier, Optional<Integer> otherTicks, Optional<Integer> otherCost) implements SigilEffect {
    public static final MapCodec<ApplyPotionEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            MobEffect.CODEC.fieldOf("effect_type").forGetter(ApplyPotionEffect::effect),
            Codec.INT.fieldOf("amplifier").forGetter(ApplyPotionEffect::selfAmplifier),
            Codec.INT.fieldOf("duration").forGetter(ApplyPotionEffect::selfTicks),
            Codec.INT.fieldOf("upkeep").forGetter(ApplyPotionEffect::upkeep),
            Codec.INT.optionalFieldOf("amplifier_others").forGetter(ApplyPotionEffect::otherAmplifier),
            Codec.INT.optionalFieldOf("duration_others").forGetter(ApplyPotionEffect::otherTicks),
            Codec.INT.optionalFieldOf("cost_others").forGetter(ApplyPotionEffect::otherCost)
    ).apply(builder, ApplyPotionEffect::new));

    @Override
    public boolean isActivatable() {
        return true;
    }

    @Override
    public int activeTick(ItemStack sigil, Level level, Player player) {
        player.addEffect(new MobEffectInstance(effect, selfTicks, selfAmplifier));
        return upkeep;
    }

    @Override
    public int useOnEntity(ItemStack sigil, Player player, LivingEntity target) {
        // TODO allow giving effect to entities? could be used offensively as well with negative effects maybe
        return SigilEffect.super.useOnEntity(sigil, player, target);
    }

    // TODO implement these
    @Override
    public boolean hasArrayEffect() {
        return SigilEffect.super.hasArrayEffect();
    }

    @Override
    public int arrayTick(ItemStack sigil, Level level, BlockPos arrayPos) {
        return SigilEffect.super.arrayTick(sigil, level, arrayPos);
    }

    @Override
    public MapCodec<? extends SigilEffect> codec() {
        return CODEC;
    }
}
