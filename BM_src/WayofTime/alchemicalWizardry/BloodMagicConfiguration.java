package WayofTime.alchemicalWizardry;

import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorParadigm;
import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.Configuration;

import java.io.File;
import java.util.logging.Level;

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

            AlchemicalWizardry.testingBlockBlockID = config.getBlock("TestingBlock", 1400).getInt();
            AlchemicalWizardry.lifeEssenceFlowingBlockID = config.getBlock("LifeEssenceFlowing", 1401).getInt();
            AlchemicalWizardry.lifeEssenceStillBlockID = config.getBlock("LifeEssenceStill", 1402).getInt();
            AlchemicalWizardry.blockAltarBlockID = config.getBlock("BloodAltar", 1403).getInt();
            AlchemicalWizardry.bloodRuneBlockID = config.getBlock("BloodRune", 1404).getInt();
            AlchemicalWizardry.speedRuneBlockID = config.getBlock("SpeedRune", 1405).getInt();
            AlchemicalWizardry.efficiencyRuneBlockID = config.getBlock("EfficiencyRune", 1406).getInt();
            AlchemicalWizardry.lifeEssenceFluidID = config.getBlock("LifeEssenceFluid", 1407).getInt();
            AlchemicalWizardry.runeOfSacrificeBlockID = config.getBlock("RuneOfSacrifice", 1408).getInt();
            AlchemicalWizardry.runeOfSelfSacrificeBlockID = config.getBlock("RuneOfSelfSacrifice", 1409).getInt();
            AlchemicalWizardry.ritualStoneBlockID = config.getBlock("RitualStone", 1410).getInt();
            AlchemicalWizardry.blockMasterStoneBlockID = config.getBlock("MasterStone", 1411).getInt();
            AlchemicalWizardry.imperfectRitualStoneBlockID = config.getBlock("ImperfectRitualStone", 1412).getInt();
            AlchemicalWizardry.emptySocketBlockID = config.getBlock("EmptySocket", 1413).getInt();
            AlchemicalWizardry.bloodSocketBlockID = config.getBlock("BloodSocket", 1414).getInt();
            AlchemicalWizardry.armourForgeBlockID = config.getBlock("ArmourForge", 1415).getInt();
            AlchemicalWizardry.largeBloodStoneBrickBlockID = config.getBlock("LargeBloodStoneBrick", 1416).getInt();
            AlchemicalWizardry.bloodStoneBrickBlockID = config.getBlock("BloodStoneBrick", 1417).getInt();
            AlchemicalWizardry.blockWritingTableBlockID = config.getBlock("BlockWritingTable", 1418).getInt();
            AlchemicalWizardry.blockHomHeartBlockID = config.getBlock("BlockHomHeart", 1419).getInt();
            AlchemicalWizardry.blockPedestalBlockID = config.getBlock("BlockPedestal", 1420).getInt();
            AlchemicalWizardry.blockPlinthBlockID = config.getBlock("BlockPlinth", 1421).getInt();
            AlchemicalWizardry.blockTeleposerBlockID = config.getBlock("BlockTeleposer", 1422).getInt();
            AlchemicalWizardry.spectralBlockBlockID = config.getBlock("SpectralBlock", 1423).getInt();
            AlchemicalWizardry.blockBloodLightBlockID = config.getBlock("BloodLight", 1424).getInt();
            AlchemicalWizardry.blockConduitBlockID = config.getBlock("BlockConduit", 1425).getInt();
            AlchemicalWizardry.blockSpellEffectBlockID = config.getBlock("BlockSpellEffect", 1426).getInt();
            AlchemicalWizardry.blockSpellParadigmBlockID = config.getBlock("BlockSpellParadigm", 1427).getInt();
            AlchemicalWizardry.blockSpellModifierBlockID = config.getBlock("BlockSpellModifier", 1428).getInt();
            AlchemicalWizardry.blockSpellEnhancementBlockID = config.getBlock("BlockSpellEnhancement", 1429).getInt();
            //Items
            AlchemicalWizardry.weakBloodOrbItemID = config.getItem("WeakBloodOrb", 17000).getInt();
            AlchemicalWizardry.energyBlasterItemID = config.getItem("EnergyBlaster", 17001).getInt();
            AlchemicalWizardry.energySwordItemID = config.getItem("EnergySword", 17002).getInt();
            AlchemicalWizardry.lavaCrystalItemID = config.getItem("LavaCrystal", 17003).getInt();
            AlchemicalWizardry.waterSigilItemID = config.getItem("WaterSigil", 17004).getInt();
            AlchemicalWizardry.lavaSigilItemID = config.getItem("LavaSigil", 17005).getInt();
            AlchemicalWizardry.voidSigilItemID = config.getItem("VoidSigil", 17006).getInt();
            AlchemicalWizardry.blankSlateItemID = config.getItem("BlankSlate", 17007).getInt();
            AlchemicalWizardry.reinforcedSlateItemID = config.getItem("ReinforcedSlate", 17008).getInt();
            AlchemicalWizardry.sacrificialDaggerItemID = config.getItem("SacrificialDagger", 17009).getInt();
            AlchemicalWizardry.bucketLifeItemID = config.getItem("BucketLife", 17010).getInt();
            AlchemicalWizardry.apprenticeBloodOrbItemID = config.getItem("ApprenticeBloodOrb", 17011).getInt();
            AlchemicalWizardry.daggerOfSacrificeItemID = config.getItem("DaggerOfSacrifice", 17012).getInt();
            AlchemicalWizardry.airSigilItemID = config.getItem("AirSigil", 17013).getInt();
            AlchemicalWizardry.sigilOfTheFastMinerItemID = config.getItem("SigilOfTheFastMiner", 17014).getInt();
