package wayoftime.bloodmagic.datagen.content;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers.ImperfectRituals;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.common.registry.BMRegistries;
import wayoftime.bloodmagic.common.ritual.imperfect.ApplyPotionEffect;
import wayoftime.bloodmagic.common.ritual.imperfect.ImperfectRitualEffect;
import wayoftime.bloodmagic.common.ritual.imperfect.SetTimeEffect;
import wayoftime.bloodmagic.common.ritual.imperfect.SpawnMobEffect;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ImperfectRitualData {
    public static void effects(BootstrapContext<ImperfectRitualEffect> context) {
        context.register(key(ImperfectRituals.NIGHT_RITUAL), new SetTimeEffect(1000, 13000));
        context.register(key(ImperfectRituals.DAY_RITUAL), new SetTimeEffect(1000, 1000));
        context.register(key(ImperfectRituals.RESISTANCE_RITUAL), new ApplyPotionEffect(1000, MobEffects.DAMAGE_RESISTANCE, 1200, Optional.of(1), Optional.empty()));
        context.register(key(ImperfectRituals.ZOMBIE_RITUAL), new SpawnMobEffect(1000, EntityType.ZOMBIE.builtInRegistryHolder(), Optional.empty(), Optional.of(List.of(
                        new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2000),
                        new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2000, 7),
                        new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2000, 3)
                )),
                Optional.empty()
        ));
    }

    private static ResourceKey<ImperfectRitualEffect> key(ResourceLocation id) {
        return ResourceKey.create(BMRegistries.Keys.IMPERFECT_RITUALS, id);
    }

    public static void dataMap(Function<DataMapType<Block, ResourceLocation>, DataMapProvider.Builder<ResourceLocation, Block>> setup) {
        setup.apply(BMDataMaps.IMPERFECT_RITUAL_CATALYST)
                .add(Tags.Blocks.STORAGE_BLOCKS_LAPIS, ImperfectRituals.NIGHT_RITUAL, false)
                .add(Tags.Blocks.STORAGE_BLOCKS_COAL, ImperfectRituals.DAY_RITUAL, false)
                .add(Blocks.BEDROCK.builtInRegistryHolder(), ImperfectRituals.RESISTANCE_RITUAL, false)
                .add(Tags.Blocks.COBBLESTONES_MOSSY, ImperfectRituals.ZOMBIE_RITUAL, false)
                .build();
    }
}
