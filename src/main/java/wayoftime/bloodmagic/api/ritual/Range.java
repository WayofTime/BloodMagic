package wayoftime.bloodmagic.api.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import wayoftime.bloodmagic.common.ritual.Ranges;

import java.util.function.Function;

public interface Range {
    Codec<Range> CODEC = Codec.lazyInitialized(() -> Ranges.RITUAL_RANGE_TYPES
            .getRegistry()
            .get()
            .byNameCodec()
            .dispatch(Range::codec, Function.identity())
    );

    void reset();

    Boolean hasNext();

    BlockPos next();

    BlockPos getFirst();

    MapCodec<? extends Range> codec();
}
