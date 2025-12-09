package wayoftime.bloodmagic.datagen.content;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.common.ritual.weak.ImperfectRitualEffect;

import java.util.function.Function;

public class ImperfectRituals {
    public static void effects(BootstrapContext<ImperfectRitualEffect> context) {

    }

    public static void dataMap(Function<DataMapType<Block, ResourceLocation>, DataMapProvider.Builder<ResourceLocation, Block>> setup) {
        setup.apply(BMDataMaps.WEAK_RITUAL_CATALYST)
                .add(Tags.Blocks.STORAGE_BLOCKS_LAPIS, BMIdentifiers.WeakRituals.NIGHT_RITUAL, false)
                .add(Tags.Blocks.STORAGE_BLOCKS_COAL, BMIdentifiers.WeakRituals.DAY_RITUAL, false)
                .add(Blocks.BEDROCK.builtInRegistryHolder(), BMIdentifiers.WeakRituals.RESISTANCE_RITUAL, false)
                .add(Tags.Blocks.COBBLESTONES, BMIdentifiers.WeakRituals.ZOMBIE_RITUAL, false)
                .build();
    }
}
