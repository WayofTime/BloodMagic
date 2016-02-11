package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.compat.ICompatibility;
import WayofTime.bloodmagic.compat.thaumcraft.LivingArmourUpgradeThaumRunicShielding;
import WayofTime.bloodmagic.compat.thaumcraft.StatTrackerThaumRunicShielding;

public class CompatibilityThaumcraft implements ICompatibility
{
    @Override
    public void loadCompatibility(InitializationPhase phase)
    {
        if (phase == InitializationPhase.POST_INIT)
        {
            LivingArmourHandler.registerStatTracker(StatTrackerThaumRunicShielding.class);

            LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeThaumRunicShielding(0));
        }
    }

    @Override
    public String getModId()
    {
        return "Thaumcraft";
    }

    @Override
    public boolean enableCompat()
    {
        return true;
    }
}
