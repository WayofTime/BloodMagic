package WayofTime.bloodmagic;

import WayofTime.bloodmagic.meteor.MeteorConfigHandler;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = BloodMagic.MODID, name = BloodMagic.MODID + "/" + BloodMagic.MODID, category = "")
@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class ConfigHandler
{

    @Config.Comment({ "General settings" })
    public static ConfigGeneral general = new ConfigGeneral();
    @Config.Comment({ "Blacklist options for various features" })
    public static ConfigBlacklist blacklist = new ConfigBlacklist();
    @Config.Comment({ "Value modifiers for various features" })
    public static ConfigValues values = new ConfigValues();
    @Config.Comment({ "Toggles for all rituals" })
    public static ConfigRituals rituals = new ConfigRituals();
    @Config.Comment({ "Settings that only pertain to the client" })
    public static ConfigClient client = new ConfigClient();
    @Config.Comment({ "Compatibility settings" })
    public static ConfigCompat compat = new ConfigCompat();

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(BloodMagic.MODID))
        {
            ConfigManager.sync(event.getModID(), Config.Type.INSTANCE); // Sync config values
            MeteorConfigHandler.handleMeteors(false); // Reload meteors
        }
    }

    public static class ConfigGeneral
    {
        @Config.Comment({ "Enables extra information to be printed to the log.", "Warning: May drastically increase log size." })
        public boolean enableDebugLogging = false;
        @Config.Comment({ "Enables extra information to be printed to the log." })
        public boolean enableAPILogging = false;
        @Config.Comment({ "Enables extra information to be printed to the log.", "Warning: May drastically increase log size." })
        public boolean enableVerboseAPILogging = false;
    }

    public static class ConfigBlacklist
    {
        @Config.Comment({ "Stops listed blocks and entities from being teleposed.", "Use the registry name of the block or entity. Vanilla objects do not require the modid.", "If a block is specified, you can list the variants to only blacklist a given state." })
        public String[] teleposer = { "bedrock", "mob_spawner" };
        @Config.Comment({ "Stops listed blocks from being transposed.", "Use the registry name of the block. Vanilla blocks do not require the modid." })
        public String[] transposer = { "bedrock", "mob_spawner" };
        @Config.Comment({ "Stops the listed entities from being used in the Well of Suffering.", "Use the registry name of the entity. Vanilla entities do not require the modid." })
        public String[] wellOfSuffering = {};
    }

    public static class ConfigValues
    {
        @Config.Comment({ "Declares the amount of LP gained per HP sacrificed for the given entity.", "Setting the value to 0 will blacklist it.", "Use the registry name of the entity followed by a ';' and then the value you want.", "Vanilla entities do not require the modid." })
        public String[] sacrificialValues = { "villager;100", "slime;15", "enderman;10", "cow;100", "chicken;100", "horse;100", "sheep;100", "wolf;100", "ocelot;100", "pig;100", "rabbit;100" };
        @Config.Comment({ "Amount of LP the Coat of Arms should provide for each damage dealt." })
        @Config.RangeInt(min = 0, max = 100)
        public int coatOfArmsConversion = 20;
        @Config.Comment({ "Amount of LP the Sacrificial Dagger should provide for each damage dealt." })
        @Config.RangeInt(min = 0, max = 10000)
        public int sacrificialDaggerConversion = 100;
        @Config.Comment({ "Will rewrite any default meteor types with new versions.", "Disable this if you want any of your changes to stay, or do not want default meteor types regenerated." })
        public boolean shouldResyncMeteors = true;
    }

    public static class ConfigRituals
    {
        public boolean ritualAnimalGrowth = true;
        public boolean ritualContainment = true;
        public boolean ritualCrushing = true;
        public boolean ritualExpulsion = true;
        public boolean ritualFeatheredKnife = true;
        public boolean ritualFullStomach = true;
        public boolean ritualGreenGrove = true;
        public boolean ritualHarvest = true;
        public boolean ritualInterdiction = true;
        public boolean ritualJumping = true;
        public boolean ritualLava = true;
        public boolean ritualMagnetic = true;
        public boolean ritualRegeneration = true;
        public boolean ritualSpeed = true;
        public boolean ritualSuppression = true;
        public boolean ritualWater = true;
        public boolean ritualWellOfSuffering = true;
        public boolean ritualZephyr = true;
        public boolean ritualUpgradeRemove = true;
        public boolean ritualArmourEvolve = true;
        public boolean ritualForsakenSoul = true;
        public boolean ritualCrystalHarvest = true;
        public boolean ritualPlacer = true;
        public boolean ritualFelling = true;
        public boolean ritualPump = true;
        public boolean ritualAltarBuilder = true;
        public boolean ritualPortal = true;
        public boolean ritualMeteor = true;
        public boolean ritualDowngrade = true;
        public boolean ritualEllipsoid = true;
        public boolean ritualCrystalSplit = true;
        public ConfigImperfectRituals imperfect = new ConfigImperfectRituals();
    }

    public static class ConfigImperfectRituals
    {
        public boolean imperfectRitualNight = true;
        public boolean imperfectRitualRain = true;
        public boolean imperfectRitualResistance = true;
        public boolean imperfectRitualZombie = true;
    }

    public static class ConfigClient
    {
        @Config.Comment({ "Always render the beams between routing nodes.", "If disabled, the beams will only render while the Node Router is held." })
        public boolean alwaysRenderRoutingLines = false;
        @Config.Comment({ "Completely hide spectral blocks from view.", "If disabled, a transparent block will be displayed." })
        public boolean invisibleSpectralBlocks = true;
        @Config.Comment({ "When cycling through slots, the Sigil of Holding will skip over empty slots and move to the next occupied one.", "If disabled, it will behave identically to the default hotbar." })
        public boolean sigilHoldingSkipsEmptySlots = false;
    }

    public static class ConfigCompat
    {
        @Config.Comment({ "The display mode to use when looking at a Blood Altar.", "ALWAYS - Always display information.", "SIGIL_HELD - Only display information when a Divination or Seers sigil is held in either hand.", "SIGIL_CONTAINED - Only display information when a Divination or Seers sigil is somewhere in the inventory." })
        public AltarDisplayMode wailaAltarDisplayMode = AltarDisplayMode.SIGIL_HELD;

        public enum AltarDisplayMode
        {
            ALWAYS,
            SIGIL_HELD,
            SIGIL_CONTAINED, ;
        }
    }
}
