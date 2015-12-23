package WayofTime.bloodmagic.proxy;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.client.render.RenderAlchemyArray;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ModBlocks.initRenders();
        ModItems.initRenders();

        OBJLoader.instance.addDomain(Constants.Mod.MODID);
        ModelLoader.setCustomModelResourceLocation(InventoryRenderHelper.getItemFromBlock(ModBlocks.altar), 0, new ModelResourceLocation(Constants.Mod.DOMAIN + "BlockAltar", "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileAlchemyArray.class, new RenderAlchemyArray());
    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }
}
