package WayofTime.alchemicalWizardry.common.tweaker;

import minetweaker.MineTweakerAPI;

/**
 * MineTweaker3 Integration by joshie *
 */
public class MineTweakerIntegration 
{
    public static void register() 
    {
        MineTweakerAPI.registerClass(Alchemy.class);
        MineTweakerAPI.registerClass(Binding.class);
        MineTweakerAPI.registerClass(BloodAltar.class);
        MineTweakerAPI.registerClass(BloodOrb.class);
    }
}
