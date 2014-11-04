package WayofTime.alchemicalWizardry;

import WayofTime.alchemicalWizardry.client.renderer.ColourThreshold;
import WayofTime.alchemicalWizardry.client.renderer.RenderHelper;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorParadigm;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Created with IntelliJ IDEA.
 * User: Pokefenn
 * Date: 17/01/14
 * Time: 19:50
 */
public class BloodMagicConfiguration
{
    private static final String DEFAULT_COLOR_LIST = "100,f; 80,7; 60,e; 40,6; 25,c; 10,4";
    public static final List<ColourThreshold> colorList = new ArrayList<ColourThreshold>();

    public static Configuration config;
    public static final String CATEGORY_GAMEPLAY = "gameplay";


    public static void init(File configFile)
    {
        for (String s : DEFAULT_COLOR_LIST.split(";"))
        {
            String[] ct = s.split(",");
            colorList.add(new ColourThreshold(Integer.valueOf(ct[0].trim()), ct[1].trim()));
        }


        config = new Configuration(configFile);

        try
        {

            config.load();

            AlchemicalWizardry.standardBindingAgentDungeonChance = config.get("Dungeon Loot Chances", "standardBindingAgent", 30).getInt();
            AlchemicalWizardry.mundanePowerCatalystDungeonChance = config.get("Dungeon Loot Chances", "mundanePowerCatalyst", 20).getInt();
            AlchemicalWizardry.averagePowerCatalystDungeonChance = config.get("Dungeon Loot Chances", "averagePowerCatalyst", 10).getInt();
            AlchemicalWizardry.greaterPowerCatalystDungeonChance = config.get("Dungeon Loot Chances", "greaterPowerCatalyst", 05).getInt();
            AlchemicalWizardry.mundaneLengtheningCatalystDungeonChance = config.get("Dungeon Loot Chances", "mundaneLengtheningCatalyst", 20).getInt();
            AlchemicalWizardry.averageLengtheningCatalystDungeonChance = config.get("Dungeon Loot Chances", "averageLengtheningCatalyst", 10).getInt();
            AlchemicalWizardry.greaterLengtheningCatalystDungeonChance = config.get("Dungeon Loot Chances", "greaterLengtheningCatalyst", 05).getInt();
            AlchemicalWizardry.customPotionDrowningID = config.get("Potion ID", "Drowning", 100).getInt();
            AlchemicalWizardry.customPotionBoostID = config.get("Potion ID", "Boost", 101).getInt();
            AlchemicalWizardry.customPotionProjProtID = config.get("Potion ID", "ProjProt", 102).getInt();
            AlchemicalWizardry.customPotionInhibitID = config.get("Potion ID", "Inhibit", 103).getInt();
            AlchemicalWizardry.customPotionFlightID = config.get("Potion ID", "Flight", 104).getInt();
            AlchemicalWizardry.customPotionReciprocationID = config.get("Potion ID", "Reciprocation", 105).getInt();
            AlchemicalWizardry.customPotionFlameCloakID = config.get("Potion ID", "FlameCloak", 106).getInt();
            AlchemicalWizardry.customPotionIceCloakID = config.get("Potion ID", "IceCloak", 107).getInt();
            AlchemicalWizardry.customPotionHeavyHeartID = config.get("Potion ID", "HeavyHeart", 108).getInt();
            AlchemicalWizardry.customPotionFireFuseID = config.get("Potion ID", "FireFuse", 109).getInt();
            AlchemicalWizardry.customPotionPlanarBindingID = config.get("Potion ID", "PlanarBinding", 110).getInt();
            AlchemicalWizardry.customPotionSoulFrayID = config.get("Potion ID", "SoulFray", 111).getInt();
            AlchemicalWizardry.customPotionSoulHardenID = config.get("Potion ID", "SoulHarden", 112).getInt();
            AlchemicalWizardry.customPotionDeafID = config.get("Potion ID", "Deaf", 113).getInt();
            AlchemicalWizardry.customPotionFeatherFallID = config.get("Potion ID", "FeatherFall", 114).getInt();

            MeteorParadigm.maxChance = config.get("meteor", "maxChance", 1000).getInt();
            AlchemicalWizardry.doMeteorsDestroyBlocks = config.get("meteor", "doMeteorsDestroyBlocks", true).getBoolean(true);
            AlchemicalWizardry.diamondMeteorArray = config.get("meteor", "diamondMeteor", new String[]{"oreDiamond", "100", "oreEmerald", "75", "oreCinnabar", "200", "oreAmber", "200"}).getStringList();
            AlchemicalWizardry.diamondMeteorRadius = config.get("meteor", "diamondMeteorRadius", 5).getInt();
            AlchemicalWizardry.stoneMeteorArray = config.get("meteor", "stoneBlockMeteor", new String[]{"oreCoal", "150", "oreApatite", "50", "oreIron", "50"}).getStringList();
            AlchemicalWizardry.stoneMeteorRadius = config.get("meteor", "stoneMeteorRadius", 16).getInt();
            AlchemicalWizardry.ironBlockMeteorArray = config.get("meteor", "ironBlockMeteor", new String[]{"oreIron", "400", "oreGold", "30", "oreCopper", "200", "oreTin", "140", "oreSilver", "70", "oreLead", "80", "oreLapis", "60", "oreRedstone", "100"}).getStringList();
            AlchemicalWizardry.ironBlockMeteorRadius = config.get("meteor", "ironBlockMeteorRadius", 7).getInt();
            AlchemicalWizardry.netherStarMeteorArray = config.get("meteor", "netherStarMeteor", new String[]{"oreDiamond", "150", "oreEmerald", "100", "oreQuartz", "250", "oreSunstone", "5", "oreMoonstone", "50", "oreIridium", "5", "oreCertusQuartz", "150"}).getStringList();
            AlchemicalWizardry.netherStarMeteorRadius = config.get("meteor", "netherStarMeteorRadius", 3).getInt();

            AlchemicalWizardry.allowedCrushedOresArray = config.get("oreCrushing", "allowedOres", new String[]{"iron", "gold", "copper", "tin", "lead", "silver", "osmium"}).getStringList();

            AlchemicalWizardry.wimpySettings = config.get("WimpySettings", "IDontLikeFun", false).getBoolean(false);
            AlchemicalWizardry.respawnWithDebuff = config.get("WimpySettings", "RespawnWithDebuff", true).getBoolean();
            AlchemicalWizardry.causeHungerWithRegen = config.get("WimpySettings", "causeHungerWithRegen", true).getBoolean();
//            AlchemicalWizardry.lockdownAltar = config.get("WimpySettings", "LockdownAltarWithRegen", true).getBoolean();
            AlchemicalWizardry.lockdownAltar = false;

            Side side = FMLCommonHandler.instance().getSide();
            if (side == Side.CLIENT)
            {
                RenderHelper.xOffset = config.get("ClientSettings", "AlchemyHUDxOffset", 50).getInt();
                RenderHelper.yOffset = config.get("ClientSettings", "AlchemyHUDyOffset", 2).getInt();
            }


        } catch (Exception e)
        {

            //TODO Log
            //FMLLog.log(Level.SEVERE, e, "Blood Magic" + " has had a problem loading its configuration, go ask on the forums :p");

        } finally
        {
            config.save();
        }
    }

