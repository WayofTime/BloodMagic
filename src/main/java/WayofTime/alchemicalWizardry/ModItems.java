package WayofTime.alchemicalWizardry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import WayofTime.alchemicalWizardry.api.items.ItemSpellMultiTool;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.book.ItemBMBook;
import WayofTime.alchemicalWizardry.common.items.AWBaseItems;
import WayofTime.alchemicalWizardry.common.items.ActivationCrystal;
import WayofTime.alchemicalWizardry.common.items.AirScribeTool;
import WayofTime.alchemicalWizardry.common.items.ApprenticeBloodOrb;
import WayofTime.alchemicalWizardry.common.items.ArchmageBloodOrb;
import WayofTime.alchemicalWizardry.common.items.ArmourInhibitor;
import WayofTime.alchemicalWizardry.common.items.BlankSpell;
import WayofTime.alchemicalWizardry.common.items.BloodShard;
import WayofTime.alchemicalWizardry.common.items.BoundAxe;
import WayofTime.alchemicalWizardry.common.items.BoundPickaxe;
import WayofTime.alchemicalWizardry.common.items.BoundShovel;
import WayofTime.alchemicalWizardry.common.items.CheatyItem;
import WayofTime.alchemicalWizardry.common.items.CreativeDagger;
import WayofTime.alchemicalWizardry.common.items.DaggerOfSacrifice;
import WayofTime.alchemicalWizardry.common.items.DawnScribeTool;
import WayofTime.alchemicalWizardry.common.items.DemonPlacer;
import WayofTime.alchemicalWizardry.common.items.DemonicTelepositionFocus;
import WayofTime.alchemicalWizardry.common.items.DuskScribeTool;
import WayofTime.alchemicalWizardry.common.items.EarthScribeTool;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;
import WayofTime.alchemicalWizardry.common.items.EnergyBazooka;
import WayofTime.alchemicalWizardry.common.items.EnergyBlast;
import WayofTime.alchemicalWizardry.common.items.EnergySword;
import WayofTime.alchemicalWizardry.common.items.EnhancedTelepositionFocus;
import WayofTime.alchemicalWizardry.common.items.FireScribeTool;
import WayofTime.alchemicalWizardry.common.items.ItemAlchemyBase;
import WayofTime.alchemicalWizardry.common.items.ItemBloodLetterPack;
import WayofTime.alchemicalWizardry.common.items.ItemComplexSpellCrystal;
import WayofTime.alchemicalWizardry.common.items.ItemComponents;
import WayofTime.alchemicalWizardry.common.items.ItemDiabloKey;
import WayofTime.alchemicalWizardry.common.items.ItemIncense;
import WayofTime.alchemicalWizardry.common.items.ItemMailOrderCatalogue;
import WayofTime.alchemicalWizardry.common.items.ItemRitualDiviner;
import WayofTime.alchemicalWizardry.common.items.LavaCrystal;
import WayofTime.alchemicalWizardry.common.items.LifeBucket;
import WayofTime.alchemicalWizardry.common.items.MagicianBloodOrb;
import WayofTime.alchemicalWizardry.common.items.MasterBloodOrb;
import WayofTime.alchemicalWizardry.common.items.ReinforcedTelepositionFocus;
import WayofTime.alchemicalWizardry.common.items.SacrificialDagger;
import WayofTime.alchemicalWizardry.common.items.TelepositionFocus;
import WayofTime.alchemicalWizardry.common.items.TranscendentBloodOrb;
import WayofTime.alchemicalWizardry.common.items.WaterScribeTool;
import WayofTime.alchemicalWizardry.common.items.armour.BoundArmour;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmourEarth;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmourFire;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmourWater;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmourWind;
import WayofTime.alchemicalWizardry.common.items.energy.ItemAttunedCrystal;
import WayofTime.alchemicalWizardry.common.items.energy.ItemDestinationClearer;
import WayofTime.alchemicalWizardry.common.items.energy.ItemTankSegmenter;
import WayofTime.alchemicalWizardry.common.items.potion.AlchemyFlask;
import WayofTime.alchemicalWizardry.common.items.potion.AlchemyReagent;
import WayofTime.alchemicalWizardry.common.items.potion.AverageLengtheningCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.AveragePowerCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.CombinationalCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.EnhancedFillingAgent;
import WayofTime.alchemicalWizardry.common.items.potion.GreaterLengtheningCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.GreaterPowerCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.MundaneLengtheningCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.MundanePowerCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.StandardBindingAgent;
import WayofTime.alchemicalWizardry.common.items.potion.StandardFillingAgent;
import WayofTime.alchemicalWizardry.common.items.potion.WeakBindingAgent;
import WayofTime.alchemicalWizardry.common.items.potion.WeakFillingAgent;
import WayofTime.alchemicalWizardry.common.items.routing.InputRoutingFocus;
import WayofTime.alchemicalWizardry.common.items.routing.OutputRoutingFocus;
import WayofTime.alchemicalWizardry.common.items.sigil.AirSigil;
import WayofTime.alchemicalWizardry.common.items.sigil.DivinationSigil;
import WayofTime.alchemicalWizardry.common.items.sigil.ItemBloodLightSigil;
import WayofTime.alchemicalWizardry.common.items.sigil.ItemFluidSigil;
import WayofTime.alchemicalWizardry.common.items.sigil.ItemHarvestSigil;
import WayofTime.alchemicalWizardry.common.items.sigil.ItemPackRatSigil;
import WayofTime.alchemicalWizardry.common.items.sigil.ItemSeerSigil;
import WayofTime.alchemicalWizardry.common.items.sigil.ItemSigilOfEnderSeverance;
import WayofTime.alchemicalWizardry.common.items.sigil.ItemSigilOfSupression;
import WayofTime.alchemicalWizardry.common.items.sigil.ItemSigilOfTheAssassin;
import WayofTime.alchemicalWizardry.common.items.sigil.LavaSigil;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfElementalAffinity;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfGrowth;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHaste;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHolding;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfMagnetism;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfTheBridge;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfTheFastMiner;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfWind;
import WayofTime.alchemicalWizardry.common.items.sigil.VoidSigil;
import WayofTime.alchemicalWizardry.common.items.sigil.WaterSigil;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created with IntelliJ IDEA.
 * User: Pokefenn
 * Date: 17/01/14
 * Time: 19:48
 */
