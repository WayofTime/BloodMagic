package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import wayoftime.bloodmagic.common.dataattachment.BMDataAttachments;
import wayoftime.bloodmagic.common.living.LivingEntityEffect;

import java.util.Map;

public record CooldownEffect(ResourceLocation id) implements LivingEntityEffect {
    public static final MapCodec<CooldownEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(CooldownEffect::id)
    ).apply(builder, CooldownEffect::new));

    @Override
    public void apply(int upgradeLevel, Entity entity) {
        Map<ResourceLocation, Double> data = entity.getData(BMDataAttachments.LIVING_ADDITIONAL.get());
        data.compute(id, (key, amount) -> amount == null ? 20 * 60 : Math.max(amount - 1, 0));
        entity.setData(BMDataAttachments.LIVING_ADDITIONAL.get(), data);
    }

    @Override
    public MapCodec<? extends LivingEntityEffect> codec() {
        return CODEC;
    }
}
