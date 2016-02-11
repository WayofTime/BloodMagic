package WayofTime.bloodmagic.registry;

import java.util.ArrayList;

import net.minecraftforge.fml.common.Loader;
import WayofTime.bloodmagic.compat.ICompatibility;
import WayofTime.bloodmagic.compat.jei.CompatibilityJustEnoughItems;
import WayofTime.bloodmagic.compat.jei.CompatibilityThaumcraft;
import WayofTime.bloodmagic.compat.waila.CompatibilityWaila;

public class ModCompatibility
{
    private static ArrayList<ICompatibility> compatibilities = new ArrayList<ICompatibility>();

    public static void registerModCompat()
    {
        compatibilities.add(new CompatibilityJustEnoughItems());
        compatibilities.add(new CompatibilityWaila());
        compatibilities.add(new CompatibilityThaumcraft());
    }

    public static void loadCompat(ICompatibility.InitializationPhase phase)
    {
        for (ICompatibility compatibility : compatibilities)
            if (Loader.isModLoaded(compatibility.getModId()) && compatibility.enableCompat())
                compatibility.loadCompatibility(phase);
    }
}
