package wayoftime.bloodmagic;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import wayoftime.bloodmagic.api.BMIdentifiers.RegistryKeys;
import wayoftime.bloodmagic.datagen.content.AltarTiers;
import wayoftime.bloodmagic.datagen.content.BMDamageTypes;
import wayoftime.bloodmagic.datagen.content.ImperfectRitualData;
import wayoftime.bloodmagic.datagen.content.LivingUpgrades;
import wayoftime.bloodmagic.datagen.content.SigilData;
import wayoftime.bloodmagic.datagen.provider.*;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new BMItemModelProvider(output, fileHelper));
        generator.addProvider(event.includeClient(), new BMBlockstateProvider(output, fileHelper));

        event.createProvider(BMLanguageProvider::new);

        event.createDatapackRegistryObjects(new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, BMDamageTypes::types)
                .add(RegistryKeys.ALTAR_TIER_KEY, AltarTiers::tiers)
                .add(RegistryKeys.LIVING_UPGRADES, LivingUpgrades::upgrades)
                .add(RegistryKeys.IMPERFECT_RITUALS, ImperfectRitualData::effects)
                .add(RegistryKeys.SIGIL_EFFECT, SigilData::sigilTypes)
        );

        ProviderHelper helper = new ProviderHelper(fileHelper);

        event.createProvider(helper.tagsFor(RegistryKeys.LIVING_UPGRADES, LivingUpgrades::tags));
        event.createProvider(helper.tagsFor(Registries.DAMAGE_TYPE, BMDamageTypes::tags));
        event.createBlockAndItemTags(BMBlockTagProvider::new, BMItemTagProvider::new);

        event.createProvider(BMDataMapProvider::new);

        event.createProvider(BMLootTableProvider::new);
    }
}
