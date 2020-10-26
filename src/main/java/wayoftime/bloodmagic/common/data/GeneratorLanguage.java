package wayoftime.bloodmagic.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

public class GeneratorLanguage extends LanguageProvider
{
	public GeneratorLanguage(DataGenerator gen)
	{
		super(gen, BloodMagic.MODID, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		// Creative Tab
		add("itemGroup.bloodmagic.creativeTab", "Blood Magic");

		add("chat.bloodmagic.damageSource", "%s's soul became too weak");

		// Tile Entitites
		add("tile.bloodmagic.soulforge.name", "Hellfire Forge");

		// Blood Orb tooltips
		add("tooltip.bloodmagic.extraInfo", "&9-Hold shift for more info-");
		add("tooltip.bloodmagic.orb.desc", "Stores raw Life Essence");
		add("tooltip.bloodmagic.orb.owner", "Added by: %s");
		add("tooltip.bloodmagic.currentOwner", "Current owner: %s");
		add("tooltip.bloodmagic.currentTier", "Current tier: %d");
		add("tooltip.bloodmagic.config.disabled", "Currently disabled in the Config");
		add("tooltip.bloodmagic.tier", "Tier %d");

		// Sigil tooltips
		add("tooltip.bloodmagic.sigil.divination.desc", "Peer into the soul");
		add("tooltip.bloodmagic.sigil.divination.otherNetwork", "Peering into the soul of %s");
		add("tooltip.bloodmagic.sigil.divination.currentAltarTier", "Current Tier: %d");
		add("tooltip.bloodmagic.sigil.divination.currentEssence", "Current Essence: %d LP");
		add("tooltip.bloodmagic.sigil.divination.currentAltarCapacity", "Current Capacity: %d LP");
		add("tooltip.bloodmagic.sigil.divination.currentTranquility", "Current Tranquility: %d");
//		add("tooltip.bloodmagic.sigil.divination.currentInversion", "Current Inversion: %d");
		add("tooltip.bloodmagic.sigil.divination.currentBonus", "Current Bonus: +%d%%");

		add("tooltip.bloodmagic.decoration.safe", "Safe for decoration");
		add("tooltip.bloodmagic.decoration.notSafe", "Dangerous for decoration");

		// General Tooltips
		add("tooltip.bloodmagic.arcaneAshes", "Ashes used to draw an alchemy circle");
		add("tooltip.bloodmagic.will", "Will Quality: %s");
		add("tooltip.bloodmagic.sentientSword.desc", "Uses demon will to unleash its full potential.");
		add("tooltip.bloodmagic.sentientAxe.desc", "Uses demon will to unleash its full potential.");
		add("tooltip.bloodmagic.sentientPickaxe.desc", "Uses demon will to unleash its full potential.");
		add("tooltip.bloodmagic.sentientShovel.desc", "Uses demon will to unleash its full potential.");
		add("tooltip.bloodmagic.soulGem.petty", "A gem used to contain a little will");
		add("tooltip.bloodmagic.soulGem.lesser", "A gem used to contain some will");
		add("tooltip.bloodmagic.soulGem.common", "A gem used to contain more will");
		add("tooltip.bloodmagic.soulGem.greater", "A gem used to contain a greater amount of will");
		add("tooltip.bloodmagic.soulGem.grand", "A gem used to contain a large amount of will");
		add("tooltip.bloodmagic.soulSnare.desc", "Throw at a monster and then kill them to obtain their demonic will");

		add("tooltip.bloodmagic.currentType.default", "Contains: Raw Will");
		add("tooltip.bloodmagic.currentType.corrosive", "Contains: Corrosive Will");
		add("tooltip.bloodmagic.currentType.destructive", "Contains: Destructive Will");
		add("tooltip.bloodmagic.currentType.vengeful", "Contains: Vengeful Will");
		add("tooltip.bloodmagic.currentType.steadfast", "Contains: Steadfast Will");

		add("tooltip.bloodmagic.currentBaseType.default", "Raw");
		add("tooltip.bloodmagic.currentBaseType.corrosive", "Corrosive");
		add("tooltip.bloodmagic.currentBaseType.destructive", "Destructive");
		add("tooltip.bloodmagic.currentBaseType.vengeful", "Vengeful");
		add("tooltip.bloodmagic.currentBaseType.steadfast", "Steadfast");
		add("tooltip.bloodmagic.sacrificialdagger.desc", "Just a prick of the finger will suffice...");
		add("tooltip.bloodmagic.slate.desc", "Infused stone inside of a Blood Altar");
		add("tooltip.bloodmagic.inscriber.desc", "The writing is on the wall...");

		add("tooltip.bloodmagic.sigil.water.desc", "Infinite water, anyone?");
		add("tooltip.bloodmagic.sigil.lava.desc", "HOT! DO NOT EAT");
		add("tooltip.bloodmagic.sigil.void.desc", "Better than a Swiffer®!");
		add("tooltip.bloodmagic.sigil.greengrove.desc", "Environmentally friendly");
		add("tooltip.bloodmagic.sigil.magnetism.desc", "I have a very magnetic personality");
		add("tooltip.bloodmagic.sigil.fastminer.desc", "Keep mining, and mining...");
		add("tooltip.bloodmagic.sigil.air.desc", "I feel lighter already...");
		add("tooltip.bloodmagic.sigil.bloodlight.desc", "I see a light!");

		add("tooltip.bloodmagic.activationcrystal.weak", "Activates low-level rituals");
		add("tooltip.bloodmagic.activationcrystal.awakened", "Activates more powerful rituals");
		add("tooltip.bloodmagic.activationcrystal.creative", "Creative Only - Activates any ritual");

		add("itemGroup.bloodmagictab", "Blood Magic");

		// Ritual info
		add("tooltip.bloodmagic.diviner.currentRitual", "Current Ritual: %s");
		add("tooltip.bloodmagic.diviner.blankRune", "Blank Runes: %d");
		add("tooltip.bloodmagic.diviner.waterRune", "Water Runes: %d");
		add("tooltip.bloodmagic.diviner.airRune", "Air Runes: %d");
		add("tooltip.bloodmagic.diviner.fireRune", "Fire Runes: %d");
		add("tooltip.bloodmagic.diviner.earthRune", "Earth Runes: %d");
		add("tooltip.bloodmagic.diviner.duskRune", "Dusk Runes: %d");
		add("tooltip.bloodmagic.diviner.dawnRune", "Dawn Runes: %d");
		add("tooltip.bloodmagic.diviner.totalRune", "Total Runes: %d");
		add("tooltip.bloodmagic.diviner.extraInfo", "Press shift for extra info");
		add("tooltip.bloodmagic.diviner.extraExtraInfo", "-Hold shift + alt for augmentation info-");
		add("tooltip.bloodmagic.diviner.currentDirection", "Current Direction: %s");

		add("ritual.bloodmagic.testRitual", "Test Ritual");
		add("ritual.bloodmagic.waterRitual", "Ritual of the Full Spring");
		add("ritual.bloodmagic.lavaRitual", "Serenade of the Nether");
		add("ritual.bloodmagic.greenGroveRitual", "Ritual of the Green Grove");
		add("ritual.bloodmagic.jumpRitual", "Ritual of the High Jump");
		add("ritual.bloodmagic.wellOfSufferingRitual", "Well of Suffering");
		add("ritual.bloodmagic.featheredKnifeRitual", "Ritual of the Feathered Knife");
		add("ritual.bloodmagic.regenerationRitual", "Ritual of Regeneration");
		add("ritual.bloodmagic.harvestRitual", "Reap of the Harvest Moon");
		add("ritual.bloodmagic.magneticRitual", "Ritual of Magnetism");
		add("ritual.bloodmagic.crushingRitual", "Ritual of the Crusher");
		add("ritual.bloodmagic.fullStomachRitual", "Ritual of the Satiated Stomach");
		add("ritual.bloodmagic.interdictionRitual", "Ritual of Interdiction");
		add("ritual.bloodmagic.containmentRitual", "Ritual of Containment");
		add("ritual.bloodmagic.speedRitual", "Ritual of Speed");
		add("ritual.bloodmagic.suppressionRitual", "Ritual of Suppression");
		add("ritual.bloodmagic.expulsionRitual", "Aura of Expulsion");
		add("ritual.bloodmagic.zephyrRitual", "Call of the Zephyr");
		add("ritual.bloodmagic.upgradeRemoveRitual", "Sound of the Cleansing Soul");
		add("ritual.bloodmagic.armourEvolveRitual", "Ritual of Living Evolution");
		add("ritual.bloodmagic.animalGrowthRitual", "Ritual of the Shepherd");

		add("ritual.bloodmagic.cobblestoneRitual", "Le Vulcanos Frigius");
		add("ritual.bloodmagic.placerRitual", "The Filler");
		add("ritual.bloodmagic.fellingRitual", "The Timberman");
		add("ritual.bloodmagic.pumpRitual", "Hymn of Siphoning");
		add("ritual.bloodmagic.altarBuilderRitual", "The Assembly of the High Altar");
		add("ritual.bloodmagic.portalRitual", "The Gate of the Fold");

		// Block names
		addBlock(BloodMagicBlocks.BLANK_RUNE, "Blank Rune");
		addBlock(BloodMagicBlocks.SPEED_RUNE, "Speed Rune");
		addBlock(BloodMagicBlocks.SACRIFICE_RUNE, "Rune of Sacrifice");
		addBlock(BloodMagicBlocks.SELF_SACRIFICE_RUNE, "Rune of Self Sacrifice");
		addBlock(BloodMagicBlocks.DISPLACEMENT_RUNE, "DisplacementRune");
		addBlock(BloodMagicBlocks.CAPACITY_RUNE, "Rune of Capacity");
		addBlock(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE, "Rune of Augmented Capacity");
		addBlock(BloodMagicBlocks.ORB_RUNE, "Rune of the Orb");
		addBlock(BloodMagicBlocks.ACCELERATION_RUNE, "Acceleration Rune");
		addBlock(BloodMagicBlocks.CHARGING_RUNE, "Charging Rune");
		addBlock(BloodMagicBlocks.BLOOD_ALTAR, "Blood Altar");
		addBlock(BloodMagicBlocks.SOUL_FORGE, "Hellfire Forge");
		addBlock(BloodMagicBlocks.BLANK_RITUAL_STONE, "Ritual Stone");
		addBlock(BloodMagicBlocks.AIR_RITUAL_STONE, "Air Ritual Stone");
		addBlock(BloodMagicBlocks.WATER_RITUAL_STONE, "Water Ritual Stone");
		addBlock(BloodMagicBlocks.FIRE_RITUAL_STONE, "Fire Ritual Stone");
		addBlock(BloodMagicBlocks.EARTH_RITUAL_STONE, "Earth Ritual Stone");
		addBlock(BloodMagicBlocks.DUSK_RITUAL_STONE, "Dusk Ritual Stone");
		addBlock(BloodMagicBlocks.DAWN_RITUAL_STONE, "Dawn Ritual Stone");
		addBlock(BloodMagicBlocks.MASTER_RITUAL_STONE, "Master Ritual Stone");

		// Item names
		addItem(BloodMagicItems.WEAK_BLOOD_ORB, "Weak Blood Orb");
		addItem(BloodMagicItems.APPRENTICE_BLOOD_ORB, "Apprentice Blood Orb");
		addItem(BloodMagicItems.MAGICIAN_BLOOD_ORB, "Magician Blood Orb");
		addItem(BloodMagicItems.MASTER_BLOOD_ORB, "Master Blood Orb");
		addItem(BloodMagicItems.DIVINATION_SIGIL, "Divination Sigil");
		addItem(BloodMagicItems.WATER_SIGIL, "Water Sigil");
		addItem(BloodMagicItems.LAVA_SIGIL, "Lava Sigil");
		addItem(BloodMagicItems.VOID_SIGIL, "Void Sigil");
		addItem(BloodMagicItems.GREEN_GROVE_SIGIL, "Sigil of the Green Grove");
		addItem(BloodMagicItems.FAST_MINER_SIGIL, "Sigil of the Fast Miner");
		addItem(BloodMagicItems.MAGNETISM_SIGIL, "Sigil of Magnetism");
		addItem(BloodMagicItems.ICE_SIGIL, "Sigil of the Frozen Lake");
		addItem(BloodMagicItems.AIR_SIGIL, "Air Sigil");
		addItem(BloodMagicItems.BLOOD_LIGHT_SIGIL, "Sigil of the Blood Lamp");

		addItem(BloodMagicBlocks.LIFE_ESSENCE_BUCKET, "Bucket of Life");
		addItem(BloodMagicItems.ARCANE_ASHES, "Arcane Ashes");
		addItem(BloodMagicItems.SLATE, "Blank Slate");
		addItem(BloodMagicItems.REINFORCED_SLATE, "Reinforced Slate");
		addItem(BloodMagicItems.IMBUED_SLATE, "Imbued Slate");
		addItem(BloodMagicItems.DEMONIC_SLATE, "Demonic Slate");
		addItem(BloodMagicItems.ETHEREAL_SLATE, "Ethereal Slate");

		addItem(BloodMagicItems.DAGGER_OF_SACRIFICE, "Dagger of Sacrifice");
		addItem(BloodMagicItems.SACRIFICIAL_DAGGER, "Sacrificial Knife");
		addItem(BloodMagicItems.LAVA_CRYSTAL, "Lava Crystal");

		addItem(BloodMagicItems.REAGENT_WATER, "Water Reagent");
		addItem(BloodMagicItems.REAGENT_LAVA, "Lava Reagent");
		addItem(BloodMagicItems.REAGENT_FAST_MINER, "Mining Reagent");
		addItem(BloodMagicItems.REAGENT_GROWTH, "Growth Reagent");
		addItem(BloodMagicItems.REAGENT_VOID, "Void Reagent");
		addItem(BloodMagicItems.REAGENT_MAGNETISM, "Magnetism Reagent");
		addItem(BloodMagicItems.REAGENT_AIR, "Air Reagent");
		addItem(BloodMagicItems.REAGENT_BLOOD_LIGHT, "Blood Lamp Reagent");

		addItem(BloodMagicItems.PETTY_GEM, "Petty Tartaric Gem");
		addItem(BloodMagicItems.LESSER_GEM, "Lesser Tartaric Gem");
		addItem(BloodMagicItems.COMMON_GEM, "Common Tartaric Gem");
		addItem(BloodMagicItems.MONSTER_SOUL_RAW, "Demon Will");
		addItem(BloodMagicItems.MONSTER_SOUL_CORROSIVE, "Demon Will");
		addItem(BloodMagicItems.MONSTER_SOUL_DESTRUCTIVE, "Demon Will");
		addItem(BloodMagicItems.MONSTER_SOUL_STEADFAST, "Demon Will");
		addItem(BloodMagicItems.MONSTER_SOUL_VENGEFUL, "Demon Will");
		addItem(BloodMagicItems.SOUL_SNARE, "Soul Snare");
		addItem(BloodMagicItems.SENTIENT_SWORD, "Sentient Sword");

		addItem(BloodMagicItems.WEAK_ACTIVATION_CRYSTAL, "Weak Activation Crystal");
		addItem(BloodMagicItems.AWAKENED_ACTIVATION_CRYSTAL, "Awakened Activation Crystal");
		addItem(BloodMagicItems.CREATIVE_ACTIVATION_CRYSTAL, "Creative Activation Crystal");
		addItem(BloodMagicItems.WATER_INSCRIPTION_TOOL, "Inscription Tool: Water");
		addItem(BloodMagicItems.FIRE_INSCRIPTION_TOOL, "Inscription Tool: Fire");
		addItem(BloodMagicItems.EARTH_INSCRIPTION_TOOL, "Inscription Tool: Earth");
		addItem(BloodMagicItems.AIR_INSCRIPTION_TOOL, "Inscription Tool: Air");
		addItem(BloodMagicItems.DUSK_INSCRIPTION_TOOL, "Inscription Tool: Dusk");

		addItem(BloodMagicItems.BASE_RITUAL_DIVINER, "Ritual Diviner");
		addItem(BloodMagicItems.DUSK_RITUAL_DIVINER, "Ritual Diviner [Dusk]");

//		addItem(BloodMagicItems , "");

		// JEI
		add("jei.bloodmagic.recipe.minimumsouls", "Minimum: %s Will");
		add("jei.bloodmagic.recipe.soulsdrained", "Drained: %s Will");
		add("jei.bloodmagic.recipe.requiredlp", "LP: %d");
		add("jei.bloodmagic.recipe.requiredtier", "Tier: %d");
		add("jei.bloodmagic.recipe.consumptionrate", "Consumption: %s LP/t");
		add("jei.bloodmagic.recipe.drainrate", "Drain: %s LP/t");

		add("jei.bloodmagic.recipe.altar", "Blood Altar");
		add("jei.bloodmagic.recipe.soulforge", "Hellfire Forge");
		add("jei.bloodmagic.recipe.alchemyarraycrafting", "Alchemy Array");

		// Chat
		add("chat.bloodmagic.ritual.weak", "You feel a push, but are too weak to perform this ritual.");
		add("chat.bloodmagic.ritual.prevent", "The ritual is actively resisting you!");
		add("chat.bloodmagic.ritual.activate", "A rush of energy flows through the ritual!");
		add("chat.bloodmagic.ritual.notValid", "You feel that these runes are not configured correctly...");

	}
}
