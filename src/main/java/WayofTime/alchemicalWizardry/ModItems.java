package WayofTime.alchemicalWizardry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.alchemicalWizardry.api.items.ItemSpellMultiTool;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.items.BaseItems;
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
import WayofTime.alchemicalWizardry.common.items.CreativeOrb;
import WayofTime.alchemicalWizardry.common.items.CreativeDagger;
import WayofTime.alchemicalWizardry.common.items.DaggerOfSacrifice;
import WayofTime.alchemicalWizardry.common.items.DawnScribeTool;
import WayofTime.alchemicalWizardry.common.items.DemonCrystal;
import WayofTime.alchemicalWizardry.common.items.DemonicTelepositionFocus;
import WayofTime.alchemicalWizardry.common.items.DuskScribeTool;
import WayofTime.alchemicalWizardry.common.items.EarthScribeTool;
import WayofTime.alchemicalWizardry.common.items.Orb;
import WayofTime.alchemicalWizardry.common.items.EnergyBazooka;
import WayofTime.alchemicalWizardry.common.items.EnergyBlaster;
import WayofTime.alchemicalWizardry.common.items.BoundBlade;
import WayofTime.alchemicalWizardry.common.items.EnhancedTelepositionFocus;
import WayofTime.alchemicalWizardry.common.items.FireScribeTool;
import WayofTime.alchemicalWizardry.common.items.ItemAlchemyBase;
import WayofTime.alchemicalWizardry.common.items.ItemBloodLetterPack;
import WayofTime.alchemicalWizardry.common.items.ItemComplexSpellCrystal;
import WayofTime.alchemicalWizardry.common.items.ItemComponents;
import WayofTime.alchemicalWizardry.common.items.KeyOfBinding;
import WayofTime.alchemicalWizardry.common.items.ItemIncense;
import WayofTime.alchemicalWizardry.common.items.ItemRitualDismantler;
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
import WayofTime.alchemicalWizardry.common.items.energy.AlchemicalRouter;
import WayofTime.alchemicalWizardry.common.items.energy.AlchemicalCleanser;
import WayofTime.alchemicalWizardry.common.items.energy.AlchemicalSegmenter;
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
import WayofTime.alchemicalWizardry.common.items.sigil.SigilAir;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilBloodLight;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilDivination;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilFluid;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilHarvest;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilLava;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfElementalAffinity;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfEnderSeverance;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfGrowth;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHaste;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfMagnetism;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfSupression;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfTheAssassin;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfTheBridge;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfTheFastMiner;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfWind;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilCompress;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilSeer;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilVoid;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilWater;
import WayofTime.alchemicalWizardry.common.items.sigil.holding.SigilOfHolding;

