package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import wayoftime.bloodmagic.common.living.LivingEntityEffect;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;

public record EntityBasedExp(Holder<LivingUpgrade> upgrade) implements LivingEntityEffect {
    public static final MapCodec<EntityBasedExp> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LivingUpgrade.HOLDER_CODEC.fieldOf("upgrade").forGetter(EntityBasedExp::upgrade)
    ).apply(builder, EntityBasedExp::new));

    @Override
    public void apply(int upgradeLevel, Entity entity) {
        LivingHelper.applyExp((Player) entity, upgrade, 1);
    }

    @Override
    public MapCodec<? extends LivingEntityEffect> codec() {
        return CODEC;
    }
}
