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
		// HUD
		add("hud.bloodmagic.inactive", "Inactive");

		// Creative Tab
		add("itemGroup.bloodmagic.creativeTab", "Blood Magic");

		add("chat.bloodmagic.damageSource", "%s's soul became too weak");

		// Tile Entitites
		add("tile.bloodmagic.soulforge.name", "Hellfire Forge");
		add("tile.bloodmagic.arc.name", "Alchemical Reaction Chamber");
		add("tile.bloodmagic.alchemytable.name", "Alchemy Table");

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

		add("tooltip.bloodmagic.sigil.seer.desc", "When seeing all is not enough");
		add("tooltip.bloodmagic.sigil.seer.currentAltarProgress", "Current Progress: %d LP/ %s LP");
		add("tooltip.bloodmagic.sigil.seer.currentAltarProgress.percent", "Current Progress: %s");
		add("tooltip.bloodmagic.sigil.seer.currentAltarConsumptionRate", "Consumption Rate: %d LP");
		add("tooltip.bloodmagic.sigil.seer.currentAltarTier", "Current Tier: %d");
		add("tooltip.bloodmagic.sigil.seer.currentEssence", "Current Essence: %d LP");
		add("tooltip.bloodmagic.sigil.seer.currentAltarCapacity", "Current Capacity: %d LP");
		add("tooltip.bloodmagic.sigil.seer.currentCharge", "Current Charge: %d");
		add("tooltip.bloodmagic.sigil.seer.currentTranquility", "Current Tranquility: %d");
		add("tooltip.bloodmagic.sigil.seer.currentBonus", "Current Bonus: +%d%%");

		add("tooltip.bloodmagic.sigil.holding.press", "Press %s to modify");
		add("tooltip.bloodmagic.sigil.holding.desc", "Sigil-ception");
		add("tooltip.bloodmagic.sigil.holding.sigilInSlot", "Slot %d: %s");

		add("tooltip.bloodmagic.activated", "Activated");
		add("tooltip.bloodmagic.deactivated", "Deactivated");

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
		add("tooltip.bloodmagic.sigil.void.desc", "Better than a Swiffer\u00AE!");
		add("tooltip.bloodmagic.sigil.greengrove.desc", "Environmentally friendly");
		add("tooltip.bloodmagic.sigil.magnetism.desc", "I have a very magnetic personality");
		add("tooltip.bloodmagic.sigil.fastminer.desc", "Keep mining, and mining...");
		add("tooltip.bloodmagic.sigil.air.desc", "I feel lighter already...");
		add("tooltip.bloodmagic.sigil.bloodlight.desc", "I see a light!");

		add("tooltip.bloodmagic.activationcrystal.weak", "Activates low-level rituals");
		add("tooltip.bloodmagic.activationcrystal.awakened", "Activates more powerful rituals");
		add("tooltip.bloodmagic.activationcrystal.creative", "Creative Only - Activates any ritual");

		add("tooltip.bloodmagic.arctool.additionaldrops", "Increases chance of additional outputs by: x%s");
		add("tooltip.bloodmagic.arctool.uses", "Uses remaining: %s");

		add("itemGroup.bloodmagictab", "Blood Magic");

		add("tooltip.bloodmagic.experienceTome", "A book used to store experience");
		add("tooltip.bloodmagic.experienceTome.exp", "Exp: %0.3f");
		add("tooltip.bloodmagic.experienceTome.expLevel", "Level: %d");

		add("tooltip.bloodmagic.throwing_dagger.desc", "Not to be used in the kitchen");

		add("key.bloodmagic.category", "Blood Magic");

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

		add("tooltip.bloodmagic.holdShiftForInfo", "Press shift for extra info");

		add("ritual.bloodmagic.blockRange.tooBig", "The block range given is too big! Needs to be at most %s blocks.");
		add("ritual.bloodmagic.blockRange.tooFar", "The block range given is too far! Needs to be within a vertical range of %s blocks and a horizontal range of %s blocks.");
		add("ritual.bloodmagic.blockRange.inactive", "The ritual stone is currently inactive, and cannot have its range modified.");
		add("ritual.bloodmagic.blockRange.noRange", "The range was not properly chosen.");
		add("ritual.bloodmagic.blockRange.firstBlock", "First block for new range stored.");
		add("ritual.bloodmagic.blockRange.success", "New range successfully set!");
		add("ritual.bloodmagic.willConfig.set", "The ritual will use these Demon Will types: %s");
		add("ritual.bloodmagic.willConfig.void", "The ritual no longer uses Demon Will");

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
		add("ritual.bloodmagic.crystalHarvestRitual", "Crack of the Fractured Crystal");
		add("ritual.bloodmagic.forsakenSoulRitual", "Gathering of the Forsaken Souls");
		add("ritual.bloodmagic.crystalSplitRitual", "Resonance of the Faceted Crystal");
		add("ritual.bloodmagic.ellipseRitual", "Focus of the Ellipsoid");

		add("ritual.bloodmagic.cobblestoneRitual", "Le Vulcanos Frigius");
		add("ritual.bloodmagic.placerRitual", "The Filler");
		add("ritual.bloodmagic.fellingRitual", "The Timberman");
		add("ritual.bloodmagic.pumpRitual", "Hymn of Siphoning");
		add("ritual.bloodmagic.altarBuilderRitual", "The Assembly of the High Altar");
		add("ritual.bloodmagic.portalRitual", "The Gate of the Fold");

		add("ritual.bloodmagic.waterRitual.info", "Generates a source of water from the Master Ritual Stone.");
		add("ritual.bloodmagic.lavaRitual.info", "Generates a source of lava from the master ritual stone.");
		add("ritual.bloodmagic.lavaRitual.default.info", "(Raw) Decreases the LP cost of placing lava and allows the lava to be directly placed into a tank or other fluid-handling block.");
		add("ritual.bloodmagic.lavaRitual.corrosive.info", "(Corrosive) Entities within range that are immune to fire are damaged severely.");
		add("ritual.bloodmagic.lavaRitual.destructive.info", "(Destructive) Lava placement range is increased based on the total amount of Destructive Will in the Aura.");
		add("ritual.bloodmagic.lavaRitual.vengeful.info", "(Vengeful) Entities within range have Fire Fuse applied to them.");
		add("ritual.bloodmagic.lavaRitual.steadfast.info", "(Steadfast) Players within this range have Fire Resistance applied to them.");

		add("ritual.bloodmagic.greenGroveRitual.info", "Grows crops within its area.");
		add("ritual.bloodmagic.jumpRitual.info", "Causes entities to leap up into the air.");
		add("ritual.bloodmagic.wellOfSufferingRitual.info", "Attacks mobs within its damage zone and puts the LP into a nearby blood altar.");
		add("ritual.bloodmagic.featheredKnifeRitual.info", "Drains health from players in its area and puts the LP into a nearby blood altar. LP Gains are affected by Runes of Self Sacrifice.");
		add("ritual.bloodmagic.regenerationRitual.info", "Casts regeneration on entities within its range if they are missing health.");
		add("ritual.bloodmagic.regenerationRitual.default.info", "(Raw)");
		add("ritual.bloodmagic.regenerationRitual.corrosive.info", "(Corrosive) Steals health from non-players inside of its Vampirism range and directly heals players.");
		add("ritual.bloodmagic.regenerationRitual.destructive.info", "(Destructive)");
		add("ritual.bloodmagic.regenerationRitual.vengeful.info", "(Vengeful)");
		add("ritual.bloodmagic.regenerationRitual.steadfast.info", "(Steadfast)");
		add("ritual.bloodmagic.harvestRitual.info", "Harvests plants within its range, dropping the results on the ground.");
		add("ritual.bloodmagic.magneticRitual.info", "Pulls up ores from the ground and puts them into its placement range.");
		add("ritual.bloodmagic.crushingRitual.info", "Breaks blocks within its crushing range and places the items into the linked chest.");
		add("ritual.bloodmagic.crushingRitual.destructive.info", "(Destructive) Blocks are broken down forcefully: all blocks broken are affected by Fortune III.");
		add("ritual.bloodmagic.crushingRitual.steadfast.info", "(Steadfast) Causes all blocks that are broken to be picked up with Silk Touch. Overrides Fortune where applicable.");
		add("ritual.bloodmagic.crushingRitual.corrosive.info", "(Corrosive) All blocks are broken to be processed with a form of Cutting Fluid. Overrides Silk Touch where applicable.");
		add("ritual.bloodmagic.crushingRitual.vengeful.info", "(Vengeful) Compresses the inventory on successful operation. Currently only does one compression per operation.");
		add("ritual.bloodmagic.crushingRitual.default.info", "(Raw) Increases the speed of the ritual based on the total amount of Raw Will in the Aura.");
		add("ritual.bloodmagic.greenGroveRitual.corrosive.info", "(Corrosive) Entities within range are attacked by nearby plants, leeching away their life to feed their own growth.");
		add("ritual.bloodmagic.greenGroveRitual.default.info", "(Raw) Increases the speed of all ritual operations based on the total amount of Raw Will in the Aura.");
		add("ritual.bloodmagic.greenGroveRitual.vengeful.info", "(Vengeful) Increases the rate that a growth tick is successful.");
		add("ritual.bloodmagic.greenGroveRitual.steadfast.info", "(Steadfast) Seeds are replanted and blocks are hydrated within the Hydration range.");
		add("ritual.bloodmagic.greenGroveRitual.destructive.info", "(Destructive) Increases the maximum growth range of the ritual based on the total amount of Destructive Will in the Aura.");
		add("ritual.bloodmagic.featheredKnifeRitual.default.info", "(Raw) Increases the speed of the ritual based on the total amount of Raw Will in the Aura.");
		add("ritual.bloodmagic.featheredKnifeRitual.destructive.info", "(Destructive) Increases the yield of the ritual based on the total amount of Destructive Will in the Aura.");
		add("ritual.bloodmagic.featheredKnifeRitual.vengeful.info", "(Vengeful) Sets the minimum health for sacrificing to 10%%. Overridden by Steadfast for the Owner if active.");
		add("ritual.bloodmagic.featheredKnifeRitual.corrosive.info", "(Corrosive) Uses the player's current Incense Bonus (if any) to increase the yield. Stand near an Incense Altar to maintain this bonus.");
		add("ritual.bloodmagic.featheredKnifeRitual.steadfast.info", "(Steadfast) Sets the minimum health for sacrificing from 30%% to 70%%.");
		add("ritual.bloodmagic.speedRitual.default.info", "(Raw) Increases the velocity caused by the ritual based on total Will.");
		add("ritual.bloodmagic.speedRitual.vengeful.info", "(Vengeful) Prevents adult mobs and players from being transported. Players are transported if paired with Destructive.");
		add("ritual.bloodmagic.speedRitual.destructive.info", "(Destructive) Prevents child mobs and players from being transported. Players are transported if paired with Vengeful.");
		add("ritual.bloodmagic.animalGrowthRitual.vengeful.info", "(Vengeful) Decreases the time it takes for adults to breed again.");
		add("ritual.bloodmagic.animalGrowthRitual.steadfast.info", "(Steadfast) Automatically breeds adults within its area using items in the connected chest.");
		add("ritual.bloodmagic.animalGrowthRitual.default.info", "(Raw) Increases the speed of the ritual based on the total amount of Raw Will in the Aura.");
		add("ritual.bloodmagic.animalGrowthRitual.destructive.info", "(Destructive) Causes adults that have not bred lately to run at mobs and explode.");
		add("ritual.bloodmagic.animalGrowthRitual.corrosive.info", "(Corrosive) Unimplemented.");
		add("ritual.bloodmagic.groundingRitual.info", "Forces entities on the ground and prevents jumping.");
		add("ritual.bloodmagic.groundingRitual.default.info", "(Raw) Affects players.");
		add("ritual.bloodmagic.groundingRitual.corrosive.info", "(Corrosive) Disables gravity (+Vengeful) Applies Levitation.");
		add("ritual.bloodmagic.groundingRitual.destructive.info", "(Destructive) Applies Heavy Heart (increases fall damage) (+Vengeful) Stronger effect.");
		add("ritual.bloodmagic.groundingRitual.steadfast.info", "(Steadfast) Affects Bosses. Doesn't affect bosses that are immune against motion change or immune against potions (except Wither and Ender Dragon).");
		add("ritual.bloodmagic.groundingRitual.vengeful.info", "(Vengeful) Makes effects stronger. (+Corrosive) Applies Levitation. (+Destructive) Higher Heavy Heart amplifier.");
		add("ritual.bloodmagic.condorRitual.info", "Provides flight in an area around the ritual.");
		add("ritual.bloodmagic.eternalSoulRitual.info", "Capable of transferring Life Essence from a Network back into an Altar at a cost.");

		add("ritual.bloodmagic.crystalSplitRitual.info", "Splits apart a well-grown Raw crystal cluster into separately aspected crystal clusters.");
		add("ritual.bloodmagic.fullStomachRitual.info", "Takes food from the linked chest and fills the player's saturation with it.");
		add("ritual.bloodmagic.interdictionRitual.info", "Pushes all mobs within its area away from the master ritual stone.");
		add("ritual.bloodmagic.containmentRitual.info", "Pulls all mobs within its area towards the master ritual stone.");
		add("ritual.bloodmagic.speedRitual.info", "Launches players within its range in the direction of the ritual.");
		add("ritual.bloodmagic.suppressionRitual.info", "Suppresses fluids within its range - deactivating the ritual returns the fluids back to the world.");
		add("ritual.bloodmagic.expulsionRitual.info", "Expels players from its range that are neither the owner nor have a bound blood orb in the chest on top of the master ritual stone.");
		add("ritual.bloodmagic.zephyrRitual.info", "Picks up items within its range and places them into the linked chest.");
		add("ritual.bloodmagic.upgradeRemoveRitual.info", "Removes all upgrades from your Living Armor and gives you the corresponding Upgrade Tomes. These Tomes can be used to re-apply them to your Living Armor.");
		add("ritual.bloodmagic.armourEvolveRitual.info", "Increases the amount of maximum Upgrade Points on your Living Armor to 300.");
		add("ritual.bloodmagic.animalGrowthRitual.info", "Increases the maturity rate of baby animals within its range.");
		add("ritual.bloodmagic.forsakenSoulRitual.info", "Damages mobs within its damage range and when the mob dies a demon crystal within its crystal range will be grown.");
		add("ritual.bloodmagic.crystalHarvestRitual.info", "Breaks Demon Will Crystal Clusters of all aspects within its range, dropping the results on top of the crystals.");
		add("ritual.bloodmagic.placerRitual.info", "Grabs blocks that are inside of the connected inventory and places them into the world.");
		add("ritual.bloodmagic.fellingRitual.info", "A standard tree-cutting machine, this ritual will cut down all trees and leaves within its area and collect the drops.");
		add("ritual.bloodmagic.pumpRitual.info", "Looks around the world and grabs fluids from the defined area. Will only remove and put the fluid into the connected tank if the tank has at least a bucket's worth of the same fluid.");
		add("ritual.bloodmagic.altarBuilderRitual.info", "Builds an altar out of the components inside of the connected inventory.");
		add("ritual.bloodmagic.portalRitual.info", "Creates a portal network based on the activator and the immediately surrounding blocks. Blocks can be changed after activation without changing the network of portals, and portals with the same \"key\" will link together.");
		add("ritual.bloodmagic.meteorRitual.info", "Consumes an item inside of its item range to summon a meteor full of resources from the sky, aimed directly at the ritual.");

		add("ritual.bloodmagic.waterRitual.waterRange.info", "(Water) The area within which the Ritual will place Water Source Blocks.");
		add("ritual.bloodmagic.waterRitual.waterTank.info", "(Raw) The tank that the ritual will place water into.");
		add("ritual.bloodmagic.lavaRitual.lavaRange.info", "(Lava) The area within which the Ritual will place Lava Source Blocks.");
		add("ritual.bloodmagic.lavaRitual.lavaTank.info", "(Raw) The tank that the ritual will place lava into.");
		add("ritual.bloodmagic.lavaRitual.fireFuse.info", "(Vengeful) Entities in this range are afflicted by Fire Fuse.");
		add("ritual.bloodmagic.lavaRitual.fireResist.info", "(Steadfast) Players within this range have Fire Resistance applied to them.");
		add("ritual.bloodmagic.lavaRitual.fireDamage.info", "(Corrosive) Entities within this range that are immune to fire damage are hurt proportionally to the amount of Corrosive Will in the Aura.");
		add("ritual.bloodmagic.greenGroveRitual.growing.info", "(Growth) The area that the ritual will grow plants in.");
		add("ritual.bloodmagic.greenGroveRitual.leech.info", "(Corrosive) Entities in this area have their life drained to grow nearby crops.");
		add("ritual.bloodmagic.greenGroveRitual.hydrate.info", "(Steadfast) Blocks within this range are rehydrated into farmland, and seeds within the area are planted nearby.");
		add("ritual.bloodmagic.jumpRitual.jumpRange.info", "(Jumping) Entities in this range will be launched in the air.");
		add("ritual.bloodmagic.wellOfSufferingRitual.altar.info", "(Altar) The area that the ritual searches for a Blood Altar to deposit its Blood into.");
		add("ritual.bloodmagic.wellOfSufferingRitual.damage.info", "(Damage) All mobs within this area will take damage every second or so until they die. This does not include players, fortunately. ");
		add("ritual.bloodmagic.featheredKnifeRitual.altar.info", "(Altar) This range defines the area that the ritual searches for the Blood Altar. Changing this will either expand or limit the range to a certain region.");
		add("ritual.bloodmagic.featheredKnifeRitual.damage.info", "(Damage) This defines where the ritual will damage a player. Players inside of this range will receive damage over time up to the specified limit.");
		add("ritual.bloodmagic.regenerationRitual.heal.info", "(Healing) Entities within this range will receive a regeneration buff.");
		add("ritual.bloodmagic.regenerationRitual.vampire.info", "(Vampirism) Mobs within this range have their health syphoned to heal players in the Healing range.");
		add("ritual.bloodmagic.harvestRitual.harvestRange.info", "(Harvesting) Plants within this range will be harvested.");
		add("ritual.bloodmagic.magneticRitual.placementRange.info", "(Placement) The area that the ritual will place the grabbed ores into.");
		add("ritual.bloodmagic.crushingRitual.crushingRange.info", "(Crushing) The blocks that the ritual will break.");
		add("ritual.bloodmagic.crushingRitual.chest.info", "(Chest) The location of the inventory that the ritual will place the broken blocks into.");
		add("ritual.bloodmagic.fullStomachRitual.fillRange.info", "(Feeding) The range that the ritual will look at to feed players.");
		add("ritual.bloodmagic.fullStomachRitual.chest.info", "(Chest) The location of the inventory that the ritual will grab food from to feed players in range.");
		add("ritual.bloodmagic.interdictionRitual.interdictionRange.info", "(Push) The area of the ritual where mobs will be pushed. All mobs are pushed away from the master ritual stone, regardless of where this area is.");
		add("ritual.bloodmagic.containmentRitual.containmentRange.info", "(Containment) The area of the ritual where mobs will be pulled. All mobs are pulled towards the master ritual stone, regardless of where this area is.");
		add("ritual.bloodmagic.speedRitual.sanicRange.info", "(Speed) All entities within this area are launched in the direction of the arrow formed by the ritual.");
		add("ritual.bloodmagic.suppressionRitual.suppressionRange.info", "(Suppress) All liquids within the range are suppressed.");
		add("ritual.bloodmagic.expulsionRitual.expulsionRange.info", "(Expulsion) The area from which players that are not owner or have an orb in the chest will be teleported away from.");
		add("ritual.bloodmagic.zephyrRitual.zephyrRange.info", "(Suction) Items within this range will be sucked into the linked chest.");
		add("ritual.bloodmagic.zephyrRitual.chest.info", "(Chest) The location of the inventory that the ritual will place the picked up items into.");
		add("ritual.bloodmagic.animalGrowthRitual.growing.info", "(Growth) Animals within this range will grow much faster.");
		add("ritual.bloodmagic.animalGrowthRitual.chest.info", "(Chest) Chest for breeding items if properly augmented.");
		add("ritual.bloodmagic.forsakenSoulRitual.crystal.info", "(Crystal) Demon Crystals in this range receive an increase in growth speed when a mob is killed by the ritual.");
		add("ritual.bloodmagic.forsakenSoulRitual.damage.info", "(Damage) Mobs within this range will be slowly damaged, and when killed will grow the crystals.");
		add("ritual.bloodmagic.crystalHarvestRitual.crystal.info", "(Crystal) All Demon Will crystal clusters have a single crystal broken off, spawning the crystal into the world. If there is only one crystal on the cluster, it will not break it.");

		add("ritual.bloodmagic.ellipseRitual.info", "Creates a hollow spheroid around the ritual using the blocks in the attached chest.");
		add("ritual.bloodmagic.ellipseRitual.spheroidRange.info", "(Placement) The range that the ritual will place its blocks in. Note that the Spheroid is centered on the ritual - if one side is shorter than the side opposite, the spheroid will be truncated.");
		add("ritual.bloodmagic.ellipseRitual.chest.info", "(Chest) The location of the inventory that the ritual will grab blocks from to place in the world.");

		add("ritual.bloodmagic.placerRitual.placerRange.info", "(Placement) The range that the ritual will place its blocks in.");
		add("ritual.bloodmagic.placerRitual.chest.info", "(Chest) The location of the inventory that the ritual will grab blocks from to place in the world.");
		add("ritual.bloodmagic.fellingRitual.fellingRange.info", "(Cutting) The range that the ritual will search out logs and leaves in order to cut down.");
		add("ritual.bloodmagic.fellingRitual.chest.info", "(Chest) The location of the inventory that the ritual will place the results into.");
		add("ritual.bloodmagic.pumpRitual.pumpRange.info", "(Pump) The region that the ritual will look for fluids to grab from the world.");

		add("tooltip.bloodmagic.ritualReader.currentState", "Current mode: %s");
		add("tooltip.bloodmagic.ritualReader.set_area", "Define Area");
		add("tooltip.bloodmagic.ritualReader.information", "Information");
		add("tooltip.bloodmagic.ritualReader.set_will_types", "Set Will Consumed");
		add("tooltip.bloodmagic.ritualReader.desc.set_area", "Right click on an active Master Ritual stone to cycle what area of the ritual you want to modify. Then click on the two corners of the new range you want to set the range.");
		add("tooltip.bloodmagic.ritualReader.desc.information", "Right click on an active Master Ritual Stone to gather basic information about the ritual.");
		add("tooltip.bloodmagic.ritualReader.desc.set_will_types", "Set the types of demon will that the ritual will consume from the aura by right clicking on the MRS with the same types of crystals on your hotbar.");

		// Living Armour - the 'u' is important, TehNut!
		add("living_upgrade.bloodmagic.arrow_protect", "Pin Cushion");
		add("living_upgrade.bloodmagic.speed", "Quick Feet");
		add("living_upgrade.bloodmagic.digging", "Dwarven Might");
		add("living_upgrade.bloodmagic.poison_resist", "Poison Resistance");
		add("living_upgrade.bloodmagic.fire_resist", "Gift of Ignis");
		add("living_upgrade.bloodmagic.self_sacrifice", "Tough Palms");
		add("living_upgrade.bloodmagic.knockback_resist", "Body Builder");
		add("living_upgrade.bloodmagic.physical_protect", "Tough");
		add("living_upgrade.bloodmagic.health", "Healthy");
		add("living_upgrade.bloodmagic.melee_damage", "Fierce Strike");
		add("living_upgrade.bloodmagic.arrow_shot", "Trick Shot");
		add("living_upgrade.bloodmagic.step_assist", "Step Assist");
		add("living_upgrade.bloodmagic.grim_reaper", "Grim Reaper's Sprint");
		add("living_upgrade.bloodmagic.solar_powered", "Solar Powered");
		add("living_upgrade.bloodmagic.thaumRunicShielding", "Runic Shielding");
		add("living_upgrade.bloodmagic.revealing", "Revealing");
		add("living_upgrade.bloodmagic.experienced", "Experienced");
		add("living_upgrade.bloodmagic.jump", "Strong Legs");
		add("living_upgrade.bloodmagic.fall_protect", "Soft Fall");
		add("living_upgrade.bloodmagic.grave_digger", "Grave Digger");
		add("living_upgrade.bloodmagic.sprint_attack", "Charging Strike");
		add("living_upgrade.bloodmagic.critical_strike", "True Strike");
		add("living_upgrade.bloodmagic.elytra", "Elytra");
		add("living_upgrade.bloodmagic.night_sight", "Nocturnal Prowess");
		add("living_upgrade.bloodmagic.repair", "Repairing");

		add("living_upgrade.bloodmagic.slowness", "Limp Leg");
		add("living_upgrade.bloodmagic.crippledArm", "Crippled Arm");
		add("living_upgrade.bloodmagic.slippery", "Loose Traction");
		add("living_upgrade.bloodmagic.battleHunger", "Battle Hungry");
		add("living_upgrade.bloodmagic.quenched", "Quenched");
		add("living_upgrade.bloodmagic.meleeDecrease", "Dulled Blade");
		add("living_upgrade.bloodmagic.digSlowdown", "Weakened Pick");
		add("living_upgrade.bloodmagic.stormTrooper", "Storm Trooper");
		add("living_upgrade.bloodmagic.slowHeal", "Diseased");
		add("living_upgrade.bloodmagic.disoriented", "Disoriented");

		add("tooltip.bloodmagic.livingarmour.upgrade.level", "%s (Level %d)");
		add("tooltip.bloodmagic.livingarmour.upgrade.progress", "%s (%d/100)");
		add("tooltip.bloodmagic.livingarmour.upgrade.points", "Upgrade points: %s / %s");

		add("tooltip.bloodmagic.livingarmour.extraExtraInfo", "&9-Hold shift + M for progress info-");

		add("tooltip.bloodmagic.slate_vial", "A glass vial infused with a simple slate");
		add("tooltip.bloodmagic.blood_provider.slate.desc", "A simple ampoule containing 500LP");

		add("chat.bloodmagic.living_upgrade_level_increase", "%s has leveled up to %d");

		// Anointments. Doesn't have any spelling to be pedantic about.
		add("anointment.bloodmagic.melee_damage", "Whetstone");
		add("anointment.bloodmagic.silk_touch", "Soft Touch");
		add("anointment.bloodmagic.fortune", "Fortunate");
		add("anointment.bloodmagic.holy_water", "Holy Light");
		add("anointment.bloodmagic.hidden_knowledge", "Miner's Secrets");
		add("anointment.bloodmagic.quick_draw", "Deft Hands");
		add("anointment.bloodmagic.bow_power", "Heavy Shot");
		add("anointment.bloodmagic.looting", "Plundering");
		add("anointment.bloodmagic.smelting", "Heated Tool");

		// Guide
		add("guide.bloodmagic.name", "Sanguine Scientiem");
		add("guide.bloodmagic.landing_text", "\"It is my dear hope that by holding this tome in your hands, I may impart the knowledge of the lost art that is Blood Magic\"$(br)$(o)- Magus Arcana$()");

		// Patchouli Guidebook
		add("patchouli.bloodmagic.common.double_new_line", "$(br2)%s");
		add("patchouli.bloodmagic.arc_processor.fluid", "%dmb of %s");
		add("patchouli.bloodmagic.arc_processor.no_fluid", "None");
		add("patchouli.bloodmagic.living_armour_upgrade_table.level", "Level");
		add("patchouli.bloodmagic.living_armour_upgrade_table.upgrade_points", "Upgrade Points");
		add("patchouli.bloodmagic.ritual_info.activation_cost", "Activation Cost: $(blood)%d LP$()$(br)");
		add("patchouli.bloodmagic.ritual_info.upkeep_cost", "Base Usage Cost: $(blood)%d LP$()$(br)Base Interval: %d Ticks$(br)");
		add("patchouli.bloodmagic.ritual_info.weak_activation_crystal_link", "$(l:bloodmagic:rituals/activation_crystals#weak)%s$(/l)");
		add("patchouli.bloodmagic.ritual_info.awakened_activation_crystal_link", "$(l:bloodmagic:rituals/activation_crystals#awakened)%s$(/l)");
		add("patchouli.bloodmagic.ritual_info.counter_formatter", "%s%s$()$(br)");
		add("patchouli.bloodmagic.ritual_info.text_override_formatter", "\\$(%s)%s\\$()");
		add("patchouli.bloodmagic.ritual_info.info_formatter", "%s$(br2)%s$(br2)%s$(br)%s$(br)%s$(br)%s");
		add("patchouli.bloodmagic.ritual_info.range_formatter", "$(br) $(li)Max Volume: %s$(li)Horizontal Radius: %s$(li)Vertical Radius: %s");
		add("patchouli.bloodmagic.ritual_info.full_range", "Full Range");

		// Keybinds
		add("bloodmagic.keybind.open_holding", "Open Sigil of Holding");
		add("bloodmagic.keybind.cycle_holding_pos", "Cycle Sigil (+)");
		add("bloodmagic.keybind.cycle_holding_neg", "Cycle Sigil (-)");

		// Block names
		addBlock(BloodMagicBlocks.BLANK_RUNE, "Blank Rune");
		addBlock(BloodMagicBlocks.SPEED_RUNE, "Speed Rune");
		addBlock(BloodMagicBlocks.SACRIFICE_RUNE, "Rune of Sacrifice");
		addBlock(BloodMagicBlocks.SELF_SACRIFICE_RUNE, "Rune of Self Sacrifice");
		addBlock(BloodMagicBlocks.DISPLACEMENT_RUNE, "Displacement Rune");
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

		addBlock(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER, "Alchemical Reaction Chamber");
		addBlock(BloodMagicBlocks.ALCHEMY_TABLE, "Alchemy Table");
		addBlock(BloodMagicBlocks.INCENSE_ALTAR, "Incense Altar");

		addBlock(BloodMagicBlocks.BLOODSTONE, "Large Bloodstone Brick");
		addBlock(BloodMagicBlocks.BLOODSTONE_BRICK, "Bloodstone Brick");

		addBlock(BloodMagicBlocks.RAW_CRYSTAL_BLOCK, "Raw Crystal Cluster");
		addBlock(BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK, "Corrosive Crystal Cluster");
		addBlock(BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK, "Destructive Crystal Cluster");
		addBlock(BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK, "Vengeful Crystal Cluster");
		addBlock(BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK, "Steadfast Crystal Cluster");

		addBlock(BloodMagicBlocks.DEMON_CRUCIBLE, "Demon Crucible");
		addBlock(BloodMagicBlocks.DEMON_CRYSTALLIZER, "Demon Crystallizer");

		addBlock(BloodMagicBlocks.WOOD_PATH, "Wooden Path");
		addBlock(BloodMagicBlocks.WOOD_TILE_PATH, "Tiled Wooden Path");
		addBlock(BloodMagicBlocks.STONE_PATH, "Stone Path");
		addBlock(BloodMagicBlocks.STONE_TILE_PATH, "Tiled Stone Path");
		addBlock(BloodMagicBlocks.WORN_STONE_PATH, "Worn Stone Path");
		addBlock(BloodMagicBlocks.WORN_STONE_TILE_PATH, "Tiled Worn Stone Path");
		addBlock(BloodMagicBlocks.OBSIDIAN_PATH, "Obsidian Path");
		addBlock(BloodMagicBlocks.OBSIDIAN_TILE_PATH, "Tiled Obsidian Path");

		addBlock(BloodMagicBlocks.DUNGEON_BRICK_1, "Demon Bricks");
		addBlock(BloodMagicBlocks.DUNGEON_BRICK_2, "Offset Demon Bricks");
		addBlock(BloodMagicBlocks.DUNGEON_BRICK_3, "Long Demon Bricks");
		addBlock(BloodMagicBlocks.DUNGEON_SMALL_BRICK, "Small Demon Bricks");
		addBlock(BloodMagicBlocks.DUNGEON_BRICK_ASSORTED, "Assorted Demon Bricks");

		addBlock(BloodMagicBlocks.DUNGEON_STONE, "Demon Stone");
		addBlock(BloodMagicBlocks.DUNGEON_POLISHED_STONE, "Polished Demon Stone");
		addBlock(BloodMagicBlocks.DUNGEON_TILE, "Demon Stone Tiles");
		addBlock(BloodMagicBlocks.DUNGEON_TILE_SPECIAL, "Accented Demon Stone Tiles");

		addBlock(BloodMagicBlocks.DUNGEON_BRICK_GATE, "Demon Brick Gate");
		addBlock(BloodMagicBlocks.DUNGEON_POLISHED_GATE, "Demon Stone Gate");

		addBlock(BloodMagicBlocks.DUNGEON_BRICK_STAIRS, "Demon Brick Stairs");
		addBlock(BloodMagicBlocks.DUNGEON_POLISHED_STAIRS, "Demon Stone Stairs");

		addBlock(BloodMagicBlocks.DUNGEON_BRICK_WALL, "Demon Brick Wall");
		addBlock(BloodMagicBlocks.DUNGEON_POLISHED_WALL, "Demon Stone Wall");

		addBlock(BloodMagicBlocks.DUNGEON_PILLAR_CAP, "Demon Stone Pillar Cap");
		addBlock(BloodMagicBlocks.DUNGEON_PILLAR_CENTER, "Demon Stone Pillar");
		addBlock(BloodMagicBlocks.DUNGEON_PILLAR_SPECIAL, "Accented Demon Stone Pillar");
		addBlock(BloodMagicBlocks.DUNGEON_EYE, "Demon Eye");

		addBlock(BloodMagicBlocks.DUNGEON_ORE, "Demonite");

		addBlock(BloodMagicBlocks.SHAPED_CHARGE, "Shaped Charge");
		addBlock(BloodMagicBlocks.DEFORESTER_CHARGE, "Deforester Charge");
		addBlock(BloodMagicBlocks.VEINMINE_CHARGE, "Controlled Charge");
		addBlock(BloodMagicBlocks.FUNGAL_CHARGE, "Fungal Charge");

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
		addItem(BloodMagicItems.SEER_SIGIL, "Seer's Sigil");
		addItem(BloodMagicItems.HOLDING_SIGIL, "Sigil of Holding");

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
		addItem(BloodMagicItems.REAGENT_SIGHT, "Sight Reagent");
		addItem(BloodMagicItems.REAGENT_BINDING, "Binding Reagent");
		addItem(BloodMagicItems.REAGENT_HOLDING, "Holding Reagent");

		addItem(BloodMagicItems.PETTY_GEM, "Petty Tartaric Gem");
		addItem(BloodMagicItems.LESSER_GEM, "Lesser Tartaric Gem");
		addItem(BloodMagicItems.COMMON_GEM, "Common Tartaric Gem");
		addItem(BloodMagicItems.GREATER_GEM, "Greater Tartaric Gem");
		addItem(BloodMagicItems.MONSTER_SOUL_RAW, "Demon Will");
		addItem(BloodMagicItems.MONSTER_SOUL_CORROSIVE, "Demon Will");
		addItem(BloodMagicItems.MONSTER_SOUL_DESTRUCTIVE, "Demon Will");
		addItem(BloodMagicItems.MONSTER_SOUL_STEADFAST, "Demon Will");
		addItem(BloodMagicItems.MONSTER_SOUL_VENGEFUL, "Demon Will");
		addItem(BloodMagicItems.SOUL_SNARE, "Soul Snare");
		addItem(BloodMagicItems.SENTIENT_SWORD, "Sentient Sword");
		addItem(BloodMagicItems.SENTIENT_AXE, "Sentient Axe");
		addItem(BloodMagicItems.SENTIENT_PICKAXE, "Sentient Pickaxe");
		addItem(BloodMagicItems.SENTIENT_SHOVEL, "Sentient Shovel");
		addItem(BloodMagicItems.SENTIENT_SCYTHE, "Sentient Scythe");

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
		addItem(BloodMagicItems.RITUAL_READER, "Ritual Tinkerer");

		addItem(BloodMagicItems.WEAK_BLOOD_SHARD, "Weak Blood Shard");
		addItem(BloodMagicItems.RAW_CRYSTAL, "Demon Will Crystal");
		addItem(BloodMagicItems.CORROSIVE_CRYSTAL, "Corrosive Will Crystal");
		addItem(BloodMagicItems.DESTRUCTIVE_CRYSTAL, "Destructive Will Crystal");
		addItem(BloodMagicItems.VENGEFUL_CRYSTAL, "Vengeful Will Crystal");
		addItem(BloodMagicItems.STEADFAST_CRYSTAL, "Steadfast Will Crystal");

		addItem(BloodMagicItems.SANGUINE_REVERTER, "Sanguine Reverter");
		addItem(BloodMagicItems.PRIMITIVE_FURNACE_CELL, "Primitive Fuel Cell");

		addItem(BloodMagicItems.PRIMITIVE_CRYSTALLINE_RESONATOR, "Primitive Resonator");
		addItem(BloodMagicItems.CRYSTALLINE_RESONATOR, "Crystalline Resonator");

		addItem(BloodMagicItems.PRIMITIVE_HYDRATION_CELL, "Primitive Hydration Cell");
		addItem(BloodMagicItems.PRIMITIVE_EXPLOSIVE_CELL, "Primitive Explosive Cell");
		addItem(BloodMagicItems.EXPLOSIVE_POWDER, "Explosive Powder");

		addItem(BloodMagicItems.BASIC_CUTTING_FLUID, "Basic Cutting Fluid");

		addItem(BloodMagicItems.EXPERIENCE_TOME, "Tome of Peritia");

		addItem(BloodMagicItems.LIVING_HELMET, "Living Helmet");
		addItem(BloodMagicItems.LIVING_PLATE, "Living Chestplate");
		addItem(BloodMagicItems.LIVING_LEGGINGS, "Living Leggings");
		addItem(BloodMagicItems.LIVING_BOOTS, "Living Boots");

		addItem(BloodMagicItems.LIVING_TOME, "Living Armour Upgrade Tome");

		addItem(BloodMagicItems.THROWING_DAGGER, "Iron Throwing Dagger");
		addItem(BloodMagicItems.THROWING_DAGGER_SYRINGE, "Syringe Throwing Dagger");
		addItem(BloodMagicItems.SLATE_AMPOULE, "Slate Ampoule");

		// Anointment Items
		addItem(BloodMagicItems.SLATE_VIAL, "Slate-infused Vial");
		addItem(BloodMagicItems.MELEE_DAMAGE_ANOINTMENT, "Honing Oil");
		addItem(BloodMagicItems.SILK_TOUCH_ANOINTMENT, "Soft Coating");
		addItem(BloodMagicItems.FORTUNE_ANOINTMENT, "Fortuna Extract");
		addItem(BloodMagicItems.HOLY_WATER_ANOINTMENT, "Holy Water");
		addItem(BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT, "Miner's Secrets");
		addItem(BloodMagicItems.QUICK_DRAW_ANOINTMENT, "Dexterity Alkahest");
		addItem(BloodMagicItems.BOW_POWER_ANOINTMENT, "Iron Tip");
		addItem(BloodMagicItems.LOOTING_ANOINTMENT, "Plunderer's Glint");
		addItem(BloodMagicItems.SMELTING_ANOINTMENT, "Slow-burning Oil");

		// Alchemy Items
		addItem(BloodMagicItems.PLANT_OIL, "Plant Oil");

		// Sands
		addItem(BloodMagicItems.COAL_SAND, "Coal Sand");
		addItem(BloodMagicItems.IRON_SAND, "Iron Sand");
		addItem(BloodMagicItems.GOLD_SAND, "Gold Sand");
		addItem(BloodMagicItems.NETHERITE_SCRAP_SAND, "Netherite Scrap Sand");
		addItem(BloodMagicItems.SULFUR, "Sulfur");
		addItem(BloodMagicItems.SALTPETER, "Saltpeter");

		// Fragments
		addItem(BloodMagicItems.IRON_FRAGMENT, "Iron Ore Fragment");
		addItem(BloodMagicItems.GOLD_FRAGMENT, "Gold Ore Fragment");
		addItem(BloodMagicItems.NETHERITE_SCRAP_FRAGMENT, "Ancient Debris Fragment");

		// Gravels
		addItem(BloodMagicItems.IRON_GRAVEL, "Iron Gravel");
		addItem(BloodMagicItems.GOLD_GRAVEL, "Gold Gravel");
		addItem(BloodMagicItems.NETHERITE_SCRAP_GRAVEL, "Ancient Debris Gravel");

