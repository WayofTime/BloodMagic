package wayoftime.bloodmagic;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import wayoftime.bloodmagic.common.registry.BMRegistries;
import wayoftime.bloodmagic.datagen.content.AltarTiers;
import wayoftime.bloodmagic.datagen.content.BloodyDamageSources;
import wayoftime.bloodmagic.datagen.provider.*;


import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new BMLanguageProvider(output, "en_us"));
        generator.addProvider(event.includeClient(), new BMItemModelProvider(output, fileHelper));
        generator.addProvider(event.includeClient(), new BMBlockstateProvider(output, fileHelper));

        generator.addProvider(event.includeServer(), new BMDataMapProvider(output, registries));

        BMDataPackProvider dataPack = generator.addProvider(event.includeServer(), new BMDataPackProvider(output, registries));
        BMTagsProvider tags = new BMTagsProvider(output, dataPack.getRegistryProvider(), fileHelper);
        generator.addProvider(event.includeServer(), tags.setup(BMRegistries.Keys.ALTAR_TIER_KEY, AltarTiers::tags));
        generator.addProvider(event.includeServer(), tags.setup(Registries.DAMAGE_TYPE, BloodyDamageSources::tags));

        BMBlockTagProvider blockTags = generator.addProvider(event.includeServer(), new BMBlockTagProvider(output, registries, fileHelper));
        generator.addProvider(event.includeServer(), new BMItemTagProvider(output, registries, blockTags.contentsGetter(), fileHelper));

        generator.addProvider(event.includeServer(), new BMLootTableProvider(output, registries));
    }
}
