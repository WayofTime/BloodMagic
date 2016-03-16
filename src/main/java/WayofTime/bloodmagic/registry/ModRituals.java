package WayofTime.bloodmagic.registry;

import net.minecraft.init.Blocks;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.registry.HarvestRegistry;
import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.ritual.RitualAltarBuilder;
import WayofTime.bloodmagic.ritual.RitualAnimalGrowth;
import WayofTime.bloodmagic.ritual.RitualArmourEvolve;
import WayofTime.bloodmagic.ritual.RitualCobblestone;
import WayofTime.bloodmagic.ritual.RitualContainment;
import WayofTime.bloodmagic.ritual.RitualCrushing;
import WayofTime.bloodmagic.ritual.RitualExpulsion;
import WayofTime.bloodmagic.ritual.RitualFeatheredKnife;
import WayofTime.bloodmagic.ritual.RitualFelling;
import WayofTime.bloodmagic.ritual.RitualFullStomach;
import WayofTime.bloodmagic.ritual.RitualGreenGrove;
import WayofTime.bloodmagic.ritual.RitualHarvest;
import WayofTime.bloodmagic.ritual.RitualInterdiction;
import WayofTime.bloodmagic.ritual.RitualJumping;
import WayofTime.bloodmagic.ritual.RitualLava;
import WayofTime.bloodmagic.ritual.RitualMagnetic;
import WayofTime.bloodmagic.ritual.RitualPlacer;
import WayofTime.bloodmagic.ritual.RitualPortal;
import WayofTime.bloodmagic.ritual.RitualPump;
import WayofTime.bloodmagic.ritual.RitualRegeneration;
import WayofTime.bloodmagic.ritual.RitualSpeed;
import WayofTime.bloodmagic.ritual.RitualSuppression;
import WayofTime.bloodmagic.ritual.RitualUpgradeRemove;
import WayofTime.bloodmagic.ritual.RitualWater;
import WayofTime.bloodmagic.ritual.RitualWellOfSuffering;
import WayofTime.bloodmagic.ritual.RitualZephyr;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerPlantable;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerStem;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerTall;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualNight;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualRain;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualResistance;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualZombie;

public class ModRituals
{
    public static Ritual waterRitual;
    public static Ritual lavaRitual;
    public static Ritual greenGroveRitual;
    public static Ritual jumpRitual;
    public static Ritual sufferingRitual;
    public static Ritual featheredKnifeRitual;
    public static Ritual regenerationRitual;
    public static Ritual animalGrowthRitual;
    public static Ritual harvestRitual;
    public static Ritual magneticRitual;
    public static Ritual crushingRitual;
    public static Ritual stomachRitual;
    public static Ritual interdictionRitual;
    public static Ritual containmentRitual;
    public static Ritual speedRitual;
    public static Ritual suppressionRitual;
    public static Ritual expulsionRitual;
    public static Ritual zephyrRitual;
    public static Ritual upgradeRemoveRitual;
    public static Ritual armourEvolveRitual;

    public static Ritual cobblestoneRitual;
    public static Ritual placerRitual;
    public static Ritual fellingRitual;
    public static Ritual pumpRitual;
    public static Ritual altarBuilderRitual;
    public static Ritual portalRitual;

    public static ImperfectRitual imperfectNight;
    public static ImperfectRitual imperfectRain;
    public static ImperfectRitual imperfectResistance;
    public static ImperfectRitual imperfectZombie;

