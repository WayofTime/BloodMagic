package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.compat.ICompatibility;
import WayofTime.bloodmagic.compat.jei.CompatibilityJustEnoughItems;
import WayofTime.bloodmagic.compat.waila.CompatibilityWaila;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;

public class ModCompatibility
{
    private static ArrayList<ICompatibility> compatibilities = new ArrayList<ICompatibility>();

    public static void registerModCompat()
    {
        compatibilities.add(new CompatibilityJustEnoughItems());
        compatibilities.add(new CompatibilityWaila());

        for (ICompatibility compat : compatibilities)
        {
            if (compat.enableCompat() && Loader.isModLoaded(compat.getModId()))
                compat.loadCompatibility();
        }
    }
}
