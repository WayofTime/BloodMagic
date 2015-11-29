package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.compat.ICompatibility;
import mezz.jei.api.JEIManager;

public class CompatibilityJustEnoughItems implements ICompatibility {

    @Override
    public void loadCompatibility() {
        JEIManager.pluginRegistry.registerPlugin(new BloodMagicPlugin());
    }

    @Override
    public String getModId() {
        return "JEI";
    }
}
