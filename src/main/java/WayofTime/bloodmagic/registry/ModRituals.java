package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualNight;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualRain;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualResistance;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualZombie;

public class ModRituals
{
    public static Ritual testRitual;
    public static Ritual waterRitual;
    public static Ritual lavaRitual;
    public static Ritual greenGroveRitual;
    public static Ritual jumpRitual;

    public static ImperfectRitual imperfectNight;
    public static ImperfectRitual imperfectRain;
    public static ImperfectRitual imperfectResistance;
    public static ImperfectRitual imperfectZombie;

    public static void initRituals()
    {
        testRitual = new RitualTest();
        waterRitual = new RitualWater();
        lavaRitual = new RitualLava();
        greenGroveRitual = new RitualGreenGrove();
        jumpRitual = new RitualJumping();

        RitualRegistry.registerRitual(testRitual, testRitual.getName());
        RitualRegistry.registerRitual(waterRitual, waterRitual.getName());
        RitualRegistry.registerRitual(lavaRitual, lavaRitual.getName());
        RitualRegistry.registerRitual(greenGroveRitual, greenGroveRitual.getName());
        RitualRegistry.registerRitual(jumpRitual, jumpRitual.getName());
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
}
