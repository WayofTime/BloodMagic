package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeDigging;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeSpeed;
import WayofTime.bloodmagic.livingArmour.StatTrackerDigging;
import WayofTime.bloodmagic.livingArmour.StatTrackerMovement;

public class ModArmourTrackers
{
    public static void init()
    {
        LivingArmourHandler.registerStatTracker(StatTrackerMovement.class);
        LivingArmourHandler.registerStatTracker(StatTrackerDigging.class);

        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSpeed(1));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeDigging(0));
    }
}
