package WayofTime.bloodmagic;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.RitualHelper;
import WayofTime.bloodmagic.registry.ModPotions;
import WayofTime.bloodmagic.util.Utils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigHandler {

    @Getter @Setter
    private static Configuration config;

    // Teleposer
    public static String[] teleposerBlacklisting;
    public static ArrayList<BlockStack> teleposerBlacklist = new ArrayList<BlockStack>();

    // Item/Block Disabling
    public static List itemBlacklist;
    public static List blockBlacklist;

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

    // Compat

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        String category;

        category = "Item/Block Blacklisting";
        config.addCustomCategoryComment(category, "Allows disabling of specific Blocks/Items.\nNote that using this may result in crashes. Use is not supported.");
        config.setCategoryRequiresMcRestart(category, true);
        itemBlacklist = Arrays.asList(config.getStringList("itemBlacklist", category, new String[]{}, "Items to not be registered. This requires their mapping name. Usually the same as the class name. Can be found in F3+H mode."));
        blockBlacklist = Arrays.asList(config.getStringList("blockBlacklist", category, new String[]{}, "Blocks to not be registered. This requires their mapping name. Usually the same as the class name. Can be found in F3+H mode."));

        category = "Teleposer Blacklist";
        config.addCustomCategoryComment(category, "Block blacklisting");
        teleposerBlacklisting = config.getStringList("teleposerBlacklist", category, new String[]{"minecraft:bedrock"}, "Stops specified blocks from being teleposed. Put entries on new lines. Valid syntax is:\nmodid:blockname:meta");
        buildTeleposerBlacklist();

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

        category = "General";
        config.addCustomCategoryComment(category, "General settings");
        BloodMagicAPI.setLoggingEnabled(config.getBoolean("enableLogging", category, true, "Allows logging information to the console. Fatal errors will bypass this"));

        category = "Compatibility";
        config.addCustomCategoryComment(category, "Compatibility settings");

        config.save();
    }

    private static void buildTeleposerBlacklist() {

        // Make sure it's empty before setting the blacklist.
        // Otherwise, reloading the config while in-game will duplicate the list.
        teleposerBlacklist.clear();

        for (String blockSet : teleposerBlacklisting) {
            String[] blockData = blockSet.split(":");

            Block block = GameRegistry.findBlock(blockData[0], blockData[1]);
            int meta = 0;

            // If the block follows full syntax: modid:blockname:meta
            if (blockData.length == 3) {
                // Check if it's an int, if so, parse it. If not, set meta to 0 to avoid crashing.
                if (Utils.isInteger(blockData[2]))
                    meta = Integer.parseInt(blockData[2]);
                else if (blockData[2].equals("*"))
                    meta = OreDictionary.WILDCARD_VALUE;
                else
                    meta = 0;
            }

            teleposerBlacklist.add(new BlockStack(block, meta));
        }
    }

    public static void checkRituals() {
        RitualHelper.checkImperfectRituals(config, "WayofTime.bloodmagic.ritual.imperfect", "Rituals.imperfect");
        config.save();
    }
}
