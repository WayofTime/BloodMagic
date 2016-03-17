package WayofTime.bloodmagic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.util.Utils;

public class ConfigHandler
{
    @Getter
    private static Configuration config;

    // Teleposer
    public static String[] teleposerBlacklisting;
    public static ArrayList<BlockStack> teleposerBlacklist = new ArrayList<BlockStack>();

    // Transposition Sigil
    public static String[] transpositionBlacklisting;
    public static ArrayList<BlockStack> transpositionBlacklist = new ArrayList<BlockStack>();

    // Item/Block Disabling
    public static List<String> itemBlacklist;
    public static List<String> blockBlacklist;

    // Well of Suffering Blacklist
    public static List<String> wellOfSufferingBlacklist;

    // Blood Altar Sacrificial Values
    public static String[] entitySacrificeValuesList;
    public static Map<String, Integer> entitySacrificeValues = new HashMap<String, Integer>();

    // Rituals
    public static boolean ritualAnimalGrowth;
    public static boolean ritualContainment;
    public static boolean ritualCrushing;
    public static boolean ritualExpulsion;
    public static boolean ritualFeatheredKnife;
    public static boolean ritualFullStomach;
    public static boolean ritualGreenGrove;
    public static boolean ritualHarvest;
    public static boolean ritualInterdiction;
    public static boolean ritualJumping;
    public static boolean ritualLava;
    public static boolean ritualMagnetic;
    public static boolean ritualRegeneration;
    public static boolean ritualSpeed;
    public static boolean ritualSuppression;
    public static boolean ritualWater;
    public static boolean ritualWellOfSuffering;
    public static boolean ritualZephyr;
    public static boolean ritualUpgradeRemove;
    public static boolean ritualArmourEvolve;

    public static boolean cobblestoneRitual;
    public static boolean placerRitual;
    public static boolean fellingRitual;
    public static boolean pumpRitual;
    public static boolean altarBuilderRitual;
    public static boolean portalRitual;

    // Imperfect Rituals
    public static boolean imperfectRitualNight;
    public static boolean imperfectRitualRain;
    public static boolean imperfectRitualResistance;
    public static boolean imperfectRitualZombie;

    // Potion ID's
    public static int customPotionDrowningID;
    public static int customPotionBoostID;
    public static int customPotionProjProtID;
    public static int customPotionInhibitID;
    public static int customPotionFlightID;
    public static int customPotionReciprocationID;
    public static int customPotionFlameCloakID;
    public static int customPotionIceCloakID;
    public static int customPotionHeavyHeartID;
    public static int customPotionFireFuseID;
    public static int customPotionPlanarBindingID;
    public static int customPotionSoulFrayID;
    public static int customPotionSoulHardenID;
    public static int customPotionDeafID;
    public static int customPotionFeatherFallID;
    public static int customPotionDemonCloakID;
    public static int customPotionAmphibianID;

    // Potion toggles
    public static boolean customPotionDrowningEnabled;
    public static boolean customPotionBoostEnabled;
    public static boolean customPotionProjProtEnabled;
    public static boolean customPotionInhibitEnabled;
    public static boolean customPotionFlightEnabled;
    public static boolean customPotionReciprocationEnabled;
    public static boolean customPotionFlameCloakEnabled;
    public static boolean customPotionIceCloakEnabled;
    public static boolean customPotionHeavyHeartEnabled;
    public static boolean customPotionFireFuseEnabled;
    public static boolean customPotionPlanarBindingEnabled;
    public static boolean customPotionSoulFrayEnabled;
    public static boolean customPotionSoulHardenEnabled;
    public static boolean customPotionDeafEnabled;
    public static boolean customPotionFeatherFallEnabled;
    public static boolean customPotionDemonCloakEnabled;
    public static boolean customPotionAmphibianEnabled;
    public static boolean vanillaPotionRegenerationEnabled;
    public static boolean vanillaPotionNightVisionEnabled;
    public static boolean vanillaPotionFireResistEnabled;
    public static boolean vanillaPotionWaterBreathingEnabled;
    public static boolean vanillaPotionSpeedEnabled;
    public static boolean vanillaPotionHealthEnabled;
    public static boolean vanillaPotionPoisonEnabled;
    public static boolean vanillaPotionBlindnessEnabled;
    public static boolean vanillaPotionWeaknessEnabled;
    public static boolean vanillaPotionStrengthEnabled;
    public static boolean vanillaPotionJumpBoostEnabled;
    public static boolean vanillaPotionSlownessEnabled;
    public static boolean vanillaPotionMiningEnabled;
    public static boolean vanillaPotionInvisibilityEnabled;
    public static boolean vanillaPotionResistanceEnabled;
    public static boolean vanillaPotionSaturationEnabled;
    public static boolean vanillaPotionHealthBoostEnabled;
    public static boolean vanillaPotionAbsorptionEnabled;

