package wayoftime.bloodmagic.datagen.content;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.material.Fluids;
import wayoftime.bloodmagic.api.ritual.Ritual;
import wayoftime.bloodmagic.api.ritual.RitualStructure;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.ritual.types.SpawnFluidType;

import wayoftime.bloodmagic.api.BMIdentifiers.Rituals;
import wayoftime.bloodmagic.api.BMIdentifiers.RitualStructures;
import wayoftime.bloodmagic.datagen.builder.RitualStructureBuilder;

import java.util.function.BiConsumer;

public class RitualData {
    public static void rituals(BootstrapContext<Ritual> context) {
        context.register(Rituals.WATER, new SpawnFluidType(Fluids.WATER, 1000, 25, 1, 500));
        context.register(Rituals.LAVA, new SpawnFluidType(Fluids.LAVA, 1000, 500, 1, 10000));
    }

    public static void structures(BootstrapContext<RitualStructure> context) {
        context.register(RitualStructures.WATER, new RitualStructureBuilder()
                .addCornerRunes(1, 0, BMBlocks.RITUAL_STONE_WATER.block().get())
                .build()
        );
        context.register(RitualStructures.LAVA, new RitualStructureBuilder()
                .addParallelRunes(1, 0, BMBlocks.RITUAL_STONE_FIRE.block().get())
                .build()
        );
    }

    public static void translations(BiConsumer<String, String> translator) {
        translator.accept("ritual.bloodmagic.water", "Ritual of the Full Spring");
        translator.accept("ritual.bloodmagic.lava", "Serenade of the Nether");
    }
}
