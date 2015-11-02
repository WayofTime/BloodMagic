package WayofTime.bloodmagic.proxy;

import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ModBlocks.initRenders();
        ModItems.initRenders();
    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }
}
