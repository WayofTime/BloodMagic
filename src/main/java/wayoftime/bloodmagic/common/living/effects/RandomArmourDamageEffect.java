package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import wayoftime.bloodmagic.api.living.LivingEntityEffect;

import java.util.ArrayList;
import java.util.List;

public record RandomArmourDamageEffect(LevelBasedValue amounts) implements LivingEntityEffect {
    public static final MapCodec<RandomArmourDamageEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("amounts").forGetter(RandomArmourDamageEffect::amounts)
    ).apply(builder, RandomArmourDamageEffect::new));

    @Override
    public void apply(int upgradeLevel, Entity entity) {
        List<ItemStack> stacks = new ArrayList<>();
        ((LivingEntity) entity).getArmorSlots().forEach(stacks::add);
        ItemStack chosen = stacks.get(entity.level().random.nextInt(stacks.size()));
        chosen.setDamageValue(chosen.getDamageValue() + (int) amounts.calculate(upgradeLevel));
    }

    @Override
    public MapCodec<? extends LivingEntityEffect> codec() {
        return CODEC;
    }
}