    // General
    public static int sacrificialPackConversion;

    // Client
    public static boolean alwaysRenderRoutingLines;

    // Compat
    public static int wailaAltarDisplayMode;
    public static boolean thaumcraftGogglesUpgrade;

    // IDontLikeFun
    public static boolean antiHitler;

    public static void init(File file)
    {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig()
    {
        String category;

        category = "Item/Block Blacklisting";
        config.addCustomCategoryComment(category, "Allows disabling of specific Blocks/Items.\nNote that using this may result in crashes. Use is not supported.");
        config.setCategoryRequiresMcRestart(category, true);
        itemBlacklist = Arrays.asList(config.getStringList("itemBlacklist", category, new String[] {}, "Items to not be registered. This requires their mapping name. Usually the same as the class name. Can be found in F3+H mode."));
        blockBlacklist = Arrays.asList(config.getStringList("blockBlacklist", category, new String[] {}, "Blocks to not be registered. This requires their mapping name. Usually the same as the class name. Can be found in F3+H mode."));

        category = "Teleposer Blacklist";
        config.addCustomCategoryComment(category, "Block blacklisting");
        teleposerBlacklisting = config.getStringList("teleposerBlacklist", category, new String[] { "minecraft:bedrock", "minecraft:mob_spawner" }, "Stops specified blocks from being teleposed. Put entries on new lines. Valid syntax is:\nmodid:blockname:meta");
        buildBlacklist(teleposerBlacklisting, teleposerBlacklist);

        category = "Transposition Sigil Blacklist";
        config.addCustomCategoryComment(category, "Block blacklisting");
        transpositionBlacklisting = config.getStringList("transpositionBlacklist", category, new String[] { "minecraft:bedrock" }, "Stops specified blocks from being teleposed. Put entries on new lines. Valid syntax is:\nmodid:blockname:meta");
        buildBlacklist(transpositionBlacklisting, transpositionBlacklist);

        category = "Well of Suffering Blacklist";
        config.addCustomCategoryComment(category, "Entity blacklisting from WoS");
        wellOfSufferingBlacklist = Arrays.asList(config.getStringList("wellOfSufferingBlacklist", category, new String[] {}, "Use the class name of the Entity to blacklist it from usage.\nIE: EntityWolf, EntityWitch, etc"));

        category = "Blood Altar Sacrificial Values";
        config.addCustomCategoryComment(category, "Entity Sacrificial Value Settings");
        entitySacrificeValuesList = config.getStringList("entitySacrificeValues", category, new String[] { "EntityVillager;2000", "EntitySlime;150", "EntityEnderman;200", "EntityCow;250", "EntityChicken;250", "EntityHorse;250", "EntitySheep;250", "EntityWolf;250", "EntityOcelot;250", "EntityPig;250", "EntityRabbit;250" }, "Used to edit the amount of LP gained per sacrifice of the given entity.\nSetting an entity to 0 effectively blacklists it.\nIf a mod modifies an entity via the API, it will take precedence over this config.\nSyntax: EntityClassName;LPPerSacrifice");
        buildEntitySacrificeValues();

        category = "Potions";
        config.addCustomCategoryComment(category, "Potion settings");
        config.addCustomCategoryComment(category + ".id", "Potion ID settings");
        customPotionDrowningID = config.getInt("customPotionDrowningID", category + ".id", 100, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Drowning potion");
        customPotionBoostID = config.getInt("customPotionBoostID", category + ".id", 101, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Boost potion");
        customPotionProjProtID = config.getInt("customPotionProjProtID", category + ".id", 102, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Projectile Protection potion");
        customPotionInhibitID = config.getInt("customPotionInhibitID", category + ".id", 103, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Inhibit potion");
        customPotionFlightID = config.getInt("customPotionFlightID", category + ".id", 104, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Flight potion");
        customPotionReciprocationID = config.getInt("customPotionReciprocationID", category + ".id", 105, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Reciprocation potion");
        customPotionFlameCloakID = config.getInt("customPotionFlameCloakID", category + ".id", 106, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Flame Cloak potion");
        customPotionIceCloakID = config.getInt("customPotionIceCloakID", category + ".id", 107, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Ice Cloak potion");
        customPotionHeavyHeartID = config.getInt("customPotionHeavyHeartID", category + ".id", 108, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Heavy Heart potion");
        customPotionFireFuseID = config.getInt("customPotionFireFuseID", category + ".id", 109, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Fire Fuse potion");
        customPotionPlanarBindingID = config.getInt("customPotionPlanarBindingID", category + ".id", 110, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Planar Binding potion");
        customPotionSoulFrayID = config.getInt("customPotionSoulFrayID", category + ".id", 111, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Soul Fray potion");
        customPotionSoulHardenID = config.getInt("customPotionSoulHardenID", category + ".id", 112, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Soul Harden potion");
        customPotionDeafID = config.getInt("customPotionDeafID", category + ".id", 113, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Deaf potion");
        customPotionFeatherFallID = config.getInt("customPotionFeatherFallID", category + ".id", 114, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Feather Fall potion");
        customPotionDemonCloakID = config.getInt("customPotionDemonCloakID", category + ".id", 115, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Demon Cloak potion");
        customPotionAmphibianID = config.getInt("customPotionAmphibianID", category + ".id", 116, 20, Constants.Misc.POTION_ARRAY_SIZE, "ID of the Amphibian potion");

        config.addCustomCategoryComment(category + ".toggle", "Toggle potions available in Alchemy");
        customPotionDrowningEnabled = config.getBoolean("customPotionDrowningEnabled", category + ".toggle", true, "Enables the Drowning potion in Alchemy");
        customPotionBoostEnabled = config.getBoolean("customPotionBoostEnabled", category + ".toggle", true, "Enables the Boost potion in Alchemy");
        customPotionProjProtEnabled = config.getBoolean("customPotionProjProtEnabled", category + ".toggle", true, "Enables the Projectile Protection potion in Alchemy");
        customPotionInhibitEnabled = config.getBoolean("customPotionInhibitEnabled", category + ".toggle", true, "Enables the Inhibit potion in Alchemy");
        customPotionFlightEnabled = config.getBoolean("customPotionFlightEnabled", category + ".toggle", true, "Enables the Flight potion in Alchemy");
        customPotionReciprocationEnabled = config.getBoolean("customPotionReciprocationEnabled", category + ".toggle", true, "Enables the Reciprocation potion in Alchemy");
        customPotionFlameCloakEnabled = config.getBoolean("customPotionFlameCloakEnabled", category + ".toggle", true, "Enables the Flame Cloak potion in Alchemy");
        customPotionIceCloakEnabled = config.getBoolean("customPotionIceCloakEnabled", category + ".toggle", true, "Enables the Ice Cloak potion in Alchemy");
        customPotionHeavyHeartEnabled = config.getBoolean("customPotionHeavyHeartEnabled", category + ".toggle", true, "Enables the Heavy Heart potion in Alchemy");
        customPotionFireFuseEnabled = config.getBoolean("customPotionFireFuseEnabled", category + ".toggle", true, "Enables the Fire Fuse potion in Alchemy");
        customPotionPlanarBindingEnabled = config.getBoolean("customPotionPlanarBindingEnabled", category + ".toggle", true, "Enables the Planar Binding potion in Alchemy");
        customPotionSoulFrayEnabled = config.getBoolean("customPotionSoulFrayEnabled", category + ".toggle", true, "Enables the Soul Fray potion in Alchemy");
        customPotionSoulHardenEnabled = config.getBoolean("customPotionSoulHardenEnabled", category + ".toggle", true, "Enables the Soul Harden potion in Alchemy");
        customPotionDeafEnabled = config.getBoolean("customPotionDeafEnabled", category + ".toggle", true, "Enables the Deaf potion in Alchemy");
        customPotionFeatherFallEnabled = config.getBoolean("customPotionFeatherFallEnabled", category + ".toggle", true, "Enables the Feather Fall potion in Alchemy");
        customPotionDemonCloakEnabled = config.getBoolean("customPotionDemonCloakEnabled", category + ".toggle", true, "Enables the Demon Cloak potion in Alchemy");
        customPotionAmphibianEnabled = config.getBoolean("customPotionAmphibianEnabled", category + ".toggle", true, "Enables the Amphibian potion in Alchemy");
        vanillaPotionAbsorptionEnabled = config.getBoolean("vanillaPotionAbsorptionEnabled", category + ".toggle", true, "Enables the Absorption potion in Alchemy");
        vanillaPotionBlindnessEnabled = config.getBoolean("vanillaPotionBlindnessEnabled", category + ".toggle", true, "Enables the Blindness potion in Alchemy");
        vanillaPotionFireResistEnabled = config.getBoolean("vanillaPotionFireResistEnabled", category + ".toggle", true, "Enables the Fire Resistance potion in Alchemy");
        vanillaPotionHealthBoostEnabled = config.getBoolean("vanillaPotionHealthBoostEnabled", category + ".toggle", true, "Enables the Health Boost potion in Alchemy");
        vanillaPotionHealthEnabled = config.getBoolean("vanillaPotionHealthEnabled", category + ".toggle", true, "Enables the Instant Health potion in Alchemy");
        vanillaPotionInvisibilityEnabled = config.getBoolean("vanillaPotionInvisibilityEnabled", category + ".toggle", true, "Enables the Invisibility potion in Alchemy");
        vanillaPotionJumpBoostEnabled = config.getBoolean("vanillaPotionJumpBoostEnabled", category + ".toggle", true, "Enables the Jump Boost potion in Alchemy");
        vanillaPotionMiningEnabled = config.getBoolean("vanillaPotionMiningEnabled", category + ".toggle", true, "Enables the Mining potion in Alchemy");
        vanillaPotionPoisonEnabled = config.getBoolean("vanillaPotionPoisonEnabled", category + ".toggle", true, "Enables the Poison potion in Alchemy");
        vanillaPotionRegenerationEnabled = config.getBoolean("vanillaPotionRegenerationEnabled", category + ".toggle", true, "Enables the Regeneration potion in Alchemy");
        vanillaPotionNightVisionEnabled = config.getBoolean("vanillaPotionNightVisionEnabled", category + ".toggle", true, "Enables the Night Vision potion in Alchemy");
        vanillaPotionResistanceEnabled = config.getBoolean("vanillaPotionResistanceEnabled", category + ".toggle", true, "Enables the Resistance potion in Alchemy");
        vanillaPotionSaturationEnabled = config.getBoolean("vanillaPotionSaturationEnabled", category + ".toggle", true, "Enables the Saturation potion in Alchemy");
        vanillaPotionSlownessEnabled = config.getBoolean("vanillaPotionSlownessEnabled", category + ".toggle", true, "Enables the Slowness potion in Alchemy");
        vanillaPotionSpeedEnabled = config.getBoolean("vanillaPotionSpeedEnabled", category + ".toggle", true, "Enables the Speed potion in Alchemy");
        vanillaPotionStrengthEnabled = config.getBoolean("vanillaPotionStrengthEnabled", category + ".toggle", true, "Enables the Strength potion in Alchemy");
        vanillaPotionWaterBreathingEnabled = config.getBoolean("vanillaPotionWaterBreathingEnabled", category + ".toggle", true, "Enables the Water Breathing potion in Alchemy");
        vanillaPotionWeaknessEnabled = config.getBoolean("vanillaPotionWeaknessEnabled", category + ".toggle", true, "Enables the Weakness potion in Alchemy");

        category = "Rituals";
        config.addCustomCategoryComment(category, "Ritual toggling");
        config.setCategoryRequiresMcRestart(category, true);
        ritualAnimalGrowth = config.get(category, "ritualAnimalGrowth", true).getBoolean();
        ritualContainment = config.get(category, "ritualContainment", true).getBoolean();
        ritualCrushing = config.get(category, "ritualCrushing", true).getBoolean();
        ritualExpulsion = config.get(category, "ritualExpulsion", true).getBoolean();
        ritualFeatheredKnife = config.get(category, "ritualFeatheredKnife", true).getBoolean();
        ritualFullStomach = config.get(category, "ritualFullStomach", true).getBoolean();
        ritualGreenGrove = config.get(category, "ritualGreenGrove", true).getBoolean();
        ritualHarvest = config.get(category, "ritualHarvest", true).getBoolean();
        ritualInterdiction = config.get(category, "ritualInterdiction", true).getBoolean();
        ritualJumping = config.get(category, "ritualJumping", true).getBoolean();
        ritualLava = config.get(category, "ritualLava", true).getBoolean();
        ritualMagnetic = config.get(category, "ritualMagnetic", true).getBoolean();
        ritualRegeneration = config.get(category, "ritualRegeneration", true).getBoolean();
        ritualSpeed = config.get(category, "ritualSpeed", true).getBoolean();
        ritualSuppression = config.get(category, "ritualSuppression", true).getBoolean();
        ritualWater = config.get(category, "ritualWater", true).getBoolean();
        ritualWellOfSuffering = config.get(category, "ritualWellOfSuffering", true).getBoolean();
        ritualZephyr = config.get(category, "ritualZephyr", true).getBoolean();
        ritualUpgradeRemove = config.get(category, "ritualRemove", true).getBoolean();
        ritualArmourEvolve = config.get(category, "ritualArmourEvolve", true).getBoolean();

        cobblestoneRitual = config.get(category, "ritualCobblestone", true).getBoolean();
        placerRitual = config.get(category, "ritualPlacer", true).getBoolean();
        fellingRitual = config.get(category, "ritualFelling", true).getBoolean();
        pumpRitual = config.get(category, "ritualPump", true).getBoolean();
        altarBuilderRitual = config.get(category, "ritualAltarBuilder", true).getBoolean();
        portalRitual = config.get(category, "ritualPortal", true).getBoolean();

        category = "Rituals.Imperfect";
        imperfectRitualNight = config.get(category, "imperfectRitualNight", true).getBoolean();
        imperfectRitualRain = config.get(category, "imperfectRitualRain", true).getBoolean();
        imperfectRitualResistance = config.get(category, "imperfectRitualResistance", true).getBoolean();
        imperfectRitualZombie = config.get(category, "imperfectRitualZombie", true).getBoolean();

        category = "General";
        config.addCustomCategoryComment(category, "General settings");
        BloodMagicAPI.setLoggingEnabled(config.getBoolean("enableLogging", category, true, "Allows logging information to the console. Fatal errors will bypass this"));
        sacrificialPackConversion = config.getInt("sacrificialPackConversion", category, 20, 0, 100, "Base multiplier for the Coat of Arms. DamageDealt * sacrificialPackConversion");

        category = "Client";
        config.addCustomCategoryComment(category, "Client only settings");
        alwaysRenderRoutingLines = config.getBoolean("alwaysRenderRoutingLines", category, false, "Always renders the beams between routing nodes. If false, only renders while a Node Router is being held.");

        category = "Compatibility";
        config.addCustomCategoryComment(category, "Compatibility settings");
        wailaAltarDisplayMode = config.getInt("wailaAltarDisplayMode", category + ".waila", 1, 0, 2, "The mode for the Waila display on Blood Altars.\n0 - Always display information\n1 - Only display when Divination/Seer sigil is in hand.\n2 - Only display when Divination/Seer sigil is in inventory");
        thaumcraftGogglesUpgrade = config.getBoolean("thaumcraftGogglesUpgrade", category + ".thaumcraft", true, "Allows the Living Helmet to be upgraded with Goggles of Revealing in an Anvil.");

        category = "IDontLikeFun";
        config.addCustomCategoryComment(category, "My name is Scrooge.");
        antiHitler = config.get(category, "replaceNauseaWithWeakness", false).getBoolean();

        config.save();
    }

    private static void buildBlacklist(String[] blacklisting, List<BlockStack> blockBlacklist)
    {
        blockBlacklist.clear();

        for (String blockSet : blacklisting)
        {
            String[] blockData = blockSet.split(":");

            Block block = GameRegistry.findBlock(blockData[0], blockData[1]);
            int meta = 0;

            if (blockData.length == 3)
            {
                // Check if it's an int, if so, parse it. If not, set meta to 0
                // to avoid crashing.
                if (Utils.isInteger(blockData[2]))
                    meta = Integer.parseInt(blockData[2]);
                else if (blockData[2].equals("*"))
                    meta = OreDictionary.WILDCARD_VALUE;
                else
                    meta = 0;
            }

            blockBlacklist.add(new BlockStack(block, meta));
        }
    }

    private static void buildEntitySacrificeValues()
    {
        entitySacrificeValues.clear();

        for (String entityData : entitySacrificeValuesList)
        {
            String[] split = entityData.split(";");

            int amount = 500;
            if (Utils.isInteger(split[1]))
                amount = Integer.parseInt(split[1]);

            if (!entitySacrificeValues.containsKey(split[0]))
                entitySacrificeValues.put(split[0], amount);
        }
    }
}