public class ModItems
{
    public static Item weakBloodOrb;
    public static Item apprenticeBloodOrb;
    public static Item magicianBloodOrb;
    public static Item energyBlaster;
    public static Item energySword;
    public static Item lavaCrystal;
    public static Item waterSigil;
    public static Item lavaSigil;
    public static Item voidSigil;
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
    public static Item waterScribeTool;
    public static Item fireScribeTool;
    public static Item earthScribeTool;
    public static Item airScribeTool;
    public static Item dawnScribeTool;
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
    public static Item creativeDagger;

    public static Item baseItems;
    public static Item baseAlchemyItems;

    public static Item weakFillingAgent;
    public static Item standardFillingAgent;
    public static Item enhancedFillingAgent;
    public static Item weakBindingAgent;
    public static Item itemRitualDiviner;
    public static Item sanguineHelmet;
    public static Item sanguineRobe;
    public static Item sanguinePants;
    public static Item sanguineBoots;
    public static Item focusBloodBlast;
    public static Item focusGravityWell;
    public static Item sigilOfMagnetism;
    public static Item itemKeyOfDiablo;
    public static Item energyBazooka;
    public static Item itemBloodLightSigil;
    public static Item itemComplexSpellCrystal;
    public static Item itemBloodFrame;

    public static Item itemSigilOfEnderSeverance;
    public static Item itemSigilOfSupression;
    public static Item itemFluidSigil;
    public static Item itemSeerSigil;
    public static Item itemCombinationalCatalyst;

    public static Item customTool;

    public static Item itemAttunedCrystal;
    public static Item itemTankSegmenter;
    public static Item itemDestinationClearer;
    
    public static Item itemBloodMagicBook;
    public static Item itemHarvestSigil;
    public static Item itemCompressionSigil;

