package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.downgrade.*;
import WayofTime.bloodmagic.livingArmour.tracker.*;
import WayofTime.bloodmagic.livingArmour.upgrade.*;

public class ModArmourTrackers {
    public static void init() {
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
        LivingArmourHandler.registerStatTracker(StatTrackerStepAssist.class);
        LivingArmourHandler.registerStatTracker(StatTrackerSprintAttack.class);
        LivingArmourHandler.registerStatTracker(StatTrackerCriticalStrike.class);
        LivingArmourHandler.registerStatTracker(StatTrackerFireResist.class);
        LivingArmourHandler.registerStatTracker(StatTrackerNightSight.class);
        LivingArmourHandler.registerStatTracker(StatTrackerRepairing.class);

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
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSprintAttack(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeCriticalStrike(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeElytra(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeFireResist(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeNightSight(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeRepairing(0));

        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSlowness(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeCrippledArm(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSlippery(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeBattleHungry(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeQuenched(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeMeleeDecrease(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeDisoriented(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeDigSlowdown(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeStormTrooper(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSlowHeal(0));
    }
}
