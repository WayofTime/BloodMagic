package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.common.living.LivingEntityEffect;

public record MovementModifier(LevelBasedValue amounts) implements LivingEntityEffect {
    public static final MapCodec<MovementModifier> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("amounts").forGetter(MovementModifier::amounts)
    ).apply(builder, MovementModifier::new));

    @Override
    public void apply(int upgradeLevel, Entity entity) {
        Vec3 delta = entity.getDeltaMovement();
        ServerLevel level = (ServerLevel) entity.level();
        double variation = amounts.calculate(upgradeLevel) * Math.sqrt(delta.x * delta.x + delta.y * delta.y + delta.z * delta.z) * 2;
        Vec3 motion = delta.add(rand(level) * variation, rand(level) * variation, rand(level) * variation);
        entity.setDeltaMovement(motion);
    }

    private double rand(ServerLevel level) {
        return level.random.nextDouble() - 0.5;
    }

    @Override
    public MapCodec<? extends LivingEntityEffect> codec() {
        return CODEC;
    }
}
