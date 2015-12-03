package WayofTime.bloodmagic.proxy;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.render.RenderEntityBloodLight;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ModBlocks.initRenders();
        ModItems.initRenders();

        OBJLoader.instance.addDomain(Constants.Mod.MODID);
        ModelLoader.setCustomModelResourceLocation(InventoryRenderHelper.getItemFromBlock(ModBlocks.altar), 0, new ModelResourceLocation(Constants.Mod.DOMAIN + "BlockAltar", "inventory"));
    }

    @Override
    public void init() {
        RenderingRegistry.registerEntityRenderingHandler(EntityBloodLight.class, new RenderEntityBloodLight(Minecraft.getMinecraft().getRenderManager()));
    }

    @Override
    public void postInit() {

    }
}
