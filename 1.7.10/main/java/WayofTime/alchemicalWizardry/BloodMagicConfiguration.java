package WayofTime.alchemicalWizardry;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.config.Configuration;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorParadigm;
import cpw.mods.fml.common.FMLLog;

/**
 * Created with IntelliJ IDEA.
 * User: Pokefenn
 * Date: 17/01/14
 * Time: 19:50
 */
public class BloodMagicConfiguration
{


    public static Configuration config;
    public static final String CATEGORY_GAMEPLAY = "gameplay";


    public static void init(File configFile)
    {

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
            AlchemicalWizardry.customPotionFlameCloakID = config.get("Potion ID","FlameCloak",106).getInt();
            AlchemicalWizardry.customPotionIceCloakID = config.get("Potion ID","IceCloak",107).getInt();
            AlchemicalWizardry.customPotionHeavyHeartID = config.get("Potion ID","HeavyHeart",108).getInt();
            AlchemicalWizardry.customPotionFireFuseID = config.get("Potion ID","FireFuse",109).getInt();
            AlchemicalWizardry.customPotionPlanarBindingID = config.get("Potion ID","PlanarBinding",110).getInt();
            
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

            AlchemicalWizardry.allowedCrushedOresArray = config.get("oreCrushing", "allowedOres", new String[]{"iron","gold","copper","tin","lead","silver","osmium"}).getStringList();
            
            AlchemicalWizardry.wimpySettings = config.get("WimpySettings","IDontLikeFun",false).getBoolean(false);
            
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

}
