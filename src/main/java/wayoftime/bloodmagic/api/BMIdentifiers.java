package wayoftime.bloodmagic.api;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.ritual.Range;
import wayoftime.bloodmagic.api.ritual.Ritual;
import wayoftime.bloodmagic.api.ritual.RitualStructure;
import wayoftime.bloodmagic.api.ritual.imperfect.ImperfectRitualEffect;
import wayoftime.bloodmagic.api.sigil.SigilEffect;
import wayoftime.bloodmagic.api.living.LivingEntityEffect;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.api.living.LivingValueEffect;
import wayoftime.bloodmagic.api.altar.AltarTier;

public class BMIdentifiers {
    public static class ImperfectRituals {
        public static final ResourceLocation DAY_RITUAL = bm("day");
        public static final ResourceLocation NIGHT_RITUAL = bm("night");
        public static final ResourceLocation RESISTANCE_RITUAL = bm("resistance");
        public static final ResourceLocation ZOMBIE_RITUAL = bm("zombie");
    }

    public static class RegistryKeys {
        public static final ResourceKey<Registry<MapCodec<? extends SigilEffect>>> SIGIL_EFFECT_TYPES = ResourceKey.createRegistryKey(bm("sigil_effect_type"));
        public static final ResourceKey<Registry<SigilEffect>> SIGIL_EFFECT = ResourceKey.createRegistryKey(bm("sigil"));

        public static final ResourceKey<Registry<AltarTier>> ALTAR_TIER_KEY = ResourceKey.createRegistryKey(bm("altar_tier"));

        public static final ResourceKey<Registry<LivingUpgrade>> LIVING_UPGRADES = ResourceKey.createRegistryKey(bm("living_upgrades"));
        public static final ResourceKey<Registry<DataComponentType<?>>> LIVING_EFFECT_COMPONENTS = ResourceKey.createRegistryKey(bm("living_effect_component"));
        public static final ResourceKey<Registry<MapCodec<? extends LivingValueEffect>>> VALUE_BASED_EFFECT_TYPE = ResourceKey.createRegistryKey(bm("value_based_effect_type"));
        public static final ResourceKey<Registry<MapCodec<? extends LivingEntityEffect>>> ENTITY_EFFECT_TYPE = ResourceKey.createRegistryKey(bm("entity_effect_type"));

        public static final ResourceKey<Registry<ImperfectRitualEffect>> IMPERFECT_RITUALS = ResourceKey.createRegistryKey(bm("imperfect_rituals"));
        public static final ResourceKey<Registry<MapCodec<? extends ImperfectRitualEffect>>> IMPERFECT_RITUAL_EFFECT_TYPE = ResourceKey.createRegistryKey(bm("imperfect_ritual_effect_types"));

        public static final ResourceKey<Registry<MapCodec<? extends Range>>> RITUAL_RANGE_TYPES = ResourceKey.createRegistryKey(bm("ritual_ranges"));
        public static final ResourceKey<Registry<MapCodec<? extends Ritual>>> RITUAL_TYPES = ResourceKey.createRegistryKey(bm("ritual_types"));
        public static final ResourceKey<Registry<Ritual>> RITUALS = ResourceKey.createRegistryKey(bm("rituals"));
        public static final ResourceKey<Registry<RitualStructure>> RITUAL_STRUCTURES = ResourceKey.createRegistryKey(bm("ritual_structures"));
    }

    public static class Rituals {
        public static final ResourceKey<Ritual> WATER = ResourceKey.create(RegistryKeys.RITUALS, bm("water"));
        public static final ResourceKey<Ritual> LAVA = ResourceKey.create(RegistryKeys.RITUALS, bm("lava"));
    }

    public static class RitualStructures {
        public static final ResourceKey<RitualStructure> WATER = ResourceKey.create(RegistryKeys.RITUAL_STRUCTURES, fromRitual(Rituals.WATER));
        public static final ResourceKey<RitualStructure> LAVA = ResourceKey.create(RegistryKeys.RITUAL_STRUCTURES, fromRitual(Rituals.LAVA));

        private static ResourceLocation fromRitual(ResourceKey<Ritual> key) {
            return key.location();
        }
    }

    public static class Sigils {
        public static final ResourceKey<SigilEffect> DIVINATION = ResourceKey.create(RegistryKeys.SIGIL_EFFECT, bm("divination"));
        public static final ResourceKey<SigilEffect> SEER = ResourceKey.create(RegistryKeys.SIGIL_EFFECT, bm("seer"));
        public static final ResourceKey<SigilEffect> LAVA = ResourceKey.create(RegistryKeys.SIGIL_EFFECT, bm("lava"));
        public static final ResourceKey<SigilEffect> WATER = ResourceKey.create(RegistryKeys.SIGIL_EFFECT, bm("water"));
        public static final ResourceKey<SigilEffect> VOID = ResourceKey.create(RegistryKeys.SIGIL_EFFECT, bm("void"));
        public static final ResourceKey<SigilEffect> MINER = ResourceKey.create(RegistryKeys.SIGIL_EFFECT, bm("miner"));
    }

