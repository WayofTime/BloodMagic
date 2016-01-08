package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.registry.HarvestRegistry;
import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerPlantable;
import WayofTime.bloodmagic.ritual.imperfect.*;

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

    public static ImperfectRitual imperfectNight;
    public static ImperfectRitual imperfectRain;
    public static ImperfectRitual imperfectResistance;
    public static ImperfectRitual imperfectZombie;

    public static void initRituals()
    {
        waterRitual = new RitualWater();
        RitualRegistry.registerRitual(waterRitual, waterRitual.getName());
        lavaRitual = new RitualLava();
        RitualRegistry.registerRitual(lavaRitual, lavaRitual.getName());
        greenGroveRitual = new RitualGreenGrove();
        RitualRegistry.registerRitual(greenGroveRitual, greenGroveRitual.getName());
        jumpRitual = new RitualJumping();
        RitualRegistry.registerRitual(jumpRitual, jumpRitual.getName());
        sufferingRitual = new RitualWellOfSuffering();
        RitualRegistry.registerRitual(sufferingRitual, sufferingRitual.getName());
        featheredKnifeRitual = new RitualFeatheredKnife();
        RitualRegistry.registerRitual(featheredKnifeRitual, featheredKnifeRitual.getName());
        regenerationRitual = new RitualRegeneration();
        RitualRegistry.registerRitual(regenerationRitual, regenerationRitual.getName());
        animalGrowthRitual = new RitualAnimalGrowth();
        RitualRegistry.registerRitual(animalGrowthRitual, animalGrowthRitual.getName());
        harvestRitual = new RitualHarvest();
        RitualRegistry.registerRitual(harvestRitual, harvestRitual.getName());
        initHarvestHandlers();
    }

    public static void initImperfectRituals()
    {
        imperfectNight = new ImperfectRitualNight();
        ImperfectRitualRegistry.registerRitual(imperfectNight);
        imperfectRain = new ImperfectRitualRain();
        ImperfectRitualRegistry.registerRitual(imperfectRain);
        imperfectResistance = new ImperfectRitualResistance();
        ImperfectRitualRegistry.registerRitual(imperfectResistance);
        imperfectZombie = new ImperfectRitualZombie();
        ImperfectRitualRegistry.registerRitual(imperfectZombie);
    }

    public static void initHarvestHandlers()
    {
        HarvestRegistry.registerHandler(new HarvestHandlerPlantable());
    }
}
