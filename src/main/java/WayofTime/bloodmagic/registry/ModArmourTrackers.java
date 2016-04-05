package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.tracker.*;
import WayofTime.bloodmagic.livingArmour.upgrade.*;

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
        LivingArmourHandler.registerStatTracker(StatTrackerArrowShot.class);
        LivingArmourHandler.registerStatTracker(StatTrackerGrimReaperSprint.class);
        LivingArmourHandler.registerStatTracker(StatTrackerSolarPowered.class);
        LivingArmourHandler.registerStatTracker(StatTrackerExperience.class);
        LivingArmourHandler.registerStatTracker(StatTrackerJump.class);
        LivingArmourHandler.registerStatTracker(StatTrackerFallProtect.class);
        LivingArmourHandler.registerStatTracker(StatTrackerGraveDigger.class);

        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSpeed(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeDigging(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradePoisonResist(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSelfSacrifice(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeKnockbackResist(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradePhysicalProtect(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeHealthboost(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeMeleeDamage(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeArrowShot(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeStepAssist(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeGrimReaperSprint(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSolarPowered(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeExperience(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeJump(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeFallProtect(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeGraveDigger(0));
    }
}
