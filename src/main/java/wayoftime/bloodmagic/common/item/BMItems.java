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

    // Demon Crystals (use BASIC_ITEMS since they ARE specific will types, not items that hold variable will types)
    public static final DeferredHolder<Item, DemonCrystalItem> DEMON_CRYSTAL_DEFAULT = BASIC_ITEMS.register("demon_crystal_default", () -> new DemonCrystalItem(EnumWillType.DEFAULT));
    public static final DeferredHolder<Item, DemonCrystalItem> DEMON_CRYSTAL_CORROSIVE = BASIC_ITEMS.register("demon_crystal_corrosive", () -> new DemonCrystalItem(EnumWillType.CORROSIVE));
    public static final DeferredHolder<Item, DemonCrystalItem> DEMON_CRYSTAL_DESTRUCTIVE = BASIC_ITEMS.register("demon_crystal_destructive", () -> new DemonCrystalItem(EnumWillType.DESTRUCTIVE));
    public static final DeferredHolder<Item, DemonCrystalItem> DEMON_CRYSTAL_STEADFAST = BASIC_ITEMS.register("demon_crystal_steadfast", () -> new DemonCrystalItem(EnumWillType.STEADFAST));
    public static final DeferredHolder<Item, DemonCrystalItem> DEMON_CRYSTAL_VENGEFUL = BASIC_ITEMS.register("demon_crystal_vengeful", () -> new DemonCrystalItem(EnumWillType.VENGEFUL));

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

    public static void register(IEventBus modBus) {
        BASIC_ITEMS.register(modBus);
        ITEMS.register(modBus);
        WILL_ITEMS.register(modBus);
        TAB_REQ.register(modBus);
    }
}
