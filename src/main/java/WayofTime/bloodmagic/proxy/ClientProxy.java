package WayofTime.bloodmagic.proxy;

import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.client.helper.ShaderHelper;
import WayofTime.bloodmagic.client.render.RenderAlchemyArray;
import WayofTime.bloodmagic.client.render.RenderAltar;
import WayofTime.bloodmagic.client.render.RenderItemRoutingNode;
import WayofTime.bloodmagic.client.render.entity.BloodLightRenderFactory;
import WayofTime.bloodmagic.client.render.entity.SentientArrowRenderFactory;
import WayofTime.bloodmagic.client.render.entity.SoulSnareRenderFactory;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import WayofTime.bloodmagic.entity.projectile.EntitySoulSnare;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.tile.routing.TileRoutingNode;
import WayofTime.bloodmagic.util.handler.ClientEventHandler;
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
        super.preInit();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

        renderHelper = new InventoryRenderHelper(Constants.Mod.DOMAIN);

        OBJLoader.instance.addDomain(Constants.Mod.MODID);

        ModBlocks.initRenders();
        ModItems.initRenders();

        ClientRegistry.bindTileEntitySpecialRenderer(TileAlchemyArray.class, new RenderAlchemyArray());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderAltar());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRoutingNode.class, new RenderItemRoutingNode());
    }

    @Override
    public void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntitySoulSnare.class, new SoulSnareRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntitySentientArrow.class, new SentientArrowRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityBloodLight.class, new BloodLightRenderFactory());
        ShaderHelper.init();
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