    public static void initRituals()
    {
        waterRitual = new RitualWater();
        RitualRegistry.registerRitual(waterRitual, ConfigHandler.ritualWater);
        lavaRitual = new RitualLava();
        RitualRegistry.registerRitual(lavaRitual, ConfigHandler.ritualLava);
        greenGroveRitual = new RitualGreenGrove();
        RitualRegistry.registerRitual(greenGroveRitual, ConfigHandler.ritualGreenGrove);
        jumpRitual = new RitualJumping();
        RitualRegistry.registerRitual(jumpRitual, ConfigHandler.ritualJumping);
        sufferingRitual = new RitualWellOfSuffering();
        RitualRegistry.registerRitual(sufferingRitual, ConfigHandler.ritualWellOfSuffering);
        featheredKnifeRitual = new RitualFeatheredKnife();
        RitualRegistry.registerRitual(featheredKnifeRitual, ConfigHandler.ritualFeatheredKnife);
        regenerationRitual = new RitualRegeneration();
        RitualRegistry.registerRitual(regenerationRitual, ConfigHandler.ritualRegeneration);
        animalGrowthRitual = new RitualAnimalGrowth();
        RitualRegistry.registerRitual(animalGrowthRitual, ConfigHandler.ritualAnimalGrowth);
        harvestRitual = new RitualHarvest();
        RitualRegistry.registerRitual(harvestRitual, ConfigHandler.ritualHarvest);
        initHarvestHandlers();
        magneticRitual = new RitualMagnetic();
        RitualRegistry.registerRitual(magneticRitual, ConfigHandler.ritualMagnetic);
        crushingRitual = new RitualCrushing();
        RitualRegistry.registerRitual(crushingRitual, ConfigHandler.ritualCrushing);
        stomachRitual = new RitualFullStomach();
        RitualRegistry.registerRitual(stomachRitual, ConfigHandler.ritualFullStomach);
        interdictionRitual = new RitualInterdiction();
        RitualRegistry.registerRitual(interdictionRitual, ConfigHandler.ritualInterdiction);
        containmentRitual = new RitualContainment();
        RitualRegistry.registerRitual(containmentRitual, ConfigHandler.ritualContainment);
        speedRitual = new RitualSpeed();
        RitualRegistry.registerRitual(speedRitual, ConfigHandler.ritualSpeed);
        suppressionRitual = new RitualSuppression();
        RitualRegistry.registerRitual(suppressionRitual, ConfigHandler.ritualSuppression);
        zephyrRitual = new RitualZephyr();
        RitualRegistry.registerRitual(zephyrRitual, ConfigHandler.ritualZephyr);
        expulsionRitual = new RitualExpulsion();
        RitualRegistry.registerRitual(expulsionRitual, ConfigHandler.ritualExpulsion);
        upgradeRemoveRitual = new RitualUpgradeRemove();
        RitualRegistry.registerRitual(upgradeRemoveRitual, ConfigHandler.ritualUpgradeRemove);
        armourEvolveRitual = new RitualArmourEvolve();
        RitualRegistry.registerRitual(armourEvolveRitual, ConfigHandler.ritualArmourEvolve);

        cobblestoneRitual = new RitualCobblestone();
        RitualRegistry.registerRitual(cobblestoneRitual, ConfigHandler.cobblestoneRitual);
        placerRitual = new RitualPlacer();
        RitualRegistry.registerRitual(placerRitual, ConfigHandler.placerRitual);
        fellingRitual = new RitualFelling();
        RitualRegistry.registerRitual(fellingRitual, ConfigHandler.fellingRitual);
        pumpRitual = new RitualPump();
        RitualRegistry.registerRitual(pumpRitual, ConfigHandler.pumpRitual);
        altarBuilderRitual = new RitualAltarBuilder();
        RitualRegistry.registerRitual(altarBuilderRitual, ConfigHandler.altarBuilderRitual);
        portalRitual = new RitualPortal();
        RitualRegistry.registerRitual(portalRitual, ConfigHandler.portalRitual);
    }

    public static void initImperfectRituals()
    {
        imperfectNight = new ImperfectRitualNight();
        ImperfectRitualRegistry.registerRitual(imperfectNight, ConfigHandler.imperfectRitualNight);
        imperfectRain = new ImperfectRitualRain();
        ImperfectRitualRegistry.registerRitual(imperfectRain, ConfigHandler.imperfectRitualRain);
        imperfectResistance = new ImperfectRitualResistance();
        ImperfectRitualRegistry.registerRitual(imperfectResistance, ConfigHandler.imperfectRitualResistance);
        imperfectZombie = new ImperfectRitualZombie();
        ImperfectRitualRegistry.registerRitual(imperfectZombie, ConfigHandler.imperfectRitualZombie);
    }

    public static void initHarvestHandlers()
    {
        HarvestRegistry.registerRangeAmplifier(new BlockStack(Blocks.diamond_block), 15);
        HarvestRegistry.registerRangeAmplifier(new BlockStack(Blocks.gold_block), 10);
        HarvestRegistry.registerRangeAmplifier(new BlockStack(Blocks.iron_block), 6);

        HarvestRegistry.registerHandler(new HarvestHandlerPlantable());
        HarvestRegistry.registerHandler(new HarvestHandlerTall());
        HarvestRegistry.registerHandler(new HarvestHandlerStem());
    }
}
