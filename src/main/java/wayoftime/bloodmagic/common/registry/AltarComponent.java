package wayoftime.bloodmagic.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs.TagOrElementLocation;
import net.minecraft.util.ExtraCodecs;

public record AltarComponent(BlockPos pos, TagOrElementLocation material, boolean isUpgrade) {
    public static final Codec<AltarComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(AltarComponent::pos),
            ExtraCodecs.TAG_OR_ELEMENT_ID.fieldOf("valid").forGetter(AltarComponent::material),
            Codec.BOOL.fieldOf("upgrade").forGetter(AltarComponent::isUpgrade)
    ).apply(instance, AltarComponent::new));
}
