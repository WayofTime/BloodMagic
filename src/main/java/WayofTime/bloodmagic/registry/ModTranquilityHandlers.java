package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.incense.IncenseTranquilityRegistry;
import WayofTime.bloodmagic.incense.TranquilityHandlers;

public class ModTranquilityHandlers {
    public static void init() {
        IncenseTranquilityRegistry.registerTranquilityHandler(new TranquilityHandlers.Plant());
        IncenseTranquilityRegistry.registerTranquilityHandler(new TranquilityHandlers.Crop());
        IncenseTranquilityRegistry.registerTranquilityHandler(new TranquilityHandlers.Water());
        IncenseTranquilityRegistry.registerTranquilityHandler(new TranquilityHandlers.Earth());
        IncenseTranquilityRegistry.registerTranquilityHandler(new TranquilityHandlers.Fire());
        IncenseTranquilityRegistry.registerTranquilityHandler(new TranquilityHandlers.Lava());
        IncenseTranquilityRegistry.registerTranquilityHandler(new TranquilityHandlers.Tree());
    }
}
