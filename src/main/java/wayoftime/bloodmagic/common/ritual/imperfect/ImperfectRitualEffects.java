package wayoftime.bloodmagic.common.ritual.imperfect;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.ritual.imperfect.ImperfectRitualEffect;

import java.util.function.Supplier;

public class ImperfectRitualEffects {
    public static final DeferredRegister<MapCodec<? extends ImperfectRitualEffect>> IMPERFECT_RITUAL_EFFECT_TYPE = DeferredRegister.create(BMIdentifiers.RegistryKeys.IMPERFECT_RITUAL_EFFECT_TYPE, BloodMagic.MODID);

    public static final Supplier<MapCodec<SetTimeEffect>> SET_TIME_EFFECT = IMPERFECT_RITUAL_EFFECT_TYPE.register("set_time", () -> SetTimeEffect.CODEC);
    public static final Supplier<MapCodec<ApplyPotionEffect>> APPLY_POTION_EFFECT = IMPERFECT_RITUAL_EFFECT_TYPE.register("apply_potion", () -> ApplyPotionEffect.CODEC);
    public static final Supplier<MapCodec<SpawnMobEffect>> SPAWN_MOB_EFFECT = IMPERFECT_RITUAL_EFFECT_TYPE.register("spawn_mob", () -> SpawnMobEffect.CODEC);

    public static void register(IEventBus modBus) {
        IMPERFECT_RITUAL_EFFECT_TYPE.makeRegistry(builder -> {});
        IMPERFECT_RITUAL_EFFECT_TYPE.register(modBus);
    }

}
