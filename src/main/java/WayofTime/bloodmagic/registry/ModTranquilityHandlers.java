package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.incense.IncenseTranquilityRegistry;
import WayofTime.bloodmagic.incense.PlantTranquilityHandler;

public class ModTranquilityHandlers
{
    public static void init()
    {
        IncenseTranquilityRegistry.registerTranquilityHandler(new PlantTranquilityHandler());
    }
}
