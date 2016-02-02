package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.registry.HarvestRegistry;
import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerPlantable;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerStem;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerTall;
import WayofTime.bloodmagic.ritual.imperfect.*;
import net.minecraft.init.Blocks;

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
