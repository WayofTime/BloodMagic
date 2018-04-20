package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.ritual.harvest.HarvestRegistry;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualRegistry;
import WayofTime.bloodmagic.ritual.RitualRegistry;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerPlantable;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerStem;
import WayofTime.bloodmagic.ritual.harvest.HarvestHandlerTall;
import WayofTime.bloodmagic.ritual.types.imperfect.ImperfectRitualNight;
import WayofTime.bloodmagic.ritual.types.imperfect.ImperfectRitualRain;
import WayofTime.bloodmagic.ritual.types.imperfect.ImperfectRitualResistance;
import WayofTime.bloodmagic.ritual.types.imperfect.ImperfectRitualZombie;
import WayofTime.bloodmagic.ritual.types.*;
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
    public static Ritual upgradeRemoveRitual;
    public static Ritual armourEvolveRitual;
    public static Ritual forsakenSoulRitual;
    public static Ritual crystalHarvestRitual;

    public static Ritual placerRitual;
    public static Ritual fellingRitual;
    public static Ritual pumpRitual;
    public static Ritual altarBuilderRitual;
    public static Ritual portalRitual;

    public static Ritual ellipsoidRitual;
    public static Ritual crystalSplitRitual;

    public static Ritual meteorRitual;

    public static Ritual downgradeRitual;

    public static ImperfectRitual imperfectNight;
    public static ImperfectRitual imperfectRain;
    public static ImperfectRitual imperfectResistance;
    public static ImperfectRitual imperfectZombie;

    public static void initRituals()
    {
        waterRitual = new RitualWater();
        RitualRegistry.registerRitual(waterRitual, ConfigHandler.rituals.ritualWater);
        lavaRitual = new RitualLava();
        RitualRegistry.registerRitual(lavaRitual, ConfigHandler.rituals.ritualLava);
        greenGroveRitual = new RitualGreenGrove();
        RitualRegistry.registerRitual(greenGroveRitual, ConfigHandler.rituals.ritualGreenGrove);
        jumpRitual = new RitualJumping();
        RitualRegistry.registerRitual(jumpRitual, ConfigHandler.rituals.ritualJumping);
        sufferingRitual = new RitualWellOfSuffering();
        RitualRegistry.registerRitual(sufferingRitual, ConfigHandler.rituals.ritualWellOfSuffering);
        featheredKnifeRitual = new RitualFeatheredKnife();
        RitualRegistry.registerRitual(featheredKnifeRitual, ConfigHandler.rituals.ritualFeatheredKnife);
        regenerationRitual = new RitualRegeneration();
        RitualRegistry.registerRitual(regenerationRitual, ConfigHandler.rituals.ritualRegeneration);
        animalGrowthRitual = new RitualAnimalGrowth();
        RitualRegistry.registerRitual(animalGrowthRitual, ConfigHandler.rituals.ritualAnimalGrowth);
        harvestRitual = new RitualHarvest();
        RitualRegistry.registerRitual(harvestRitual, ConfigHandler.rituals.ritualHarvest);
        initHarvestHandlers();
        magneticRitual = new RitualMagnetic();
        RitualRegistry.registerRitual(magneticRitual, ConfigHandler.rituals.ritualMagnetic);
        crushingRitual = new RitualCrushing();
        RitualRegistry.registerRitual(crushingRitual, ConfigHandler.rituals.ritualCrushing);
        stomachRitual = new RitualFullStomach();
        RitualRegistry.registerRitual(stomachRitual, ConfigHandler.rituals.ritualFullStomach);
        interdictionRitual = new RitualInterdiction();
        RitualRegistry.registerRitual(interdictionRitual, ConfigHandler.rituals.ritualInterdiction);
        containmentRitual = new RitualContainment();
        RitualRegistry.registerRitual(containmentRitual, ConfigHandler.rituals.ritualContainment);
        speedRitual = new RitualSpeed();
        RitualRegistry.registerRitual(speedRitual, ConfigHandler.rituals.ritualSpeed);
        suppressionRitual = new RitualSuppression();
        RitualRegistry.registerRitual(suppressionRitual, ConfigHandler.rituals.ritualSuppression);
        zephyrRitual = new RitualZephyr();
        RitualRegistry.registerRitual(zephyrRitual, ConfigHandler.rituals.ritualZephyr);
        expulsionRitual = new RitualExpulsion();
        RitualRegistry.registerRitual(expulsionRitual, ConfigHandler.rituals.ritualExpulsion);
        upgradeRemoveRitual = new RitualUpgradeRemove();
        RitualRegistry.registerRitual(upgradeRemoveRitual, ConfigHandler.rituals.ritualUpgradeRemove);
        armourEvolveRitual = new RitualArmourEvolve();
        RitualRegistry.registerRitual(armourEvolveRitual, ConfigHandler.rituals.ritualArmourEvolve);
        forsakenSoulRitual = new RitualForsakenSoul();
        RitualRegistry.registerRitual(forsakenSoulRitual, ConfigHandler.rituals.ritualForsakenSoul);
        crystalHarvestRitual = new RitualCrystalHarvest();
        RitualRegistry.registerRitual(crystalHarvestRitual, ConfigHandler.rituals.ritualCrystalHarvest);
        placerRitual = new RitualPlacer();
        RitualRegistry.registerRitual(placerRitual, ConfigHandler.rituals.ritualPlacer);
        fellingRitual = new RitualFelling();
        RitualRegistry.registerRitual(fellingRitual, ConfigHandler.rituals.ritualFelling);
        pumpRitual = new RitualPump();
        RitualRegistry.registerRitual(pumpRitual, ConfigHandler.rituals.ritualPump);
        altarBuilderRitual = new RitualAltarBuilder();
        RitualRegistry.registerRitual(altarBuilderRitual, ConfigHandler.rituals.ritualAltarBuilder);
        portalRitual = new RitualPortal();
        RitualRegistry.registerRitual(portalRitual, ConfigHandler.rituals.ritualPortal);
        meteorRitual = new RitualMeteor();
        RitualRegistry.registerRitual(meteorRitual, ConfigHandler.rituals.ritualMeteor);

        downgradeRitual = new RitualLivingArmourDowngrade();
        RitualRegistry.registerRitual(downgradeRitual, ConfigHandler.rituals.ritualDowngrade);

        ellipsoidRitual = new RitualEllipsoid();
        RitualRegistry.registerRitual(ellipsoidRitual, ConfigHandler.rituals.ritualEllipsoid);

        crystalSplitRitual = new RitualCrystalSplit();
        RitualRegistry.registerRitual(crystalSplitRitual, ConfigHandler.rituals.ritualCrystalSplit);

        RitualCrushing.registerCuttingFluid(ItemCuttingFluid.FluidType.BASIC.getStack(), 250, 0.5);
        RitualCrushing.registerCuttingFluid(ItemCuttingFluid.FluidType.EXPLOSIVE.getStack(), 25, 0.05);
    }

    public static void initImperfectRituals()
    {
        imperfectNight = new ImperfectRitualNight();
        ImperfectRitualRegistry.registerRitual(imperfectNight, ConfigHandler.rituals.imperfect.imperfectRitualNight);
        imperfectRain = new ImperfectRitualRain();
        ImperfectRitualRegistry.registerRitual(imperfectRain, ConfigHandler.rituals.imperfect.imperfectRitualRain);
        imperfectResistance = new ImperfectRitualResistance();
        ImperfectRitualRegistry.registerRitual(imperfectResistance, ConfigHandler.rituals.imperfect.imperfectRitualResistance);
        imperfectZombie = new ImperfectRitualZombie();
        ImperfectRitualRegistry.registerRitual(imperfectZombie, ConfigHandler.rituals.imperfect.imperfectRitualZombie);
    }

    public static void initHarvestHandlers()
    {
        HarvestRegistry.registerRangeAmplifier(Blocks.DIAMOND_BLOCK.getDefaultState(), 15);
        HarvestRegistry.registerRangeAmplifier(Blocks.GOLD_BLOCK.getDefaultState(), 10);
        HarvestRegistry.registerRangeAmplifier(Blocks.IRON_BLOCK.getDefaultState(), 6);

        HarvestRegistry.registerHandler(new HarvestHandlerPlantable());
        HarvestRegistry.registerHandler(new HarvestHandlerTall());
        HarvestRegistry.registerHandler(new HarvestHandlerStem());
    }
}
