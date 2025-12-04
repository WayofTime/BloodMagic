package wayoftime.bloodmagic.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BMMaterialsAndTiers;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.registry.AltarTier;
import wayoftime.bloodmagic.common.registry.BMRegistries;

public class BMTags {
    public static class Items {
        public static final TagKey<Item> SOUL_GEM = tag(bm("soul_gems"));
        public static final TagKey<Item> DEMON_CRYSTALS = tag(bm("crystals/demon"));

        public static final TagKey<Item> STORAGE_BLOCKS_HELLFORGED = fromBlock(Blocks.STORAGE_BLOCKS_HELLFORGED);

        public static final TagKey<Item> ARC_TOOL = tag(bm("arc_tool"));

        public static final TagKey<Item> REVERTER = withParent(ARC_TOOL, bm("reverter"));
        public static final TagKey<Item> RESONATOR = withParent(ARC_TOOL, bm("resonator"));
        public static final TagKey<Item> EXPLOSIVES = withParent(ARC_TOOL, bm("explosives"));
        public static final TagKey<Item> CUTTING_FLUIDS = withParent(ARC_TOOL, bm("cutting_fluids"));
        public static final TagKey<Item> HYDRATION = withParent(ARC_TOOL, bm("hydration"));

        public static final TagKey<Item> ARC_FURNACE = withParent(ARC_TOOL, bm("furnace"));
        public static final TagKey<Item> ARC_BLASTING = withParent(ARC_FURNACE, bm("blasting"));
        public static final TagKey<Item> ARC_SMELTING = withParent(ARC_FURNACE, bm("smelting"));
        public static final TagKey<Item> ARC_SMOKING = withParent(ARC_FURNACE, bm("smoking"));

        public static final TagKey<Item> LIVING_UPGRADE_SET = tag(bm("living_upgrade_set"));
        public static final TagKey<Item> LIVING_SET = withParent(LIVING_UPGRADE_SET, BMMaterialsAndTiers.LIVING_ARMOUR_MATERIAL.getId());

        // Processing item tags
        public static final TagKey<Item> FRAGMENTS_IRON = tag(bm("fragments/iron"));
        public static final TagKey<Item> FRAGMENTS_GOLD = tag(bm("fragments/gold"));
        public static final TagKey<Item> FRAGMENTS_COPPER = tag(bm("fragments/copper"));
        public static final TagKey<Item> FRAGMENTS_NETHERITE_SCRAP = tag(bm("fragments/netherite_scrap"));
        public static final TagKey<Item> FRAGMENTS_HELLFORGED = tag(bm("fragments/hellforged"));

        public static final TagKey<Item> GRAVELS_IRON = tag(bm("gravels/iron"));
        public static final TagKey<Item> GRAVELS_GOLD = tag(bm("gravels/gold"));
        public static final TagKey<Item> GRAVELS_COPPER = tag(bm("gravels/copper"));
        public static final TagKey<Item> GRAVELS_NETHERITE_SCRAP = tag(bm("gravels/netherite_scrap"));
        public static final TagKey<Item> GRAVELS_HELLFORGED = tag(bm("gravels/hellforged"));

        // Dust tags (sands)
        public static final TagKey<Item> DUSTS_IRON = tag(bm("dusts/iron"));
        public static final TagKey<Item> DUSTS_GOLD = tag(bm("dusts/gold"));
        public static final TagKey<Item> DUSTS_COPPER = tag(bm("dusts/copper"));
        public static final TagKey<Item> DUSTS_COAL = tag(bm("dusts/coal"));
        public static final TagKey<Item> DUSTS_SULFUR = tag(bm("dusts/sulfur"));
        public static final TagKey<Item> DUSTS_SALTPETER = tag(bm("dusts/saltpeter"));
        public static final TagKey<Item> DUSTS_NETHERITE_SCRAP = tag(bm("dusts/netherite_scrap"));
        public static final TagKey<Item> DUSTS_HELLFORGED = tag(bm("dusts/hellforged"));
        public static final TagKey<Item> DUSTS_CORRUPTED = tag(bm("dusts/corrupted"));
        public static final TagKey<Item> TINY_DUSTS_CORRUPTED = tag(bm("tiny_dusts/corrupted"));

        // Ingot tags
        public static final TagKey<Item> INGOTS_HELLFORGED = tag(c("ingots/hellforged"));

        private static TagKey<Item> fromBlock(TagKey<Block> input) {
            return tag(input.location());
        }

        private static TagKey<Item> withParent(TagKey<Item> parent, ResourceLocation location) {
            return TagKey.create(Registries.ITEM, location.withPrefix(parent.location().getPath()+"/"));
        }

        private static TagKey<Item> tag(ResourceLocation id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    public static class Blocks {
        public static final TagKey<Block> RUNES = tag(bm("altar/runes"));
        public static final TagKey<Block> PILLARS = tag(bm("altar/pillars"));
        public static final TagKey<Block> T3_CAPSTONES = tag(bm("altar/t3_capstones"));
        public static final TagKey<Block> T4_CAPSTONES = tag(bm("altar/t4_capstones"));
        public static final TagKey<Block> T5_CAPSTONES = tag(bm("altar/t5_capstones"));
        public static final TagKey<Block> T6_CAPSTONES = tag(bm("altar/t6_capstones"));

        public static final TagKey<Block> PULSE_ON_CRAFTING = tag(bm("altar/pulse_on_crafting"));
        public static final TagKey<Block> SOUL_NETWORK_COMPARATOR = tag(bm("altar/soul_network_comparator"));

        public static final TagKey<Block> STORAGE_BLOCKS_HELLFORGED = tag(c("storage_blocks/hellforged"));

        private static TagKey<Block> tag(ResourceLocation id) {
            return TagKey.create(Registries.BLOCK, id);
        }
    }

    public static class DamageTypes {
        public static final TagKey<DamageType> SELF_SACRIFICE = TagKey.create(Registries.DAMAGE_TYPE, bm("self_sacrifice"));
        public static final TagKey<DamageType> TOUGH_IGNORED = TagKey.create(Registries.DAMAGE_TYPE, bm("tough_ignored"));
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> TELEPOSE_BLACKLIST = tag(bm("telepose_blacklist"));

        private static TagKey<EntityType<?>> tag(ResourceLocation id) {
            return TagKey.create(Registries.ENTITY_TYPE, id);
        }
    }

    public static class Tiers {
        public static final TagKey<AltarTier> VALID_TIERS = TagKey.create(BMRegistries.Keys.ALTAR_TIER_KEY, bm("valid_tiers"));
    }

    public static class Living {
        public static final TagKey<LivingUpgrade> TOOLTIP_ORDER = tag(bm("tooltip_order"));
        public static final TagKey<LivingUpgrade> TOOLTIP_HIDE = tag(bm("tooltip_hide"));
        public static final TagKey<LivingUpgrade> IS_DOWNGRADE = tag(bm("is_downgrade"));
        public static final TagKey<LivingUpgrade> LIVING_START = tag(bm("living_start"));
        public static final TagKey<LivingUpgrade> TRAINERS = tag(bm("trainer"));
        public static final TagKey<LivingUpgrade> IS_SCRAPPABLE = tag(bm("is_scrappable"));

        private static TagKey<LivingUpgrade> tag(ResourceLocation id) {
            return TagKey.create(BMRegistries.Keys.LIVING_UPGRADES, id);
        }
    }

    private static ResourceLocation bm(String path) {
        return ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, path);
    }

    private static ResourceLocation c(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
}
