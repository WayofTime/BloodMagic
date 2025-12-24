package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.api.living.LivingValueEffect;

public record ValueBasedExp(Holder<LivingUpgrade> upgrade, boolean victim) implements LivingValueEffect {
    public static final MapCodec<ValueBasedExp> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LivingUpgrade.HOLDER_CODEC.fieldOf("upgrade").forGetter(ValueBasedExp::upgrade),
            Codec.BOOL.fieldOf("victim").forGetter(ValueBasedExp::victim)
    ).apply(builder, ValueBasedExp::new));

    public static final boolean THIS_ENTITY = true;
    public static final boolean ATTACKER = false;

    @Override
    public float process(int level, LootContext lootContext, float value) {
        Player player = (Player) lootContext.getParam(victim ? LootContextParams.THIS_ENTITY : LootContextParams.ATTACKING_ENTITY);
        LivingHelper.applyExp(player, upgrade, value);
        return value;
    }

    @Override
    public MapCodec<? extends LivingValueEffect> codec() {
        return CODEC;
    }
}
