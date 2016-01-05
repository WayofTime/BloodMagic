package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeDigging;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradePoisonResist;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeSpeed;
import WayofTime.bloodmagic.livingArmour.StatTrackerDigging;
import WayofTime.bloodmagic.livingArmour.StatTrackerMovement;
import WayofTime.bloodmagic.livingArmour.StatTrackerPoison;
import WayofTime.bloodmagic.livingArmour.StatTrackerSelfSacrifice;

public class ModArmourTrackers
{
    public static void init()
    {
        LivingArmourHandler.registerStatTracker(StatTrackerMovement.class);
        LivingArmourHandler.registerStatTracker(StatTrackerDigging.class);
        LivingArmourHandler.registerStatTracker(StatTrackerPoison.class);
        LivingArmourHandler.registerStatTracker(StatTrackerSelfSacrifice.class);

        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSpeed(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeDigging(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradePoisonResist(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSelfSacrifice(0));
    }
}