    public static class Upgrades {
        // Downgrades
        public static final ResourceKey<LivingUpgrade> BATTLE_HUNGRY = key("battle_hungry");
        public static final ResourceKey<LivingUpgrade> CRIPPLED_ARM = key("crippled_arm");
        public static final ResourceKey<LivingUpgrade> DIG_SLOWDOWN = key("dig_slowdown");
        public static final ResourceKey<LivingUpgrade> MELEE_DECREASE = key("melee_decrease");
        public static final ResourceKey<LivingUpgrade> QUENCHED = key("quenched");
        public static final ResourceKey<LivingUpgrade> SLOW_HEAL = key("slow_heal");
        public static final ResourceKey<LivingUpgrade> SPEED_DECREASE = key("speed_decrease");
        public static final ResourceKey<LivingUpgrade> STORM_TROOPER = key("storm_trooper");
        public static final ResourceKey<LivingUpgrade> SWIM_DECREASE = key("swim_decrease");

        // Upgrades
        public static final ResourceKey<LivingUpgrade> ARROW_PROTECT = key("arrow_protect");
        public static final ResourceKey<LivingUpgrade> CURIOS_SOCKET = key("curios_socket");
        public static final ResourceKey<LivingUpgrade> NETHERITE_PROTECT = key("netherite_protect");
        public static final ResourceKey<LivingUpgrade> DIGGING = key("digging");
        public static final ResourceKey<LivingUpgrade> ELYTRA = key("elytra");
        public static final ResourceKey<LivingUpgrade> EXPERIENCED = key("experienced");
        public static final ResourceKey<LivingUpgrade> FALL_PROTECT = key("fall_protect");
        public static final ResourceKey<LivingUpgrade> FIRE_RESIST = key("fire_resist");
        public static final ResourceKey<LivingUpgrade> GILDED = key("gilded");
        public static final ResourceKey<LivingUpgrade> HEALTH = key("health");
        public static final ResourceKey<LivingUpgrade> JUMP = key("jump");
        public static final ResourceKey<LivingUpgrade> KNOCKBACK_RESIST = key("knockback_resist");
        public static final ResourceKey<LivingUpgrade> MELEE_DAMAGE = key("melee_damage");
        public static final ResourceKey<LivingUpgrade> PHYSICAL_PROTECT = key("physical_protect");
        public static final ResourceKey<LivingUpgrade> POISON_RESIST = key("poison_resist");
        public static final ResourceKey<LivingUpgrade> REPAIR = key("repair");
        public static final ResourceKey<LivingUpgrade> SELF_SACRIFICE = key("self_sacrifice");
        public static final ResourceKey<LivingUpgrade> SPEED = key("speed");
        public static final ResourceKey<LivingUpgrade> SPRINT_ATTACK = key("sprint_attack");

        public static final ResourceKey<LivingUpgrade> LUCK = key("luck");

        private static ResourceKey<LivingUpgrade> key(String name) {
            return ResourceKey.create(RegistryKeys.LIVING_UPGRADES, bm(name));
        }
    }

    public static class DamageTypes {
        public static final ResourceKey<DamageType> SACRIFICE = key("sacrifice"); // used by soul network to forcibly receive needed LP. TODO Potentially by Dagger of Sacrifice?
        public static final ResourceKey<DamageType> SELF_SACRIFICE = key("self_sacrifice");

        private static ResourceKey<DamageType> key(String path) {
            return ResourceKey.create(Registries.DAMAGE_TYPE, BloodMagic.rl(path));
        }
    }

    public static class ItemProperties {
        public static final ResourceLocation SIGIL_ACTIVE = bm("sigil_active");
    }

    public static class ModelLoaders {
        public static final ResourceLocation SIGILS = bm("sigil_loader");
    }

    public static class ModelLocations {
        public static final ModelResourceLocation DIVINATION = fromSigilKey(Sigils.DIVINATION);
        public static final ModelResourceLocation SEER = fromSigilKey(Sigils.SEER);
        public static final ModelResourceLocation LAVA = fromSigilKey(Sigils.LAVA);
        public static final ModelResourceLocation WATER = fromSigilKey(Sigils.WATER);
        public static final ModelResourceLocation VOID = fromSigilKey(Sigils.VOID);
        public static final ModelResourceLocation MINER = fromSigilKey(Sigils.MINER);

        public static ModelResourceLocation fromSigilKey(ResourceKey<SigilEffect> key) {
            return ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(key.location().getNamespace(), "item/sigil_" + key.location().getPath()));
        }
    }

    private static ResourceLocation bm(String path) {
        return BloodMagic.rl(path);
    }
}
