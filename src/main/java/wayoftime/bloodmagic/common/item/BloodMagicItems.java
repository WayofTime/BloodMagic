package wayoftime.bloodmagic.common.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.arc.ItemARCToolBase;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilAir;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilBloodLight;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilDivination;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilFastMiner;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilFrost;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilGreenGrove;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilLava;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilMagnetism;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilVoid;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilWater;
import wayoftime.bloodmagic.common.item.soul.ItemMonsterSoul;
import wayoftime.bloodmagic.common.item.soul.ItemSentientSword;
import wayoftime.bloodmagic.common.item.soul.ItemSoulGem;
import wayoftime.bloodmagic.common.item.soul.ItemSoulSnare;
import wayoftime.bloodmagic.common.registration.impl.BloodOrbDeferredRegister;
import wayoftime.bloodmagic.common.registration.impl.BloodOrbRegistryObject;
import wayoftime.bloodmagic.orb.BloodOrb;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.will.EnumDemonWillType;

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
	public static final RegistryObject<Item> SOUL_FORGE_ITEM = ITEMS.register("soulforge", () -> new BlockItem(BloodMagicBlocks.SOUL_FORGE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> BLANK_RUNE_ITEM = ITEMS.register("blankrune", () -> new BlockItem(BloodMagicBlocks.BLANK_RUNE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> SPEED_RUNE_ITEM = ITEMS.register("speedrune", () -> new BlockItem(BloodMagicBlocks.SPEED_RUNE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> SACRIFICE_RUNE_ITEM = ITEMS.register("sacrificerune", () -> new BlockItem(BloodMagicBlocks.SACRIFICE_RUNE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> SELF_SACRIFICE_RUNE_ITEM = ITEMS.register("selfsacrificerune", () -> new BlockItem(BloodMagicBlocks.SELF_SACRIFICE_RUNE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> DISPLACEMENT_RUNE_ITEM = ITEMS.register("dislocationrune", () -> new BlockItem(BloodMagicBlocks.DISPLACEMENT_RUNE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> CAPACITY_RUNE_ITEM = ITEMS.register("altarcapacityrune", () -> new BlockItem(BloodMagicBlocks.CAPACITY_RUNE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> AUGMENTED_CAPACITY_RUNE_ITEM = ITEMS.register("bettercapacityrune", () -> new BlockItem(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> ORB_RUNE_ITEM = ITEMS.register("orbcapacityrune", () -> new BlockItem(BloodMagicBlocks.ORB_RUNE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> ACCELERATION_RUNE_ITEM = ITEMS.register("accelerationrune", () -> new BlockItem(BloodMagicBlocks.ACCELERATION_RUNE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> CHARGING_RUNE_ITEM = ITEMS.register("chargingrune", () -> new BlockItem(BloodMagicBlocks.CHARGING_RUNE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> BLANK_RITUAL_STONE_ITEM = ITEMS.register("ritualstone", () -> new BlockItem(BloodMagicBlocks.BLANK_RITUAL_STONE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> AIR_RITUAL_STONE_ITEM = ITEMS.register("airritualstone", () -> new BlockItem(BloodMagicBlocks.AIR_RITUAL_STONE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> WATER_RITUAL_STONE_ITEM = ITEMS.register("waterritualstone", () -> new BlockItem(BloodMagicBlocks.WATER_RITUAL_STONE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> FIRE_RITUAL_STONE_ITEM = ITEMS.register("fireritualstone", () -> new BlockItem(BloodMagicBlocks.FIRE_RITUAL_STONE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> EARTH_RITUAL_STONE_ITEM = ITEMS.register("earthritualstone", () -> new BlockItem(BloodMagicBlocks.EARTH_RITUAL_STONE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> DUSK_RITUAL_STONE_ITEM = ITEMS.register("duskritualstone", () -> new BlockItem(BloodMagicBlocks.DUSK_RITUAL_STONE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> DAWN_RITUAL_STONE_ITEM = ITEMS.register("lightritualstone", () -> new BlockItem(BloodMagicBlocks.DAWN_RITUAL_STONE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> ALCHEMICAL_REACTION_CHAMBER_ITEM = ITEMS.register("alchemicalreactionchamber", () -> new BlockItem(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get(), new Item.Properties().group(BloodMagic.TAB)));

	public static final RegistryObject<Item> BLOODSTONE_ITEM = ITEMS.register("largebloodstonebrick", () -> new BlockItem(BloodMagicBlocks.BLOODSTONE.get(), new Item.Properties().group(BloodMagic.TAB)));
	public static final RegistryObject<Item> BLOODSTONE_BRICK_ITEM = ITEMS.register("bloodstonebrick", () -> new BlockItem(BloodMagicBlocks.BLOODSTONE_BRICK.get(), new Item.Properties().group(BloodMagic.TAB)));

	public static final RegistryObject<Item> MASTER_RITUAL_STONE_ITEM = ITEMS.register("masterritualstone", () -> new BlockItem(BloodMagicBlocks.MASTER_RITUAL_STONE.get(), new Item.Properties().group(BloodMagic.TAB)));

	public static final RegistryObject<Item> BLOOD_ALTAR_ITEM = ITEMS.register("altar", () -> new BlockItem(BloodMagicBlocks.BLOOD_ALTAR.get(), new Item.Properties().group(BloodMagic.TAB)));

	// TODO: Need to rework the above instantiations for the ItemBlocks so that it's
	// done with the Blocks.

//	public static final RegistryObject<Item> WEAK_BLOOD_ORB = BASICITEMS.register("weakbloodorb", ItemBloodOrb::new);
//	public static final RegistryObject<Item> WEAK_BLOOD_ORB = BASICITEMS.register("weakbloodorb", () -> new ItemBloodOrb(WEAK_ORB_INSTANCE));
	public static final RegistryObject<Item> WEAK_BLOOD_ORB = BASICITEMS.register("weakbloodorb", () -> new ItemBloodOrb(ORB_WEAK));
	public static final RegistryObject<Item> APPRENTICE_BLOOD_ORB = BASICITEMS.register("apprenticebloodorb", () -> new ItemBloodOrb(ORB_APPRENTICE));
	public static final RegistryObject<Item> MAGICIAN_BLOOD_ORB = BASICITEMS.register("magicianbloodorb", () -> new ItemBloodOrb(ORB_MAGICIAN));
	public static final RegistryObject<Item> MASTER_BLOOD_ORB = BASICITEMS.register("masterbloodorb", () -> new ItemBloodOrb(ORB_MASTER));

	public static final RegistryObject<Item> DIVINATION_SIGIL = BASICITEMS.register("divinationsigil", () -> new ItemSigilDivination(true));
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

	public static final RegistryObject<Item> ARCANE_ASHES = BASICITEMS.register("arcaneashes", () -> new ItemArcaneAshes());
	public static final RegistryObject<Item> DAGGER_OF_SACRIFICE = BASICITEMS.register("daggerofsacrifice", () -> new ItemDaggerOfSacrifice());
	public static final RegistryObject<Item> LAVA_CRYSTAL = BASICITEMS.register("lavacrystal", () -> new ItemLavaCrystal());
	public static final RegistryObject<Item> WEAK_BLOOD_SHARD = BASICITEMS.register("weakbloodshard", () -> new ItemBase());

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

	// Reagents used to make the Sigils
	public static final RegistryObject<Item> REAGENT_WATER = BASICITEMS.register("reagentwater", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_LAVA = BASICITEMS.register("reagentlava", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_VOID = BASICITEMS.register("reagentvoid", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_GROWTH = BASICITEMS.register("reagentgrowth", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_FAST_MINER = BASICITEMS.register("reagentfastminer", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_MAGNETISM = BASICITEMS.register("reagentmagnetism", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_AIR = BASICITEMS.register("reagentair", () -> new ItemBase());
	public static final RegistryObject<Item> REAGENT_BLOOD_LIGHT = BASICITEMS.register("reagentbloodlight", () -> new ItemBase());

	// Tartaric Gems
	public static final RegistryObject<Item> PETTY_GEM = ITEMS.register("soulgempetty", () -> new ItemSoulGem("petty", 64));
	public static final RegistryObject<Item> LESSER_GEM = ITEMS.register("soulgemlesser", () -> new ItemSoulGem("lesser", 256));
	public static final RegistryObject<Item> COMMON_GEM = ITEMS.register("soulgemcommon", () -> new ItemSoulGem("common", 1024));

	public static final RegistryObject<Item> MONSTER_SOUL_RAW = BASICITEMS.register("basemonstersoul", () -> new ItemMonsterSoul(EnumDemonWillType.DEFAULT));
	public static final RegistryObject<Item> MONSTER_SOUL_CORROSIVE = BASICITEMS.register("basemonstersoul_corrosive", () -> new ItemMonsterSoul(EnumDemonWillType.CORROSIVE));
	public static final RegistryObject<Item> MONSTER_SOUL_DESTRUCTIVE = BASICITEMS.register("basemonstersoul_destructive", () -> new ItemMonsterSoul(EnumDemonWillType.DESTRUCTIVE));
	public static final RegistryObject<Item> MONSTER_SOUL_STEADFAST = BASICITEMS.register("basemonstersoul_steadfast", () -> new ItemMonsterSoul(EnumDemonWillType.STEADFAST));
	public static final RegistryObject<Item> MONSTER_SOUL_VENGEFUL = BASICITEMS.register("basemonstersoul_vengeful", () -> new ItemMonsterSoul(EnumDemonWillType.VENGEFUL));

	public static final RegistryObject<Item> SOUL_SNARE = BASICITEMS.register("soulsnare", ItemSoulSnare::new);
	public static final RegistryObject<Item> SENTIENT_SWORD = ITEMS.register("soulsword", () -> new ItemSentientSword());

	// ARC Tools
	public static final RegistryObject<Item> SANGUINE_REVERTER = BASICITEMS.register("sanguinereverter", () -> new ItemARCToolBase(32, 2));
	public static final RegistryObject<Item> PRIMITIVE_FURNACE_CELL = BASICITEMS.register("furnacecell_primitive", () -> new ItemARCToolBase(128, 1.25));
}
