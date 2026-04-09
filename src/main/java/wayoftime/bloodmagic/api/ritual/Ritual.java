package wayoftime.bloodmagic.api.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import wayoftime.bloodmagic.common.blockentity.MasterRitualTile;
import wayoftime.bloodmagic.common.ritual.RitualTypes;

import java.util.HashMap;
import java.util.function.Function;

public interface Ritual {
    Codec<Ritual> CODEC = Codec.lazyInitialized(() -> RitualTypes.RITUAL_TYPES
            .getRegistry()
            .get()
            .byNameCodec()
            .dispatch(Ritual::codec, Function.identity())
    );

    int getRefreshTicks(MasterRitualTile mrs);

    int getActivationCost();

    // return false to terminate ritual
    boolean perform(MasterRitualTile mrs);

    HashMap<String, Range> getDefaultRanges(MasterRitualTile mrs);

    MapCodec<? extends Ritual> codec();
}
