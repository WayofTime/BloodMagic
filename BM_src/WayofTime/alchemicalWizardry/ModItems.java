package WayofTime.alchemicalWizardry;

import WayofTime.alchemicalWizardry.common.items.*;
import WayofTime.alchemicalWizardry.common.items.potion.*;
import WayofTime.alchemicalWizardry.common.items.sigil.*;
import net.minecraft.item.Item;

/**
 * Created with IntelliJ IDEA.
 * User: Pokefenn
 * Date: 17/01/14
 * Time: 19:48
 */
public class ModItems
{


    public static Item orbOfTesting;
    public static Item weakBloodOrb;
    public static Item apprenticeBloodOrb;
    public static Item magicianBloodOrb;
    public static Item energyBlaster;
    public static Item energySword;
    public static Item lavaCrystal;
    public static Item waterSigil;
    public static Item lavaSigil;
    public static Item voidSigil;
    //public static Item glassShard = new GlassShard(17009).setUnlocalizedName("glassShard");
    //public static Item bloodiedShard = new BloodiedShard(17010).setUnlocalizedName("bloodiedShard");
    public static Item blankSlate;
    public static Item reinforcedSlate;
    public static Item sacrificialDagger;
    public static Item daggerOfSacrifice;
    public static Item airSigil;
    public static Item sigilOfTheFastMiner;
    public static Item sigilOfElementalAffinity;
    public static Item sigilOfHaste;
    public static Item sigilOfHolding;
    public static Item divinationSigil;
    //    public static Item elementalInkWater;
//    public static Item elementalInkFire;
//    public static Item elementalInkEarth;
//    public static Item elementalInkAir;
    public static Item waterScribeTool;
    public static Item fireScribeTool;
    public static Item earthScribeTool;
    public static Item airScribeTool;
    public static Item activationCrystal;
    public static Item boundPickaxe;
    public static Item boundAxe;
    public static Item boundShovel;
    public static Item boundHelmet;
    public static Item boundPlate;
    public static Item boundLeggings;
    public static Item boundBoots;
    public static Item weakBloodShard;
    public static Item growthSigil;
    //public static Item fireSpell;
    public static Item blankSpell;
    public static Item masterBloodOrb;
    public static Item alchemyFlask;
    public static Item standardBindingAgent;
    public static Item mundanePowerCatalyst;
    public static Item averagePowerCatalyst;
    public static Item greaterPowerCatalyst;
    public static Item mundaneLengtheningCatalyst;
    public static Item averageLengtheningCatalyst;
    public static Item greaterLengtheningCatalyst;
    public static Item incendium;
    public static Item magicales;
    public static Item sanctus;
    public static Item aether;
    public static Item simpleCatalyst;
    public static Item crepitous;
    public static Item crystallos;
    public static Item terrae;
    public static Item aquasalus;
    public static Item tennebrae;
    public static Item demonBloodShard;
    public static Item archmageBloodOrb;
    public static Item sigilOfWind;
    public static Item telepositionFocus;
    public static Item enhancedTelepositionFocus;
    public static Item reinforcedTelepositionFocus;
    public static Item demonicTelepositionFocus;
    public static Item imbuedSlate;
    public static Item demonicSlate;
    public static Item duskScribeTool;
    public static Item sigilOfTheBridge;
    public static Item armourInhibitor;
    public static Item creativeFiller;
    public static Item demonPlacer;

    public static Item weakFillingAgent;
    public static Item standardFillingAgent;
    public static Item enhancedFillingAgent;
    public static Item weakBindingAgent;
    public static Item itemRitualDiviner;
    public static Item sanguineHelmet;
    public static Item focusBloodBlast;
    public static Item focusGravityWell;
    public static Item sigilOfMagnetism;
    public static Item itemKeyOfDiablo;
    public static Item energyBazooka;
    public static Item itemBloodLightSigil;
    public static Item itemComplexSpellCrystal;