//		addItem(BloodMagicItems , "");

		// JEI
		add("jei.bloodmagic.recipe.minimumsouls", "Minimum: %s Will");
		add("jei.bloodmagic.recipe.soulsdrained", "Drained: %s Will");
		add("jei.bloodmagic.recipe.requiredlp", "LP: %d");
		add("jei.bloodmagic.recipe.requiredtier", "Tier: %d");
		add("jei.bloodmagic.recipe.consumptionrate", "Consumption: %s LP/t");
		add("jei.bloodmagic.recipe.drainrate", "Drain: %s LP/t");

		add("jei.bloodmagic.recipe.lpDrained", "Drained: %s LP");
		add("jei.bloodmagic.recipe.ticksRequired", "Time: %sTicks");

		add("jei.bloodmagic.recipe.altar", "Blood Altar");
		add("jei.bloodmagic.recipe.soulforge", "Hellfire Forge");
		add("jei.bloodmagic.recipe.alchemyarraycrafting", "Alchemy Array");
		add("jei.bloodmagic.recipe.arc", "ARC Recipe");
		add("jei.bloodmagic.recipe.arcfurnace", "ARC Furnace Recipe");
		add("jei.bloodmagic.recipe.alchemytable", "Alchemy Table");

		// Chat
		add("chat.bloodmagic.ritual.weak", "You feel a push, but are too weak to perform this ritual.");
		add("chat.bloodmagic.ritual.prevent", "The ritual is actively resisting you!");
		add("chat.bloodmagic.ritual.activate", "A rush of energy flows through the ritual!");
		add("chat.bloodmagic.ritual.notValid", "You feel that these runes are not configured correctly...");
		add("chat.bloodmagic.diviner.blockedBuild", "Unable to replace block at %d, %d, %d.");

		// GUI
		add("gui.bloodmagic.empty", "Empty");

	}
}
