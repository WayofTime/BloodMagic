package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualNight;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualRain;

public class ModRituals {

    public static ImperfectRitual imperfectNight;
    public static ImperfectRitual imperfectRain;

    public static void initRituals() {

    }

    public static void initImperfectRituals() {
        imperfectNight = new ImperfectRitualNight();
        ImperfectRitualRegistry.registerRitual(imperfectNight);
        imperfectRain = new ImperfectRitualRain();
        ImperfectRitualRegistry.registerRitual(imperfectRain);
    }
}
