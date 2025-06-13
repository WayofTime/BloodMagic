package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.dataattachment.BMDataAttachments;
import wayoftime.bloodmagic.common.living.LivingEntityEffect;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;

import java.util.HashMap;
import java.util.Map;

public record DistanceExpGain(Holder<LivingUpgrade> upgrade, Movement movement) implements LivingEntityEffect {
    public static final MapCodec<DistanceExpGain> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LivingUpgrade.HOLDER_CODEC.fieldOf("upgrade").forGetter(DistanceExpGain::upgrade),
            Movement.CODEC.fieldOf("movement").forGetter(DistanceExpGain::movement)
    ).apply(builder, DistanceExpGain::new));


    private static final ResourceLocation X = ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, "x");
    private static final ResourceLocation Y = ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, "y");
    private static final ResourceLocation Z = ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, "z");

    @Override
    public void apply(int upgradeLevel, Entity entity) {
        Player wearer = (Player) entity;

        double x = wearer.getX();
        double y = wearer.getY();
        double z = wearer.getZ();

        Map<ResourceLocation, Double> data = wearer.getData(BMDataAttachments.LIVING_ADDITIONAL);
        double oldX = data.getOrDefault(X, x);
        double oldY = data.getOrDefault(Y, y);
        double oldZ = data.getOrDefault(Z, z);

        data.put(X, x);
        data.put(Y, y);
        data.put(Z, z);

        wearer.setData(BMDataAttachments.LIVING_ADDITIONAL, data);

        double amount = switch (movement) {
            case HORIZONTAL -> Math.sqrt(Math.abs(x - oldX) * Math.abs(x - oldX) + Math.abs(z - oldZ) * Math.abs(z - oldZ));
            case VERTICAL -> y - oldY;
        };

        if (amount > 0 && amount < 50) {
            LivingHelper.applyExp(wearer, upgrade, (float) amount);
        }
    }

    @Override
    public MapCodec<? extends LivingEntityEffect> codec() {
        return CODEC;
    }

    public enum Movement {
        HORIZONTAL,
        VERTICAL;
        /*
        X,
        Y,
        Z,
        ALL
         */

        public static final Codec<Movement> CODEC = Codec.stringResolver(Movement::toString, Movement::valueOf);
    }
}