//        elementalInkWaterItemID = config.getItem("ElementalInkWater", 17015).getInt();
//        elementalInkFireItemID = config.getItem("ElementalInkFire", 17016).getInt();
//        elementalInkEarthItemID = config.getItem("ElementalInkEarth", 17017).getInt();
//        elementalInkAirItemID = config.getItem("ElementalInkAir", 17018).getInt();
            AlchemicalWizardry.duskScribeToolItemID = config.getItem("DuskScribeTool", 17015).getInt();
            AlchemicalWizardry.demonPlacerItemID = config.getItem("DemonPlacer", 17016).getInt();
            AlchemicalWizardry.itemBloodRuneBlockItemID = config.getItem("ItemBloodRuneBlock", 17017).getInt();
            AlchemicalWizardry.armourInhibitorItemID = config.getItem("ArmourInhibitor", 17018).getInt();
            AlchemicalWizardry.divinationSigilItemID = config.getItem("DivinationSigin", 17019).getInt();
            AlchemicalWizardry.waterScribeToolItemID = config.getItem("WaterScribeTool", 17020).getInt();
            AlchemicalWizardry.fireScribeToolItemID = config.getItem("FireScribeTool", 17021).getInt();
            AlchemicalWizardry.earthScribeToolItemID = config.getItem("EarthScribeTool", 17022).getInt();
            AlchemicalWizardry.airScribeToolItemID = config.getItem("AirScribeTool", 17023).getInt();
            AlchemicalWizardry.weakActivationCrystalItemID = config.getItem("WeakActivationCrystal", 17024).getInt();
            AlchemicalWizardry.magicianBloodOrbItemID = config.getItem("MagicianBloodOrb", 17025).getInt();
            AlchemicalWizardry.sigilOfElementalAffinityItemID = config.getItem("SigilOfElementalAffinity", 17026).getInt();
            AlchemicalWizardry.sigilOfHasteItemID = config.getItem("SigilOfHaste", 17027).getInt();
            AlchemicalWizardry.sigilOfHoldingItemID = config.getItem("SigilOfHolding", 17028).getInt();
            AlchemicalWizardry.boundPickaxeItemID = config.getItem("BoundPickaxe", 17029).getInt();
            AlchemicalWizardry.boundAxeItemID = config.getItem("BoundAxe", 17030).getInt();
            AlchemicalWizardry.boundShovelItemID = config.getItem("BoundShovel", 17031).getInt();
            AlchemicalWizardry.boundHelmetItemID = config.getItem("BoundHelmet", 17032).getInt();
            AlchemicalWizardry.boundPlateItemID = config.getItem("BoundPlate", 17033).getInt();
            AlchemicalWizardry.boundLeggingsItemID = config.getItem("BoundLeggings", 17034).getInt();
            AlchemicalWizardry.boundBootsItemID = config.getItem("BoundBoots", 17035).getInt();
            AlchemicalWizardry.weakBloodShardItemID = config.getItem("WeakBloodShard", 17036).getInt();
            AlchemicalWizardry.growthSigilItemID = config.getItem("SigilOfGrowth", 17037).getInt();
            AlchemicalWizardry.masterBloodOrbItemID = config.getItem("MasterBloodOrb", 17038).getInt();
            AlchemicalWizardry.blankSpellItemID = config.getItem("BlankSpell", 17039).getInt();
            AlchemicalWizardry.alchemyFlaskItemID = config.getItem("AlchemyFlask", 17040).getInt();
            AlchemicalWizardry.standardBindingAgentItemID = config.getItem("StandardBindingAgent", 17041).getInt();
            AlchemicalWizardry.mundanePowerCatalystItemID = config.getItem("MundanePowerCatalyst", 17042).getInt();
            AlchemicalWizardry.averagePowerCatalystItemID = config.getItem("AveragePowerCatalyst", 17043).getInt();
            AlchemicalWizardry.greaterPowerCatalystItemID = config.getItem("GreaterPowerCatalyst", 17044).getInt();
            AlchemicalWizardry.mundaneLengtheningCatalystItemID = config.getItem("MundaneLengtheningCatalyst", 17045).getInt();
            AlchemicalWizardry.averageLengtheningCatalystItemID = config.getItem("AverageLengtheningCatalyst", 17046).getInt();
            AlchemicalWizardry.greaterLengtheningCatalystItemID = config.getItem("GreaterLengtheningCatalyst", 17047).getInt();
            AlchemicalWizardry.demonBloodShardItemID = config.getItem("DemonBloodShard", 17048).getInt();
            AlchemicalWizardry.archmageBloodOrbItemID = config.getItem("ArchmageBloodOrb", 17049).getInt();
            AlchemicalWizardry.sigilOfWindItemID = config.getItem("SigilOfWind", 17050).getInt();
            AlchemicalWizardry.telepositionFocusItemID = config.getItem("TelepositionFocusItemID", 17051).getInt();
            AlchemicalWizardry.enhancedTelepositionFocusItemID = config.getItem("EnhancedTelepositionFocus", 17052).getInt();
            AlchemicalWizardry.reinforcedTelepositionFocusItemID = config.getItem("ReinforcedTelepositionFocus", 17053).getInt();
            AlchemicalWizardry.demonicTelepositionFocusItemID = config.getItem("DemonicTelepositionFocus", 17054).getInt();
            AlchemicalWizardry.imbuedSlateItemID = config.getItem("ImbuedSlate", 17055).getInt();
            AlchemicalWizardry.demonicSlateItemID = config.getItem("DemonicSlate", 17056).getInt();
            AlchemicalWizardry.sigilOfTheBridgeItemID = config.getItem("SigilOfTheBridge", 17057).getInt();
            AlchemicalWizardry.creativeFillerItemID = config.getItem("CreativeFiller", 17058).getInt();
            AlchemicalWizardry.itemRitualDivinerItemID = config.getItem("ItemRitualDiviner", 17059).getInt();
            AlchemicalWizardry.itemKeyOfDiabloItemID = config.getItem("ItemKeyOfDiablo", 17081).getInt();
            AlchemicalWizardry.energyBazookaItemID = config.getItem("EnergyBazooka", 17082).getInt();
            AlchemicalWizardry.itemBloodLightSigilItemID = config.getItem("BloodLightSigil", 17083).getInt();
            AlchemicalWizardry.simpleCatalystItemID = config.getItem("SimpleCatalyst", 17060).getInt();
            AlchemicalWizardry.incendiumItemID = config.getItem("Incendium", 17061).getInt();
            AlchemicalWizardry.magicalesItemID = config.getItem("Magicales", 17062).getInt();
            AlchemicalWizardry.sanctusItemID = config.getItem("Sanctus", 17063).getInt();
            AlchemicalWizardry.aetherItemID = config.getItem("Aether", 17064).getInt();
            AlchemicalWizardry.crepitousItemID = config.getItem("Crepitous", 17065).getInt();
            AlchemicalWizardry.crystallosItemID = config.getItem("Crystallos", 17066).getInt();
            AlchemicalWizardry.terraeItemID = config.getItem("Terrae", 17067).getInt();
            AlchemicalWizardry.aquasalusItemID = config.getItem("Aquasalus", 17068).getInt();
            AlchemicalWizardry.tennebraeItemID = config.getItem("Tennebrae", 17069).getInt();
            AlchemicalWizardry.weakBindingAgentItemID = config.getItem("WeakBindingAgent", 17070).getInt();
            AlchemicalWizardry.weakFillingAgentItemID = config.getItem("WeakFillingAgent", 17072).getInt();
            AlchemicalWizardry.standardFillingAgentItemID = config.getItem("StandardFillingAgent", 17073).getInt();
            AlchemicalWizardry.enhancedFillingAgentItemID = config.getItem("EnhancedFillingAgent", 17074).getInt();
            AlchemicalWizardry.sanguineHelmetItemID = config.getItem("SanguineHelmet", 17075).getInt();
            AlchemicalWizardry.focusBloodBlastItemID = config.getItem("FocusBloodBlast", 17076).getInt();
            AlchemicalWizardry.focusGravityWellItemID = config.getItem("FocusGravityWell", 17077).getInt();
            AlchemicalWizardry.sigilOfMagnetismItemID = config.getItem("SigilOfMagnetism", 17080).getInt();
            AlchemicalWizardry.itemComplexSpellCrystalItemID = config.getItem("ComplexSpellCrystal",17081).getInt();
            AlchemicalWizardry.itemBloodFrameItemID = config.getItem("BloodFrame", 17082).getInt();
            
        } catch (Exception e)
        {

            FMLLog.log(Level.SEVERE, e, "Blood Magic" + " has had a problem loading its configuration, go ask on the forums :p");

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