    public static Item bucketLife;
    
    public static Item itemBloodPack;
    
    public static Item transcendentBloodOrb;
    public static Item itemAssassinSigil;
    
    public static Item boundHelmetWater;
    public static Item boundPlateWater;
    public static Item boundLeggingsWater;
    public static Item boundBootsWater;
    
    public static Item boundHelmetEarth;
    public static Item boundPlateEarth;
    public static Item boundLeggingsEarth;
    public static Item boundBootsEarth;
    
    public static Item boundHelmetWind;
    public static Item boundPlateWind;
    public static Item boundLeggingsWind;
    public static Item boundBootsWind;
    
    public static Item boundHelmetFire;
    public static Item boundPlateFire;
    public static Item boundLeggingsFire;
    public static Item boundBootsFire;
    
    public static Item inputRoutingFocus;
    public static Item outputRoutingFocus;
    
    public static Item itemMailCatalogue;
    
    public static Item itemIncense;

    public static void init()
    {
        weakBloodOrb = new EnergyBattery(5000).setUnlocalizedName("weakBloodOrb");
        apprenticeBloodOrb = new ApprenticeBloodOrb(25000).setUnlocalizedName("apprenticeBloodOrb");
        magicianBloodOrb = new MagicianBloodOrb(150000).setUnlocalizedName("magicianBloodOrb");
        masterBloodOrb = new MasterBloodOrb(1000000).setUnlocalizedName("masterBloodOrb");
        archmageBloodOrb = new ArchmageBloodOrb(10000000).setUnlocalizedName("archmageBloodOrb");
        transcendentBloodOrb = new TranscendentBloodOrb(30000000).setUnlocalizedName("transcendentBloodOrb");
        energyBlaster = new EnergyBlast().setUnlocalizedName("energyBlast");
        energySword = new EnergySword().setUnlocalizedName("energySword");
        lavaCrystal = new LavaCrystal().setUnlocalizedName("lavaCrystal");
        waterSigil = new WaterSigil().setUnlocalizedName("waterSigil");
        lavaSigil = new LavaSigil().setUnlocalizedName("lavaSigil");
        voidSigil = new VoidSigil().setUnlocalizedName("voidSigil");
        blankSlate = new AWBaseItems().setUnlocalizedName("blankSlate");
        reinforcedSlate = new AWBaseItems().setUnlocalizedName("reinforcedSlate");
        sacrificialDagger = new SacrificialDagger().setUnlocalizedName("sacrificialDagger");
        daggerOfSacrifice = new DaggerOfSacrifice().setUnlocalizedName("daggerOfSacrifice");
        airSigil = new AirSigil().setUnlocalizedName("airSigil");
        sigilOfTheFastMiner = new SigilOfTheFastMiner().setUnlocalizedName("sigilOfTheFastMiner");
        sigilOfElementalAffinity = new SigilOfElementalAffinity().setUnlocalizedName("sigilOfElementalAffinity");
        sigilOfHaste = new SigilOfHaste().setUnlocalizedName("sigilOfHaste");
        sigilOfHolding = new SigilOfHolding().setUnlocalizedName("sigilOfHolding");
        divinationSigil = new DivinationSigil().setUnlocalizedName("divinationSigil");
        waterScribeTool = new WaterScribeTool().setUnlocalizedName("waterScribeTool");
        fireScribeTool = new FireScribeTool().setUnlocalizedName("fireScribeTool");
        earthScribeTool = new EarthScribeTool().setUnlocalizedName("earthScribeTool");
        airScribeTool = new AirScribeTool().setUnlocalizedName("airScribeTool");
        activationCrystal = new ActivationCrystal();
        boundPickaxe = new BoundPickaxe().setUnlocalizedName("boundPickaxe");
        boundAxe = new BoundAxe().setUnlocalizedName("boundAxe");
        boundShovel = new BoundShovel().setUnlocalizedName("boundShovel");
        boundHelmet = new BoundArmour(0).setUnlocalizedName("boundHelmet");
        boundPlate = new BoundArmour(1).setUnlocalizedName("boundPlate");
        boundLeggings = new BoundArmour(2).setUnlocalizedName("boundLeggings");
        boundBoots = new BoundArmour(3).setUnlocalizedName("boundBoots");
        weakBloodShard = new BloodShard().setUnlocalizedName("weakBloodShard");
        growthSigil = new SigilOfGrowth().setUnlocalizedName("growthSigil");
        blankSpell = new BlankSpell().setUnlocalizedName("blankSpell");
        alchemyFlask = new AlchemyFlask().setUnlocalizedName("alchemyFlask");
        standardBindingAgent = new StandardBindingAgent().setUnlocalizedName("standardBindingAgent");
        mundanePowerCatalyst = new MundanePowerCatalyst().setUnlocalizedName("mundanePowerCatalyst");
        averagePowerCatalyst = new AveragePowerCatalyst().setUnlocalizedName("averagePowerCatalyst");
        greaterPowerCatalyst = new GreaterPowerCatalyst().setUnlocalizedName("greaterPowerCatalyst");
        mundaneLengtheningCatalyst = new MundaneLengtheningCatalyst().setUnlocalizedName("mundaneLengtheningCatalyst");
        averageLengtheningCatalyst = new AverageLengtheningCatalyst().setUnlocalizedName("averageLengtheningCatalyst");
        greaterLengtheningCatalyst = new GreaterLengtheningCatalyst().setUnlocalizedName("greaterLengtheningCatalyst");
        incendium = new AlchemyReagent().setUnlocalizedName("incendium");
        magicales = new AlchemyReagent().setUnlocalizedName("magicales");
        sanctus = new AlchemyReagent().setUnlocalizedName("sanctus");
        aether = new AlchemyReagent().setUnlocalizedName("aether");
        simpleCatalyst = new AlchemyReagent().setUnlocalizedName("simpleCatalyst");
        crepitous = new AlchemyReagent().setUnlocalizedName("crepitous");
        crystallos = new AlchemyReagent().setUnlocalizedName("crystallos");
        terrae = new AlchemyReagent().setUnlocalizedName("terrae");
        aquasalus = new AlchemyReagent().setUnlocalizedName("aquasalus");
        tennebrae = new AlchemyReagent().setUnlocalizedName("tennebrae");
        demonBloodShard = new BloodShard().setUnlocalizedName("demonBloodShard");
        sigilOfWind = new SigilOfWind().setUnlocalizedName("sigilOfWind");
        telepositionFocus = new TelepositionFocus(1).setUnlocalizedName("telepositionFocus");
        enhancedTelepositionFocus = new EnhancedTelepositionFocus().setUnlocalizedName("enhancedTelepositionFocus");
        reinforcedTelepositionFocus = new ReinforcedTelepositionFocus().setUnlocalizedName("reinforcedTelepositionFocus");
        demonicTelepositionFocus = new DemonicTelepositionFocus().setUnlocalizedName("demonicTelepositionFocus");
        imbuedSlate = new AWBaseItems().setUnlocalizedName("imbuedSlate");
        demonicSlate = new AWBaseItems().setUnlocalizedName("demonicSlate");
        duskScribeTool = new DuskScribeTool().setUnlocalizedName("duskScribeTool");
        sigilOfTheBridge = new SigilOfTheBridge().setUnlocalizedName("sigilOfTheBridge");
        armourInhibitor = new ArmourInhibitor().setUnlocalizedName("armourInhibitor");
        creativeFiller = new CheatyItem().setUnlocalizedName("cheatyItem");
        demonPlacer = new DemonPlacer().setUnlocalizedName("demonPlacer");
        creativeDagger = new CreativeDagger().setUnlocalizedName("creativeDagger");
        weakFillingAgent = new WeakFillingAgent().setUnlocalizedName("weakFillingAgent");
        standardFillingAgent = new StandardFillingAgent().setUnlocalizedName("standardFillingAgent");
        enhancedFillingAgent = new EnhancedFillingAgent().setUnlocalizedName("enhancedFillingAgent");
        weakBindingAgent = new WeakBindingAgent().setUnlocalizedName("weakBindingAgent");
        itemRitualDiviner = new ItemRitualDiviner().setUnlocalizedName("ritualDiviner");
        sigilOfMagnetism = new SigilOfMagnetism().setUnlocalizedName("sigilOfMagnetism");
        itemKeyOfDiablo = new ItemDiabloKey().setUnlocalizedName("itemDiabloKey");
        energyBazooka = new EnergyBazooka().setUnlocalizedName("energyBazooka");
        itemBloodLightSigil = new ItemBloodLightSigil().setUnlocalizedName("bloodLightSigil");
        itemComplexSpellCrystal = new ItemComplexSpellCrystal().setUnlocalizedName("itemComplexSpellCrystal");
        bucketLife = (new LifeBucket(ModBlocks.blockLifeEssence)).setUnlocalizedName("bucketLife").setContainerItem(Items.bucket).setCreativeTab(CreativeTabs.tabMisc);
        itemSigilOfEnderSeverance = (new ItemSigilOfEnderSeverance()).setUnlocalizedName("itemSigilOfEnderSeverance");
        baseItems = new ItemComponents().setUnlocalizedName("baseItems");
        baseAlchemyItems = new ItemAlchemyBase().setUnlocalizedName("baseAlchemyItems");
        itemSigilOfSupression = new ItemSigilOfSupression().setUnlocalizedName("itemSigilOfSupression");
        itemFluidSigil = new ItemFluidSigil().setUnlocalizedName("itemFluidSigil");
        itemSeerSigil = new ItemSeerSigil().setUnlocalizedName("itemSeerSigil");
        customTool = new ItemSpellMultiTool().setUnlocalizedName("multiTool");
        
        SpellParadigmTool.customTool = customTool;
        
        itemCombinationalCatalyst = new CombinationalCatalyst().setUnlocalizedName("itemCombinationalCatalyst");
        itemAttunedCrystal = new ItemAttunedCrystal().setUnlocalizedName("itemAttunedCrystal");
        itemTankSegmenter = new ItemTankSegmenter().setUnlocalizedName("itemTankSegmenter");
        itemDestinationClearer = new ItemDestinationClearer().setUnlocalizedName("destinationClearer");
        itemBloodMagicBook = new ItemBMBook().setUnlocalizedName("bmBook");
        
        dawnScribeTool = new DawnScribeTool().setUnlocalizedName("dawnScribeTool");
        
        itemBloodPack = new ItemBloodLetterPack().setUnlocalizedName("itemBloodPack");
        itemHarvestSigil = new ItemHarvestSigil().setUnlocalizedName("itemHarvestSigil");
        itemCompressionSigil = new ItemPackRatSigil().setUnlocalizedName("itemCompressionSigil");
        itemAssassinSigil = new ItemSigilOfTheAssassin().setUnlocalizedName("itemAssassinSigil");
        
        boundHelmetWater = new OmegaArmourWater(0).setUnlocalizedName("boundHelmetWater");
        boundPlateWater = new OmegaArmourWater(1).setUnlocalizedName("boundPlateWater");
        boundLeggingsWater = new OmegaArmourWater(2).setUnlocalizedName("boundLeggingsWater");
        boundBootsWater = new OmegaArmourWater(3).setUnlocalizedName("boundBootsWater");
        
        boundHelmetEarth = new OmegaArmourEarth(0).setUnlocalizedName("boundHelmetEarth");
        boundPlateEarth = new OmegaArmourEarth(1).setUnlocalizedName("boundPlateEarth");
        boundLeggingsEarth = new OmegaArmourEarth(2).setUnlocalizedName("boundLeggingsEarth");
        boundBootsEarth = new OmegaArmourEarth(3).setUnlocalizedName("boundBootsEarth");
        
        boundHelmetWind = new OmegaArmourWind(0).setUnlocalizedName("boundHelmetWind");
        boundPlateWind = new OmegaArmourWind(1).setUnlocalizedName("boundPlateWind");
        boundLeggingsWind = new OmegaArmourWind(2).setUnlocalizedName("boundLeggingsWind");
        boundBootsWind = new OmegaArmourWind(3).setUnlocalizedName("boundBootsWind");
        
        boundHelmetFire = new OmegaArmourFire(0).setUnlocalizedName("boundHelmetFire");
        boundPlateFire = new OmegaArmourFire(1).setUnlocalizedName("boundPlateFire");
        boundLeggingsFire = new OmegaArmourFire(2).setUnlocalizedName("boundLeggingsFire");
        boundBootsFire = new OmegaArmourFire(3).setUnlocalizedName("boundBootsFire");
        
        inputRoutingFocus = new InputRoutingFocus().setUnlocalizedName("inputRoutingFocus");
        outputRoutingFocus = new OutputRoutingFocus().setUnlocalizedName("outputRoutingFocus");
        
        itemMailCatalogue = new ItemMailOrderCatalogue().setUnlocalizedName("itemMailCatalogue");
        
        itemIncense = new ItemIncense().setUnlocalizedName("bloodMagicIncenseItem");
    }

