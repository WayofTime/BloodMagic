package WayofTime.bloodmagic.compat.guideapi;

import WayofTime.bloodmagic.compat.ICompatibility;
import WayofTime.bloodmagic.compat.guideapi.guide.GuideBloodMagic;

public class CompatibilityGuideAPI implements ICompatibility
{

    @Override
    public void loadCompatibility(InitializationPhase phase)
    {
        if (phase == InitializationPhase.PRE_INIT)
            GuideBloodMagic.initGuide();
    }

    @Override
    public String getModId()
    {
        return "guideapi";
    }

    @Override
    public boolean enableCompat()
    {
        return true;
    }
}
