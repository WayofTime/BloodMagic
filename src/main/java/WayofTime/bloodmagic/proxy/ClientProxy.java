package WayofTime.bloodmagic.proxy;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ModBlocks.initRenders();
        ModItems.initRenders();

        OBJLoader.instance.addDomain(BloodMagic.MODID);
        ModelLoader.setCustomModelResourceLocation(InventoryRenderHelper.getItemFromBlock(ModBlocks.altar), 0, new ModelResourceLocation(BloodMagic.DOMAIN + "BlockAltar", "inventory"));
    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }
}
