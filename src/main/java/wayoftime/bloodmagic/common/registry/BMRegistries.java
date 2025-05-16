package wayoftime.bloodmagic.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import wayoftime.bloodmagic.BloodMagic;

public class BMRegistries {
    public static class Keys {
        public static final ResourceKey<Registry<AltarTier>> ALTAR_TIER_KEY = ResourceKey.createRegistryKey(bm("altar_tier"));
    }

    private static void registerPack(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Keys.ALTAR_TIER_KEY, AltarTier.CODEC);
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(BMRegistries::registerPack);
    }

    private static ResourceLocation bm(String path) {
        return ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, path);
    }
}
