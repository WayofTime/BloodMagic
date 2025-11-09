package wayoftime.bloodmagic.common.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.registry.BMRegistries;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.function.Consumer;

public record UpgradeTome(Holder<LivingUpgrade> upgrade, float exp) implements TooltipProvider {
    public static final Codec<UpgradeTome> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            LivingUpgrade.HOLDER_CODEC.fieldOf("upgrade").forGetter(UpgradeTome::upgrade),
            Codec.FLOAT.fieldOf("exp").forGetter(UpgradeTome::exp)
    ).apply(builder, UpgradeTome::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeTome> STREAM_CODEC = StreamCodec.composite(
            LivingUpgrade.HOLDER_STREAM_CODEC, UpgradeTome::upgrade,
            ByteBufCodecs.FLOAT, UpgradeTome::exp,
            UpgradeTome::new
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        tooltipAdder.accept(LivingHelper.getTooltip(upgrade, exp, tooltipFlag.hasShiftDown()));
    }
}
