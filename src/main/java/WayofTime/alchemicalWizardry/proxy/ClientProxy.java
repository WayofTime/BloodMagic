package WayofTime.alchemicalWizardry.proxy;

import WayofTime.alchemicalWizardry.registry.ModBlocks;
import WayofTime.alchemicalWizardry.registry.ModItems;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {
        ModItems.initRenders();
        ModBlocks.initRenders();
    }
}
