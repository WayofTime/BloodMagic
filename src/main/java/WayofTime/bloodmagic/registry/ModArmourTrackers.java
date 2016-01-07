package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeDigging;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeHealthboost;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeKnockbackResist;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeMeleeDamage;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradePhysicalProtect;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradePoisonResist;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeSpeed;
import WayofTime.bloodmagic.livingArmour.StatTrackerDigging;
import WayofTime.bloodmagic.livingArmour.StatTrackerFood;
import WayofTime.bloodmagic.livingArmour.StatTrackerHealthboost;
import WayofTime.bloodmagic.livingArmour.StatTrackerMeleeDamage;
import WayofTime.bloodmagic.livingArmour.StatTrackerMovement;
import WayofTime.bloodmagic.livingArmour.StatTrackerPhysicalProtect;
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
    }
}
