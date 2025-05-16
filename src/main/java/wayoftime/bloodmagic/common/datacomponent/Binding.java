package wayoftime.bloodmagic.common.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import wayoftime.bloodmagic.util.helper.BlockEntityHelper;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record Binding(UUID uuid, String name) {
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

    public Component getHoverText() {
        return BlockEntityHelper.translatableHover("tooltip.bloodmagic.current_owner", this.name);
    }
}
