package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.living.LivingEntityEffect;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;

public record ItemDamageBasedExpGain(Holder<LivingUpgrade> upgrade) implements LivingEntityEffect {
    public static final MapCodec<ItemDamageBasedExpGain> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LivingUpgrade.HOLDER_CODEC.fieldOf("upgrade").forGetter(ItemDamageBasedExpGain::upgrade)
    ).apply(builder, ItemDamageBasedExpGain::new));

    @Override
    public void apply(int upgradeLevel, Entity entity) {
        Player wearer = (Player) entity;
        ItemStack chestStack = LivingHelper.getChest(wearer);
        if (chestStack.has(BMDataComponents.PREVIOUS_DAMAGE)) {
            Integer prev = chestStack.get(BMDataComponents.PREVIOUS_DAMAGE);
            int delta = prev - chestStack.getDamageValue();
            if (delta > 0) {
                LivingHelper.applyExp(wearer, upgrade, delta);
            }
            if (delta != 0) {
                chestStack.set(BMDataComponents.PREVIOUS_DAMAGE, chestStack.getDamageValue());
            }
        } else {
            chestStack.set(BMDataComponents.PREVIOUS_DAMAGE, chestStack.getDamageValue());
        }
    }

    @Override
    public MapCodec<? extends LivingEntityEffect> codec() {
        return CODEC;
    }
}
