package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.registry.BMRegistries;
import wayoftime.bloodmagic.datagen.content.AltarTiers;
import wayoftime.bloodmagic.datagen.content.BloodyDamageSources;
import wayoftime.bloodmagic.datagen.content.LivingUpgrades;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BMDataPackProvider extends DatapackBuiltinEntriesProvider {
    public BMDataPackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(
                output,
                registries,
                new RegistrySetBuilder()
                        .add(Registries.DAMAGE_TYPE, BloodyDamageSources::bootstrap)
                        .add(BMRegistries.Keys.ALTAR_TIER_KEY, AltarTiers::bootstrap)
                        .add(BMRegistries.Keys.LIVING_UPGRADES, LivingUpgrades::bootstrap),
                Set.of(BloodMagic.MODID)
        );
    }
}
