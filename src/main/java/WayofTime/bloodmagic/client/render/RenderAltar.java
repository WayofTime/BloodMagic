package WayofTime.bloodmagic.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.tile.TileAltar;

public class RenderAltar extends TileEntitySpecialRenderer<TileAltar>
{
    public static Minecraft mc = Minecraft.getMinecraft();
    public static ResourceLocation resource = new ResourceLocation("bloodmagic", "textures/blocks/lifeEssenceStill.png");
    public static float minHeight = 0.6497f;
    public static float maxHeight = 0.79f;

    @Override
    public void renderTileEntityAt(TileAltar tileAltar, double x, double y, double z, float partialTicks, int destroyStage)
    {
        ItemStack inputStack = tileAltar.getStackInSlot(0);

        float level = ((float) tileAltar.getCurrentBlood()) / (float) tileAltar.getCapacity();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.renderFluid(getWorld(), level);
        this.renderItem(tileAltar.getWorld(), inputStack, partialTicks);
        GlStateManager.popMatrix();
    }

    private void renderFluid(World world, float fluidLevel)
    {
        GlStateManager.pushMatrix();

        Fluid fluid = BlockLifeEssence.getLifeEssence();
        FluidStack fluidStack = new FluidStack(fluid, 1000);

        GlStateManager.translate(0.5, minHeight + (fluidLevel) * (maxHeight - minHeight), 0.5);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        float size = 0.8f;

        TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());

        int fluidColor = fluid.getColor(fluidStack);

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        setGLColorFromInt(fluidColor);

        double uMin = (double) fluidStillSprite.getMinU();
        double uMax = (double) fluidStillSprite.getMaxU();
        double vMin = (double) fluidStillSprite.getMinV();
        double vMax = (double) fluidStillSprite.getMaxV();

        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
//        wr.setBrightness(200);
        wr.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
        wr.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
        wr.pos(-size / 2f, 0, -size / 2f).tex(uMin, vMin).endVertex();
        wr.pos(-size / 2f, 0, size / 2f).tex(uMin, vMax).endVertex();
        tessellator.draw();

        GlStateManager.popMatrix();
    }

    private static void setGLColorFromInt(int color)
    {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        GlStateManager.color(red, green, blue, 1.0F);
    }

    private void renderItem(World world, ItemStack stack, float partialTicks)
    {
        RenderItem itemRenderer = mc.getRenderItem();
        if (stack != null)
        {
            GlStateManager.translate(0.5, 1, 0.5);
            EntityItem entityitem = new EntityItem(world, 0.0D, 0.0D, 0.0D, stack);
            entityitem.getEntityItem().stackSize = 1;
            entityitem.hoverStart = 0.0F;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

            GlStateManager.rotate(rotation, 0.0F, 1.0F, 0);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}
