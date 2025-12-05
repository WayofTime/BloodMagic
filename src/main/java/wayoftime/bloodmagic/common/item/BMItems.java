package wayoftime.bloodmagic.common.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.item.arc.ItemARCToolBase;
import wayoftime.bloodmagic.ritual.EnumRuneType;

import java.util.function.Supplier;

public class BMItems {
    public static final DeferredRegister<Item> BASIC_ITEMS = DeferredRegister.createItems(BloodMagic.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(BloodMagic.MODID);
    public static final DeferredRegister<Item> WILL_ITEMS = DeferredRegister.createItems(BloodMagic.MODID);
    public static final DeferredRegister<Item> TAB_REQ = DeferredRegister.createItems(BloodMagic.MODID);

    // these go first for creative tab order
    public static final DeferredHolder<Item, ArmorItem> LIVING_HELMET = BASIC_ITEMS.register("living_helmet", makeLivingArmour(ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, LivingArmourItem> LIVING_PLATE = TAB_REQ.register("living_plate", LivingArmourItem::new);
    public static final DeferredHolder<Item, ArmorItem> LIVING_LEGGINGS = BASIC_ITEMS.register("living_leggings", makeLivingArmour(ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, ArmorItem> LIVING_BOOTS = BASIC_ITEMS.register("living_boots", makeLivingArmour(ArmorItem.Type.BOOTS));
    public static final DeferredHolder<Item, UpgradeTomeItem> UPGRADE_TOME = TAB_REQ.register("upgrade_tome", UpgradeTomeItem::new);

    public static final DeferredHolder<Item, ScrapItem> UPGRADE_SCRAP = BASIC_ITEMS.register("upgrade_scrap", () -> new ScrapItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, ScrapItem> SYNTHETIC_POINT = BASIC_ITEMS.register("synthetic_point", () -> new ScrapItem(new Item.Properties().component(BMDataComponents.UPGRADE_SCRAP, 1)));

    public static final DeferredHolder<Item, TrainerItem> TRAINING_BRACELET = BASIC_ITEMS.register("training_bracelet", TrainerItem::new);

    public static final DeferredHolder<Item, BloodOrbItem> ORB_WEAK = BASIC_ITEMS.register("blood_orb_weak", BloodOrbItem::new);
    public static final DeferredHolder<Item, BloodOrbItem> ORB_APPRENTICE = BASIC_ITEMS.register("blood_orb_apprentice", BloodOrbItem::new);
    public static final DeferredHolder<Item, BloodOrbItem> ORB_MAGICIAN = BASIC_ITEMS.register("blood_orb_magician", BloodOrbItem::new);
    public static final DeferredHolder<Item, BloodOrbItem> ORB_MASTER = BASIC_ITEMS.register("blood_orb_master", BloodOrbItem::new);
    public static final DeferredHolder<Item, BloodOrbItem> ORB_ARCHMAGE = BASIC_ITEMS.register("blood_orb_archmage", BloodOrbItem::new);
    public static final DeferredHolder<Item, BloodOrbItem> ORB_TRANSCENDENT = BASIC_ITEMS.register("blood_orb_transcendent", BloodOrbItem::new);

    private static Supplier<ArmorItem> makeLivingArmour(ArmorItem.Type type) {
        return () -> new ArmorItem(BMMaterialsAndTiers.LIVING_ARMOUR_MATERIAL, type, new Item.Properties().durability(type.getDurability(33)));
    }
    public static final DeferredHolder<Item, SacrificialDaggerItem> SACRIFICIAL_DAGGER = ITEMS.register("sacrificial_dagger", SacrificialDaggerItem::new);

    public static final DeferredHolder<Item, RawSoulItem> RAW_WILL = WILL_ITEMS.register("raw_will", RawSoulItem::new);

    public static final DeferredHolder<Item, SoulGemItem> SOUL_GEM_PETTY = WILL_ITEMS.register("soul_gem_petty", SoulGemItem::new);
    public static final DeferredHolder<Item, SoulGemItem> SOUL_GEM_LESSER = WILL_ITEMS.register("soul_gem_lesser", SoulGemItem::new);
    public static final DeferredHolder<Item, SoulGemItem> SOUL_GEM_COMMON = WILL_ITEMS.register("soul_gem_common", SoulGemItem::new);
    public static final DeferredHolder<Item, SoulGemItem> SOUL_GEM_GREATER = WILL_ITEMS.register("soul_gem_greater", SoulGemItem::new);
    public static final DeferredHolder<Item, SoulGemItem> SOUL_GEM_GRAND = WILL_ITEMS.register("soul_gem_grand", SoulGemItem::new);

    // Slates
    public static final DeferredHolder<Item, Item> SLATE_BLANK = BASIC_ITEMS.register("blank_slate", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SLATE_REINFORCED = BASIC_ITEMS.register("reinforced_slate", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SLATE_IMBUED = BASIC_ITEMS.register("imbued_slate", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SLATE_DEMONIC = BASIC_ITEMS.register("demonic_slate", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SLATE_ETHEREAL = BASIC_ITEMS.register("ethereal_slate", () -> new Item(new Item.Properties()));

    // Sigils (placeholder items - functionality to be added later)
    public static final DeferredHolder<Item, Item> SIGIL_DIVINATION = BASIC_ITEMS.register("sigil_divination", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_SEER = BASIC_ITEMS.register("sigil_seer", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_WATER = BASIC_ITEMS.register("sigil_water", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_LAVA = BASIC_ITEMS.register("sigil_lava", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_VOID = BASIC_ITEMS.register("sigil_void", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_GREEN_GROVE = BASIC_ITEMS.register("sigil_green_grove", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_AIR = BASIC_ITEMS.register("sigil_air", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_BLOOD_LIGHT = BASIC_ITEMS.register("sigil_blood_light", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_FAST_MINER = BASIC_ITEMS.register("sigil_fast_miner", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_MAGNETISM = BASIC_ITEMS.register("sigil_magnetism", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_FROST = BASIC_ITEMS.register("sigil_frost", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_SUPPRESSION = BASIC_ITEMS.register("sigil_suppression", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_HOLDING = BASIC_ITEMS.register("sigil_holding", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SIGIL_TELEPOSITION = BASIC_ITEMS.register("sigil_teleposition", () -> new Item(new Item.Properties().stacksTo(1)));

    // Alchemy items
    public static final DeferredHolder<Item, ItemArcaneAshes> ARCANE_ASHES = BASIC_ITEMS.register("arcane_ashes", ItemArcaneAshes::new);

    // Tau items (TODO: Add tau functionality - crop-like growth mechanics)
    public static final DeferredHolder<Item, Item> TAU_OIL = BASIC_ITEMS.register("tauoil", () -> new Item(new Item.Properties()));

    // Reagents (used to make Sigils via Alchemy Array)
    public static final DeferredHolder<Item, Item> REAGENT_WATER = BASIC_ITEMS.register("reagentwater", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_LAVA = BASIC_ITEMS.register("reagentlava", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_VOID = BASIC_ITEMS.register("reagentvoid", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_GROWTH = BASIC_ITEMS.register("reagentgrowth", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_FAST_MINER = BASIC_ITEMS.register("reagentfastminer", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_MAGNETISM = BASIC_ITEMS.register("reagentmagnetism", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_AIR = BASIC_ITEMS.register("reagentair", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_BLOOD_LIGHT = BASIC_ITEMS.register("reagentbloodlight", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_SIGHT = BASIC_ITEMS.register("reagentsight", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_BINDING = BASIC_ITEMS.register("reagentbinding", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_HOLDING = BASIC_ITEMS.register("reagentholding", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_SUPPRESSION = BASIC_ITEMS.register("reagentsuppression", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REAGENT_TELEPOSITION = BASIC_ITEMS.register("reagentteleposition", () -> new Item(new Item.Properties()));

    // Misc items
    public static final DeferredHolder<Item, Item> SOUL_SNARE = BASIC_ITEMS.register("soul_snare", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WEAK_BLOOD_SHARD = BASIC_ITEMS.register("weak_blood_shard", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> DAGGER_OF_SACRIFICE = ITEMS.register("dagger_of_sacrifice", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> LAVA_CRYSTAL = BASIC_ITEMS.register("lava_crystal", () -> new Item(new Item.Properties()));

    // Teleposer Focus items
    public static final DeferredHolder<Item, TeleposerFocusItem> TELEPOSER_FOCUS = ITEMS.register("teleposerfocus", () -> new TeleposerFocusItem(0));
    public static final DeferredHolder<Item, TeleposerFocusItem> TELEPOSER_FOCUS_ENHANCED = ITEMS.register("enhancedteleposerfocus", () -> new TeleposerFocusItem(1));
    public static final DeferredHolder<Item, TeleposerFocusItem> TELEPOSER_FOCUS_REINFORCED = ITEMS.register("reinforcedteleposerfocus", () -> new TeleposerFocusItem(2));

    // Fragments
    public static final DeferredHolder<Item, Item> IRON_FRAGMENT = BASIC_ITEMS.register("ironfragment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GOLD_FRAGMENT = BASIC_ITEMS.register("goldfragment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COPPER_FRAGMENT = BASIC_ITEMS.register("copperfragment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> NETHERITE_SCRAP_FRAGMENT = BASIC_ITEMS.register("fragment_netherite_scrap", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> DEMONITE_FRAGMENT = BASIC_ITEMS.register("demonitefragment", () -> new Item(new Item.Properties()));

    // Gravels
    public static final DeferredHolder<Item, Item> IRON_GRAVEL = BASIC_ITEMS.register("irongravel", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GOLD_GRAVEL = BASIC_ITEMS.register("goldgravel", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COPPER_GRAVEL = BASIC_ITEMS.register("coppergravel", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> NETHERITE_SCRAP_GRAVEL = BASIC_ITEMS.register("gravel_netherite_scrap", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> DEMONITE_GRAVEL = BASIC_ITEMS.register("demonitegravel", () -> new Item(new Item.Properties()));

    // Sands/Dusts
    public static final DeferredHolder<Item, Item> IRON_SAND = BASIC_ITEMS.register("ironsand", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GOLD_SAND = BASIC_ITEMS.register("goldsand", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COPPER_SAND = BASIC_ITEMS.register("coppersand", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COAL_SAND = BASIC_ITEMS.register("coalsand", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> NETHERITE_SCRAP_SAND = BASIC_ITEMS.register("sand_netherite", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HELLFORGED_SAND = BASIC_ITEMS.register("sand_hellforged", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CORRUPTED_DUST = BASIC_ITEMS.register("corrupted_dust", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CORRUPTED_DUST_TINY = BASIC_ITEMS.register("corrupted_tinydust", () -> new Item(new Item.Properties()));

    // ARC Tools
    public static final DeferredHolder<Item, ItemARCToolBase> BASIC_CUTTING_FLUID = BASIC_ITEMS.register("basiccuttingfluid", () -> new ItemARCToolBase(64, 1, EnumWillType.CORROSIVE));
    public static final DeferredHolder<Item, ItemARCToolBase> INTERMEDIATE_CUTTING_FLUID = BASIC_ITEMS.register("intermediatecuttingfluid", () -> new ItemARCToolBase(256, 1.5, EnumWillType.CORROSIVE));
    public static final DeferredHolder<Item, ItemARCToolBase> ADVANCED_CUTTING_FLUID = BASIC_ITEMS.register("advancedcuttingfluid", () -> new ItemARCToolBase(1024, 2, 2, EnumWillType.CORROSIVE));
    public static final DeferredHolder<Item, ItemARCToolBase> EXPLOSIVE_POWDER = BASIC_ITEMS.register("explosivepowder", () -> new ItemARCToolBase(64, 1, EnumWillType.DESTRUCTIVE));
    public static final DeferredHolder<Item, ItemARCToolBase> RESONATOR = BASIC_ITEMS.register("resonator", () -> new ItemARCToolBase(64, 1, EnumWillType.VENGEFUL));
    public static final DeferredHolder<Item, ItemARCToolBase> SANGUINE_REVERTER = BASIC_ITEMS.register("sanguinereverter", () -> new ItemARCToolBase(32, 2, EnumWillType.STEADFAST));
    public static final DeferredHolder<Item, ItemARCToolBase> PRIMITIVE_FURNACE_CELL = BASIC_ITEMS.register("furnacecell_primitive", () -> new ItemARCToolBase(128, 3));
    public static final DeferredHolder<Item, ItemARCToolBase> PRIMITIVE_EXPLOSIVE_CELL = BASIC_ITEMS.register("primitive_explosive_cell", () -> new ItemARCToolBase(256, 1.5, EnumWillType.DESTRUCTIVE));
    public static final DeferredHolder<Item, ItemARCToolBase> PRIMITIVE_HYDRATION_CELL = BASIC_ITEMS.register("primitive_hydration_cell", () -> new ItemARCToolBase(128, 1.5));
    public static final DeferredHolder<Item, ItemARCToolBase> PRIMITIVE_CRYSTALLINE_RESONATOR = BASIC_ITEMS.register("primitive_crystalline_resonator", () -> new ItemARCToolBase(256, 1.5, EnumWillType.VENGEFUL));
    public static final DeferredHolder<Item, ItemARCToolBase> HELLFORGED_EXPLOSIVE_CELL = BASIC_ITEMS.register("hellforged_explosive_cell", () -> new ItemARCToolBase(1024, 2, EnumWillType.DESTRUCTIVE));
    public static final DeferredHolder<Item, ItemARCToolBase> HELLFORGED_RESONATOR = BASIC_ITEMS.register("hellforged_resonator", () -> new ItemARCToolBase(1024, 2, 2, EnumWillType.VENGEFUL));

    // Activation Crystals
    public static final DeferredHolder<Item, ItemActivationCrystal> ACTIVATION_CRYSTAL_WEAK = BASIC_ITEMS.register("activationcrystalweak", () -> new ItemActivationCrystal(ItemActivationCrystal.CrystalType.WEAK));
    public static final DeferredHolder<Item, ItemActivationCrystal> ACTIVATION_CRYSTAL_AWAKENED = BASIC_ITEMS.register("activationcrystalawakened", () -> new ItemActivationCrystal(ItemActivationCrystal.CrystalType.AWAKENED));
    public static final DeferredHolder<Item, ItemActivationCrystal> ACTIVATION_CRYSTAL_CREATIVE = BASIC_ITEMS.register("activationcrystalcreative", () -> new ItemActivationCrystal(ItemActivationCrystal.CrystalType.CREATIVE));

    // Inscription Tools
    public static final DeferredHolder<Item, ItemInscriptionTool> INSCRIPTION_TOOL_AIR = BASIC_ITEMS.register("airscribetool", () -> new ItemInscriptionTool(EnumRuneType.AIR));
    public static final DeferredHolder<Item, ItemInscriptionTool> INSCRIPTION_TOOL_FIRE = BASIC_ITEMS.register("firescribetool", () -> new ItemInscriptionTool(EnumRuneType.FIRE));
    public static final DeferredHolder<Item, ItemInscriptionTool> INSCRIPTION_TOOL_WATER = BASIC_ITEMS.register("waterscribetool", () -> new ItemInscriptionTool(EnumRuneType.WATER));
    public static final DeferredHolder<Item, ItemInscriptionTool> INSCRIPTION_TOOL_EARTH = BASIC_ITEMS.register("earthscribetool", () -> new ItemInscriptionTool(EnumRuneType.EARTH));
    public static final DeferredHolder<Item, ItemInscriptionTool> INSCRIPTION_TOOL_DUSK = BASIC_ITEMS.register("duskscribetool", () -> new ItemInscriptionTool(EnumRuneType.DUSK));

    // Ritual Diviners
    public static final DeferredHolder<Item, ItemRitualDiviner> RITUAL_DIVINER = BASIC_ITEMS.register("ritualdiviner", () -> new ItemRitualDiviner(0));
    public static final DeferredHolder<Item, ItemRitualDiviner> RITUAL_DIVINER_DUSK = BASIC_ITEMS.register("ritualdivinerdusk", () -> new ItemRitualDiviner(1));

    // Sentient Tools (placeholder - functionality to be added later)
    public static final DeferredHolder<Item, Item> SENTIENT_SWORD = BASIC_ITEMS.register("soulsword", () -> new Item(new Item.Properties().stacksTo(1).durability(520)));
    public static final DeferredHolder<Item, Item> SENTIENT_AXE = BASIC_ITEMS.register("soulaxe", () -> new Item(new Item.Properties().stacksTo(1).durability(520)));
    public static final DeferredHolder<Item, Item> SENTIENT_PICKAXE = BASIC_ITEMS.register("soulpickaxe", () -> new Item(new Item.Properties().stacksTo(1).durability(520)));
    public static final DeferredHolder<Item, Item> SENTIENT_SHOVEL = BASIC_ITEMS.register("soulshovel", () -> new Item(new Item.Properties().stacksTo(1).durability(520)));
    public static final DeferredHolder<Item, Item> SENTIENT_SCYTHE = BASIC_ITEMS.register("soulscythe", () -> new Item(new Item.Properties().stacksTo(1).durability(520)));

    // Demon Will Items (crystal items - same naming as 1.20.1)
    public static final DeferredHolder<Item, DemonCrystalItem> RAW_CRYSTAL = BASIC_ITEMS.register("defaultcrystal", () -> new DemonCrystalItem(EnumWillType.DEFAULT));
    public static final DeferredHolder<Item, DemonCrystalItem> CORROSIVE_CRYSTAL = BASIC_ITEMS.register("corrosivecrystal", () -> new DemonCrystalItem(EnumWillType.CORROSIVE));
    public static final DeferredHolder<Item, DemonCrystalItem> DESTRUCTIVE_CRYSTAL = BASIC_ITEMS.register("destructivecrystal", () -> new DemonCrystalItem(EnumWillType.DESTRUCTIVE));
    public static final DeferredHolder<Item, DemonCrystalItem> VENGEFUL_CRYSTAL = BASIC_ITEMS.register("vengefulcrystal", () -> new DemonCrystalItem(EnumWillType.VENGEFUL));
    public static final DeferredHolder<Item, DemonCrystalItem> STEADFAST_CRYSTAL = BASIC_ITEMS.register("steadfastcrystal", () -> new DemonCrystalItem(EnumWillType.STEADFAST));
    public static final DeferredHolder<Item, Item> DEMON_WILL_GAUGE = BASIC_ITEMS.register("demonwillgauge", () -> new Item(new Item.Properties().stacksTo(1)));

    // Crystal Catalysts (used in soul forge recipes)
    public static final DeferredHolder<Item, Item> RAW_CRYSTAL_CATALYST = BASIC_ITEMS.register("rawcatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CORROSIVE_CRYSTAL_CATALYST = BASIC_ITEMS.register("corrosivecatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> DESTRUCTIVE_CRYSTAL_CATALYST = BASIC_ITEMS.register("destructivecatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> VENGEFUL_CRYSTAL_CATALYST = BASIC_ITEMS.register("vengefulcatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> STEADFAST_CRYSTAL_CATALYST = BASIC_ITEMS.register("steadfastcatalyst", () -> new Item(new Item.Properties()));

    // Routing Node Items
    public static final DeferredHolder<Item, Item> NODE_ROUTER = BASIC_ITEMS.register("noderouter", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> MASTER_NODE_UPGRADE = BASIC_ITEMS.register("mastercore", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MASTER_NODE_UPGRADE_SPEED = BASIC_ITEMS.register("mastercorespeed", () -> new Item(new Item.Properties()));

    // Throwing Daggers
    public static final DeferredHolder<Item, Item> THROWING_DAGGER = BASIC_ITEMS.register("throwing_dagger", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> THROWING_DAGGER_AMETHYST = BASIC_ITEMS.register("amethystthrowingdagger", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> THROWING_DAGGER_SYRINGE = BASIC_ITEMS.register("throwing_dagger_syringe", () -> new Item(new Item.Properties().stacksTo(16)));

    // Dungeon/Key Items
    public static final DeferredHolder<Item, Item> SIMPLE_KEY = BASIC_ITEMS.register("simplekey", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> MINE_KEY = BASIC_ITEMS.register("minekey", () -> new Item(new Item.Properties().stacksTo(16)));

    // Simple Recipe Ingredients
    public static final DeferredHolder<Item, Item> SULFUR = BASIC_ITEMS.register("sulfur", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SALTPETER = BASIC_ITEMS.register("saltpeter", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PLANT_OIL = BASIC_ITEMS.register("plantoil", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HELLFORGED_INGOT = BASIC_ITEMS.register("ingot_hellforged", () -> new Item(new Item.Properties()));

    // Alchemy Flask Items (placeholder - potions system functionality to be added later)
    public static final DeferredHolder<Item, Item> SLATE_VIAL = BASIC_ITEMS.register("slate_vial", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> ALCHEMY_FLASK = BASIC_ITEMS.register("alchemy_flask", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> ALCHEMY_FLASK_THROWABLE = BASIC_ITEMS.register("alchemy_flask_throwable", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> ALCHEMY_FLASK_LINGERING = BASIC_ITEMS.register("alchemy_flask_lingering", () -> new Item(new Item.Properties().stacksTo(1)));

    // Anointment Items (placeholder - weapon upgrade functionality to be added later)
    // Base tier anointments
    public static final DeferredHolder<Item, Item> MELEE_DAMAGE_ANOINTMENT = BASIC_ITEMS.register("melee_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SILK_TOUCH_ANOINTMENT = BASIC_ITEMS.register("silk_touch_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FORTUNE_ANOINTMENT = BASIC_ITEMS.register("fortune_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HOLY_WATER_ANOINTMENT = BASIC_ITEMS.register("holy_water_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HIDDEN_KNOWLEDGE_ANOINTMENT = BASIC_ITEMS.register("hidden_knowledge_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> QUICK_DRAW_ANOINTMENT = BASIC_ITEMS.register("quick_draw_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LOOTING_ANOINTMENT = BASIC_ITEMS.register("looting_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_POWER_ANOINTMENT = BASIC_ITEMS.register("bow_power_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WILL_POWER_ANOINTMENT = BASIC_ITEMS.register("will_power_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SMELTING_ANOINTMENT = BASIC_ITEMS.register("smelting_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> VOIDING_ANOINTMENT = BASIC_ITEMS.register("voiding_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_VELOCITY_ANOINTMENT = BASIC_ITEMS.register("bow_velocity_anointment", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WEAPON_REPAIR_ANOINTMENT = BASIC_ITEMS.register("weapon_repair_anointment", () -> new Item(new Item.Properties()));

    // Anointment _L variants (extended duration - 1024 uses)
    public static final DeferredHolder<Item, Item> MELEE_DAMAGE_ANOINTMENT_L = BASIC_ITEMS.register("melee_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SILK_TOUCH_ANOINTMENT_L = BASIC_ITEMS.register("silk_touch_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FORTUNE_ANOINTMENT_L = BASIC_ITEMS.register("fortune_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HOLY_WATER_ANOINTMENT_L = BASIC_ITEMS.register("holy_water_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HIDDEN_KNOWLEDGE_ANOINTMENT_L = BASIC_ITEMS.register("hidden_knowledge_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> QUICK_DRAW_ANOINTMENT_L = BASIC_ITEMS.register("quick_draw_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LOOTING_ANOINTMENT_L = BASIC_ITEMS.register("looting_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_POWER_ANOINTMENT_L = BASIC_ITEMS.register("bow_power_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SMELTING_ANOINTMENT_L = BASIC_ITEMS.register("smelting_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> VOIDING_ANOINTMENT_L = BASIC_ITEMS.register("voiding_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_VELOCITY_ANOINTMENT_L = BASIC_ITEMS.register("bow_velocity_anointment_l", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WEAPON_REPAIR_ANOINTMENT_L = BASIC_ITEMS.register("weapon_repair_anointment_l", () -> new Item(new Item.Properties()));

    // Anointment _2 variants (level 2 effect)
    public static final DeferredHolder<Item, Item> MELEE_DAMAGE_ANOINTMENT_2 = BASIC_ITEMS.register("melee_anointment_2", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FORTUNE_ANOINTMENT_2 = BASIC_ITEMS.register("fortune_anointment_2", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HOLY_WATER_ANOINTMENT_2 = BASIC_ITEMS.register("holy_water_anointment_2", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HIDDEN_KNOWLEDGE_ANOINTMENT_2 = BASIC_ITEMS.register("hidden_knowledge_anointment_2", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> QUICK_DRAW_ANOINTMENT_2 = BASIC_ITEMS.register("quick_draw_anointment_2", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LOOTING_ANOINTMENT_2 = BASIC_ITEMS.register("looting_anointment_2", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_POWER_ANOINTMENT_2 = BASIC_ITEMS.register("bow_power_anointment_2", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_POWER_ANOINTMENT_STRONG = BASIC_ITEMS.register("bow_power_anointment_strong", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_VELOCITY_ANOINTMENT_2 = BASIC_ITEMS.register("bow_velocity_anointment_2", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WEAPON_REPAIR_ANOINTMENT_2 = BASIC_ITEMS.register("weapon_repair_anointment_2", () -> new Item(new Item.Properties()));

    // Anointment _XL variants (extra long duration - 4096 uses)
    public static final DeferredHolder<Item, Item> MELEE_DAMAGE_ANOINTMENT_XL = BASIC_ITEMS.register("melee_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SILK_TOUCH_ANOINTMENT_XL = BASIC_ITEMS.register("silk_touch_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FORTUNE_ANOINTMENT_XL = BASIC_ITEMS.register("fortune_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HOLY_WATER_ANOINTMENT_XL = BASIC_ITEMS.register("holy_water_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HIDDEN_KNOWLEDGE_ANOINTMENT_XL = BASIC_ITEMS.register("hidden_knowledge_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> QUICK_DRAW_ANOINTMENT_XL = BASIC_ITEMS.register("quick_draw_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LOOTING_ANOINTMENT_XL = BASIC_ITEMS.register("looting_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_POWER_ANOINTMENT_XL = BASIC_ITEMS.register("bow_power_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SMELTING_ANOINTMENT_XL = BASIC_ITEMS.register("smelting_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> VOIDING_ANOINTMENT_XL = BASIC_ITEMS.register("voiding_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_VELOCITY_ANOINTMENT_XL = BASIC_ITEMS.register("bow_velocity_anointment_xl", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WEAPON_REPAIR_ANOINTMENT_XL = BASIC_ITEMS.register("weapon_repair_anointment_xl", () -> new Item(new Item.Properties()));

    // Anointment _3 variants (level 3 effect)
    public static final DeferredHolder<Item, Item> MELEE_DAMAGE_ANOINTMENT_3 = BASIC_ITEMS.register("melee_anointment_3", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FORTUNE_ANOINTMENT_3 = BASIC_ITEMS.register("fortune_anointment_3", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HOLY_WATER_ANOINTMENT_3 = BASIC_ITEMS.register("holy_water_anointment_3", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HIDDEN_KNOWLEDGE_ANOINTMENT_3 = BASIC_ITEMS.register("hidden_knowledge_anointment_3", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> QUICK_DRAW_ANOINTMENT_3 = BASIC_ITEMS.register("quick_draw_anointment_3", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LOOTING_ANOINTMENT_3 = BASIC_ITEMS.register("looting_anointment_3", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_POWER_ANOINTMENT_3 = BASIC_ITEMS.register("bow_power_anointment_3", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BOW_VELOCITY_ANOINTMENT_3 = BASIC_ITEMS.register("bow_velocity_anointment_3", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WEAPON_REPAIR_ANOINTMENT_3 = BASIC_ITEMS.register("weapon_repair_anointment_3", () -> new Item(new Item.Properties()));

    // Routing/Filter Items (placeholder - routing system functionality to be added later)
    public static final DeferredHolder<Item, Item> FRAME_PARTS = BASIC_ITEMS.register("componentframeparts", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ITEM_ROUTER_FILTER = BASIC_ITEMS.register("itemrouterfilterexact", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ITEM_TAG_FILTER = BASIC_ITEMS.register("itemrouterfilteroredict", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ITEM_ENCHANT_FILTER = BASIC_ITEMS.register("itemrouterfilterenchant", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ITEM_MOD_FILTER = BASIC_ITEMS.register("itemrouterfiltermoditems", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ITEM_COMPOSITE_FILTER = BASIC_ITEMS.register("itemrouterfiltercomposite", () -> new Item(new Item.Properties()));


    // Bleeding Edge Music Disc
    public static final DeferredHolder<Item, Item> BLEEDING_EDGE = BASIC_ITEMS.register("bleedingedge", () -> new Item(new Item.Properties().stacksTo(1)));

    // Alchemy Catalysts (used in potion brewing system)
    public static final DeferredHolder<Item, Item> SIMPLE_CATALYST = BASIC_ITEMS.register("simplecatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> STRENGTHENED_CATALYST = BASIC_ITEMS.register("strengthenedcatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CYCLING_CATALYST = BASIC_ITEMS.register("cyclingcatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COMBINATIONAL_CATALYST = BASIC_ITEMS.register("combinationalcatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MUNDANE_LENGTHENING_CATALYST = BASIC_ITEMS.register("mundanelengtheningcatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MUNDANE_POWER_CATALYST = BASIC_ITEMS.register("mundanepowercatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> AVERAGE_LENGTHENING_CATALYST = BASIC_ITEMS.register("averagelengtheningcatalyst", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> AVERAGE_POWER_CATALYST = BASIC_ITEMS.register("averagepowercatalyst", () -> new Item(new Item.Properties()));

    // Weak Filling Agent (used in potion brewing)
    public static final DeferredHolder<Item, Item> WEAK_FILLING_AGENT = BASIC_ITEMS.register("weakfillingagent", () -> new Item(new Item.Properties()));

    // Hellforged Parts (dropped from rune reversion)
    public static final DeferredHolder<Item, Item> HELLFORGED_PARTS = BASIC_ITEMS.register("hellforgedparts", () -> new Item(new Item.Properties()));

    public static void register(IEventBus modBus) {
        BASIC_ITEMS.register(modBus);
        ITEMS.register(modBus);
        WILL_ITEMS.register(modBus);
        TAB_REQ.register(modBus);
    }
}
