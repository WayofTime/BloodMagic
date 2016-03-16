package WayofTime.bloodmagic.compat.thaumcraft;

import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.compat.ICompatibility;
import WayofTime.bloodmagic.compat.thaumcraft.research.BloodMagicResearch;

public class CompatibilityThaumcraft implements ICompatibility
{
    @Override
    public void loadCompatibility(InitializationPhase phase)
    {
        if (phase == InitializationPhase.POST_INIT)
        {
            BloodMagicResearch.addResearch();

            LivingArmourHandler.registerStatTracker(StatTrackerThaumRunicShielding.class);

            LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeThaumRunicShielding(0));
            LivingArmourHandler.registerArmourUpgrade(new LivingArmourUpgradeThaumRevealing(0));
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
