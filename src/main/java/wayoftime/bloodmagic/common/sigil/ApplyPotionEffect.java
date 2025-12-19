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

public record ApplyPotionEffect(Holder<MobEffect> effect, int amplifier, int duration, int upkeep) implements SigilEffect {
    public static final MapCodec<ApplyPotionEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            MobEffect.CODEC.fieldOf("mob_effect_type").forGetter(ApplyPotionEffect::effect),
            Codec.INT.fieldOf("amplifier").forGetter(ApplyPotionEffect::amplifier),
            Codec.INT.fieldOf("duration").forGetter(ApplyPotionEffect::duration),
            Codec.INT.fieldOf("upkeep_cost").forGetter(ApplyPotionEffect::upkeep)
    ).apply(builder, ApplyPotionEffect::new));

    @Override
    public boolean isActivatable() {
        return true;
    }

    @Override
    public int activeTick(ItemStack sigil, Level level, Player player) {
        player.addEffect(new MobEffectInstance(effect, duration, amplifier));
        return upkeep;
    }

    @Override
    public int useOnEntity(ItemStack sigil, Player player, LivingEntity target) {
        // TODO allow giving effect to entities? could be used offensively as well with negative effects maybe
        return SigilEffect.super.useOnEntity(sigil, player, target);
    }

    @Override
    public MapCodec<? extends SigilEffect> codec() {
        return CODEC;
    }
}
