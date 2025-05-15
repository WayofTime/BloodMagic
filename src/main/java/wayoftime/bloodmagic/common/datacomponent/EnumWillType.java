package wayoftime.bloodmagic.common.datacomponent;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.StringUtils;

public enum EnumWillType implements StringRepresentable {

    DEFAULT("default"),
    CORROSIVE("corrosive"),
    DESTRUCTIVE("destructive"),
    STEADFAST("steadfast"),
    VENGEFUL("vengeful");

    private final String name;
    EnumWillType(String name) {
        this.name = name;
    }

    public String toCapitalized() {
        return StringUtils.capitalize(this.name);
    }

    public static final Codec<EnumWillType> CODEC = StringRepresentable.fromEnum(EnumWillType::values);

    public static final StreamCodec<ByteBuf, EnumWillType> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    @Override
    public String getSerializedName() {
        return name;
    }
}
