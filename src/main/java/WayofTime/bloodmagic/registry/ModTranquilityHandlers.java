package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.incense.IncenseTranquilityRegistry;
import WayofTime.bloodmagic.incense.*;

public class ModTranquilityHandlers
{
    public static void init()
    {
        IncenseTranquilityRegistry.registerTranquilityHandler(new PlantTranquilityHandler());
        IncenseTranquilityRegistry.registerTranquilityHandler(new CropTranquilityHandler());
        IncenseTranquilityRegistry.registerTranquilityHandler(new WaterTranquilityHandler());
        IncenseTranquilityRegistry.registerTranquilityHandler(new EarthTranquilityHandler());
        IncenseTranquilityRegistry.registerTranquilityHandler(new FireTranquilityHandler());
        IncenseTranquilityRegistry.registerTranquilityHandler(new LavaTranquilityHandler());
        IncenseTranquilityRegistry.registerTranquilityHandler(new TreeTranquilityHandler());
    }
}