import java.util.ArrayList;

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
    public static Item boundChestplate;
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

    public static ItemSpellMultiTool customTool;

    public static Item itemAttunedCrystal;
    public static Item itemTankSegmenter;
    public static Item itemDestinationClearer;
    
    public static Item itemHarvestSigil;
    public static Item itemCompressionSigil;

    public static Item bucketLife;
    
    public static Item itemBloodPack;
    
    public static Item transcendentBloodOrb;
    public static Item itemAssassinSigil;
    
    public static Item boundHelmetWater;
    public static Item boundChestplateWater;
    public static Item boundLeggingsWater;
    public static Item boundBootsWater;
    
    public static Item boundHelmetEarth;
    public static Item boundChestplateEarth;
    public static Item boundLeggingsEarth;
    public static Item boundBootsEarth;
    
    public static Item boundHelmetWind;
    public static Item boundChestplateWind;
    public static Item boundLeggingsWind;
    public static Item boundBootsWind;
    
    public static Item boundHelmetFire;
    public static Item boundChestplateFire;
    public static Item boundLeggingsFire;
    public static Item boundBootsFire;
    
    public static Item inputRoutingFocus;
    public static Item outputRoutingFocus;
    
    public static Item itemMailCatalogue;
    
    public static Item itemIncense;

    public static Item ritualDismantler;

    public static ArrayList<String> itemsNotToBeRegistered = new ArrayList<String>();

    public static void init()
    {
        weakBloodOrb = registerItem(new Orb(5000), "weak_blood_orb");
        apprenticeBloodOrb = registerItem(new ApprenticeBloodOrb(25000), "apprentice_blood_orb");
        magicianBloodOrb = registerItem(new MagicianBloodOrb(150000), "magician_blood_orb");
        masterBloodOrb = registerItem(new MasterBloodOrb(1000000), "master_blood_orb");
        archmageBloodOrb = registerItem(new ArchmageBloodOrb(10000000), "archmage_blood_orb");
        transcendentBloodOrb = registerItem(new TranscendentBloodOrb(30000000), "transcendent_blood_orb");
        energyBlaster = registerItem(new EnergyBlaster(), "energy_blaster");
        energySword = registerItem(new BoundBlade(), "bound_blade");
        lavaCrystal = registerItem(new LavaCrystal(), "lava_crystal");
        waterSigil = registerItem(new SigilWater(), "water_sigil");
        lavaSigil = registerItem(new SigilLava(), "lava_sigil");
        voidSigil = registerItem(new SigilVoid(), "void_sigil");
        blankSlate = registerItem(new BaseItems(), "blank_slate");
        reinforcedSlate = registerItem(new BaseItems(), "reinforced_slate");
        sacrificialDagger = registerItem(new SacrificialDagger(), "sacrificial_agger");
        daggerOfSacrifice = registerItem(new DaggerOfSacrifice(), "dagger_of_sacrifice");
        airSigil = registerItem(new SigilAir(), "air_sigil");
        sigilOfTheFastMiner = registerItem(new SigilOfTheFastMiner(), "sigil_of_the_fast_miner");
        sigilOfElementalAffinity = registerItem(new SigilOfElementalAffinity(), "sigil_of_elemental_affinity");
        sigilOfHaste = registerItem(new SigilOfHaste(), "sigil_of_haste");
        sigilOfHolding = registerItem(new SigilOfHolding(), "sigil_of_holding");
        divinationSigil = registerItem(new SigilDivination(), "divination_sigil");
        waterScribeTool = registerItem(new WaterScribeTool(), "water_scribe_tool");
        fireScribeTool = registerItem(new FireScribeTool(), "fire_scribe_tool");
        earthScribeTool = registerItem(new EarthScribeTool(), "earth_scribe_tool");
        airScribeTool = registerItem(new AirScribeTool(), "air_scribe_tool");
        activationCrystal = registerItem(new ActivationCrystal(), "activation_crystal");
        boundPickaxe = registerItem(new BoundPickaxe(), "bound_pickaxe");
        boundAxe = registerItem(new BoundAxe(), "bound_axe");
        boundShovel = registerItem(new BoundShovel(), "bound_shovel");
        boundHelmet = registerItem(new BoundArmour(0), "bound_helmet");
        boundChestplate = registerItem(new BoundArmour(1), "bound_chestplate");
        boundLeggings = registerItem(new BoundArmour(2), "bound_leggings");
        boundBoots = registerItem(new BoundArmour(3), "bound_boots");
        weakBloodShard = registerItem(new BloodShard(), "weak_blood_shard");
        growthSigil = registerItem(new SigilOfGrowth(), "growth_sigil");
        blankSpell = registerItem(new BlankSpell(), "blank_spell");
        alchemyFlask = registerItem(new AlchemyFlask(), "alchemy_flask");
        standardBindingAgent = registerItem(new StandardBindingAgent(), "standard_binding_agent");
        mundanePowerCatalyst = registerItem(new MundanePowerCatalyst(), "mundane_power_catalyst");
        averagePowerCatalyst = registerItem(new AveragePowerCatalyst(), "average_power_catalyst");
        greaterPowerCatalyst = registerItem(new GreaterPowerCatalyst(), "greater_power_catalyst");
        mundaneLengtheningCatalyst = registerItem(new MundaneLengtheningCatalyst(), "mundane_lengthening_catalyst");
        averageLengtheningCatalyst = registerItem(new AverageLengtheningCatalyst(), "average_lengthening_catalyst");
        greaterLengtheningCatalyst = registerItem(new GreaterLengtheningCatalyst(), "greater_lengthening_catalyst");
        incendium = registerItem(new AlchemyReagent(), "incendium");
        magicales = registerItem(new AlchemyReagent(), "magicales");
        sanctus = registerItem(new AlchemyReagent(), "sanctus");
        aether = registerItem(new AlchemyReagent(), "aether");
        simpleCatalyst = registerItem(new AlchemyReagent(), "simple_catalyst");
        crepitous = registerItem(new AlchemyReagent(), "crepitous");
        crystallos = registerItem(new AlchemyReagent(), "crystallos");
        terrae = registerItem(new AlchemyReagent(), "terrae");
        aquasalus = registerItem(new AlchemyReagent(), "aquasalus");
        tennebrae = registerItem(new AlchemyReagent(), "tennebrae");
        demonBloodShard = registerItem(new BloodShard(), "demon_blood_shard");
        sigilOfWind = registerItem(new SigilOfWind(), "sigil_of_wind");
        telepositionFocus = registerItem(new TelepositionFocus(1), "teleposition_focus");
        enhancedTelepositionFocus = registerItem(new EnhancedTelepositionFocus(), "enhanced_teleposition_focus");
        reinforcedTelepositionFocus = registerItem(new ReinforcedTelepositionFocus(), "reinforced_teleposition_focus");
        demonicTelepositionFocus = registerItem(new DemonicTelepositionFocus(), "demonic_teleposition_focus");
        imbuedSlate = registerItem(new BaseItems(), "imbued_slate");
        demonicSlate = registerItem(new BaseItems(), "demonic_slate");
        duskScribeTool = registerItem(new DuskScribeTool(), "dusk_scribe_tool");
        sigilOfTheBridge = registerItem(new SigilOfTheBridge(), "sigil_of_the_bridge");
        armourInhibitor = registerItem(new ArmourInhibitor(), "armour_inhibitor");
        creativeFiller = registerItem(new CreativeOrb(), "creative_orb");
        demonPlacer = registerItem(new DemonCrystal(), "demon_crystal");
        creativeDagger = registerItem(new CreativeDagger(), "creative_dagger");
        weakFillingAgent = registerItem(new WeakFillingAgent(), "weak_filling_agent");
        standardFillingAgent = registerItem(new StandardFillingAgent(), "standard_filling_agent");
        enhancedFillingAgent = registerItem(new EnhancedFillingAgent(), "enhanced_filling_agent");
        weakBindingAgent = registerItem(new WeakBindingAgent(), "weak_binding_agent");
        itemRitualDiviner = registerItem(new ItemRitualDiviner(), "ritual_diviner");
        sigilOfMagnetism = registerItem(new SigilOfMagnetism(), "sigil_of_magnetism");
        itemKeyOfDiablo = registerItem(new KeyOfBinding(), "key_of_binding");
        energyBazooka = registerItem(new EnergyBazooka(), "energy_bazooka");
        itemBloodLightSigil = registerItem(new SigilBloodLight(), "blood_light_sigil");
        itemComplexSpellCrystal = registerItem(new ItemComplexSpellCrystal(), "complex_spell_crystal");
        bucketLife = registerItem(new LifeBucket(ModBlocks.blockLifeEssence), "bucket_life").setContainerItem(Items.bucket).setCreativeTab(CreativeTabs.tabMisc);
        itemSigilOfEnderSeverance = registerItem(new SigilOfEnderSeverance(), "sigil_of_ender_severance");
        baseItems = registerItem(new ItemComponents(), "base_items");
        baseAlchemyItems = registerItem(new ItemAlchemyBase(), "base_alchemy_items");
        itemSigilOfSupression = registerItem(new SigilOfSupression(), "sigil_of_suppression");
        itemFluidSigil = registerItem(new SigilFluid(), "fluid_sigil");
        itemSeerSigil = registerItem(new SigilSeer(), "sigil_of_sight");
        customTool = (ItemSpellMultiTool) registerItem(new ItemSpellMultiTool(), "multi_tool");
        
        SpellParadigmTool.customTool = customTool;
        
        itemCombinationalCatalyst = registerItem(new CombinationalCatalyst(), "combinational_catalyst");
        itemAttunedCrystal = registerItem(new AlchemicalRouter(), "alchemical_router");
        itemTankSegmenter = registerItem(new AlchemicalSegmenter(), "alchemical_segmenter");
        itemDestinationClearer = registerItem(new AlchemicalCleanser(), "alchemical_cleanser");
        
        dawnScribeTool = registerItem(new DawnScribeTool(), "dawn_scribe_tool");
        
        itemBloodPack = registerItem(new ItemBloodLetterPack(), "blood_pack");
        itemHarvestSigil = registerItem(new SigilHarvest(), "harvest_sigil");
        itemCompressionSigil = registerItem(new SigilCompress(), "compression_sigil");
        itemAssassinSigil = registerItem(new SigilOfTheAssassin(), "assassin_sigil");
        
        boundHelmetWater = registerItem(new OmegaArmourWater(0), "bound_helmet_water");
        boundChestplateWater = registerItem(new OmegaArmourWater(1), "bound_chestplate_water");
        boundLeggingsWater = registerItem(new OmegaArmourWater(2), "bound_leggings_water");
        boundBootsWater = registerItem(new OmegaArmourWater(3), "bound_boots_water");
        
        boundHelmetEarth = registerItem(new OmegaArmourEarth(0), "bound_helmet_earth");
        boundChestplateEarth = registerItem(new OmegaArmourEarth(1), "bound_chestplate_earth");
        boundLeggingsEarth = registerItem(new OmegaArmourEarth(2), "bound_leggings_earth");
        boundBootsEarth = registerItem(new OmegaArmourEarth(3), "bound_boots_earth");
        
        boundHelmetWind = registerItem(new OmegaArmourWind(0), "bound_helmet_wind");
        boundChestplateWind = registerItem(new OmegaArmourWind(1), "bound_chestplate_wind");
        boundLeggingsWind = registerItem(new OmegaArmourWind(2), "bound_leggings_wind");
        boundBootsWind = registerItem(new OmegaArmourWind(3), "bound_boots_wind");
        
        boundHelmetFire = registerItem(new OmegaArmourFire(0), "bound_helmet_fire");
        boundChestplateFire = registerItem(new OmegaArmourFire(1), "bound_chestplate_fire");
        boundLeggingsFire = registerItem(new OmegaArmourFire(2), "bound_leggings_fire");
        boundBootsFire = registerItem(new OmegaArmourFire(3), "bound_boots_fire");
        
        inputRoutingFocus = registerItem(new InputRoutingFocus(), "input_routing_focus");
        outputRoutingFocus = registerItem(new OutputRoutingFocus(), "output_routing_focus");
                
        itemIncense = registerItem(new ItemIncense(), "incense");

        ritualDismantler = registerItem(new ItemRitualDismantler(), "ritual_dismantler");
    }

    public static Item registerItem(Item item, String unlocalizedName)
    {
        item.setUnlocalizedName(unlocalizedName);
        item.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
//        itemsNotToBeRegistered.clear();

//        for (String unlocName : BloodMagicConfiguration.itemsToBeDisabled)
        {
//            if (unlocName.equals(unlocalizedName))
            {
//                itemsNotToBeRegistered.add(unlocName);
            }
        }

//        if (!itemsNotToBeRegistered.contains(unlocalizedName))
        {
            GameRegistry.registerItem(item, unlocalizedName);
        }

        return item;
    }
}
