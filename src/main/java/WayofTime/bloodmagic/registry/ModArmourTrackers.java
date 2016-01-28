package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.upgrade.*;
import WayofTime.bloodmagic.livingArmour.tracker.*;

public class ModArmourTrackers
{
    public static void init()
    {
        LivingArmourHandler.registerStatTracker(StatTrackerMovement.class);
        LivingArmourHandler.registerStatTracker(StatTrackerDigging.class);
        LivingArmourHandler.registerStatTracker(StatTrackerPoison.class);
        LivingArmourHandler.registerStatTracker(StatTrackerSelfSacrifice.class);
        LivingArmourHandler.registerStatTracker(StatTrackerFood.class);
        LivingArmourHandler.registerStatTracker(StatTrackerPhysicalProtect.class);
        LivingArmourHandler.registerStatTracker(StatTrackerHealthboost.class);
        LivingArmourHandler.registerStatTracker(StatTrackerMeleeDamage.class);

        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSpeed(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeDigging(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradePoisonResist(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSelfSacrifice(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeKnockbackResist(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradePhysicalProtect(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeHealthboost(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeMeleeDamage(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeArrowShot(0));
    }
}
