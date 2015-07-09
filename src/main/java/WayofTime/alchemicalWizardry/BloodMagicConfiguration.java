package WayofTime.alchemicalWizardry;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.config.Configuration;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.client.renderer.ColourThreshold;
import WayofTime.alchemicalWizardry.client.renderer.RenderHelper;
import WayofTime.alchemicalWizardry.common.demonVillage.DemonVillagePath;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;
import WayofTime.alchemicalWizardry.common.items.armour.BoundArmour;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorParadigm;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

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

    public static String[] teleposerBlacklist;
    public static String[] blacklist = {};

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
			syncConfig();

		} catch (Exception e)
		{
			AlchemicalWizardry.logger.error("There has been a problem loading the configuration, go ask on the forums :p");

		} finally
		{
			config.save();
		}
	}

	public static void syncConfig()
	{
		AlchemicalWizardry.standardBindingAgentDungeonChance = config.get("Dungeon Loot Chances", "standardBindingAgent", 30).getInt();
		AlchemicalWizardry.mundanePowerCatalystDungeonChance = config.get("Dungeon Loot Chances", "mundanePowerCatalyst", 20).getInt();
		AlchemicalWizardry.averagePowerCatalystDungeonChance = config.get("Dungeon Loot Chances", "averagePowerCatalyst", 10).getInt();
		AlchemicalWizardry.greaterPowerCatalystDungeonChance = config.get("Dungeon Loot Chances", "greaterPowerCatalyst", 5).getInt();
		AlchemicalWizardry.mundaneLengtheningCatalystDungeonChance = config.get("Dungeon Loot Chances", "mundaneLengtheningCatalyst", 20).getInt();
		AlchemicalWizardry.averageLengtheningCatalystDungeonChance = config.get("Dungeon Loot Chances", "averageLengtheningCatalyst", 10).getInt();
		AlchemicalWizardry.greaterLengtheningCatalystDungeonChance = config.get("Dungeon Loot Chances", "greaterLengtheningCatalyst", 5).getInt();
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
		AlchemicalWizardry.customPotionDemonCloakID = config.get("Potion ID", "DemonCloak", 115).getInt();
		AlchemicalWizardry.customPotionAmphibianID = config.get("Potion ID", "Amphibian", 116).getInt();

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
		AlchemicalWizardry.causeHungerChatMessage = config.get("WimpySettings", "causeHungerChatMessage", true).getBoolean();
//		AlchemicalWizardry.lockdownAltar = config.get("WimpySettings", "LockdownAltarWithRegen", true).getBoolean();
		AlchemicalWizardry.lockdownAltar = false;

		AlchemicalWizardry.ritualDisabledWater = config.get("Ritual Blacklist", "Ritual of the Full Spring", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledLava = config.get("Ritual Blacklist", "Serenade of the Nether", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledGreenGrove = config.get("Ritual Blacklist", "Ritual of the Green Grove", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledInterdiction = config.get("Ritual Blacklist", "Interdiction Ritual", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledContainment = config.get("Ritual Blacklist", "Ritual of Containment", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledBinding = config.get("Ritual Blacklist", "Ritual of Binding", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledUnbinding = config.get("Ritual Blacklist", "Ritual of Unbinding", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledHighJump = config.get("Ritual Blacklist", "Ritual of the High Jump", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledMagnetism = config.get("Ritual Blacklist", "Ritual of Magnetism", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledCrusher = config.get("Ritual Blacklist", "Ritual of the Crusher", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledSpeed = config.get("Ritual Blacklist", "Ritual of Speed", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledAnimalGrowth = config.get("Ritual Blacklist", "Ritual of the Shepherd", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledSuffering = config.get("Ritual Blacklist", "Well of Suffering", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledRegen = config.get("Ritual Blacklist", "Ritual of Regeneration", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledFeatheredKnife = config.get("Ritual Blacklist", "Ritual of the Feathered Knife", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledFeatheredEarth = config.get("Ritual Blacklist", "Ritual of the Feathered Earth", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledGaia = config.get("Ritual Blacklist", "Ritual of Gaia's Transformation", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledCondor = config.get("Ritual Blacklist", "Reverence of the Condor", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledFallingTower = config.get("Ritual Blacklist", "Mark of the Falling Tower", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledBalladOfAlchemy = config.get("Ritual Blacklist", "Ballad of Alchemy", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledExpulsion = config.get("Ritual Blacklist", "Aura of Expulsion", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledSuppression = config.get("Ritual Blacklist", "Dome of Supression", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledZephyr = config.get("Ritual Blacklist", "Call of the Zephyr", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledHarvest = config.get("Ritual Blacklist", "Reap of the Harvest Moon", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledConduit = config.get("Ritual Blacklist", "Cry of the Eternal Soul", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledEllipsoid = config.get("Ritual Blacklist", "Focus of the Ellipsoid", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledEvaporation = config.get("Ritual Blacklist", "Song of Evaporation", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledSpawnWard = config.get("Ritual Blacklist", "Ward of Sacrosanctity", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledVeilOfEvil = config.get("Ritual Blacklist", "Veil of Evil", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledFullStomach = config.get("Ritual Blacklist", "Requiem of the Satiated Stomach", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledConvocation = config.get("Ritual Blacklist", "Convocation of the Damned", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledSymmetry = config.get("Ritual Blacklist", "Symmetry of the Omega", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledStalling = config.get("Ritual Blacklist", "Duet of the Fused Souls", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledCrafting = config.get("Ritual Blacklist", "Rhythm of the Beating Anvil", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledPhantomHands = config.get("Ritual Blacklist", "Orchestra of the Phantom Hands", false).getBoolean(false);
		AlchemicalWizardry.ritualDisabledSphereIsland = config.get("Ritual Blacklist", "Birth of the Bastion", false).getBoolean(false);

		AlchemicalWizardry.potionDisableRegen = config.get("Alchemy Potion Blacklist", "Regeneration", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableNightVision = config.get("Alchemy Potion Blacklist", "Night Vision", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableFireResistance = config.get("Alchemy Potion Blacklist", "Fire Resistance", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableWaterBreathing = config.get("Alchemy Potion Blacklist", "Water Breathing", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableMoveSpeed = config.get("Alchemy Potion Blacklist", "Move Speed", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableInstantHealth = config.get("Alchemy Potion Blacklist", "Instant Health", false).getBoolean(false);
	    AlchemicalWizardry.potionDisablePoison = config.get("Alchemy Potion Blacklist", "Poison", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableBlindness = config.get("Alchemy Potion Blacklist", "Blindness", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableWeakness = config.get("Alchemy Potion Blacklist", "Weakness", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableStrength = config.get("Alchemy Potion Blacklist", "Strength", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableJumpBoost = config.get("Alchemy Potion Blacklist", "Jump Boost", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableSlowness = config.get("Alchemy Potion Blacklist", "Slowness", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableMining = config.get("Alchemy Potion Blacklist", "Mining Speed", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableDrowning = config.get("Alchemy Potion Blacklist", "Drowning", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableInvisibility = config.get("Alchemy Potion Blacklist", "Invisibility", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableResistance = config.get("Alchemy Potion Blacklist", "Resistance", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableSaturation = config.get("Alchemy Potion Blacklist", "Saturation", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableHealthBoost = config.get("Alchemy Potion Blacklist", "Health Boost", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableAbsorption = config.get("Alchemy Potion Blacklist", "Absorption", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableBoost = config.get("Alchemy Potion Blacklist", "Boost", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableFlight = config.get("Alchemy Potion Blacklist", "Flight", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableReciprocation = config.get("Alchemy Potion Blacklist", "Reciprocation", false).getBoolean(false);
	    AlchemicalWizardry.potionDisablePlanarBinding = config.get("Alchemy Potion Blacklist", "Planar Binding", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableSoulFray = config.get("Alchemy Potion Blacklist", "Soul Fray", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableSoulHarden = config.get("Alchemy Potion Blacklist", "Soul Harden", false).getBoolean(false);
	    AlchemicalWizardry.potionDisableDeafness = config.get("Alchemy Potion Blacklist", "Deafness", false).getBoolean(false);

        teleposerBlacklist = config.get("Teleposer Blacklist", "Blacklist", blacklist, "Stops specified blocks from being teleposed. Put entries on new lines. Valid syntax is: \nmodid:blockname:meta").getStringList();

		String tempDemonConfigs = "Demon Configs";
		TEDemonPortal.buildingGridDelay = config.get(tempDemonConfigs, "Building Grid Delay", 25).getInt();
		TEDemonPortal.roadGridDelay = config.get(tempDemonConfigs, "Road Grid Delay", 10).getInt();
		TEDemonPortal.demonHoardDelay = config.get(tempDemonConfigs, "Demon Hoard Delay", 40).getInt();
		TEDemonPortal.demonRoadChance = (float)(config.get(tempDemonConfigs, "Demon Road Chance", 0.3f).getDouble());
		TEDemonPortal.demonHouseChance = (float)(config.get(tempDemonConfigs, "Demon House Chance", 0.6f).getDouble());
		TEDemonPortal.demonPortalChance = (float)(config.get(tempDemonConfigs, "Demon Portal Chance", 0.5f).getDouble());
		TEDemonPortal.demonHoardChance = (float)(config.get(tempDemonConfigs, "Demon Hoard Chance", 0.8f).getDouble());
		TEDemonPortal.portalTickRate = (float)(config.get(tempDemonConfigs, "Portal Tick Rate", 0.1f).getDouble());
		
		DemonVillagePath.canGoDown = config.get(tempDemonConfigs, "canRoadGoDown", true).getBoolean();
		DemonVillagePath.tunnelIfObstructed = config.get(tempDemonConfigs, "tunnelIfObstructed", false).getBoolean();
		DemonVillagePath.createBridgeInAirIfObstructed = config.get(tempDemonConfigs, "createBridgeInAirIfObstructed", false).getBoolean();

		TEDemonPortal.limit = config.get(tempDemonConfigs, "demonGridSpaceLimit", 100).getInt();
		TEDemonPortal.demonLimit = config.get(tempDemonConfigs, "demonHoardLimit", 100).getInt();
		
		AlchemicalWizardry.isDemonRitualCreativeOnly = config.get(tempDemonConfigs, "IsDemonRitualCreativeOnly", false).getBoolean();
		
		BoundArmour.tryComplexRendering = config.get("WimpySettings", "UseFancyBoundArmour", true).getBoolean(true);
		
//		ItemIncense.itemDuration = config.get("TestIncenseSettings", "ItemDuration", 100).getInt();
//		ItemIncense.minValue = config.get("TestIncenseSettings", "MinValue", 0).getInt();
//		ItemIncense.maxValue = config.get("TestIncenseSettings", "MaxValue", 100).getInt();
//		PlayerSacrificeHandler.scalingOfSacrifice = (float) config.get("TestIncenseSettings", "ScalingFactor", 0.0025f).getDouble();
//		PlayerSacrificeHandler.soulFrayDuration = config.get("TestIncenseSettings", "SoulFrayDuration", 400).getInt();


		Side side = FMLCommonHandler.instance().getSide();
		if (side == Side.CLIENT)
		{
			RenderHelper.xOffset = config.get("ClientSettings", "AlchemyHUDxOffset", 50).getInt();
			RenderHelper.yOffset = config.get("ClientSettings", "AlchemyHUDyOffset", 2).getInt();
			RenderHelper.lpBarX = config.get("ClientSettings", "LPHUDxOffset", 12).getInt();
			RenderHelper.lpBarY = config.get("ClientSettings", "LPHUDyOffset", 75).getInt();
			AlchemicalWizardry.displayRitualAnimation = config.get("ClientSettings", "Display Ritual Animation", true).getBoolean(true);
		}

		config.save();
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
		if (AlchemicalWizardry.ritualDisabledConvocation)
		{
			Rituals.ritualMap.remove("AW031Convocation");
			Rituals.keyList.remove("AW031Convocation");
		}
		if (AlchemicalWizardry.ritualDisabledSymmetry)
		{
			Rituals.ritualMap.remove("AW032Symmetry");
			Rituals.keyList.remove("AW032Symmetry");
		}
		if (AlchemicalWizardry.ritualDisabledStalling)
		{
			Rituals.ritualMap.remove("AW033Stalling");
			Rituals.keyList.remove("AW033Stalling");
		}
		if (AlchemicalWizardry.ritualDisabledCrafting)
		{
			Rituals.ritualMap.remove("AW034Crafting");
			Rituals.keyList.remove("AW034Crafting");
		}
		if (AlchemicalWizardry.ritualDisabledPhantomHands)
		{
			Rituals.ritualMap.remove("AW035PhantomHands");
			Rituals.keyList.remove("AW035PhantomHands");
		}
		if (AlchemicalWizardry.ritualDisabledSphereIsland)
		{
			Rituals.ritualMap.remove("AW036SphereIsland");
			Rituals.keyList.remove("AW036SphereIsland");
		}
	}
}
