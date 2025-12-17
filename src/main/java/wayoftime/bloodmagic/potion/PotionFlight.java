package wayoftime.bloodmagic.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PotionFlight extends PotionBloodMagic {
    public PotionFlight(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    // called every tick the potion is on
    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap p_19479_, int amplifier) {
        if (entity instanceof Player player) {
            player.fallDistance = 0;
            player.getAbilities().mayfly = true;
            if (!prevFlySpeedMap.containsKey(player.getUUID()))
            {
                prevFlySpeedMap.put(player.getUUID(), player.getAbilities().getFlyingSpeed());
            }

            if (player.level().isClientSide) {
                player.getAbilities().setFlyingSpeed(getFlySpeedForFlightLevel(amplifier));
            }
            player.onUpdateAbilities();
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap p_19470_, int amplifier) {
        if (entity instanceof Player player) {
            player.getAbilities().mayfly = player.isCreative();
            player.getAbilities().flying = false;

            if (player.level().isClientSide)
            {
                player.getAbilities().setFlyingSpeed(prevFlySpeedMap.getOrDefault((player.getUUID()), getFlySpeedForFlightLevel(-1)));
                prevFlySpeedMap.remove(player.getUUID());
            }

            player.onUpdateAbilities();
        }
    }

    // moved from util/handler/event/GenericHandler.java
    public static Map<UUID, Float> prevFlySpeedMap = new HashMap<>();

    private float getFlySpeedForFlightLevel(int level) {
        if (level >= 0)
        {
            return 0.05F * (level + 1);
        } else
        {
            // Default fly speed
            return 0.05F;
        }
    }
}
