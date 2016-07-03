package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeCrippledArm;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeSlowness;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerArrowShot;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerCriticalStrike;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerDigging;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerExperience;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerFallProtect;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerFireResist;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerFood;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerGraveDigger;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerGrimReaperSprint;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerHealthboost;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerJump;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerMeleeDamage;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerMovement;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerPhysicalProtect;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerPoison;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSolarPowered;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSprintAttack;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerStepAssist;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeArrowShot;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeCriticalStrike;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeDigging;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeElytra;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeExperience;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeFallProtect;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeFireResist;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeGraveDigger;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeGrimReaperSprint;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeHealthboost;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeJump;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeKnockbackResist;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeMeleeDamage;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradePhysicalProtect;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradePoisonResist;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSolarPowered;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSpeed;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSprintAttack;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeStepAssist;

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
        LivingArmourHandler.registerStatTracker(StatTrackerStepAssist.class);
        LivingArmourHandler.registerStatTracker(StatTrackerSprintAttack.class);
        LivingArmourHandler.registerStatTracker(StatTrackerCriticalStrike.class);
        LivingArmourHandler.registerStatTracker(StatTrackerFireResist.class);

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
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeSlowness(0));
        LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeCrippledArm(0));
    }
}
