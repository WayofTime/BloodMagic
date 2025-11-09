package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import wayoftime.bloodmagic.common.living.LivingEntityEffect;

public record CauseExhaustionEffect(LevelBasedValue amounts) implements LivingEntityEffect {
    public static final MapCodec<CauseExhaustionEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("amounts").forGetter(CauseExhaustionEffect::amounts)
    ).apply(builder, CauseExhaustionEffect::new));

    @Override
    public void apply(int upgradeLevel, Entity entity) {
        ((Player) entity).causeFoodExhaustion(amounts.calculate(upgradeLevel));
    }

    @Override
    public MapCodec<? extends LivingEntityEffect> codec() {
        return CODEC;
    }
}
