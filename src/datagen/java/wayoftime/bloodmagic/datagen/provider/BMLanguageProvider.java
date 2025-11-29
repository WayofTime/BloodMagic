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
        add(BMItems.SOUL_SNARE.get(), "Soul Snare");
        add(BMItems.WEAK_BLOOD_SHARD.get(), "Weak Blood Shard");
        add(BMItems.DAGGER_OF_SACRIFICE.get(), "Dagger of Sacrifice");
        add(BMItems.LAVA_CRYSTAL.get(), "Lava Crystal");

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
        addJei("recipe.requiredtier", "Required Tier: %s");
        addJei("recipe.requiredlp", "Required LP: %s");
        addJei("recipe.consumptionrate", "Consumption Rate: %s LP/t");
        addJei("recipe.drainrate", "Drain Rate: %s LP/t");
        addJei("recipe.minimumsouls", "Minimum Souls: %s");
        addJei("recipe.soulsdrained", "Souls Drained: %s");
        addJei("recipe.will", "Will");
        addJei("recipe.info", "Hover for info");
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
