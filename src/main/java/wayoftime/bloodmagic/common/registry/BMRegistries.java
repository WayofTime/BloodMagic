package wayoftime.bloodmagic.common.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers.RegistryKeys;
import wayoftime.bloodmagic.common.living.LivingEffectComponents;
import wayoftime.bloodmagic.common.living.LivingEntityEffect;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.living.LivingValueEffect;
import wayoftime.bloodmagic.common.ritual.imperfect.ImperfectRitualEffect;
import wayoftime.bloodmagic.api.sigil.SigilType;
import wayoftime.bloodmagic.common.sigil.SigilEffects;

public class BMRegistries {
    public static class Keys {
        public static final ResourceKey<Registry<AltarTier>> ALTAR_TIER_KEY = ResourceKey.createRegistryKey(bm("altar_tier"));

        public static final ResourceKey<Registry<LivingUpgrade>> LIVING_UPGRADES = ResourceKey.createRegistryKey(bm("living_upgrades"));
        public static final ResourceKey<Registry<DataComponentType<?>>> LIVING_EFFECT_COMPONENTS = ResourceKey.createRegistryKey(bm("living_effect_component"));
        public static final ResourceKey<Registry<MapCodec<? extends LivingValueEffect>>> VALUE_BASED_EFFECT_TYPE = ResourceKey.createRegistryKey(bm("value_based_effect_type"));
        public static final ResourceKey<Registry<MapCodec<? extends LivingEntityEffect>>> ENTITY_EFFECT_TYPE = ResourceKey.createRegistryKey(bm("entity_effect_type"));

        public static final ResourceKey<Registry<ImperfectRitualEffect>> IMPERFECT_RITUALS = ResourceKey.createRegistryKey(bm("imperfect_rituals"));
        public static final ResourceKey<Registry<MapCodec<? extends ImperfectRitualEffect>>> IMPERFECT_RITUAL_EFFECT_TYPE = ResourceKey.createRegistryKey(bm("imperfect_ritual_effect_types"));
    }

    private static void registerPack(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Keys.ALTAR_TIER_KEY, AltarTier.CODEC);
        event.dataPackRegistry(
                Keys.LIVING_UPGRADES,
                LivingUpgrade.CODEC,
                LivingUpgrade.CLIENT_CODEC,
                builder -> builder.sync(true)
        );
        event.dataPackRegistry(
                Keys.IMPERFECT_RITUALS,
                ImperfectRitualEffect.CODEC,
                ImperfectRitualEffect.CODEC,
                builder -> builder.sync(true)
        );
        event.dataPackRegistry(
                RegistryKeys.SIGIL_TYPES,
                SigilType.CODEC,
                SigilType.CODEC,
                builder -> builder.sync(true)
        );
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(BMRegistries::registerPack);

        LivingEffectComponents.register(modBus);
        LivingValueEffect.register(modBus);
        LivingEntityEffect.register(modBus);
        ImperfectRitualEffect.register(modBus);
        SigilEffects.register(modBus);
    }

    private static ResourceLocation bm(String path) {
        return ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, path);
    }
}
