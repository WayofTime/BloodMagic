package WayofTime.bloodmagic.proxy;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.client.helper.ShaderHelper;
import WayofTime.bloodmagic.client.hud.HUDElementHolding;
import WayofTime.bloodmagic.client.render.*;
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
import WayofTime.bloodmagic.tile.TileDemonCrucible;
import WayofTime.bloodmagic.tile.routing.TileRoutingNode;
import WayofTime.bloodmagic.util.handler.event.ClientHandler;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelper;
import WayofTime.bloodmagic.util.helper.InventoryRenderHelperV2;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;

public class ClientProxy extends CommonProxy
{
    private InventoryRenderHelper renderHelper;
    private InventoryRenderHelperV2 renderHelperV2;

    @Override
    public InventoryRenderHelper getRenderHelper()
    {
        return renderHelper;
    }

    @Override
    public InventoryRenderHelperV2 getRenderHelperV2()
    {
        return renderHelperV2;
    }

    @Override
    public void preInit()
    {
        super.preInit();

        renderHelper = new InventoryRenderHelper(Constants.Mod.DOMAIN);
        renderHelperV2 = new InventoryRenderHelperV2(Constants.Mod.DOMAIN);

        OBJLoader.INSTANCE.addDomain(Constants.Mod.MODID);

        ModBlocks.initRenders();
        ModItems.initRenders();

        ClientRegistry.bindTileEntitySpecialRenderer(TileAlchemyArray.class, new RenderAlchemyArray());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderAltar());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRoutingNode.class, new RenderItemRoutingNode());
        ClientRegistry.bindTileEntitySpecialRenderer(TileDemonCrucible.class, new RenderDemonCrucible());
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
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(
                new IItemColor()
                {
                    @Override
                    public int getColorFromItemstack(ItemStack stack, int tintIndex)
                    {
                        try {
                            if (stack.hasTagCompound() && stack.getTagCompound().hasKey(Constants.NBT.COLOR))
                                if (tintIndex == 1)
                                    return Color.decode(stack.getTagCompound().getString(Constants.NBT.COLOR)).getRGB();
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                        return -1;
                    }
                },
                ModItems.sigilHolding
        );

        addElytraLayer();
    }

    @Override
    public void postInit()
    {
        new HUDElementHolding();
    }

    @Override
    public void tryHandleBlockModel(Block block, String name)
    {
        if (block instanceof IVariantProvider)
        {
            IVariantProvider variantProvider = (IVariantProvider) block;
            for (Pair<Integer, String> variant : variantProvider.getVariants())
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), variant.getLeft(), new ModelResourceLocation(new ResourceLocation(Constants.Mod.MODID, name), variant.getRight()));
        }
    }

    @Override
    public void tryHandleItemModel(Item item, String name)
    {
        if (item instanceof IMeshProvider)
        {
            IMeshProvider meshProvider = (IMeshProvider) item;
            ModelLoader.setCustomMeshDefinition(item, meshProvider.getMeshDefinition());
            ResourceLocation resourceLocation = meshProvider.getCustomLocation();
            if (resourceLocation == null)
                resourceLocation = new ResourceLocation(Constants.Mod.MODID, "item/" + name);
            for (String variant : meshProvider.getVariants())
                ModelLoader.registerItemVariants(item, new ModelResourceLocation(resourceLocation, variant));
        } else if (item instanceof IVariantProvider)
        {
            IVariantProvider variantProvider = (IVariantProvider) item;
            for (Pair<Integer, String> variant : variantProvider.getVariants())
                ModelLoader.setCustomModelResourceLocation(item, variant.getLeft(), new ModelResourceLocation(new ResourceLocation(Constants.Mod.MODID, "item/" + name), variant.getRight()));
        }
    }

    private void addElytraLayer() {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        try {
            RenderPlayer renderPlayer = ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, renderManager, "playerRenderer", "field_178637_m");
            renderPlayer.addLayer(new LayerBloodElytra(renderPlayer));
            BloodMagic.instance.getLogger().info("Elytra layer added");
        } catch (Exception e) {
            BloodMagic.instance.getLogger().error("Failed to set custom Elytra Layer for Elytra Living Armour Upgrade.");
            BloodMagic.instance.getLogger().error(e.getLocalizedMessage());
        }
    }
}
