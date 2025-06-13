package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import wayoftime.bloodmagic.common.dataattachment.BMDataAttachments;
import wayoftime.bloodmagic.common.living.LivingEntityEffect;

import java.util.Map;
import java.util.Optional;

public record ResetCooldownEffect(ResourceLocation id, LevelBasedValue amounts, Optional<LivingEntityEffect> effect) implements LivingEntityEffect {
    public static final MapCodec<ResetCooldownEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(ResetCooldownEffect::id),
            LevelBasedValue.CODEC.fieldOf("amounts").forGetter(ResetCooldownEffect::amounts),
            LivingEntityEffect.CODEC.optionalFieldOf("reset_effect").forGetter(ResetCooldownEffect::effect)
    ).apply(builder, ResetCooldownEffect::new));

    @Override
    public void apply(int upgradeLevel, Entity entity) {
        Map<ResourceLocation, Double> data = entity.getData(BMDataAttachments.LIVING_ADDITIONAL);
        data.compute(id, (key, amount) -> (double) amounts.calculate(upgradeLevel));
        entity.setData(BMDataAttachments.LIVING_ADDITIONAL, data);
        effect.ifPresent(livingEntityEffect -> livingEntityEffect.apply(upgradeLevel, entity));
    }

    @Override
    public MapCodec<? extends LivingEntityEffect> codec() {
        return CODEC;
    }
}