    public static void init()
    {
        weakBloodOrb = new EnergyBattery(AlchemicalWizardry.weakBloodOrbItemID, 5000).setUnlocalizedName("weakBloodOrb");
        apprenticeBloodOrb = new ApprenticeBloodOrb(AlchemicalWizardry.apprenticeBloodOrbItemID, 25000).setUnlocalizedName("apprenticeBloodOrb");
        magicianBloodOrb = new MagicianBloodOrb(AlchemicalWizardry.magicianBloodOrbItemID, 150000).setUnlocalizedName("magicianBloodOrb");
        masterBloodOrb = new MasterBloodOrb(AlchemicalWizardry.masterBloodOrbItemID, 1000000).setUnlocalizedName("masterBloodOrb");
        archmageBloodOrb = new ArchmageBloodOrb(AlchemicalWizardry.archmageBloodOrbItemID, 10000000).setUnlocalizedName("archmageBloodOrb");
        energyBlaster = new EnergyBlast(AlchemicalWizardry.energyBlasterItemID).setUnlocalizedName("energyBlast");
        energySword = new EnergySword(AlchemicalWizardry.energySwordItemID).setUnlocalizedName("energySword");
        lavaCrystal = new LavaCrystal(AlchemicalWizardry.lavaCrystalItemID).setUnlocalizedName("lavaCrystal");
        waterSigil = new WaterSigil(AlchemicalWizardry.waterSigilItemID).setUnlocalizedName("waterSigil");
        lavaSigil = new LavaSigil(AlchemicalWizardry.lavaSigilItemID).setUnlocalizedName("lavaSigil");
        voidSigil = new VoidSigil(AlchemicalWizardry.voidSigilItemID).setUnlocalizedName("voidSigil");
        blankSlate = new AWBaseItems(AlchemicalWizardry.blankSlateItemID).setUnlocalizedName("blankSlate");
        reinforcedSlate = new AWBaseItems(AlchemicalWizardry.reinforcedSlateItemID).setUnlocalizedName("reinforcedSlate");
        sacrificialDagger = new SacrificialDagger(AlchemicalWizardry.sacrificialDaggerItemID).setUnlocalizedName("sacrificialDagger");
        daggerOfSacrifice = new DaggerOfSacrifice(AlchemicalWizardry.daggerOfSacrificeItemID).setUnlocalizedName("daggerOfSacrifice");
        airSigil = new AirSigil(AlchemicalWizardry.airSigilItemID).setUnlocalizedName("airSigil");
        sigilOfTheFastMiner = new SigilOfTheFastMiner(AlchemicalWizardry.sigilOfTheFastMinerItemID).setUnlocalizedName("sigilOfTheFastMiner");
        sigilOfElementalAffinity = new SigilOfElementalAffinity(AlchemicalWizardry.sigilOfElementalAffinityItemID).setUnlocalizedName("sigilOfElementalAffinity");
        sigilOfHaste = new SigilOfHaste(AlchemicalWizardry.sigilOfHasteItemID).setUnlocalizedName("sigilOfHaste");
        sigilOfHolding = new SigilOfHolding(AlchemicalWizardry.sigilOfHoldingItemID).setUnlocalizedName("sigilOfHolding");
        divinationSigil = new DivinationSigil(AlchemicalWizardry.divinationSigilItemID).setUnlocalizedName("divinationSigil");
        waterScribeTool = new WaterScribeTool(AlchemicalWizardry.waterScribeToolItemID).setUnlocalizedName("waterScribeTool");
        fireScribeTool = new FireScribeTool(AlchemicalWizardry.fireScribeToolItemID).setUnlocalizedName("fireScribeTool");
        earthScribeTool = new EarthScribeTool(AlchemicalWizardry.earthScribeToolItemID).setUnlocalizedName("earthScribeTool");
        airScribeTool = new AirScribeTool(AlchemicalWizardry.airScribeToolItemID).setUnlocalizedName("airScribeTool");
        activationCrystal = new ActivationCrystal(AlchemicalWizardry.weakActivationCrystalItemID);
        boundPickaxe = new BoundPickaxe(AlchemicalWizardry.boundPickaxeItemID).setUnlocalizedName("boundPickaxe");
        boundAxe = new BoundAxe(AlchemicalWizardry.boundAxeItemID).setUnlocalizedName("boundAxe");
        boundShovel = new BoundShovel(AlchemicalWizardry.boundShovelItemID).setUnlocalizedName("boundShovel");
        boundHelmet = new BoundArmour(AlchemicalWizardry.boundHelmetItemID, 0).setUnlocalizedName("boundHelmet");
        boundPlate = new BoundArmour(AlchemicalWizardry.boundPlateItemID, 1).setUnlocalizedName("boundPlate");
        boundLeggings = new BoundArmour(AlchemicalWizardry.boundLeggingsItemID, 2).setUnlocalizedName("boundLeggings");
        boundBoots = new BoundArmour(AlchemicalWizardry.boundBootsItemID, 3).setUnlocalizedName("boundBoots");
        weakBloodShard = new BloodShard(AlchemicalWizardry.weakBloodShardItemID).setUnlocalizedName("weakBloodShard");
        growthSigil = new SigilOfGrowth(AlchemicalWizardry.growthSigilItemID).setUnlocalizedName("growthSigil");
        blankSpell = new BlankSpell(AlchemicalWizardry.blankSpellItemID).setUnlocalizedName("blankSpell");
        alchemyFlask = new AlchemyFlask(AlchemicalWizardry.alchemyFlaskItemID).setUnlocalizedName("alchemyFlask");
        standardBindingAgent = new StandardBindingAgent(AlchemicalWizardry.standardBindingAgentItemID).setUnlocalizedName("standardBindingAgent");
        mundanePowerCatalyst = new MundanePowerCatalyst(AlchemicalWizardry.mundanePowerCatalystItemID).setUnlocalizedName("mundanePowerCatalyst");
        averagePowerCatalyst = new AveragePowerCatalyst(AlchemicalWizardry.averagePowerCatalystItemID).setUnlocalizedName("averagePowerCatalyst");
        greaterPowerCatalyst = new GreaterPowerCatalyst(AlchemicalWizardry.greaterPowerCatalystItemID).setUnlocalizedName("greaterPowerCatalyst");
        mundaneLengtheningCatalyst = new MundaneLengtheningCatalyst(AlchemicalWizardry.mundaneLengtheningCatalystItemID).setUnlocalizedName("mundaneLengtheningCatalyst");
        averageLengtheningCatalyst = new AverageLengtheningCatalyst(AlchemicalWizardry.averageLengtheningCatalystItemID).setUnlocalizedName("averageLengtheningCatalyst");
        greaterLengtheningCatalyst = new GreaterLengtheningCatalyst(AlchemicalWizardry.greaterLengtheningCatalystItemID).setUnlocalizedName("greaterLengtheningCatalyst");
        incendium = new AlchemyReagent(AlchemicalWizardry.incendiumItemID).setUnlocalizedName("incendium");
        magicales = new AlchemyReagent(AlchemicalWizardry.magicalesItemID).setUnlocalizedName("magicales");
        sanctus = new AlchemyReagent(AlchemicalWizardry.sanctusItemID).setUnlocalizedName("sanctus");
        aether = new AlchemyReagent(AlchemicalWizardry.aetherItemID).setUnlocalizedName("aether");
        simpleCatalyst = new AlchemyReagent(AlchemicalWizardry.simpleCatalystItemID).setUnlocalizedName("simpleCatalyst");
        crepitous = new AlchemyReagent(AlchemicalWizardry.crepitousItemID).setUnlocalizedName("crepitous");
        crystallos = new AlchemyReagent(AlchemicalWizardry.crystallosItemID).setUnlocalizedName("crystallos");
        terrae = new AlchemyReagent(AlchemicalWizardry.terraeItemID).setUnlocalizedName("terrae");
        aquasalus = new AlchemyReagent(AlchemicalWizardry.aquasalusItemID).setUnlocalizedName("aquasalus");
        tennebrae = new AlchemyReagent(AlchemicalWizardry.tennebraeItemID).setUnlocalizedName("tennebrae");
        demonBloodShard = new BloodShard(AlchemicalWizardry.demonBloodShardItemID).setUnlocalizedName("demonBloodShard");
        sigilOfWind = new SigilOfWind(AlchemicalWizardry.sigilOfWindItemID).setUnlocalizedName("sigilOfWind");
        telepositionFocus = new TelepositionFocus(AlchemicalWizardry.telepositionFocusItemID, 1).setUnlocalizedName("telepositionFocus");
        enhancedTelepositionFocus = new EnhancedTelepositionFocus(AlchemicalWizardry.enhancedTelepositionFocusItemID).setUnlocalizedName("enhancedTelepositionFocus");
        reinforcedTelepositionFocus = new ReinforcedTelepositionFocus(AlchemicalWizardry.reinforcedTelepositionFocusItemID).setUnlocalizedName("reinforcedTelepositionFocus");
        demonicTelepositionFocus = new DemonicTelepositionFocus(AlchemicalWizardry.demonicTelepositionFocusItemID).setUnlocalizedName("demonicTelepositionFocus");
        imbuedSlate = new AWBaseItems(AlchemicalWizardry.imbuedSlateItemID).setUnlocalizedName("imbuedSlate");
        demonicSlate = new AWBaseItems(AlchemicalWizardry.demonicSlateItemID).setUnlocalizedName("demonicSlate");
        duskScribeTool = new DuskScribeTool(AlchemicalWizardry.duskScribeToolItemID).setUnlocalizedName("duskScribeTool");
        sigilOfTheBridge = new SigilOfTheBridge(AlchemicalWizardry.sigilOfTheBridgeItemID).setUnlocalizedName("sigilOfTheBridge");
        armourInhibitor = new ArmourInhibitor(AlchemicalWizardry.armourInhibitorItemID).setUnlocalizedName("armourInhibitor");
        creativeFiller = new CheatyItem(AlchemicalWizardry.creativeFillerItemID).setUnlocalizedName("cheatyItem");
        demonPlacer = new DemonPlacer(AlchemicalWizardry.demonPlacerItemID).setUnlocalizedName("demonPlacer");
        weakFillingAgent = new WeakFillingAgent(AlchemicalWizardry.weakFillingAgentItemID).setUnlocalizedName("weakFillingAgent");
        standardFillingAgent = new StandardFillingAgent(AlchemicalWizardry.standardFillingAgentItemID).setUnlocalizedName("standardFillingAgent");
        enhancedFillingAgent = new EnhancedFillingAgent(AlchemicalWizardry.enhancedFillingAgentItemID).setUnlocalizedName("enhancedFillingAgent");
        weakBindingAgent = new WeakBindingAgent(AlchemicalWizardry.weakBindingAgentItemID).setUnlocalizedName("weakBindingAgent");
        itemRitualDiviner = new ItemRitualDiviner(AlchemicalWizardry.itemRitualDivinerItemID).setUnlocalizedName("ritualDiviner");
        sigilOfMagnetism = new SigilOfMagnetism(AlchemicalWizardry.sigilOfMagnetismItemID).setUnlocalizedName("sigilOfMagnetism");
        itemKeyOfDiablo = new ItemDiabloKey(AlchemicalWizardry.itemKeyOfDiabloItemID).setUnlocalizedName("itemDiabloKey");
        energyBazooka = new EnergyBazooka(AlchemicalWizardry.energyBazookaItemID).setUnlocalizedName("energyBazooka");
        itemBloodLightSigil = new ItemBloodLightSigil(AlchemicalWizardry.itemBloodLightSigilItemID).setUnlocalizedName("bloodLightSigil");
        itemComplexSpellCrystal = new ItemComplexSpellCrystal(AlchemicalWizardry.itemComplexSpellCrystalItemID).setUnlocalizedName("itemComplexSpellCrystal");
    }
}
