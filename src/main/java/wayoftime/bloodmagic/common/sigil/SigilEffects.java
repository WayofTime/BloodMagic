package wayoftime.bloodmagic.common.sigil;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.sigil.SigilEffect;

import java.util.function.Supplier;

public class SigilEffects {
    public static final DeferredRegister<MapCodec<? extends SigilEffect>> SIGIL_EFFECT_TYPE = DeferredRegister.create(BMIdentifiers.RegistryKeys.SIGIL_EFFECT_TYPES, BloodMagic.MODID);

    public static final Supplier<MapCodec<ApplyPotionEffect>> APPLY_POTION_EFFECT = SIGIL_EFFECT_TYPE.register("apply_potion_effect", () -> ApplyPotionEffect.CODEC);
    public static final Supplier<MapCodec<DivinationEffect>> DIVINATION_EFFECT = SIGIL_EFFECT_TYPE.register("divination_effect", () -> DivinationEffect.CODEC);
    public static final Supplier<MapCodec<FluidPlaceEffect>> FLUID_PLACE_EFFECT = SIGIL_EFFECT_TYPE.register("fluid_place_effect", () -> FluidPlaceEffect.CODEC);
    public static final Supplier<MapCodec<FluidRemoveEffect>> FLUID_REMOVE_EFFECT = SIGIL_EFFECT_TYPE.register("fluid_remove_effect", () -> FluidRemoveEffect.CODEC);

    public static void register(IEventBus modBus) {
        SIGIL_EFFECT_TYPE.makeRegistry(builder -> {});
        SIGIL_EFFECT_TYPE.register(modBus);
    }


}
