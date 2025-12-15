package wayoftime.bloodmagic.api;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.sigil.SigilEffect;
import wayoftime.bloodmagic.api.sigil.SigilType;

public class BMIdentifiers {
    public static class ImperfectRituals {
        public static final ResourceLocation DAY_RITUAL = bm("day");
        public static final ResourceLocation NIGHT_RITUAL = bm("night");
        public static final ResourceLocation RESISTANCE_RITUAL = bm("resistance");
        public static final ResourceLocation ZOMBIE_RITUAL = bm("zombie");
    }

    // TODO move all the stuff here, including from Datagen

    public static class RegistryKeys {
        public static final ResourceKey<Registry<MapCodec<? extends SigilEffect>>> SIGIL_EFFECT_TYPES = ResourceKey.createRegistryKey(bm("sigil_effect_type"));
        public static final ResourceKey<Registry<SigilType>> SIGIL_TYPES = ResourceKey.createRegistryKey(bm("sigil"));
    }

    public static class Sigils {
        public static final ResourceKey<SigilType> DIVINATION = ResourceKey.create(RegistryKeys.SIGIL_TYPES, bm("divination"));
        public static final ResourceKey<SigilType> SEER = ResourceKey.create(RegistryKeys.SIGIL_TYPES, bm("seer"));
        public static final ResourceKey<SigilType> LAVA = ResourceKey.create(RegistryKeys.SIGIL_TYPES, bm("lava"));
        public static final ResourceKey<SigilType> WATER = ResourceKey.create(RegistryKeys.SIGIL_TYPES, bm("water"));
        public static final ResourceKey<SigilType> VOID = ResourceKey.create(RegistryKeys.SIGIL_TYPES, bm("void"));
        public static final ResourceKey<SigilType> MINER = ResourceKey.create(RegistryKeys.SIGIL_TYPES, bm("miner"));
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

        public static ModelResourceLocation fromSigilKey(ResourceKey<SigilType> key) {
            return ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(key.location().getNamespace(), "item/sigil_" + key.location().getPath()));
        }
    }

    private static ResourceLocation bm(String path) {
        return BloodMagic.rl(path);
    }
}