    public static void registerItems()
    {
        GameRegistry.registerItem(ModItems.weakBloodOrb, "weakBloodOrb");
        GameRegistry.registerItem(ModItems.apprenticeBloodOrb, "apprenticeBloodOrb");
        GameRegistry.registerItem(ModItems.magicianBloodOrb, "magicianBloodOrb");
        GameRegistry.registerItem(ModItems.energyBlaster, "energyBlaster");

        GameRegistry.registerItem(ModItems.energySword, "energySword");
        GameRegistry.registerItem(ModItems.lavaCrystal, "lavaCrystal");
        GameRegistry.registerItem(ModItems.waterSigil, "waterSigil");
        GameRegistry.registerItem(ModItems.lavaSigil, "lavaSigil");
        GameRegistry.registerItem(ModItems.voidSigil, "voidSigil");
        GameRegistry.registerItem(ModItems.blankSlate, "blankSlate");
        GameRegistry.registerItem(ModItems.reinforcedSlate, "reinforcedSlate");
        GameRegistry.registerItem(ModItems.sacrificialDagger, "sacrificialKnife");
        GameRegistry.registerItem(ModItems.daggerOfSacrifice, "daggerOfSacrifice");
        GameRegistry.registerItem(ModItems.airSigil, "airSigil");
        GameRegistry.registerItem(ModItems.sigilOfTheFastMiner, "sigilOfTheFastMiner");
        GameRegistry.registerItem(ModItems.sigilOfElementalAffinity, "sigilOfElementalAffinity");
        GameRegistry.registerItem(ModItems.sigilOfHaste, "sigilOfHaste");
        GameRegistry.registerItem(ModItems.sigilOfHolding, "sigilOfHolding");
        GameRegistry.registerItem(ModItems.divinationSigil, "divinationSigil");
        GameRegistry.registerItem(ModItems.waterScribeTool, "waterScribeTool");
        GameRegistry.registerItem(ModItems.fireScribeTool, "fireScribeTool");
        GameRegistry.registerItem(ModItems.earthScribeTool, "earthScribeTool");
        GameRegistry.registerItem(ModItems.airScribeTool, "airScribeTool");
        GameRegistry.registerItem(ModItems.activationCrystal, "activationCrystal");
        GameRegistry.registerItem(ModItems.boundPickaxe, "boundPickaxe");
        GameRegistry.registerItem(ModItems.boundAxe, "boundAxe");
        GameRegistry.registerItem(ModItems.boundShovel, "boundShovel");
        GameRegistry.registerItem(ModItems.boundHelmet, "boundHelmet");
        GameRegistry.registerItem(ModItems.boundPlate, "boundPlate");
        GameRegistry.registerItem(ModItems.boundLeggings, "boundLeggings");
        GameRegistry.registerItem(ModItems.boundBoots, "boundBoots");
        GameRegistry.registerItem(ModItems.weakBloodShard, "weakBloodShard");
        GameRegistry.registerItem(ModItems.growthSigil, "growthSigil");
        GameRegistry.registerItem(ModItems.blankSpell, "blankSpell");
        GameRegistry.registerItem(ModItems.masterBloodOrb, "masterBloodOrb");
        GameRegistry.registerItem(ModItems.alchemyFlask, "alchemyFlask");
        GameRegistry.registerItem(ModItems.standardBindingAgent, "standardBindingAgent");
        GameRegistry.registerItem(ModItems.mundanePowerCatalyst, "mundanePowerCatalyst");
        GameRegistry.registerItem(ModItems.averagePowerCatalyst, "averagePowerCatalyst");
        GameRegistry.registerItem(ModItems.greaterPowerCatalyst, "greaterPowerCatalyst");
        GameRegistry.registerItem(ModItems.mundaneLengtheningCatalyst, "mundaneLengtheningCatalyst");
        GameRegistry.registerItem(ModItems.averageLengtheningCatalyst, "averageLengtheningCatalyst");
        GameRegistry.registerItem(ModItems.greaterLengtheningCatalyst, "greaterLengtheningCatalyst");
        GameRegistry.registerItem(ModItems.incendium, "incendium");
        GameRegistry.registerItem(ModItems.magicales, "magicales");
        GameRegistry.registerItem(ModItems.sanctus, "sanctus");
        GameRegistry.registerItem(ModItems.aether, "aether");
        GameRegistry.registerItem(ModItems.simpleCatalyst, "simpleCatalyst");
        GameRegistry.registerItem(ModItems.crepitous, "crepitous");
        GameRegistry.registerItem(ModItems.crystallos, "crystallos");
        GameRegistry.registerItem(ModItems.terrae, "terrae");
        GameRegistry.registerItem(ModItems.aquasalus, "aquasalus");
        GameRegistry.registerItem(ModItems.tennebrae, "tennebrae");
        GameRegistry.registerItem(ModItems.demonBloodShard, "demonBloodShard");
        GameRegistry.registerItem(ModItems.archmageBloodOrb, "archmageBloodOrb");
        GameRegistry.registerItem(ModItems.transcendentBloodOrb, "transcendentBloodOrb");
        GameRegistry.registerItem(ModItems.sigilOfWind, "sigilOfWind");
        GameRegistry.registerItem(ModItems.telepositionFocus, "telepositionFocus");
        GameRegistry.registerItem(ModItems.enhancedTelepositionFocus, "enhancedTelepositionFocus");
        GameRegistry.registerItem(ModItems.reinforcedTelepositionFocus, "reinforcedTelepositionFocus");
        GameRegistry.registerItem(ModItems.demonicTelepositionFocus, "demonicTelepositionFocus");
        GameRegistry.registerItem(ModItems.imbuedSlate, "imbuedSlate");
        GameRegistry.registerItem(ModItems.demonicSlate, "demonicSlate");
        GameRegistry.registerItem(ModItems.duskScribeTool, "duskScribeTool");
        GameRegistry.registerItem(ModItems.sigilOfTheBridge, "sigilOfTheBridge");
        GameRegistry.registerItem(ModItems.armourInhibitor, "armourInhibitor");
        GameRegistry.registerItem(ModItems.creativeFiller, "creativeFiller");
        GameRegistry.registerItem(ModItems.demonPlacer, "demonPlacer");
        GameRegistry.registerItem(ModItems.creativeDagger, "creativeDagger");

        GameRegistry.registerItem(ModItems.weakFillingAgent, "weakFillingAgent");
        GameRegistry.registerItem(ModItems.standardFillingAgent, "standardFillingAgent");
        GameRegistry.registerItem(ModItems.enhancedFillingAgent, "enhancedFillingAgent");
        GameRegistry.registerItem(ModItems.weakBindingAgent, "weakBindingAgent");
        GameRegistry.registerItem(ModItems.itemRitualDiviner, "itemRitualDiviner");
        GameRegistry.registerItem(ModItems.sigilOfMagnetism, "sigilOfMagnetism");
        GameRegistry.registerItem(ModItems.itemKeyOfDiablo, "itemKeyOfDiablo");
        GameRegistry.registerItem(ModItems.energyBazooka, "energyBazooka");
        GameRegistry.registerItem(ModItems.itemBloodLightSigil, "itemBloodLightSigil");
        GameRegistry.registerItem(ModItems.itemComplexSpellCrystal, "itemComplexSpellCrystal");
        GameRegistry.registerItem(ModItems.itemSigilOfSupression, "sigilOfSupression");
        GameRegistry.registerItem(ModItems.itemSigilOfEnderSeverance, "sigilOfEnderSeverance");
        GameRegistry.registerItem(ModItems.itemFluidSigil, "fluidSigil");
        GameRegistry.registerItem(ModItems.itemSeerSigil, "seerSigil");

        GameRegistry.registerItem(ModItems.customTool, "customTool");

        GameRegistry.registerItem(ModItems.bucketLife, "bucketLife");
        GameRegistry.registerItem(ModItems.itemCombinationalCatalyst, "itemCombinationalCatalyst");

        GameRegistry.registerItem(ModItems.itemAttunedCrystal, "itemAttunedCrystal");
        GameRegistry.registerItem(ModItems.itemTankSegmenter, "itemTankSegmenter");
        GameRegistry.registerItem(ModItems.itemDestinationClearer, "itemDestinationClearer");

        GameRegistry.registerItem(ModItems.itemBloodMagicBook, "itemBloodMagicBook");
        
        GameRegistry.registerItem(ModItems.baseItems, "bloodMagicBaseItems");
        GameRegistry.registerItem(ModItems.baseAlchemyItems, "bloodMagicBaseAlchemyItems");
        
        GameRegistry.registerItem(ModItems.dawnScribeTool, "dawnScribeTool");
        
        GameRegistry.registerItem(ModItems.itemBloodPack, "itemBloodPack");
        GameRegistry.registerItem(ModItems.itemHarvestSigil, "itemHarvestSigil");
        GameRegistry.registerItem(ModItems.itemCompressionSigil, "itemCompressionSigil");
        GameRegistry.registerItem(ModItems.itemAssassinSigil, "itemAssassinSigil");
        
        GameRegistry.registerItem(ModItems.boundHelmetWater, "boundHelmetWater");
        GameRegistry.registerItem(ModItems.boundPlateWater, "boundPlateWater");
        GameRegistry.registerItem(ModItems.boundLeggingsWater, "boundLeggingsWater");
        GameRegistry.registerItem(ModItems.boundBootsWater, "boundBootsWater");
        
        GameRegistry.registerItem(ModItems.boundHelmetEarth, "boundHelmetEarth");
        GameRegistry.registerItem(ModItems.boundPlateEarth, "boundPlateEarth");
        GameRegistry.registerItem(ModItems.boundLeggingsEarth, "boundLeggingsEarth");
        GameRegistry.registerItem(ModItems.boundBootsEarth, "boundBootsEarth");
        
        GameRegistry.registerItem(ModItems.boundHelmetWind, "boundHelmetWind");
        GameRegistry.registerItem(ModItems.boundPlateWind, "boundPlateWind");
        GameRegistry.registerItem(ModItems.boundLeggingsWind, "boundLeggingsWind");
        GameRegistry.registerItem(ModItems.boundBootsWind, "boundBootsWind");
        
        GameRegistry.registerItem(ModItems.boundHelmetFire, "boundHelmetFire");
        GameRegistry.registerItem(ModItems.boundPlateFire, "boundPlateFire");
        GameRegistry.registerItem(ModItems.boundLeggingsFire, "boundLeggingsFire");
        GameRegistry.registerItem(ModItems.boundBootsFire, "boundBootsFire");
        
        GameRegistry.registerItem(ModItems.inputRoutingFocus, "inputRoutingFocus");
        GameRegistry.registerItem(ModItems.outputRoutingFocus, "outputRoutingFocus");
        
        GameRegistry.registerItem(ModItems.itemMailCatalogue, "itemMailCatalogue");
        GameRegistry.registerItem(ModItems.itemIncense, "bloodMagicIncenseItem");
        //GameRegistry.registerItem(ModItems.itemBloodFrame, "itemBloodFrame");
    }
}
