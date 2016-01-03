package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeSpeed;
import WayofTime.bloodmagic.livingArmour.StatTrackerMovement;

public class ModArmourTrackers
{
    public static void init()
    {
        LivingArmourHandler.registerStatTracker(StatTrackerMovement.class);

        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSpeed(1));
    }
}
