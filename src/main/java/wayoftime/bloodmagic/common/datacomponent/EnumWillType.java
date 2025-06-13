package wayoftime.bloodmagic.common.datacomponent;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.function.IntFunction;

public enum EnumWillType implements StringRepresentable {

    DEFAULT,
    CORROSIVE,
    DESTRUCTIVE,
    STEADFAST,
    VENGEFUL;

    public static final IntFunction<EnumWillType> BY_ID = ByIdMap.continuous(
            EnumWillType::ordinal,
            EnumWillType.values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
    );

    public static final Codec<EnumWillType> CODEC = StringRepresentable.fromEnum(EnumWillType::values);
    public static final StreamCodec<ByteBuf, EnumWillType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, EnumWillType::ordinal);

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String toCapitalized() {
        return StringUtils.capitalize(this.getSerializedName());
    }
}
