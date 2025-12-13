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

        add(BMBlocks.IMPERFECT_RITUAL_BLOCK, "Imperfect Ritual Stone");

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
}
