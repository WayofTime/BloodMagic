package wayoftime.bloodmagic.common.ritual;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.ritual.Range;
import wayoftime.bloodmagic.common.ritual.ranges.RectangleRange;

import java.util.function.Supplier;

public class Ranges {
    public static final DeferredRegister<MapCodec<? extends Range>> RITUAL_RANGE_TYPES = DeferredRegister.create(BMIdentifiers.RegistryKeys.RITUAL_RANGE_TYPES, BloodMagic.MODID);

    public static final Supplier<MapCodec<RectangleRange>> RECTANGLE = RITUAL_RANGE_TYPES.register("rectangle", () -> RectangleRange.CODEC);

    public static void register(IEventBus modBus) {
        RITUAL_RANGE_TYPES.makeRegistry(builder -> {});
        RITUAL_RANGE_TYPES.register(modBus);
    }
}
