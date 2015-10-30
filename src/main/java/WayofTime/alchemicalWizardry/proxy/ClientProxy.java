package WayofTime.alchemicalWizardry.proxy;

import WayofTime.alchemicalWizardry.registry.ModBlocks;
import WayofTime.alchemicalWizardry.registry.ModItems;

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
