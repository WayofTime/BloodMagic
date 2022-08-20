package wayoftime.bloodmagic.common.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.client.sounds.SoundRegistrator;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.arc.ItemARCToolBase;
import wayoftime.bloodmagic.common.item.block.ItemBlockAlchemyTable;
import wayoftime.bloodmagic.common.item.block.ItemBlockMimic;
import wayoftime.bloodmagic.common.item.block.ItemBlockShapedCharge;
import wayoftime.bloodmagic.common.item.dungeon.ItemDungeonKey;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlask;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlaskLingering;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlaskThrowable;
import wayoftime.bloodmagic.common.item.routing.ItemCompositeFilter;
import wayoftime.bloodmagic.common.item.routing.ItemEnchantFilterCore;
import wayoftime.bloodmagic.common.item.routing.ItemModFilter;
import wayoftime.bloodmagic.common.item.routing.ItemNodeRouter;
import wayoftime.bloodmagic.common.item.routing.ItemStandardFilter;
import wayoftime.bloodmagic.common.item.routing.ItemTagFilter;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilAir;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilBloodLight;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilDivination;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilFastMiner;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilFrost;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilGreenGrove;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilLava;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilMagnetism;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilSuppression;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilTeleposition;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilVoid;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilWater;
import wayoftime.bloodmagic.common.item.soul.ItemMonsterSoul;
import wayoftime.bloodmagic.common.item.soul.ItemSentientAxe;
import wayoftime.bloodmagic.common.item.soul.ItemSentientPickaxe;
import wayoftime.bloodmagic.common.item.soul.ItemSentientShovel;
import wayoftime.bloodmagic.common.item.soul.ItemSentientSword;
import wayoftime.bloodmagic.common.item.soul.ItemSoulGem;
import wayoftime.bloodmagic.common.item.soul.ItemSoulSnare;
import wayoftime.bloodmagic.common.registration.impl.BloodOrbDeferredRegister;
import wayoftime.bloodmagic.common.registration.impl.BloodOrbRegistryObject;
import wayoftime.bloodmagic.common.routing.ItemBasicNodeUpgrade;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.structures.ItemDungeonTester;

public class BloodMagicItems
{

//	public static Item.ToolMaterial SOUL_TOOL_MATERIAL = EnumHelper.addToolMaterial("demonic", 4, 520, 7, 8, 50);
//	public static final BloodOrb WEAK_ORB_INSTANCE = new BloodOrb(new ResourceLocation(BloodMagic.MODID, "weakbloodorb"), 0, 5000, 10);
	public static final BloodOrbDeferredRegister BLOOD_ORBS = new BloodOrbDeferredRegister(BloodMagic.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BloodMagic.MODID);
	public static final DeferredRegister<Item> BASICITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BloodMagic.MODID);

	public static final BloodOrbRegistryObject<BloodOrb> ORB_WEAK = BLOOD_ORBS.register("weakbloodorb", () -> new BloodOrb(new ResourceLocation(BloodMagic.MODID, "weakbloodorb"), 1, 5000, 2));
	public static final BloodOrbRegistryObject<BloodOrb> ORB_APPRENTICE = BLOOD_ORBS.register("apprenticebloodorb", () -> new BloodOrb(new ResourceLocation(BloodMagic.MODID, "apprenticebloodorb"), 2, 25000, 5));
	public static final BloodOrbRegistryObject<BloodOrb> ORB_MAGICIAN = BLOOD_ORBS.register("magicianbloodorb", () -> new BloodOrb(new ResourceLocation(BloodMagic.MODID, "magicianbloodorb"), 3, 150000, 15));
	public static final BloodOrbRegistryObject<BloodOrb> ORB_MASTER = BLOOD_ORBS.register("masterbloodorb", () -> new BloodOrb(new ResourceLocation(BloodMagic.MODID, "masterbloodorb"), 4, 1000000, 25));
	public static final BloodOrbRegistryObject<BloodOrb> ORB_ARCHMAGE = BLOOD_ORBS.register("archmagebloodorb", () -> new BloodOrb(new ResourceLocation(BloodMagic.MODID, "archmagebloodorb"), 5, 10000000, 50));
//	public static final DeferredRegister<BloodOrb> BLOOD_ORBS = DeferredRegister.create(RegistrarBloodMagic.BLOOD_ORBS, BloodMagic.MODID);

