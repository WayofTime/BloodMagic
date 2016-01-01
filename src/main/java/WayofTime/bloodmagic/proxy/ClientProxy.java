package WayofTime.bloodmagic.proxy;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.client.render.RenderAlchemyArray;
import WayofTime.bloodmagic.client.render.RenderAltar;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;

public class ClientProxy extends CommonProxy
{
    private InventoryRenderHelper renderHelper;

    @Override
    public InventoryRenderHelper getRenderHelper()
    {
        return renderHelper;
    }

    @Override
    public void preInit()
    {
        renderHelper = new InventoryRenderHelper(Constants.Mod.DOMAIN);

        ModBlocks.initRenders();
        ModItems.initRenders();

        OBJLoader.instance.addDomain(Constants.Mod.MODID);
        ModelLoader.setCustomModelResourceLocation(InventoryRenderHelper.getItemFromBlock(ModBlocks.altar), 0, new ModelResourceLocation(Constants.Mod.DOMAIN + "BlockAltar", "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileAlchemyArray.class, new RenderAlchemyArray());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderAltar());
    }

    @Override
    public void init()
    {
//        RenderingRegistry.registerEntityRenderingHandler(EntityBloodLight.class, new RenderEntityBloodLight(Minecraft.getMinecraft().getRenderManager()));
    }

    @Override
    public void postInit()
    {

    }
}
