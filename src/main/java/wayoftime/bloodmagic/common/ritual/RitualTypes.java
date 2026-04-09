package wayoftime.bloodmagic.common.ritual;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.ritual.Ritual;
import wayoftime.bloodmagic.common.ritual.types.SpawnFluidType;

import java.util.function.Supplier;

public class RitualTypes {
    public static final DeferredRegister<MapCodec<? extends Ritual>> RITUAL_TYPES = DeferredRegister.create(BMIdentifiers.RegistryKeys.RITUAL_TYPES, BloodMagic.MODID);

    public static final Supplier<MapCodec<SpawnFluidType>> FLUID_SPAWN = RITUAL_TYPES.register("fluid_spawn", () -> SpawnFluidType.CODEC);

    public static void register(IEventBus modBus) {
        RITUAL_TYPES.makeRegistry(builder -> {});
        RITUAL_TYPES.register(modBus);
    }
}
