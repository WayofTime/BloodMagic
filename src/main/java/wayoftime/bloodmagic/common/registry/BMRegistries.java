package wayoftime.bloodmagic.common.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import wayoftime.bloodmagic.api.BMIdentifiers.RegistryKeys;
import wayoftime.bloodmagic.api.altar.AltarTier;
import wayoftime.bloodmagic.api.ritual.RitualStructure;
import wayoftime.bloodmagic.api.ritual.imperfect.ImperfectRitualEffect;
import wayoftime.bloodmagic.api.sigil.SigilEffect;
import wayoftime.bloodmagic.common.living.LivingEffectComponents;
import wayoftime.bloodmagic.common.living.LivingEntityEffects;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.living.LivingValueEffects;
import wayoftime.bloodmagic.common.ritual.Ranges;
import wayoftime.bloodmagic.common.ritual.RitualTypes;
import wayoftime.bloodmagic.common.ritual.imperfect.ImperfectRitualEffects;
import wayoftime.bloodmagic.common.sigil.SigilEffects;

public class BMRegistries {

    private static void registerPack(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(RegistryKeys.ALTAR_TIER_KEY, AltarTier.CODEC);
        event.dataPackRegistry(
                RegistryKeys.LIVING_UPGRADES,
                LivingUpgrade.CODEC,
                LivingUpgrade.CLIENT_CODEC,
                builder -> builder.sync(true)
        );
        event.dataPackRegistry(
                RegistryKeys.IMPERFECT_RITUALS,
                ImperfectRitualEffect.CODEC,
                ImperfectRitualEffect.CODEC,
                builder -> builder.sync(true)
        );
        event.dataPackRegistry(
                RegistryKeys.SIGIL_EFFECT,
                SigilEffect.CODEC,
                SigilEffect.CODEC,
                builder -> builder.sync(true)
        );
        event.dataPackRegistry(
                RegistryKeys.RITUAL_STRUCTURES,
                RitualStructure.CODEC,
                RitualStructure.CODEC,
                builder -> builder.sync(true)
        );
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(BMRegistries::registerPack);

        LivingEffectComponents.register(modBus);
        LivingValueEffects.register(modBus);
        LivingEntityEffects.register(modBus);
        ImperfectRitualEffects.register(modBus);
        SigilEffects.register(modBus);
        Ranges.register(modBus);
        RitualTypes.register(modBus);
    }
}
