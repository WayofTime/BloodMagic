package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.compat.ICompatibility;
<<<<<<< HEAD
=======
import mezz.jei.api.JEIManager;
>>>>>>> parent of d51a908... Update JEI compat

public class CompatibilityJustEnoughItems implements ICompatibility {

    @Override
    public void loadCompatibility() {
<<<<<<< HEAD

=======
        JEIManager.pluginRegistry.registerPlugin(new BloodMagicPlugin());
>>>>>>> parent of d51a908... Update JEI compat
    }

    @Override
    public String getModId() {
        return "JEI";
    }

    @Override
    public boolean enableCompat() {
        return ConfigHandler.compatibilityJustEnoughItems;
    }
}
