package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.fluid.BMFluids;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.datagen.content.LivingUpgrades;
import wayoftime.bloodmagic.util.helper.BlockWithItemHolder;

public class BMLanguageProvider extends LanguageProvider {

    public BMLanguageProvider(PackOutput output) {
        super(output, BloodMagic.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(BMFluids.LIFE_ESSENCE_TYPE.get().getDescriptionId(), "Life Essence");
        add(BMFluids.LIFE_ESSENCE_BUCKET.get(), "Bucket of Life");
        add(BMFluids.LIFE_ESSENCE_BLOCK.get(), "Life Essence");

        add(BMItems.ORB_WEAK.get(), "Weak Blood Orb");
        add(BMItems.ORB_APPRENTICE.get(), "Apprentice Blood Orb");
        add(BMItems.ORB_MAGICIAN.get(), "Magician Blood Orb");
        add(BMItems.ORB_MASTER.get(), "Master Blood Orb");
        add(BMItems.ORB_ARCHMAGE.get(), "Archmage Blood Orb");
        add(BMItems.ORB_TRANSCENDENT.get(), "Transcendent Blood Orb");

        addTooltip("current_owner", "Current Owner: %s");
        addTooltip("no_owner", "Not bound yet");

        add(BMBlocks.BLOOD_ALTAR, "Blood Altar");
        add(BMItems.SACRIFICIAL_DAGGER.get(), "Sacrificial Dagger");

        add(BMBlocks.RUNE_BLANK, "Blank Rune");

        add(BMBlocks.RUNE_SACRIFICE, "Rune of Sacrifice");
        add(BMBlocks.RUNE_SELF_SACRIFICE, "Rune of Self Sacrifice");
        add(BMBlocks.RUNE_SPEED, "Speed Rune");
        add(BMBlocks.RUNE_ACCELERATION, "Acceleration Rune");
        add(BMBlocks.RUNE_DISLOCATION, "Displacement Rune");
        add(BMBlocks.RUNE_CAPACITY, "Capacity Rune");
        add(BMBlocks.RUNE_CAPACITY_AUGMENTED, "Augmented Capacity Rune");
        add(BMBlocks.RUNE_CHARGING, "Charging Rune");
        add(BMBlocks.RUNE_ORB, "Rune of the Orb");
        add(BMBlocks.RUNE_EFFICIENCY, "Rune of Efficiency");

        add(BMBlocks.RUNE_2_SACRIFICE, "Reinforced Rune of Sacrifice");
        add(BMBlocks.RUNE_2_SELF_SACRIFICE, "Reinforced Rune of Self Sacrifice");
        add(BMBlocks.RUNE_2_SPEED, "Reinforced Speed Rune");
        add(BMBlocks.RUNE_2_ACCELERATION, "Reinforced Acceleration Rune");
        add(BMBlocks.RUNE_2_DISLOCATION, "Reinforced Displacement Rune");
        add(BMBlocks.RUNE_2_CAPACITY, "Reinforced Capacity Rune");
        add(BMBlocks.RUNE_2_CAPACITY_AUGMENTED, "Reinforced Augmented Capacity Rune");
        add(BMBlocks.RUNE_2_CHARGING, "Reinforced Charging Rune");
        add(BMBlocks.RUNE_2_ORB, "Reinforced Rune of the Orb");
        add(BMBlocks.RUNE_2_EFFICIENCY, "Reinforced Rune of Efficiency");

        add(BMBlocks.BLOODSTONE, "Polished Bloodstone");
        add(BMBlocks.BLOODSTONE_BRICK, "Bloodstone Brick");

        add(BMBlocks.HELLFORGED_BLOCK, "Hellforged Block");

        add(BMBlocks.CRYSTAL_CLUSTER, "Crystal Cluster");
        add(BMBlocks.CRYSTAL_CLUSTER_BRICK, "Crystal Cluster Brick");

        // Demon Will Blocks
        add(BMBlocks.DEMON_CRUCIBLE, "Demon Crucible");
        add(BMBlocks.DEMON_CRYSTALLIZER, "Demon Crystallizer");
        add(BMBlocks.DEMON_PYLON, "Demon Pylon");

        // Demon Crystal Blocks (placeholder)
        add(BMBlocks.RAW_CRYSTAL_BLOCK, "Raw Demon Crystal");
        add(BMBlocks.CORROSIVE_CRYSTAL_BLOCK, "Corrosive Demon Crystal");
        add(BMBlocks.DESTRUCTIVE_CRYSTAL_BLOCK, "Destructive Demon Crystal");
        add(BMBlocks.VENGEFUL_CRYSTAL_BLOCK, "Vengeful Demon Crystal");
        add(BMBlocks.STEADFAST_CRYSTAL_BLOCK, "Steadfast Demon Crystal");

        // Routing Node Blocks
        add(BMBlocks.ROUTING_NODE, "Item Routing Node");
        add(BMBlocks.INPUT_ROUTING_NODE, "Input Routing Node");
        add(BMBlocks.OUTPUT_ROUTING_NODE, "Output Routing Node");
        add(BMBlocks.MASTER_ROUTING_NODE, "Master Routing Node");

        // Tau Blocks
        add(BMBlocks.WEAK_TAU, "Weak Tau");
        add(BMBlocks.STRONG_TAU, "Strong Tau");

        // Ritual Stones
        add(BMBlocks.BLANK_RITUAL_STONE, "Ritual Stone");
        add(BMBlocks.AIR_RITUAL_STONE, "Air Ritual Stone");
        add(BMBlocks.WATER_RITUAL_STONE, "Water Ritual Stone");
        add(BMBlocks.FIRE_RITUAL_STONE, "Fire Ritual Stone");
        add(BMBlocks.EARTH_RITUAL_STONE, "Earth Ritual Stone");
        add(BMBlocks.DUSK_RITUAL_STONE, "Dusk Ritual Stone");
        add(BMBlocks.DAWN_RITUAL_STONE, "Dawn Ritual Stone");
        add(BMBlocks.MASTER_RITUAL_STONE, "Master Ritual Stone");

        addTooltip("save_for_decoration", "Save for Decoration");

        add(BMFluids.DOUBT_TYPE.get().getDescriptionId(), "Liquid Doubt");
        add(BMFluids.DOUBT_BUCKET.get(), "Doubt Bucket");
        add(BMFluids.DOUBT_BLOCK.get(), "Liquid Doubt");

        add(BMBlocks.ARC_BLOCK, "Alchemical Reaction Chamber");

        add(BMBlocks.BLOOD_TANK, "Blood Tank");
        addTooltip("container_tier_missing", "No Tier found!");
        addTooltip("container_tier", "Current Tier: %s");
        addTooltip("fluid_content_empty", "Empty");
        addTooltip("fluid_content", "Contains: %smB of %s");

        add(BMBlocks.HELLFIRE_FORGE, "Hellfire Forge");
        add(BMItems.RAW_WILL.get(), "Raw Will");

        add(BMItems.SOUL_GEM_PETTY.get(), "Petty Tartaric Gem");
        add(BMItems.SOUL_GEM_LESSER.get(), "Lesser Tartaric Gem");
        add(BMItems.SOUL_GEM_COMMON.get(), "Common Tartaric Gem");
        add(BMItems.SOUL_GEM_GREATER.get(), "Greater Tartaric Gem");
        add(BMItems.SOUL_GEM_GRAND.get(), "Grand Tartaric Gem");
        addGemDesc(BMItems.SOUL_GEM_PETTY, "a little");
        addGemDesc(BMItems.SOUL_GEM_LESSER, "some");
        addGemDesc(BMItems.SOUL_GEM_COMMON, "more");
        addGemDesc(BMItems.SOUL_GEM_GREATER, "a greater amount of");
        addGemDesc(BMItems.SOUL_GEM_GRAND, "a large amount of");

        // Slates
        add(BMItems.SLATE_BLANK.get(), "Blank Slate");
        add(BMItems.SLATE_REINFORCED.get(), "Reinforced Slate");
        add(BMItems.SLATE_IMBUED.get(), "Imbued Slate");
        add(BMItems.SLATE_DEMONIC.get(), "Demonic Slate");
        add(BMItems.SLATE_ETHEREAL.get(), "Ethereal Slate");

        // Sigils
        add(BMItems.SIGIL_DIVINATION.get(), "Divination Sigil");
        add(BMItems.SIGIL_SEER.get(), "Seer's Sigil");
        add(BMItems.SIGIL_WATER.get(), "Water Sigil");
        add(BMItems.SIGIL_LAVA.get(), "Lava Sigil");
        add(BMItems.SIGIL_VOID.get(), "Void Sigil");
        add(BMItems.SIGIL_GREEN_GROVE.get(), "Sigil of the Green Grove");
        add(BMItems.SIGIL_AIR.get(), "Air Sigil");
        add(BMItems.SIGIL_BLOOD_LIGHT.get(), "Sigil of the Blood Lamp");
        add(BMItems.SIGIL_FAST_MINER.get(), "Sigil of the Fast Miner");
        add(BMItems.SIGIL_MAGNETISM.get(), "Sigil of Magnetism");
        add(BMItems.SIGIL_FROST.get(), "Sigil of the Phantom Bridge");
        add(BMItems.SIGIL_SUPPRESSION.get(), "Sigil of Suppression");
        add(BMItems.SIGIL_HOLDING.get(), "Sigil of Holding");
        add(BMItems.SIGIL_TELEPOSITION.get(), "Sigil of Teleposition");

        // Alchemy & Misc
        add(BMItems.ARCANE_ASHES.get(), "Arcane Ashes");
        addTooltip("arcaneAshes", "Draws an alchemy circle when placed");

        // Reagents
        add(BMItems.REAGENT_WATER.get(), "Reagent Water");
        add(BMItems.REAGENT_LAVA.get(), "Reagent Lava");
        add(BMItems.REAGENT_VOID.get(), "Reagent Void");
        add(BMItems.REAGENT_GROWTH.get(), "Reagent Growth");
        add(BMItems.REAGENT_FAST_MINER.get(), "Reagent Fast Miner");
        add(BMItems.REAGENT_MAGNETISM.get(), "Reagent Magnetism");
        add(BMItems.REAGENT_AIR.get(), "Reagent Air");
        add(BMItems.REAGENT_BLOOD_LIGHT.get(), "Reagent Blood Light");
        add(BMItems.REAGENT_SIGHT.get(), "Reagent Sight");
        add(BMItems.REAGENT_BINDING.get(), "Reagent Binding");
        add(BMItems.REAGENT_HOLDING.get(), "Reagent Holding");
        add(BMItems.REAGENT_SUPPRESSION.get(), "Reagent Suppression");
        add(BMItems.REAGENT_TELEPOSITION.get(), "Reagent Teleposition");

        // Alchemy Array and Table Blocks
        add(BMBlocks.ALCHEMY_ARRAY.get(), "Alchemy Array");
        add(BMBlocks.ALCHEMY_TABLE, "Alchemy Table");

        add(BMItems.SOUL_SNARE.get(), "Soul Snare");
        add(BMItems.WEAK_BLOOD_SHARD.get(), "Weak Blood Shard");
        add(BMItems.DAGGER_OF_SACRIFICE.get(), "Dagger of Sacrifice");
        add(BMItems.LAVA_CRYSTAL.get(), "Lava Crystal");

        // Crystal Items (bloodmagic crystals)
        add(BMItems.RAW_CRYSTAL.get(), "Demon Will Crystal");
        add(BMItems.CORROSIVE_CRYSTAL.get(), "Corrosive Will Crystal");
        add(BMItems.DESTRUCTIVE_CRYSTAL.get(), "Destructive Will Crystal");
        add(BMItems.VENGEFUL_CRYSTAL.get(), "Vengeful Will Crystal");
        add(BMItems.STEADFAST_CRYSTAL.get(), "Steadfast Will Crystal");
        add(BMItems.DEMON_WILL_GAUGE.get(), "Demon Will Aura Gauge");

        // Crystal Catalysts
        add(BMItems.RAW_CRYSTAL_CATALYST.get(), "Raw Crystal Catalyst");
        add(BMItems.CORROSIVE_CRYSTAL_CATALYST.get(), "Corrosive Crystal Catalyst");
        add(BMItems.DESTRUCTIVE_CRYSTAL_CATALYST.get(), "Destructive Crystal Catalyst");
        add(BMItems.VENGEFUL_CRYSTAL_CATALYST.get(), "Vengeful Crystal Catalyst");
        add(BMItems.STEADFAST_CRYSTAL_CATALYST.get(), "Steadfast Crystal Catalyst");

        // Sentient Tools
        add(BMItems.SENTIENT_SWORD.get(), "Sentient Sword");
        add(BMItems.SENTIENT_AXE.get(), "Sentient Axe");
        add(BMItems.SENTIENT_PICKAXE.get(), "Sentient Pickaxe");
        add(BMItems.SENTIENT_SHOVEL.get(), "Sentient Shovel");
        add(BMItems.SENTIENT_SCYTHE.get(), "Sentient Scythe");

        // Routing Items
        add(BMItems.NODE_ROUTER.get(), "Node Router");
        add(BMItems.MASTER_NODE_UPGRADE.get(), "Master Routing Node Core");
        add(BMItems.MASTER_NODE_UPGRADE_SPEED.get(), "Speed Core");

        // Throwing Daggers
        add(BMItems.THROWING_DAGGER.get(), "Throwing Dagger");
        add(BMItems.THROWING_DAGGER_AMETHYST.get(), "Amethyst Throwing Dagger");
        add(BMItems.THROWING_DAGGER_SYRINGE.get(), "Syringe Throwing Dagger");

        // Keys
        add(BMItems.SIMPLE_KEY.get(), "Simple Key");
        add(BMItems.MINE_KEY.get(), "Mine Key");

        // Simple Recipe Ingredients
        add(BMItems.SULFUR.get(), "Sulfur");
        add(BMItems.SALTPETER.get(), "Saltpeter");
        add(BMItems.PLANT_OIL.get(), "Plant Oil");
        add(BMItems.HELLFORGED_INGOT.get(), "Hellforged Ingot");

        // Explosive Charges
        add(BMBlocks.SHAPED_CHARGE, "Shaped Charge");
        add(BMBlocks.DEFORESTER_CHARGE, "Deforester Charge");
        add(BMBlocks.VEINMINE_CHARGE, "Veinmine Charge");
        add(BMBlocks.FUNGAL_CHARGE, "Fungal Charge");
        add(BMBlocks.AUG_SHAPED_CHARGE, "Augmented Shaped Charge");
        add(BMBlocks.DEFORESTER_CHARGE_2, "Reinforced Deforester Charge");
        add(BMBlocks.VEINMINE_CHARGE_2, "Reinforced Veinmine Charge");
        add(BMBlocks.FUNGAL_CHARGE_2, "Reinforced Fungal Charge");
        add(BMBlocks.SHAPED_CHARGE_DEEP, "Deep Shaped Charge");

        // Alchemy Flask Items
        add(BMItems.SLATE_VIAL.get(), "Slate Vial");
        add(BMItems.ALCHEMY_FLASK.get(), "Alchemy Flask");
        add(BMItems.ALCHEMY_FLASK_THROWABLE.get(), "Throwable Alchemy Flask");
        add(BMItems.ALCHEMY_FLASK_LINGERING.get(), "Lingering Alchemy Flask");

        // Anointment Items
        add(BMItems.MELEE_DAMAGE_ANOINTMENT.get(), "Anointment: Melee Damage");
        add(BMItems.SILK_TOUCH_ANOINTMENT.get(), "Anointment: Silk Touch");
        add(BMItems.FORTUNE_ANOINTMENT.get(), "Anointment: Fortune");
        add(BMItems.HOLY_WATER_ANOINTMENT.get(), "Anointment: Holy Water");
        add(BMItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get(), "Anointment: Hidden Knowledge");
        add(BMItems.QUICK_DRAW_ANOINTMENT.get(), "Anointment: Quick Draw");
        add(BMItems.LOOTING_ANOINTMENT.get(), "Anointment: Looting");
        add(BMItems.BOW_POWER_ANOINTMENT.get(), "Anointment: Bow Power");
        add(BMItems.SMELTING_ANOINTMENT.get(), "Anointment: Smelting");
        add(BMItems.VOIDING_ANOINTMENT.get(), "Anointment: Voiding");
        add(BMItems.BOW_VELOCITY_ANOINTMENT.get(), "Anointment: Bow Velocity");
        add(BMItems.WEAPON_REPAIR_ANOINTMENT.get(), "Anointment: Weapon Repair");

        // Routing/Filter Items
        add(BMItems.FRAME_PARTS.get(), "Frame Parts");
        add(BMItems.ITEM_ROUTER_FILTER.get(), "Standard Filter");
        add(BMItems.ITEM_TAG_FILTER.get(), "Tag Filter");
        add(BMItems.ITEM_ENCHANT_FILTER.get(), "Enchantment Filter");
        add(BMItems.ITEM_MOD_FILTER.get(), "Mod Filter");
        add(BMItems.ITEM_COMPOSITE_FILTER.get(), "Composite Filter");
        add(BMItems.NODE_ROUTER.get(), "Node Router");

        // Teleposer Focus Items
        add(BMItems.TELEPOSER_FOCUS.get(), "Teleposer Focus");
        add(BMItems.TELEPOSER_FOCUS_ENHANCED.get(), "Enhanced Teleposer Focus");
        add(BMItems.TELEPOSER_FOCUS_REINFORCED.get(), "Reinforced Teleposer Focus");
        addTooltip("telepositionfocus.coords", "Coordinates: %s, %s, %s");
        addTooltip("telepositionfocus.world", "Dimension: %s");

        // Activation Crystals
        add(BMItems.ACTIVATION_CRYSTAL_WEAK.get(), "Weak Activation Crystal");
        add(BMItems.ACTIVATION_CRYSTAL_AWAKENED.get(), "Awakened Activation Crystal");
        add(BMItems.ACTIVATION_CRYSTAL_CREATIVE.get(), "Creative Activation Crystal");
        addTooltip("activationcrystal.weak", "Activates low-level rituals.");
        addTooltip("activationcrystal.awakened", "Activates more powerful rituals.");
        addTooltip("activationcrystal.creative", "Creative Only - Activates any ritual.");

        // Inscription Tools
        add(BMItems.INSCRIPTION_TOOL_AIR.get(), "Inscription Tool: Air");
        add(BMItems.INSCRIPTION_TOOL_FIRE.get(), "Inscription Tool: Fire");
        add(BMItems.INSCRIPTION_TOOL_WATER.get(), "Inscription Tool: Water");
        add(BMItems.INSCRIPTION_TOOL_EARTH.get(), "Inscription Tool: Earth");
        add(BMItems.INSCRIPTION_TOOL_DUSK.get(), "Inscription Tool: Dusk");
        addTooltip("inscriber.desc", "The writing is on the wall...");

        // Ritual Diviners
        add(BMItems.RITUAL_DIVINER.get(), "Ritual Diviner");
        add(BMItems.RITUAL_DIVINER_DUSK.get(), "Ritual Diviner [Dusk]");
        addTooltip("diviner.desc", "Used to build rituals.");
        addTooltip("diviner.currentRitual", "Current Ritual: %s");
        addTooltip("diviner.currentDirection", "Current Direction: %s");
        addTooltip("diviner.blankRune", "Blank Runes: %d");
        addTooltip("diviner.airRune", "Air Runes: %d");
        addTooltip("diviner.waterRune", "Water Runes: %d");
        addTooltip("diviner.fireRune", "Fire Runes: %d");
        addTooltip("diviner.earthRune", "Earth Runes: %d");
        addTooltip("diviner.duskRune", "Dusk Runes: %d");
        addTooltip("diviner.dawnRune", "Dawn Runes: %d");
        addTooltip("diviner.totalRune", "Total Runes: %d");
        addTooltip("diviner.extraInfo", "Press shift for extra info.");
        addTooltip("diviner.extraExtraInfo", "-Hold shift + alt for augmentation info-");
        add("chat.bloodmagic.diviner.blockedBuild", "Unable to replace block at %d, %d, %d.");

        // Tau Oil
        add(BMItems.TAU_OIL.get(), "Tau Oil");

        // ARC Items
        add(BMItems.BASIC_CUTTING_FLUID.get(), "Basic Cutting Fluid");
        add(BMItems.EXPLOSIVE_POWDER.get(), "Explosive Powder");
        add(BMItems.RESONATOR.get(), "Crystal Resonator");
        add(BMItems.PRIMITIVE_CRYSTALLINE_RESONATOR.get(), "Reinforced Resonator");
        add(BMItems.HELLFORGED_RESONATOR.get(), "Hellforged Resonator");
        add(BMItems.PRIMITIVE_FURNACE_CELL.get(), "Primitive Fuel Cell");
        add(BMItems.PRIMITIVE_HYDRATION_CELL.get(), "Primitive Hydration Cell");
        add(BMItems.PRIMITIVE_EXPLOSIVE_CELL.get(), "Reinforced Explosive Cell");
        add(BMItems.HELLFORGED_EXPLOSIVE_CELL.get(), "Hellforged Explosive Cell");
        add(BMItems.SANGUINE_REVERTER.get(), "Sanguine Reverter");

        // Ore Processing Items
        add(BMItems.IRON_FRAGMENT.get(), "Iron Fragment");
        add(BMItems.IRON_GRAVEL.get(), "Iron Gravel");
        add(BMItems.IRON_SAND.get(), "Iron Sand");
        add(BMItems.GOLD_FRAGMENT.get(), "Gold Fragment");
        add(BMItems.GOLD_GRAVEL.get(), "Gold Gravel");
        add(BMItems.GOLD_SAND.get(), "Gold Sand");
        add(BMItems.COPPER_FRAGMENT.get(), "Copper Fragment");
        add(BMItems.COPPER_GRAVEL.get(), "Copper Gravel");
        add(BMItems.COPPER_SAND.get(), "Copper Sand");
        add(BMItems.COAL_SAND.get(), "Coal Sand");
        add(BMItems.DEMONITE_FRAGMENT.get(), "Demonite Fragment");
        add(BMItems.DEMONITE_GRAVEL.get(), "Demonite Gravel");
        add(BMItems.NETHERITE_SCRAP_FRAGMENT.get(), "Ancient Debris Fragment");
        add(BMItems.NETHERITE_SCRAP_GRAVEL.get(), "Ancient Debris Gravel");
        add(BMItems.NETHERITE_SCRAP_SAND.get(), "Netherite Scrap Sand");
        add(BMItems.HELLFORGED_SAND.get(), "Hellforged Sand");
        add(BMItems.CORRUPTED_DUST.get(), "Corrupted Dust");
        add(BMItems.CORRUPTED_DUST_TINY.get(), "Tiny Corrupted Dust");

        addTooltip("will", "Will Quality: %s");
        for (EnumWillType type : EnumWillType.values()) {
            addTooltip("current_type." + type.getSerializedName(), String.format("Contains: %s Will", type.toCapitalized()));
        }
        add("item_group.bloodmagic.main", "Blood Magic");
        add("item_group.bloodmagic.tomes", "Blood Magic Upgrade Tomes");
        add("item_group.bloodmagic.trainers", "Blood Magic Trainer Tomes");

        add(BMItems.LIVING_HELMET.get(), "Living Helmet");
        add(BMItems.LIVING_PLATE.get(), "Living Plate");
        add(BMItems.LIVING_LEGGINGS.get(), "Living Leggings");
        add(BMItems.LIVING_BOOTS.get(), "Living Boots");
        add(BMItems.UPGRADE_TOME.get(), "Upgrade Tome");

        add(BMBlocks.LIVING_STATION, "Living Upgrade Station");
        add(BMItems.UPGRADE_SCRAP.get(), "Upgrade Tome Scrap");
        add(BMItems.SYNTHETIC_POINT.get(), "Synthetic Upgrade Points");
        addTooltip("scrap", "Contained Upgrade Points: %s");

        add(BMItems.TRAINING_BRACELET.get(), "Living Training Bracelet");
        add("trainer.bloodmagic.allow_others", "Allow Others");
        add("trainer.bloodmagic.deny_others", "Deny Others");
        add("trainer.bloodmagic.save", "Save");

        add("item.bloodmagic.living_plate.dead", "Formerly Living Plate");
        addTooltip("has_living_stats", "Theres some kind of notes, but you cant decipher them");

        addCommand("upgrade.get", "%s has the following upgrades:\n");
        addCommand("upgrade.set", "Set %s to %s exp for %s");
        addCommand("upgrade.no_armour", "The chestplate %s is wearing does not have a bloodmagic:required_set component set. Upgrades cannot take effect like this");
        addCommand("cap.success", "Set max upgrade points to %s");
        addCommand("recalc.success", "Upgrades use up %s points");
        addCommand("limit.get", "%s is in '%s' mode and has the following limits:\n");
        addCommand("limit.set", "Set limit of %s to %s exp for %s");
        addCommand("limit.mode.allow", "allow others");
        addCommand("limit.mode.deny", "deny others");

        addTooltip("upgrade_points", "Upgrade Points: %s/%s");
        add("chat.bloodmagic.living_upgrade.level_up", "%s has levelled up to %s!");

        LivingUpgrades.translations(this::add);

        // JEI Integration
        addJei("recipe.altar", "Blood Altar");
        addJei("recipe.soulforge", "Hellfire Forge");
        addJei("recipe.alchemyarraycrafting", "Alchemy Array");
        addJei("recipe.alchemytable", "Alchemy Table");
        addJei("recipe.requiredtier", "Required Tier: %s");
        addJei("recipe.requiredlp", "Required LP: %s");
        addJei("recipe.consumptionrate", "Consumption Rate: %s LP/t");
        addJei("recipe.drainrate", "Drain Rate: %s LP/t");
        addJei("recipe.minimumsouls", "Minimum Souls: %s");
        addJei("recipe.soulsdrained", "Souls Drained: %s");
        addJei("recipe.will", "Will");
        addJei("recipe.info", "Hover for info");
        addJei("recipe.lp", "LP");
        addJei("recipe.lpDrained", "LP Drained: %s");
        addJei("recipe.ticksRequired", "Ticks: %s");
    }

    public void addCommand(String key, String value) {
        add("commands.bloodmagic." + key, value);
    }

    public void addGemDesc(DeferredHolder holder, String desc) {
        addTooltip("soul_gem." + holder.getId().getPath(), String.format("A gem used to contain %s will.", desc));
    }

    public void add(BlockWithItemHolder<? extends Block, ? extends BlockItem> block, String name) {
        add(block.block().get().getDescriptionId(), name);
    }

    public void addTooltip(String name, String value) {
        add("tooltip.bloodmagic." + name, value);
    }

    public void addJei(String name, String value) {
        add("jei.bloodmagic." + name, value);
    }
}