//	public static final RegistryObject<Item> BLOODSTONE_ITEM = ITEMS.register("ruby_block", () -> new BlockItem(BloodMagicBlocks.BLOODSTONE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> SOUL_FORGE_ITEM = ITEMS.register("soulforge", () -> new BlockItem(BloodMagicBlocks.SOUL_FORGE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> BLANK_RUNE_ITEM = ITEMS.register("blankrune", () -> new BlockItem(BloodMagicBlocks.BLANK_RUNE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> SPEED_RUNE_ITEM = ITEMS.register("speedrune", () -> new BlockItem(BloodMagicBlocks.SPEED_RUNE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> SACRIFICE_RUNE_ITEM = ITEMS.register("sacrificerune", () -> new BlockItem(BloodMagicBlocks.SACRIFICE_RUNE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> SELF_SACRIFICE_RUNE_ITEM = ITEMS.register("selfsacrificerune", () -> new BlockItem(BloodMagicBlocks.SELF_SACRIFICE_RUNE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DISPLACEMENT_RUNE_ITEM = ITEMS.register("dislocationrune", () -> new BlockItem(BloodMagicBlocks.DISPLACEMENT_RUNE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> CAPACITY_RUNE_ITEM = ITEMS.register("altarcapacityrune", () -> new BlockItem(BloodMagicBlocks.CAPACITY_RUNE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> AUGMENTED_CAPACITY_RUNE_ITEM = ITEMS.register("bettercapacityrune", () -> new BlockItem(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> ORB_RUNE_ITEM = ITEMS.register("orbcapacityrune", () -> new BlockItem(BloodMagicBlocks.ORB_RUNE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> ACCELERATION_RUNE_ITEM = ITEMS.register("accelerationrune", () -> new BlockItem(BloodMagicBlocks.ACCELERATION_RUNE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> CHARGING_RUNE_ITEM = ITEMS.register("chargingrune", () -> new BlockItem(BloodMagicBlocks.CHARGING_RUNE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> BLANK_RITUAL_STONE_ITEM = ITEMS.register("ritualstone", () -> new BlockItem(BloodMagicBlocks.BLANK_RITUAL_STONE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> AIR_RITUAL_STONE_ITEM = ITEMS.register("airritualstone", () -> new BlockItem(BloodMagicBlocks.AIR_RITUAL_STONE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> WATER_RITUAL_STONE_ITEM = ITEMS.register("waterritualstone", () -> new BlockItem(BloodMagicBlocks.WATER_RITUAL_STONE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> FIRE_RITUAL_STONE_ITEM = ITEMS.register("fireritualstone", () -> new BlockItem(BloodMagicBlocks.FIRE_RITUAL_STONE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> EARTH_RITUAL_STONE_ITEM = ITEMS.register("earthritualstone", () -> new BlockItem(BloodMagicBlocks.EARTH_RITUAL_STONE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUSK_RITUAL_STONE_ITEM = ITEMS.register("duskritualstone", () -> new BlockItem(BloodMagicBlocks.DUSK_RITUAL_STONE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DAWN_RITUAL_STONE_ITEM = ITEMS.register("lightritualstone", () -> new BlockItem(BloodMagicBlocks.DAWN_RITUAL_STONE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> ALCHEMICAL_REACTION_CHAMBER_ITEM = ITEMS.register("alchemicalreactionchamber", () -> new BlockItem(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DEMON_CRUCIBLE_ITEM = ITEMS.register("demoncrucible", () -> new BlockItem(BloodMagicBlocks.DEMON_CRUCIBLE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DEMON_CRYSTALLIZER_ITEM = ITEMS.register("demoncrystallizer", () -> new BlockItem(BloodMagicBlocks.DEMON_CRYSTALLIZER.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DEMON_PYLON_ITEM = ITEMS.register("demonpylon", () -> new BlockItem(BloodMagicBlocks.DEMON_PYLON.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> SPEED_RUNE_2_ITEM = ITEMS.register("speedrune2", () -> new BlockItem(BloodMagicBlocks.SPEED_RUNE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> SACRIFICE_RUNE_2_ITEM = ITEMS.register("sacrificerune2", () -> new BlockItem(BloodMagicBlocks.SACRIFICE_RUNE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> SELF_SACRIFICE_RUNE_2_ITEM = ITEMS.register("selfsacrificerune2", () -> new BlockItem(BloodMagicBlocks.SELF_SACRIFICE_RUNE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DISPLACEMENT_RUNE_2_ITEM = ITEMS.register("dislocationrune2", () -> new BlockItem(BloodMagicBlocks.DISPLACEMENT_RUNE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> CAPACITY_RUNE_2_ITEM = ITEMS.register("altarcapacityrune2", () -> new BlockItem(BloodMagicBlocks.CAPACITY_RUNE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> AUGMENTED_CAPACITY_RUNE_2_ITEM = ITEMS.register("bettercapacityrune2", () -> new BlockItem(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> ORB_RUNE_2_ITEM = ITEMS.register("orbcapacityrune2", () -> new BlockItem(BloodMagicBlocks.ORB_RUNE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> ACCELERATION_RUNE_2_ITEM = ITEMS.register("accelerationrune2", () -> new BlockItem(BloodMagicBlocks.ACCELERATION_RUNE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> CHARGING_RUNE_2_ITEM = ITEMS.register("chargingrune2", () -> new BlockItem(BloodMagicBlocks.CHARGING_RUNE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> BLOODSTONE_ITEM = ITEMS.register("largebloodstonebrick", () -> new BlockItem(BloodMagicBlocks.BLOODSTONE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> BLOODSTONE_BRICK_ITEM = ITEMS.register("bloodstonebrick", () -> new BlockItem(BloodMagicBlocks.BLOODSTONE_BRICK.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> MASTER_RITUAL_STONE_ITEM = ITEMS.register("masterritualstone", () -> new BlockItem(BloodMagicBlocks.MASTER_RITUAL_STONE.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> BLOOD_ALTAR_ITEM = ITEMS.register("altar", () -> new BlockItem(BloodMagicBlocks.BLOOD_ALTAR.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> ALCHEMY_TABLE_ITEM = ITEMS.register("alchemytable", () -> new ItemBlockAlchemyTable(BloodMagicBlocks.ALCHEMY_TABLE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> INCENSE_ALTAR_ITEM = ITEMS.register("incensealtar", () -> new BlockItem(BloodMagicBlocks.INCENSE_ALTAR.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> TELEPOSER_ITEM = ITEMS.register("teleposer", () -> new BlockItem(BloodMagicBlocks.TELEPOSER.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> WOOD_PATH_ITEM = ITEMS.register("woodbrickpath", () -> new BlockItem(BloodMagicBlocks.WOOD_PATH.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> WOOD_TILE_PATH_ITEM = ITEMS.register("woodtilepath", () -> new BlockItem(BloodMagicBlocks.WOOD_TILE_PATH.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> STONE_PATH_ITEM = ITEMS.register("stonebrickpath", () -> new BlockItem(BloodMagicBlocks.STONE_PATH.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> STONE_TILE_PATH_ITEM = ITEMS.register("stonetilepath", () -> new BlockItem(BloodMagicBlocks.STONE_TILE_PATH.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> WORN_STONE_PATH_ITEM = ITEMS.register("wornstonebrickpath", () -> new BlockItem(BloodMagicBlocks.WORN_STONE_PATH.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> WORN_STONE_TILE_PATH_ITEM = ITEMS.register("wornstonetilepath", () -> new BlockItem(BloodMagicBlocks.WORN_STONE_TILE_PATH.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> OBSIDIAN_PATH_ITEM = ITEMS.register("obsidianbrickpath", () -> new BlockItem(BloodMagicBlocks.OBSIDIAN_PATH.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> OBSIDIAN_TILE_PATH_ITEM = ITEMS.register("obsidiantilepath", () -> new BlockItem(BloodMagicBlocks.OBSIDIAN_TILE_PATH.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> MIMIC_ITEM = ITEMS.register("mimic", () -> new ItemBlockMimic(BloodMagicBlocks.MIMIC.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> MIMIC_ETHEREAL_ITEM = ITEMS.register("ethereal_mimic", () -> new ItemBlockMimic(BloodMagicBlocks.ETHEREAL_MIMIC.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> SPECTRAL_ITEM = ITEMS.register("spectral", () -> new ItemBlockMimic(BloodMagicBlocks.SPECTRAL.get(), new Item.Properties()));

	public static final RegistryObject<Item> NETHE_SOIL_ITEM = ITEMS.register("nether_soil", () -> new BlockItem(BloodMagicBlocks.NETHER_SOIL.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> GROWING_DOUBT_ITEM = ITEMS.register("growing_doubt", () -> new BlockItem(BloodMagicBlocks.GROWING_DOUBT.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> WEAK_TAU_ITEM = ITEMS.register("weak_tau", () -> new BlockItem(BloodMagicBlocks.WEAK_TAU.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> STRONG_TAU_ITEM = ITEMS.register("strong_tau", () -> new BlockItem(BloodMagicBlocks.STRONG_TAU.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> SHAPED_CHARGE_ITEM = ITEMS.register("shaped_charge", () -> new ItemBlockShapedCharge(BloodMagicBlocks.SHAPED_CHARGE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DEFORESTER_CHARGE_ITEM = ITEMS.register("deforester_charge", () -> new ItemBlockShapedCharge(BloodMagicBlocks.DEFORESTER_CHARGE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> VEINMINE_CHARGE_ITEM = ITEMS.register("veinmine_charge", () -> new ItemBlockShapedCharge(BloodMagicBlocks.VEINMINE_CHARGE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> FUNGAL_CHARGE_ITEM = ITEMS.register("fungal_charge", () -> new ItemBlockShapedCharge(BloodMagicBlocks.FUNGAL_CHARGE.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> AUG_SHAPED_CHARGE_ITEM = ITEMS.register("aug_shaped_charge", () -> new ItemBlockShapedCharge(BloodMagicBlocks.AUG_SHAPED_CHARGE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DEFORESTER_CHARGE_2_ITEM = ITEMS.register("deforester_charge_2", () -> new ItemBlockShapedCharge(BloodMagicBlocks.DEFORESTER_CHARGE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> VEINMINE_CHARGE_2_ITEM = ITEMS.register("veinmine_charge_2", () -> new ItemBlockShapedCharge(BloodMagicBlocks.VEINMINE_CHARGE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> FUNGAL_CHARGE_2_ITEM = ITEMS.register("fungal_charge_2", () -> new ItemBlockShapedCharge(BloodMagicBlocks.FUNGAL_CHARGE_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> SHAPED_CHARGE_DEEP_ITEM = ITEMS.register("shaped_charge_deep", () -> new ItemBlockShapedCharge(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get(), new Item.Properties().tab(BloodMagic.TAB)));

	// TODO: Need to rework the above instantiations for the ItemBlocks so that it's
	// done with the Blocks.

//	public static final RegistryObject<Item> WEAK_BLOOD_ORB = BASICITEMS.register("weakbloodorb", ItemBloodOrb::new);
//	public static final RegistryObject<Item> WEAK_BLOOD_ORB = BASICITEMS.register("weakbloodorb", () -> new ItemBloodOrb(WEAK_ORB_INSTANCE));
	public static final RegistryObject<Item> WEAK_BLOOD_ORB = BASICITEMS.register("weakbloodorb", () -> new ItemBloodOrb(ORB_WEAK));
	public static final RegistryObject<Item> APPRENTICE_BLOOD_ORB = BASICITEMS.register("apprenticebloodorb", () -> new ItemBloodOrb(ORB_APPRENTICE));
	public static final RegistryObject<Item> MAGICIAN_BLOOD_ORB = BASICITEMS.register("magicianbloodorb", () -> new ItemBloodOrb(ORB_MAGICIAN));
	public static final RegistryObject<Item> MASTER_BLOOD_ORB = BASICITEMS.register("masterbloodorb", () -> new ItemBloodOrb(ORB_MASTER));
	public static final RegistryObject<Item> ARCHMAGE_BLOOD_ORB = BASICITEMS.register("archmagebloodorb", () -> new ItemBloodOrb(ORB_ARCHMAGE));

	public static final RegistryObject<Item> DIVINATION_SIGIL = BASICITEMS.register("divinationsigil", () -> new ItemSigilDivination(true));
	public static final RegistryObject<Item> SEER_SIGIL = BASICITEMS.register("seersigil", () -> new ItemSigilDivination(false));
	public static final RegistryObject<Item> SACRIFICIAL_DAGGER = BASICITEMS.register("sacrificialdagger", () -> new ItemSacrificialDagger());
	public static final RegistryObject<Item> SLATE = BASICITEMS.register("blankslate", () -> new ItemBase());
	public static final RegistryObject<Item> REINFORCED_SLATE = BASICITEMS.register("reinforcedslate", () -> new ItemBase());
	public static final RegistryObject<Item> IMBUED_SLATE = BASICITEMS.register("infusedslate", () -> new ItemBase());
	public static final RegistryObject<Item> DEMONIC_SLATE = BASICITEMS.register("demonslate", () -> new ItemBase());
	public static final RegistryObject<Item> ETHEREAL_SLATE = BASICITEMS.register("etherealslate", () -> new ItemBase());
	public static final RegistryObject<Item> WATER_SIGIL = BASICITEMS.register("watersigil", () -> new ItemSigilWater());
	public static final RegistryObject<Item> VOID_SIGIL = BASICITEMS.register("voidsigil", () -> new ItemSigilVoid());
	public static final RegistryObject<Item> LAVA_SIGIL = BASICITEMS.register("lavasigil", () -> new ItemSigilLava());
	public static final RegistryObject<Item> GREEN_GROVE_SIGIL = ITEMS.register("growthsigil", () -> new ItemSigilGreenGrove());
	public static final RegistryObject<Item> FAST_MINER_SIGIL = ITEMS.register("miningsigil", () -> new ItemSigilFastMiner());
	public static final RegistryObject<Item> MAGNETISM_SIGIL = ITEMS.register("sigilofmagnetism", () -> new ItemSigilMagnetism());
	public static final RegistryObject<Item> ICE_SIGIL = ITEMS.register("icesigil", () -> new ItemSigilFrost());
	public static final RegistryObject<Item> AIR_SIGIL = BASICITEMS.register("airsigil", ItemSigilAir::new);
	public static final RegistryObject<Item> BLOOD_LIGHT_SIGIL = BASICITEMS.register("bloodlightsigil", ItemSigilBloodLight::new);
	public static final RegistryObject<Item> HOLDING_SIGIL = ITEMS.register("sigilofholding", ItemSigilHolding::new);
	public static final RegistryObject<Item> SUPPRESSION_SIGIL = ITEMS.register("sigilofsuppression", () -> new ItemSigilSuppression());
	public static final RegistryObject<Item> TELEPOSITION_SIGIL = BASICITEMS.register("telepositionsigil", () -> new ItemSigilTeleposition());

	public static final RegistryObject<Item> ARCANE_ASHES = BASICITEMS.register("arcaneashes", () -> new ItemArcaneAshes());
	public static final RegistryObject<Item> DAGGER_OF_SACRIFICE = BASICITEMS.register("daggerofsacrifice", () -> new ItemDaggerOfSacrifice());
	public static final RegistryObject<Item> LAVA_CRYSTAL = BASICITEMS.register("lavacrystal", () -> new ItemLavaCrystal());
	public static final RegistryObject<Item> WEAK_BLOOD_SHARD = BASICITEMS.register("weakbloodshard", () -> new ItemBase());
	public static final RegistryObject<Item> EXPERIENCE_TOME = BASICITEMS.register("experiencebook", () -> new ItemExperienceBook());

	public static final RegistryObject<Item> LIVING_HELMET = BASICITEMS.register("livinghelmet", () -> new ItemLivingArmor(EquipmentSlot.HEAD));
	public static final RegistryObject<Item> LIVING_PLATE = BASICITEMS.register("livingplate", () -> new ItemLivingArmor(EquipmentSlot.CHEST));
	public static final RegistryObject<Item> LIVING_LEGGINGS = BASICITEMS.register("livingleggings", () -> new ItemLivingArmor(EquipmentSlot.LEGS));
	public static final RegistryObject<Item> LIVING_BOOTS = BASICITEMS.register("livingboots", () -> new ItemLivingArmor(EquipmentSlot.FEET));

	public static final RegistryObject<Item> LIVING_TOME = BASICITEMS.register("upgradetome", () -> new ItemLivingTome());
	public static final RegistryObject<Item> LIVING_TRAINER = BASICITEMS.register("upgradetrainer", () -> new ItemLivingTrainer());

	public static final RegistryObject<Item> TELEPOSER_FOCUS = BASICITEMS.register("teleposerfocus", () -> new ItemTeleposerFocus(0));
	public static final RegistryObject<Item> ENHANCED_TELEPOSER_FOCUS = BASICITEMS.register("enhancedteleposerfocus", () -> new ItemTeleposerFocus(1));
	public static final RegistryObject<Item> REINFORCED_TELEPOSER_FOCUS = BASICITEMS.register("reinforcedteleposerfocus", () -> new ItemTeleposerFocus(2));

	// Ritual stuffs
	public static final RegistryObject<Item> WEAK_ACTIVATION_CRYSTAL = BASICITEMS.register("activationcrystalweak", () -> new ItemActivationCrystal(ItemActivationCrystal.CrystalType.WEAK));
	public static final RegistryObject<Item> AWAKENED_ACTIVATION_CRYSTAL = BASICITEMS.register("activationcrystalawakened", () -> new ItemActivationCrystal(ItemActivationCrystal.CrystalType.AWAKENED));
	public static final RegistryObject<Item> CREATIVE_ACTIVATION_CRYSTAL = BASICITEMS.register("activationcrystalcreative", () -> new ItemActivationCrystal(ItemActivationCrystal.CrystalType.CREATIVE));

	public static final RegistryObject<Item> AIR_INSCRIPTION_TOOL = BASICITEMS.register("airscribetool", () -> new ItemInscriptionTool(EnumRuneType.AIR));
	public static final RegistryObject<Item> FIRE_INSCRIPTION_TOOL = BASICITEMS.register("firescribetool", () -> new ItemInscriptionTool(EnumRuneType.FIRE));
	public static final RegistryObject<Item> WATER_INSCRIPTION_TOOL = BASICITEMS.register("waterscribetool", () -> new ItemInscriptionTool(EnumRuneType.WATER));
	public static final RegistryObject<Item> EARTH_INSCRIPTION_TOOL = BASICITEMS.register("earthscribetool", () -> new ItemInscriptionTool(EnumRuneType.EARTH));
	public static final RegistryObject<Item> DUSK_INSCRIPTION_TOOL = BASICITEMS.register("duskscribetool", () -> new ItemInscriptionTool(EnumRuneType.DUSK));

	public static final RegistryObject<Item> BASE_RITUAL_DIVINER = BASICITEMS.register("ritualdiviner", () -> new ItemRitualDiviner(0));
	public static final RegistryObject<Item> DUSK_RITUAL_DIVINER = BASICITEMS.register("ritualdivinerdusk", () -> new ItemRitualDiviner(1));

	public static final RegistryObject<Item> RITUAL_READER = BASICITEMS.register("ritualtinkerer", ItemRitualReader::new);

	// Reagents used to make the Sigils
	public static final RegistryObject<Item> REAGENT_WATER = BASICITEMS.register("reagentwater", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_LAVA = BASICITEMS.register("reagentlava", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_VOID = BASICITEMS.register("reagentvoid", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_GROWTH = BASICITEMS.register("reagentgrowth", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_FAST_MINER = BASICITEMS.register("reagentfastminer", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_MAGNETISM = BASICITEMS.register("reagentmagnetism", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_AIR = BASICITEMS.register("reagentair", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_BLOOD_LIGHT = BASICITEMS.register("reagentbloodlight", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_SIGHT = BASICITEMS.register("reagentsight", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_BINDING = BASICITEMS.register("reagentbinding", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_HOLDING = BASICITEMS.register("reagentholding", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_SUPPRESSION = BASICITEMS.register("reagentsuppression", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_TELEPOSITION = BASICITEMS.register("reagentteleposition", () -> new ItemBase());

	// Tartaric Gems
	public static final RegistryObject<Item> PETTY_GEM = ITEMS.register("soulgempetty", () -> new ItemSoulGem("petty", 64));
	public static final RegistryObject<Item> LESSER_GEM = ITEMS.register("soulgemlesser", () -> new ItemSoulGem("lesser", 256));
	public static final RegistryObject<Item> COMMON_GEM = ITEMS.register("soulgemcommon", () -> new ItemSoulGem("common", 1024));
	public static final RegistryObject<Item> GREATER_GEM = ITEMS.register("soulgemgreater", () -> new ItemSoulGem("greater", 4096));

	public static final RegistryObject<Item> MONSTER_SOUL_RAW = BASICITEMS.register("basemonstersoul", () -> new ItemMonsterSoul(EnumDemonWillType.DEFAULT));
	public static final RegistryObject<Item> MONSTER_SOUL_CORROSIVE = BASICITEMS.register("basemonstersoul_corrosive", () -> new ItemMonsterSoul(EnumDemonWillType.CORROSIVE));
	public static final RegistryObject<Item> MONSTER_SOUL_DESTRUCTIVE = BASICITEMS.register("basemonstersoul_destructive", () -> new ItemMonsterSoul(EnumDemonWillType.DESTRUCTIVE));
	public static final RegistryObject<Item> MONSTER_SOUL_STEADFAST = BASICITEMS.register("basemonstersoul_steadfast", () -> new ItemMonsterSoul(EnumDemonWillType.STEADFAST));
	public static final RegistryObject<Item> MONSTER_SOUL_VENGEFUL = BASICITEMS.register("basemonstersoul_vengeful", () -> new ItemMonsterSoul(EnumDemonWillType.VENGEFUL));

	public static final RegistryObject<Item> SOUL_SNARE = BASICITEMS.register("soulsnare", ItemSoulSnare::new);
	public static final RegistryObject<Item> SENTIENT_SWORD = ITEMS.register("soulsword", () -> new ItemSentientSword());
	public static final RegistryObject<Item> SENTIENT_AXE = ITEMS.register("soulaxe", () -> new ItemSentientAxe());
	public static final RegistryObject<Item> SENTIENT_PICKAXE = ITEMS.register("soulpickaxe", () -> new ItemSentientPickaxe());
	public static final RegistryObject<Item> SENTIENT_SHOVEL = ITEMS.register("soulshovel", () -> new ItemSentientShovel());
	public static final RegistryObject<Item> SENTIENT_SCYTHE = ITEMS.register("soulscythe", () -> new ItemSentientScythe());

	public static final RegistryObject<Item> RAW_CRYSTAL_BLOCK_ITEM = ITEMS.register("rawdemoncrystal", () -> new BlockItem(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> CORROSIVE_CRYSTAL_BLOCK_ITEM = ITEMS.register("corrosivedemoncrystal", () -> new BlockItem(BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DESTRUCTIVE_CRYSTAL_BLOCK_ITEM = ITEMS.register("destructivedemoncrystal", () -> new BlockItem(BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> VENGEFUL_CRYSTAL_BLOCK_ITEM = ITEMS.register("vengefuldemoncrystal", () -> new BlockItem(BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> STEADFAST_CRYSTAL_BLOCK_ITEM = ITEMS.register("steadfastdemoncrystal", () -> new BlockItem(BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> ROUTING_NODE_BLOCK_ITEM = ITEMS.register("itemroutingnode", () -> new BlockItem(BloodMagicBlocks.ROUTING_NODE_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> INPUT_ROUTING_NODE_BLOCK_ITEM = ITEMS.register("inputroutingnode", () -> new BlockItem(BloodMagicBlocks.INPUT_ROUTING_NODE_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> OUTPUT_ROUTING_NODE_BLOCK_ITEM = ITEMS.register("outputroutingnode", () -> new BlockItem(BloodMagicBlocks.OUTPUT_ROUTING_NODE_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> MASTER_ROUTING_NODE_BLOCK_ITEM = ITEMS.register("masterroutingnode", () -> new BlockItem(BloodMagicBlocks.MASTER_ROUTING_NODE_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> RAW_CRYSTAL = BASICITEMS.register("defaultcrystal", () -> new ItemDemonCrystal(EnumDemonWillType.DEFAULT));
	public static final RegistryObject<Item> CORROSIVE_CRYSTAL = BASICITEMS.register("corrosivecrystal", () -> new ItemDemonCrystal(EnumDemonWillType.CORROSIVE));
	public static final RegistryObject<Item> VENGEFUL_CRYSTAL = BASICITEMS.register("vengefulcrystal", () -> new ItemDemonCrystal(EnumDemonWillType.VENGEFUL));
	public static final RegistryObject<Item> DESTRUCTIVE_CRYSTAL = BASICITEMS.register("destructivecrystal", () -> new ItemDemonCrystal(EnumDemonWillType.DESTRUCTIVE));
	public static final RegistryObject<Item> STEADFAST_CRYSTAL = BASICITEMS.register("steadfastcrystal", () -> new ItemDemonCrystal(EnumDemonWillType.STEADFAST));

	public static final RegistryObject<Item> DEMON_WILL_GAUGE = BASICITEMS.register("demonwillgauge", ItemDemonWillGauge::new);
	public static final RegistryObject<Item> ITEM_ROUTER_FILTER = BASICITEMS.register("itemrouterfilterexact", ItemStandardFilter::new);
	public static final RegistryObject<Item> ITEM_TAG_FILTER = BASICITEMS.register("itemrouterfilteroredict", ItemTagFilter::new);
	public static final RegistryObject<Item> ITEM_ENCHANT_FILTER = BASICITEMS.register("itemrouterfilterenchant", ItemEnchantFilterCore::new);
	public static final RegistryObject<Item> ITEM_MOD_FILTER = BASICITEMS.register("itemrouterfiltermoditems", ItemModFilter::new);
	public static final RegistryObject<Item> ITEM_COMPOSITE_FILTER = BASICITEMS.register("itemrouterfiltercomposite", ItemCompositeFilter::new);
	public static final RegistryObject<Item> NODE_ROUTER = BASICITEMS.register("noderouter", ItemNodeRouter::new);
	public static final RegistryObject<Item> MASTER_NODE_UPGRADE = BASICITEMS.register("mastercore", ItemBasicNodeUpgrade::new);

	public static final RegistryObject<Item> FRAME_PARTS = BASICITEMS.register("componentframeparts", ItemBase::new);

	// Misc
	public static final RegistryObject<Item> SYNTHETIC_POINT = BASICITEMS.register("syntheticpoint", () -> new ItemSyntheticPoint());
	public static final RegistryObject<Item> UPGRADE_SCRAPS = BASICITEMS.register("upgradescraps", () -> new ItemLivingTomeScrap());

	public static final RegistryObject<Item> HELLFORGED_PARTS = BASICITEMS.register("hellforgedparts", () -> new ItemBase("hellforgedparts"));

	// ARC Tools
	public static final RegistryObject<Item> SANGUINE_REVERTER = BASICITEMS.register("sanguinereverter", () -> new ItemARCToolBase(32, 2, EnumDemonWillType.STEADFAST));
	public static final RegistryObject<Item> PRIMITIVE_FURNACE_CELL = BASICITEMS.register("furnacecell_primitive", () -> new ItemARCToolBase(128, 3));

	public static final RegistryObject<Item> EXPLOSIVE_POWDER = BASICITEMS.register("explosivepowder", () -> new ItemARCToolBase(64, 1, EnumDemonWillType.DESTRUCTIVE));
	public static final RegistryObject<Item> PRIMITIVE_EXPLOSIVE_CELL = BASICITEMS.register("primitive_explosive_cell", () -> new ItemARCToolBase(256, 1.5, EnumDemonWillType.DESTRUCTIVE));
	public static final RegistryObject<Item> HELLFORGED_EXPLOSIVE_CELL = BASICITEMS.register("hellforged_explosive_cell", () -> new ItemARCToolBase(1024, 2, EnumDemonWillType.DESTRUCTIVE));

	public static final RegistryObject<Item> PRIMITIVE_HYDRATION_CELL = BASICITEMS.register("primitive_hydration_cell", () -> new ItemARCToolBase(128, 1.5));

	public static final RegistryObject<Item> RESONATOR = BASICITEMS.register("resonator", () -> new ItemARCToolBase(64, 1, EnumDemonWillType.VENGEFUL));
	public static final RegistryObject<Item> PRIMITIVE_CRYSTALLINE_RESONATOR = BASICITEMS.register("primitive_crystalline_resonator", () -> new ItemARCToolBase(256, 1.5, EnumDemonWillType.VENGEFUL));
	public static final RegistryObject<Item> HELLFORGED_RESONATOR = BASICITEMS.register("hellforged_resonator", () -> new ItemARCToolBase(1024, 2, 2, EnumDemonWillType.VENGEFUL));

	// Alchemy Table items
	public static final RegistryObject<Item> BASIC_CUTTING_FLUID = BASICITEMS.register("basiccuttingfluid", () -> new ItemARCToolBase(64, 1, EnumDemonWillType.CORROSIVE));
	public static final RegistryObject<Item> INTERMEDIATE_CUTTING_FLUID = BASICITEMS.register("intermediatecuttingfluid", () -> new ItemARCToolBase(256, 1.5, EnumDemonWillType.CORROSIVE));
	public static final RegistryObject<Item> ADVANCED_CUTTING_FLUID = BASICITEMS.register("advancedcuttingfluid", () -> new ItemARCToolBase(1024, 2, 2, EnumDemonWillType.CORROSIVE));

	public static final RegistryObject<Item> SULFUR = BASICITEMS.register("sulfur", () -> new ItemBase());
	public static final RegistryObject<Item> SALTPETER = BASICITEMS.register("saltpeter", () -> new ItemBase());
	public static final RegistryObject<Item> PLANT_OIL = BASICITEMS.register("plantoil", () -> new ItemBase());
	public static final RegistryObject<Item> TAU_OIL = BASICITEMS.register("tauoil", () -> new ItemBase());

	public static final RegistryObject<Item> THROWING_DAGGER_SYRINGE = BASICITEMS.register("throwing_dagger_syringe", ItemThrowingDaggerSyringe::new);
	public static final RegistryObject<Item> THROWING_DAGGER = BASICITEMS.register("throwing_dagger", ItemIronThrowingDagger::new);
	public static final RegistryObject<Item> THROWING_DAGGER_COPPER = BASICITEMS.register("amethystthrowingdagger", ItemThrowingDagger::new);
	public static final RegistryObject<Item> THROWING_DAGGER_COPPER_POTION = ITEMS.register("tippedamethystthrowingdagger", ItemTippedThrowingDagger::new);
	public static final RegistryObject<Item> SLATE_AMPOULE = BASICITEMS.register("slate_ampoule", () -> new ItemBloodProvider("slate", 500));

	public static final RegistryObject<Item> SIMPLE_CATALYST = BASICITEMS.register("simplecatalyst", () -> new ItemBase());
	public static final RegistryObject<Item> STRENGTHENED_CATALYST = BASICITEMS.register("strengthenedcatalyst", () -> new ItemBase());
	public static final RegistryObject<Item> MUNDANE_POWER_CATALYST = BASICITEMS.register("mundanepowercatalyst", () -> new ItemBase());
	public static final RegistryObject<Item> MUNDANE_LENGTHENING_CATALYST = BASICITEMS.register("mundanelengtheningcatalyst", () -> new ItemBase());
	public static final RegistryObject<Item> COMBINATIONAL_CATALYST = BASICITEMS.register("combinationalcatalyst", () -> new ItemBase());
	public static final RegistryObject<Item> WEAK_FILLING_AGENT = BASICITEMS.register("weakfillingagent", () -> new ItemBase());
	public static final RegistryObject<Item> CYCLING_CATALYST = BASICITEMS.register("cyclingcatalyst", () -> new ItemBase());
	public static final RegistryObject<Item> AVERAGE_POWER_CATALYST = BASICITEMS.register("averagepowercatalyst", () -> new ItemBase());
	public static final RegistryObject<Item> AVERAGE_LENGTHENING_CATALYST = BASICITEMS.register("averagelengtheningcatalyst", () -> new ItemBase());
	public static final RegistryObject<Item> AVERAGE_FILLING_AGENT = BASICITEMS.register("standardfillingagent", () -> new ItemBase());

	public static final RegistryObject<Item> RAW_CRYSTAL_CATALYST = BASICITEMS.register("rawcatalyst", () -> new ItemCrystalCatalyst(EnumDemonWillType.DEFAULT, 200, 10, 25, 400));
	public static final RegistryObject<Item> CORROSIVE_CRYSTAL_CATALYST = BASICITEMS.register("corrosivecatalyst", () -> new ItemCrystalCatalyst(EnumDemonWillType.CORROSIVE, 200, 10, 25, 400));
	public static final RegistryObject<Item> DESTRUCTIVE_CRYSTAL_CATALYST = BASICITEMS.register("destructivecatalyst", () -> new ItemCrystalCatalyst(EnumDemonWillType.DESTRUCTIVE, 200, 10, 25, 400));
	public static final RegistryObject<Item> STEADFAST_CRYSTAL_CATALYST = BASICITEMS.register("steadfastcatalyst", () -> new ItemCrystalCatalyst(EnumDemonWillType.STEADFAST, 200, 10, 25, 400));
	public static final RegistryObject<Item> VENGEFUL_CRYSTAL_CATALYST = BASICITEMS.register("vengefulcatalyst", () -> new ItemCrystalCatalyst(EnumDemonWillType.VENGEFUL, 200, 10, 25, 400));

//	public static final RegistryObject<Item> STRENGTHENED_CATALYST = BASICITEMS.register("strengthenedcatalyst", () -> new ItemBase());
//	public static final RegistryObject<Item> CONCENTRATED_CATALYST = BASICITEMS.register("concentratedcatalyst", () -> new ItemBase());

	// Anointments
	public static final RegistryObject<Item> SLATE_VIAL = ITEMS.register("slate_vial", () -> new ItemBase(16, "slate_vial"));
	public static final RegistryObject<Item> MELEE_DAMAGE_ANOINTMENT = ITEMS.register("melee_anointment", () -> new ItemAnointmentProvider(BloodMagic.rl("melee_damage"), 0xFF0000, 1, 256));
	public static final RegistryObject<Item> SILK_TOUCH_ANOINTMENT = ITEMS.register("silk_touch_anointment", () -> new ItemAnointmentProvider(BloodMagic.rl("silk_touch"), 0x00B0FF, 1, 256));
	public static final RegistryObject<Item> FORTUNE_ANOINTMENT = ITEMS.register("fortune_anointment", () -> new ItemAnointmentProvider(BloodMagic.rl("fortune"), 3381504, 1, 256));
	public static final RegistryObject<Item> HOLY_WATER_ANOINTMENT = ITEMS.register("holy_water_anointment", () -> new ItemAnointmentProvider(BloodMagic.rl("holy_water"), 0xC6E6FB, 1, 256));
	public static final RegistryObject<Item> HIDDEN_KNOWLEDGE_ANOINTMENT = ITEMS.register("hidden_knowledge_anointment", () -> new ItemAnointmentProvider(BloodMagic.rl("hidden_knowledge"), 0xC8F902, 1, 256));
	public static final RegistryObject<Item> QUICK_DRAW_ANOINTMENT = ITEMS.register("quick_draw_anointment", () -> new ItemBowAnointmentProvider(BloodMagic.rl("quick_draw"), 0xF0E130, 1, 256, true));
	public static final RegistryObject<Item> LOOTING_ANOINTMENT = ITEMS.register("looting_anointment", () -> new ItemAnointmentProvider(BloodMagic.rl("looting"), 0x6D2AFF, 1, 256));
	public static final RegistryObject<Item> BOW_POWER_ANOINTMENT = ITEMS.register("bow_power_anointment", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_power"), 0xD8D8D8, 1, 256, true));
	public static final RegistryObject<Item> WILL_POWER_ANOINTMENT = ITEMS.register("will_power_anointment", () -> new ItemAnointmentProvider(BloodMagic.rl("will_power"), 0xD8D8D8, 1, 256));
	public static final RegistryObject<Item> SMELTING_ANOINTMENT = ITEMS.register("smelting_anointment", () -> new ItemAnointmentProvider(BloodMagic.rl("smelting"), 0xCE2029, 1, 256));
	public static final RegistryObject<Item> VOIDING_ANOINTMENT = ITEMS.register("voiding_anointment", () -> new ItemAnointmentProvider(BloodMagic.rl("voiding"), 0x451B52, 1, 256));
	public static final RegistryObject<Item> BOW_VELOCITY_ANOINTMENT = ITEMS.register("bow_velocity_anointment", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_velocity"), 0xFFBE00, 1, 256, true));
	public static final RegistryObject<Item> WEAPON_REPAIR_ANOINTMENT = ITEMS.register("weapon_repair_anointment", () -> new ItemAnointmentProvider(BloodMagic.rl("repairing"), 0xD35525, 1, 256));

	public static final RegistryObject<Item> BOW_POWER_ANOINTMENT_STRONG = ITEMS.register("bow_power_anointment_strong", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_power"), 0xD8D8D8, 2, 1024, true));

	public static final RegistryObject<Item> MELEE_DAMAGE_ANOINTMENT_L = ITEMS.register("melee_anointment_l", () -> new ItemAnointmentProvider(BloodMagic.rl("melee_damage"), 0xFF0000, 1, 1024));
	public static final RegistryObject<Item> SILK_TOUCH_ANOINTMENT_L = ITEMS.register("silk_touch_anointment_l", () -> new ItemAnointmentProvider(BloodMagic.rl("silk_touch"), 0x00B0FF, 1, 1024));
	public static final RegistryObject<Item> FORTUNE_ANOINTMENT_L = ITEMS.register("fortune_anointment_l", () -> new ItemAnointmentProvider(BloodMagic.rl("fortune"), 3381504, 1, 1024));
	public static final RegistryObject<Item> HOLY_WATER_ANOINTMENT_L = ITEMS.register("holy_water_anointment_l", () -> new ItemAnointmentProvider(BloodMagic.rl("holy_water"), 0xC6E6FB, 1, 1024));
	public static final RegistryObject<Item> HIDDEN_KNOWLEDGE_ANOINTMENT_L = ITEMS.register("hidden_knowledge_anointment_l", () -> new ItemAnointmentProvider(BloodMagic.rl("hidden_knowledge"), 0xC8F902, 1, 1024));
	public static final RegistryObject<Item> QUICK_DRAW_ANOINTMENT_L = ITEMS.register("quick_draw_anointment_l", () -> new ItemBowAnointmentProvider(BloodMagic.rl("quick_draw"), 0xF0E130, 1, 1024, true));
	public static final RegistryObject<Item> LOOTING_ANOINTMENT_L = ITEMS.register("looting_anointment_l", () -> new ItemAnointmentProvider(BloodMagic.rl("looting"), 0x6D2AFF, 1, 1024));
	public static final RegistryObject<Item> BOW_POWER_ANOINTMENT_L = ITEMS.register("bow_power_anointment_l", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_power"), 0xD8D8D8, 1, 1024, true));
	public static final RegistryObject<Item> SMELTING_ANOINTMENT_L = ITEMS.register("smelting_anointment_l", () -> new ItemAnointmentProvider(BloodMagic.rl("smelting"), 0xCE2029, 1, 1024));
	public static final RegistryObject<Item> VOIDING_ANOINTMENT_L = ITEMS.register("voiding_anointment_l", () -> new ItemAnointmentProvider(BloodMagic.rl("voiding"), 0x451B52, 1, 1024));
	public static final RegistryObject<Item> BOW_VELOCITY_ANOINTMENT_L = ITEMS.register("bow_velocity_anointment_l", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_velocity"), 0xFFBE00, 1, 1024, true));
	public static final RegistryObject<Item> WEAPON_REPAIR_ANOINTMENT_L = ITEMS.register("weapon_repair_anointment_l", () -> new ItemAnointmentProvider(BloodMagic.rl("repairing"), 0xD35525, 1, 1024));

	public static final RegistryObject<Item> MELEE_DAMAGE_ANOINTMENT_2 = ITEMS.register("melee_anointment_2", () -> new ItemAnointmentProvider(BloodMagic.rl("melee_damage"), 0xFF0000, 2, 256));
	public static final RegistryObject<Item> FORTUNE_ANOINTMENT_2 = ITEMS.register("fortune_anointment_2", () -> new ItemAnointmentProvider(BloodMagic.rl("fortune"), 3381504, 2, 256));
	public static final RegistryObject<Item> HOLY_WATER_ANOINTMENT_2 = ITEMS.register("holy_water_anointment_2", () -> new ItemAnointmentProvider(BloodMagic.rl("holy_water"), 0xC6E6FB, 2, 256));
	public static final RegistryObject<Item> HIDDEN_KNOWLEDGE_ANOINTMENT_2 = ITEMS.register("hidden_knowledge_anointment_2", () -> new ItemAnointmentProvider(BloodMagic.rl("hidden_knowledge"), 0xC8F902, 2, 256));
	public static final RegistryObject<Item> QUICK_DRAW_ANOINTMENT_2 = ITEMS.register("quick_draw_anointment_2", () -> new ItemBowAnointmentProvider(BloodMagic.rl("quick_draw"), 0xF0E130, 2, 256, true));
	public static final RegistryObject<Item> LOOTING_ANOINTMENT_2 = ITEMS.register("looting_anointment_2", () -> new ItemAnointmentProvider(BloodMagic.rl("looting"), 0x6D2AFF, 2, 256));
	public static final RegistryObject<Item> BOW_POWER_ANOINTMENT_2 = ITEMS.register("bow_power_anointment_2", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_power"), 0xD8D8D8, 2, 256, true));
	public static final RegistryObject<Item> BOW_VELOCITY_ANOINTMENT_2 = ITEMS.register("bow_velocity_anointment_2", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_velocity"), 0xFFBE00, 2, 256, true));
	public static final RegistryObject<Item> WEAPON_REPAIR_ANOINTMENT_2 = ITEMS.register("weapon_repair_anointment_2", () -> new ItemAnointmentProvider(BloodMagic.rl("repairing"), 0xD35525, 2, 256));

	public static final RegistryObject<Item> MELEE_DAMAGE_ANOINTMENT_XL = ITEMS.register("melee_anointment_xl", () -> new ItemAnointmentProvider(BloodMagic.rl("melee_damage"), 0xFF0000, 1, 4096));
	public static final RegistryObject<Item> SILK_TOUCH_ANOINTMENT_XL = ITEMS.register("silk_touch_anointment_xl", () -> new ItemAnointmentProvider(BloodMagic.rl("silk_touch"), 0x00B0FF, 1, 4096));
	public static final RegistryObject<Item> FORTUNE_ANOINTMENT_XL = ITEMS.register("fortune_anointment_xl", () -> new ItemAnointmentProvider(BloodMagic.rl("fortune"), 3381504, 1, 4096));
	public static final RegistryObject<Item> HOLY_WATER_ANOINTMENT_XL = ITEMS.register("holy_water_anointment_xl", () -> new ItemAnointmentProvider(BloodMagic.rl("holy_water"), 0xC6E6FB, 1, 4096));
	public static final RegistryObject<Item> HIDDEN_KNOWLEDGE_ANOINTMENT_XL = ITEMS.register("hidden_knowledge_anointment_xl", () -> new ItemAnointmentProvider(BloodMagic.rl("hidden_knowledge"), 0xC8F902, 1, 4096));
	public static final RegistryObject<Item> QUICK_DRAW_ANOINTMENT_XL = ITEMS.register("quick_draw_anointment_xl", () -> new ItemBowAnointmentProvider(BloodMagic.rl("quick_draw"), 0xF0E130, 1, 4096, true));
	public static final RegistryObject<Item> LOOTING_ANOINTMENT_XL = ITEMS.register("looting_anointment_xl", () -> new ItemAnointmentProvider(BloodMagic.rl("looting"), 0x6D2AFF, 1, 4096));
	public static final RegistryObject<Item> BOW_POWER_ANOINTMENT_XL = ITEMS.register("bow_power_anointment_xl", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_power"), 0xD8D8D8, 1, 4096, true));
	public static final RegistryObject<Item> SMELTING_ANOINTMENT_XL = ITEMS.register("smelting_anointment_xl", () -> new ItemAnointmentProvider(BloodMagic.rl("smelting"), 0xCE2029, 1, 4096));
	public static final RegistryObject<Item> VOIDING_ANOINTMENT_XL = ITEMS.register("voiding_anointment_xl", () -> new ItemAnointmentProvider(BloodMagic.rl("voiding"), 0x451B52, 1, 4096));
	public static final RegistryObject<Item> BOW_VELOCITY_ANOINTMENT_XL = ITEMS.register("bow_velocity_anointment_xl", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_velocity"), 0xFFBE00, 1, 4096, true));
	public static final RegistryObject<Item> WEAPON_REPAIR_ANOINTMENT_XL = ITEMS.register("weapon_repair_anointment_xl", () -> new ItemAnointmentProvider(BloodMagic.rl("repairing"), 0xD35525, 1, 4096));

	public static final RegistryObject<Item> MELEE_DAMAGE_ANOINTMENT_3 = ITEMS.register("melee_anointment_3", () -> new ItemAnointmentProvider(BloodMagic.rl("melee_damage"), 0xFF0000, 3, 256));
	public static final RegistryObject<Item> FORTUNE_ANOINTMENT_3 = ITEMS.register("fortune_anointment_3", () -> new ItemAnointmentProvider(BloodMagic.rl("fortune"), 3381504, 3, 256));
	public static final RegistryObject<Item> HOLY_WATER_ANOINTMENT_3 = ITEMS.register("holy_water_anointment_3", () -> new ItemAnointmentProvider(BloodMagic.rl("holy_water"), 0xC6E6FB, 3, 256));
	public static final RegistryObject<Item> HIDDEN_KNOWLEDGE_ANOINTMENT_3 = ITEMS.register("hidden_knowledge_anointment_3", () -> new ItemAnointmentProvider(BloodMagic.rl("hidden_knowledge"), 0xC8F902, 3, 256));
	public static final RegistryObject<Item> QUICK_DRAW_ANOINTMENT_3 = ITEMS.register("quick_draw_anointment_3", () -> new ItemBowAnointmentProvider(BloodMagic.rl("quick_draw"), 0xF0E130, 3, 256, true));
	public static final RegistryObject<Item> LOOTING_ANOINTMENT_3 = ITEMS.register("looting_anointment_3", () -> new ItemAnointmentProvider(BloodMagic.rl("looting"), 0x6D2AFF, 3, 256));
	public static final RegistryObject<Item> BOW_POWER_ANOINTMENT_3 = ITEMS.register("bow_power_anointment_3", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_power"), 0xD8D8D8, 3, 256, true));
	public static final RegistryObject<Item> BOW_VELOCITY_ANOINTMENT_3 = ITEMS.register("bow_velocity_anointment_3", () -> new ItemBowAnointmentProvider(BloodMagic.rl("bow_velocity"), 0xFFBE00, 3, 256, true));
	public static final RegistryObject<Item> WEAPON_REPAIR_ANOINTMENT_3 = ITEMS.register("weapon_repair_anointment_3", () -> new ItemAnointmentProvider(BloodMagic.rl("repairing"), 0xD35525, 3, 256));

	public static final RegistryObject<Item> ALCHEMY_FLASK = ITEMS.register("alchemy_flask", () -> new ItemAlchemyFlask());
	public static final RegistryObject<Item> ALCHEMY_FLASK_THROWABLE = ITEMS.register("alchemy_flask_throwable", () -> new ItemAlchemyFlaskThrowable());
	public static final RegistryObject<Item> ALCHEMY_FLASK_LINGERING = ITEMS.register("alchemy_flask_lingering", () -> new ItemAlchemyFlaskLingering());

	// Fragments
	public static final RegistryObject<Item> IRON_FRAGMENT = BASICITEMS.register("ironfragment", () -> new ItemBase());
	public static final RegistryObject<Item> GOLD_FRAGMENT = BASICITEMS.register("goldfragment", () -> new ItemBase());
	public static final RegistryObject<Item> COPPER_FRAGMENT = BASICITEMS.register("copperfragment", () -> new ItemBase());
	public static final RegistryObject<Item> NETHERITE_SCRAP_FRAGMENT = BASICITEMS.register("fragment_netherite_scrap", () -> new ItemBase());
	public static final RegistryObject<Item> DEMONITE_FRAGMENT = BASICITEMS.register("demonitefragment", () -> new ItemBase());

	// Gravels
	public static final RegistryObject<Item> IRON_GRAVEL = BASICITEMS.register("irongravel", () -> new ItemBase());
	public static final RegistryObject<Item> GOLD_GRAVEL = BASICITEMS.register("goldgravel", () -> new ItemBase());
	public static final RegistryObject<Item> COPPER_GRAVEL = BASICITEMS.register("coppergravel", () -> new ItemBase());
	public static final RegistryObject<Item> NETHERITE_SCRAP_GRAVEL = BASICITEMS.register("gravel_netherite_scrap", () -> new ItemBase());
	public static final RegistryObject<Item> DEMONITE_GRAVEL = BASICITEMS.register("demonitegravel", () -> new ItemBase());

	// Sands
	public static final RegistryObject<Item> IRON_SAND = BASICITEMS.register("ironsand", () -> new ItemBase());
	public static final RegistryObject<Item> GOLD_SAND = BASICITEMS.register("goldsand", () -> new ItemBase());
	public static final RegistryObject<Item> COPPER_SAND = BASICITEMS.register("coppersand", () -> new ItemBase());
	public static final RegistryObject<Item> COAL_SAND = BASICITEMS.register("coalsand", () -> new ItemBase());
	public static final RegistryObject<Item> NETHERITE_SCRAP_SAND = BASICITEMS.register("sand_netherite", () -> new ItemBase());
	public static final RegistryObject<Item> CORRUPTED_DUST = BASICITEMS.register("corrupted_dust", () -> new ItemBase());
	public static final RegistryObject<Item> CORRUPTED_DUST_TINY = BASICITEMS.register("corrupted_tinydust", () -> new ItemBase());
	public static final RegistryObject<Item> HELLFORGED_SAND = BASICITEMS.register("sand_hellforged", () -> new ItemBase());

	// Dungeons
	public static final RegistryObject<Item> DEMONITE_RAW = BASICITEMS.register("rawdemonite", () -> new ItemBase());
	public static final RegistryObject<Item> HELLFORGED_INGOT = BASICITEMS.register("ingot_hellforged", () -> new ItemBase());

	public static final RegistryObject<Item> DUNGEON_BRICK_1_BLOCK = ITEMS.register("dungeon_brick1", () -> new BlockItem(BloodMagicBlocks.DUNGEON_BRICK_1.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_BRICK_2_BLOCK = ITEMS.register("dungeon_brick2", () -> new BlockItem(BloodMagicBlocks.DUNGEON_BRICK_2.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_BRICK_3_BLOCK = ITEMS.register("dungeon_brick3", () -> new BlockItem(BloodMagicBlocks.DUNGEON_BRICK_3.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_BRICK_ASSORTED_BLOCK = ITEMS.register("dungeon_brick_assorted", () -> new BlockItem(BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_STONE_BLOCK = ITEMS.register("dungeon_stone", () -> new BlockItem(BloodMagicBlocks.DUNGEON_STONE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_EYE = ITEMS.register("dungeon_eye", () -> new BlockItem(BloodMagicBlocks.DUNGEON_EYE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_POLISHED_STONE_BLOCK = ITEMS.register("dungeon_polished", () -> new BlockItem(BloodMagicBlocks.DUNGEON_POLISHED_STONE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_TILE_BLOCK = ITEMS.register("dungeon_tile", () -> new BlockItem(BloodMagicBlocks.DUNGEON_TILE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_TILE_SPECIAL_BLOCK = ITEMS.register("dungeon_tilespecial", () -> new BlockItem(BloodMagicBlocks.DUNGEON_TILE_SPECIAL.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_SMALL_BRICK_BLOCK = ITEMS.register("dungeon_smallbrick", () -> new BlockItem(BloodMagicBlocks.DUNGEON_SMALL_BRICK.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_EMITTER = ITEMS.register("dungeon_emitter", () -> new BlockItem(BloodMagicBlocks.DUNGEON_EMITTER.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_ALTERNATOR = ITEMS.register("dungeon_alternator", () -> new BlockItem(BloodMagicBlocks.DUNGEON_ALTERNATOR.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> DUNGEON_BRICK_STAIRS_BLOCK = ITEMS.register("dungeon_brick_stairs", () -> new BlockItem(BloodMagicBlocks.DUNGEON_BRICK_STAIRS.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_POLISHED_STAIRS_BLOCK = ITEMS.register("dungeon_polished_stairs", () -> new BlockItem(BloodMagicBlocks.DUNGEON_POLISHED_STAIRS.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_PILLAR_CENTER_BLOCK = ITEMS.register("dungeon_pillar_center", () -> new BlockItem(BloodMagicBlocks.DUNGEON_PILLAR_CENTER.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_PILLAR_SPECIAL_BLOCK = ITEMS.register("dungeon_pillar_special", () -> new BlockItem(BloodMagicBlocks.DUNGEON_PILLAR_SPECIAL.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_PILLAR_CAP_BLOCK = ITEMS.register("dungeon_pillar_cap", () -> new BlockItem(BloodMagicBlocks.DUNGEON_PILLAR_CAP.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_BRICK_WALL_BLOCK = ITEMS.register("dungeon_brick_wall", () -> new BlockItem(BloodMagicBlocks.DUNGEON_BRICK_WALL.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_POLISHED_WALL_BLOCK = ITEMS.register("dungeon_polished_wall", () -> new BlockItem(BloodMagicBlocks.DUNGEON_POLISHED_WALL.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_BRICK_GATE_BLOCK = ITEMS.register("dungeon_brick_gate", () -> new BlockItem(BloodMagicBlocks.DUNGEON_BRICK_GATE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_POLISHED_GATE_BLOCK = ITEMS.register("dungeon_polished_gate", () -> new BlockItem(BloodMagicBlocks.DUNGEON_POLISHED_GATE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_BRICK_SLAB_BLOCK = ITEMS.register("dungeon_brick_slab", () -> new BlockItem(BloodMagicBlocks.DUNGEON_BRICK_SLAB.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_TILE_SLAB_BLOCK = ITEMS.register("dungeon_tile_slab", () -> new BlockItem(BloodMagicBlocks.DUNGEON_TILE_SLAB.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> DUNGEON_CRACKED_BRICK_1_BLOCK = ITEMS.register("dungeon_regular_cracked_brick1", () -> new BlockItem(BloodMagicBlocks.DUNGEON_CRACKED_BRICK_1.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_GLOWING_CRACKED_BRICK_1_BLOCK = ITEMS.register("dungeon_cracked_brick1", () -> new BlockItem(BloodMagicBlocks.DUNGEON_GLOWING_CRACKED_BRICK_1.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> DUNGEON_ORE_BLOCK = ITEMS.register("dungeon_ore", () -> new BlockItem(BloodMagicBlocks.DUNGEON_ORE.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> SPIKES = ITEMS.register("spikes", () -> new BlockItem(BloodMagicBlocks.SPIKES.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_SPIKE_TRAP = ITEMS.register("dungeon_spike_trap", () -> new BlockItem(BloodMagicBlocks.DUNGEON_SPIKE_TRAP.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> HELLFORGED_BLOCK = ITEMS.register("dungeon_metal", () -> new BlockItem(BloodMagicBlocks.HELLFORGED_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> RAW_HELLFORGED_BLOCK = ITEMS.register("rawdemoniteblock", () -> new BlockItem(BloodMagicBlocks.RAW_HELLFORGED_BLOCK.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> INVERSION_PILLAR_BLOCK = ITEMS.register("inversion_pillar", () -> new BlockItem(BloodMagicBlocks.INVERSION_PILLAR.get(), new Item.Properties()));
	public static final RegistryObject<Item> INVERSION_PILLAR_CAP_BLOCK = ITEMS.register("inversion_pillar_cap", () -> new BlockItem(BloodMagicBlocks.INVERSION_PILLAR_CAP.get(), new Item.Properties()));

	public static final RegistryObject<Item> DUNGEON_CONTROLLER_BLOCK = ITEMS.register("dungeon_controller", () -> new BlockItem(BloodMagicBlocks.DUNGEON_CONTROLLER.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUNGEON_SEAL_BLOCK = ITEMS.register("dungeon_seal", () -> new BlockItem(BloodMagicBlocks.DUNGEON_SEAL.get(), new Item.Properties().tab(BloodMagic.TAB)));
	public static final RegistryObject<Item> SPECIAL_DUNGEON_SEAL_BLOCK = ITEMS.register("special_dungeon_seal", () -> new BlockItem(BloodMagicBlocks.SPECIAL_DUNGEON_SEAL.get(), new Item.Properties().tab(BloodMagic.TAB)));

	public static final RegistryObject<Item> DUNGEON_TESTER = BASICITEMS.register("dungeon_tester", ItemDungeonTester::new);
	public static final RegistryObject<Item> DUNGEON_SIMPLE_KEY = BASICITEMS.register("simplekey", () -> new ItemDungeonKey("simplekey", "tier1", "standard"));
	public static final RegistryObject<Item> DUNGEON_MINE_ENTRANCE_KEY = BASICITEMS.register("mineentrancekey", () -> new ItemDungeonKey("mineentrancekey", "mine_entrances"));
	public static final RegistryObject<Item> DUNGEON_MINE_KEY = BASICITEMS.register("minekey", () -> new ItemDungeonKey("minekey", "mines"));

	public static final RegistryObject<Item> BLEEDING_EDGE_MUSIC = BASICITEMS.register("bleedingedge", () -> new RecordItem(15, SoundRegistrator.BLEEDING_EDGE_MUSIC, new Item.Properties().stacksTo(1).tab(BloodMagic.TAB)));

}