    public static void set(String categoryName, String propertyName, String newValue)
    {

        config.load();
        if (config.getCategoryNames().contains(categoryName))
        {
            if (config.getCategory(categoryName).containsKey(propertyName))
            {
                config.getCategory(categoryName).get(propertyName).set(newValue);
            }
        }
        config.save();


    }

    public static void loadBlacklist()
    {
        AlchemicalWizardry.wellBlacklist = new ArrayList<Class>();
        for (Object o : EntityList.stringToClassMapping.entrySet())
        {
            Entry entry = (Entry) o;
            Class curClass = (Class) entry.getValue();
            boolean valid = EntityLivingBase.class.isAssignableFrom(curClass) && !Modifier.isAbstract(curClass.getModifiers());
            if (valid)
            {
                boolean blacklisted = config.get("wellOfSufferingBlackList", entry.getKey().toString(), false).getBoolean();
                if (blacklisted)
                    AlchemicalWizardry.wellBlacklist.add(curClass);
            }

        }
        config.save();
    }

    public static void blacklistRituals()
    {
        if (AlchemicalWizardry.ritualDisabledWater)
        {
            Rituals.ritualMap.remove("AW001Water");
            Rituals.keyList.remove("AW001Water");
        }
        if (AlchemicalWizardry.ritualDisabledLava)
        {
            Rituals.ritualMap.remove("AW002Lava");
            Rituals.keyList.remove("AW002Lava");
        }
        if (AlchemicalWizardry.ritualDisabledGreenGrove)
        {
            Rituals.ritualMap.remove("AW003GreenGrove");
            Rituals.keyList.remove("AW003GreenGrove");
        }
        if (AlchemicalWizardry.ritualDisabledInterdiction)
        {
            Rituals.ritualMap.remove("AW004Interdiction");
            Rituals.keyList.remove("AW004Interdiction");
        }
        if (AlchemicalWizardry.ritualDisabledContainment)
        {
            Rituals.ritualMap.remove("AW005Containment");
            Rituals.keyList.remove("AW005Containment");
        }
        if (AlchemicalWizardry.ritualDisabledBinding)
        {
            Rituals.ritualMap.remove("AW006Binding");
            Rituals.keyList.remove("AW006Binding");
        }
        if (AlchemicalWizardry.ritualDisabledUnbinding)
        {
            Rituals.ritualMap.remove("AW007Unbinding");//007 reporting for duty
            Rituals.keyList.remove("AW007Unbinding");
        }
        if (AlchemicalWizardry.ritualDisabledHighJump)
        {
            Rituals.ritualMap.remove("AW008HighJump");
            Rituals.keyList.remove("AW008HighJump");
        }
        if (AlchemicalWizardry.ritualDisabledMagnetism)
        {
            Rituals.ritualMap.remove("AW009Magnetism");
            Rituals.keyList.remove("AW009Magnetism");
        }
        if (AlchemicalWizardry.ritualDisabledCrusher)
        {
            Rituals.ritualMap.remove("AW010Crusher");
            Rituals.keyList.remove("AW010Crusher");
        }
        if (AlchemicalWizardry.ritualDisabledSpeed)
        {
            Rituals.ritualMap.remove("AW011Speed");
            Rituals.keyList.remove("AW011Speed");
        }
        if (AlchemicalWizardry.ritualDisabledAnimalGrowth)
        {
            Rituals.ritualMap.remove("AW012AnimalGrowth");
            Rituals.keyList.remove("AW012AnimalGrowth");
        }
        if (AlchemicalWizardry.ritualDisabledSuffering)
        {
            Rituals.ritualMap.remove("AW013Suffering");
            Rituals.keyList.remove("AW013Suffering");
        }
        if (AlchemicalWizardry.ritualDisabledRegen)
        {
            Rituals.ritualMap.remove("AW014Regen");
            Rituals.keyList.remove("AW014Regen");
        }
        if (AlchemicalWizardry.ritualDisabledFeatheredKnife)
        {
            Rituals.ritualMap.remove("AW015FeatheredKnife");
            Rituals.keyList.remove("AW015FeatheredKnife");
        }
        if (AlchemicalWizardry.ritualDisabledFeatheredEarth)
        {
            Rituals.ritualMap.remove("AW016FeatheredEarth");
            Rituals.keyList.remove("AW016FeatheredEarth");
        }
        if (AlchemicalWizardry.ritualDisabledGaia)
        {
            Rituals.ritualMap.remove("AW017Gaia");
            Rituals.keyList.remove("AW017Gaia");
        }
        if (AlchemicalWizardry.ritualDisabledCondor)
        {
            Rituals.ritualMap.remove("AW018Condor");
            Rituals.keyList.remove("AW018Condor");
        }
        if (AlchemicalWizardry.ritualDisabledFallingTower)
        {
            Rituals.ritualMap.remove("AW019FallingTower");
            Rituals.keyList.remove("AW019FallingTower");
        }
        if (AlchemicalWizardry.ritualDisabledBalladOfAlchemy)
        {
            Rituals.ritualMap.remove("AW020BalladOfAlchemy");
            Rituals.keyList.remove("AW020BalladOfAlchemy");
        }
        if (AlchemicalWizardry.ritualDisabledExpulsion)
        {
            Rituals.ritualMap.remove("AW021Expulsion");
            Rituals.keyList.remove("AW021Expulsion");
        }
        if (AlchemicalWizardry.ritualDisabledSuppression)
        {
            Rituals.ritualMap.remove("AW022Suppression");
            Rituals.keyList.remove("AW022Suppression");
        }
        if (AlchemicalWizardry.ritualDisabledZephyr)
        {
            Rituals.ritualMap.remove("AW023Zephyr");
            Rituals.keyList.remove("AW023Zephyr");
        }
        if (AlchemicalWizardry.ritualDisabledHarvest)
        {
            Rituals.ritualMap.remove("AW024Harvest");
            Rituals.keyList.remove("AW024Harvest");
        }
        if (AlchemicalWizardry.ritualDisabledConduit)
        {
            Rituals.ritualMap.remove("AW025Conduit");
            Rituals.keyList.remove("AW025Conduit");
        }
        if (AlchemicalWizardry.ritualDisabledEllipsoid)
        {
            Rituals.ritualMap.remove("AW026Ellipsoid");
            Rituals.keyList.remove("AW026Ellipsoid");
        }
        if (AlchemicalWizardry.ritualDisabledEvaporation)
        {
            Rituals.ritualMap.remove("AW027Evaporation");
            Rituals.keyList.remove("AW027Evaporation");
        }
        if (AlchemicalWizardry.ritualDisabledSpawnWard)
        {
            Rituals.ritualMap.remove("AW028SpawnWard");
            Rituals.keyList.remove("AW028SpawnWard");
        }
        if (AlchemicalWizardry.ritualDisabledVeilOfEvil)
        {
            Rituals.ritualMap.remove("AW029VeilOfEvil");
            Rituals.keyList.remove("AW029VeilOfEvil");
        }
        if (AlchemicalWizardry.ritualDisabledFullStomach)
        {
            Rituals.ritualMap.remove("AW030FullStomach");
            Rituals.keyList.remove("AW030FullStomach");
        }
    }
}
