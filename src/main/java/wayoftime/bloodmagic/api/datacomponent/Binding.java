package wayoftime.bloodmagic.api.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import wayoftime.bloodmagic.util.BlockEntityHelper;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

public record Binding(UUID uuid, String name) implements TooltipProvider {
    public static final Codec<Binding> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("uuid").forGetter(Binding::uuid),
                    Codec.STRING.fieldOf("name").forGetter(Binding::name)
            ).apply(instance, Binding::new)
    );

    public static final StreamCodec<ByteBuf, Binding> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, Binding::uuid,
            ByteBufCodecs.STRING_UTF8, Binding::name,
            Binding::new
    );

    private static final UUID NONE = UUID.nameUUIDFromBytes("NOPLAYER: EMPTY NONAME".getBytes(StandardCharsets.UTF_8));
    public boolean isEmpty() {
        return this == EMPTY || (this.uuid == NONE && this.name.isEmpty());
    }

    public static final Binding EMPTY = new Binding(NONE, "");

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
            if (this.isEmpty()) {
                tooltip.accept(BlockEntityHelper.translatableHover("tooltip.bloodmagic.no_owner"));
            } else {
                tooltip.accept(BlockEntityHelper.translatableHover("tooltip.bloodmagic.current_owner", this.name));
            }
    }
}
