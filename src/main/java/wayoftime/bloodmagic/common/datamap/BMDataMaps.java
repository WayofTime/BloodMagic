package wayoftime.bloodmagic.common.datamap;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.ritual.Ritual;
import wayoftime.bloodmagic.api.ritual.RitualStructure;
import wayoftime.bloodmagic.common.registry.BMRegistries;

import java.util.List;

public class BMDataMaps {
    public static final DataMapType<Item, Double> TARTARIC_GEM_MAX_AMOUNTS = DataMapType.builder(
            BloodMagic.rl("tartaric_gem_max"),
            Registries.ITEM,
            Codec.DOUBLE
    ).synced(Codec.DOUBLE, true).build();

    public static final DataMapType<Item, BloodOrb> BLOOD_ORB_STATS = DataMapType.builder(
            BloodMagic.rl("blood_orb_stats"),
            Registries.ITEM,
            BloodOrb.CODEC
    ).synced(BloodOrb.CODEC, true).build();


    public static final DataMapType<Block, List<BloodRune>> BLOOD_RUNES = DataMapType.builder(
            BloodMagic.rl("blood_runes"),
            Registries.BLOCK,
            BloodRune.CODEC.listOf()
    ).synced(BloodRune.CODEC.listOf(), true).build();

    public static final DataMapType<Item, LivingArmorData> LIVING_ARMOUR_DATA = DataMapType.builder(
            BloodMagic.rl("armour_data"),
            Registries.ITEM,
            LivingArmorData.CODEC
    ).synced(LivingArmorData.CODEC, true).build();

    public static final DataMapType<Block, ResourceLocation> IMPERFECT_RITUAL_CATALYST = DataMapType.builder(
            BloodMagic.rl("imperfect_ritual_catalysts"),
            Registries.BLOCK,
            ResourceLocation.CODEC
    ).synced(ResourceLocation.CODEC, true).build();

    public static final DataMapType<RitualStructure, Ritual> RITUAL_BUILD = DataMapType.builder(
            BloodMagic.rl("ritual_build"),
            BMIdentifiers.RegistryKeys.RITUAL_STRUCTURES,
            Ritual.CODEC
    ).build();

    public static void register(RegisterDataMapTypesEvent event) {
        event.register(TARTARIC_GEM_MAX_AMOUNTS);
        event.register(BLOOD_ORB_STATS);
        event.register(BLOOD_RUNES);
        event.register(LIVING_ARMOUR_DATA);

        event.register(IMPERFECT_RITUAL_CATALYST);
        event.register(RITUAL_BUILD);
    }
}
