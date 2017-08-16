package WayofTime.bloodmagic.client.render.block;

import WayofTime.bloodmagic.api.altar.AltarComponent;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.handler.event.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderAltar extends TileEntitySpecialRenderer<TileAltar>
{
    public static Minecraft mc = Minecraft.getMinecraft();
    public static ResourceLocation resource = new ResourceLocation("bloodmagic", "textures/blocks/lifeEssenceStill.png");
    public static float minHeight = 0.499f;
    public static float maxHeight = 0.745f;

    @Override
    public void render(TileAltar tileAltar, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        ItemStack inputStack = tileAltar.getStackInSlot(0);

        float level = ((float) tileAltar.getCurrentBlood()) / (float) tileAltar.getCapacity();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (level > 0)
            this.renderFluid(getWorld(), level);
        this.renderItem(tileAltar.getWorld(), inputStack);
        GlStateManager.popMatrix();

        if (tileAltar.getCurrentTierDisplayed() != EnumAltarTier.ONE)
        {
            renderHologram(tileAltar, tileAltar.getCurrentTierDisplayed(), partialTicks);
        }
    }

    private void renderFluid(World world, float fluidLevel)
    {
        GlStateManager.pushMatrix();

        Fluid fluid = BlockLifeEssence.getLifeEssence();
        FluidStack fluidStack = new FluidStack(fluid, 1000);

        GlStateManager.translate(0.5, minHeight + (fluidLevel) * (maxHeight - minHeight), 0.5);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();

        float size = 0.8f;

        TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());

        int fluidColor = fluid.getColor(fluidStack);

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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

    private void renderItem(World world, ItemStack stack)
    {
        RenderItem itemRenderer = mc.getRenderItem();
        if (!stack.isEmpty())
        {
            GlStateManager.translate(0.5, 1, 0.5);
            EntityItem entityitem = new EntityItem(world, 0.0D, 0.0D, 0.0D, stack);
            entityitem.getItem().setCount(1);
            entityitem.hoverStart = 0.0F;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

            GlStateManager.rotate(rotation, 0.0F, 1.0F, 0);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(entityitem.getItem(), ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    private void renderHologram(TileAltar altar, EnumAltarTier tier, float partialTicks)
    {
        EntityPlayerSP player = mc.player;
        World world = player.world;

        if (tier == EnumAltarTier.ONE)
            return;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 0.6125F);

        BlockPos vec3, vX;
        vec3 = altar.getPos();
        double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        for (AltarComponent altarComponent : tier.getAltarComponents())
        {
            vX = vec3.add(altarComponent.getOffset());
            double minX = vX.getX() - posX;
            double minY = vX.getY() - posY;
            double minZ = vX.getZ() - posZ;

            if (!world.getBlockState(vX).isOpaqueCube())
            {
                TextureAtlasSprite texture = null;

                switch (altarComponent.getComponent())
                {
                case BLOODRUNE:
                    texture = ClientHandler.blankBloodRune;
                    break;
                case NOTAIR:
                    texture = ClientHandler.stoneBrick;
                    break;
                case GLOWSTONE:
                    texture = ClientHandler.glowstone;
                    break;
                case BLOODSTONE:
                    texture = ClientHandler.bloodStoneBrick;
                    break;
                case BEACON:
                    texture = ClientHandler.beacon;
                    break;
                case CRYSTAL:
                    texture = ClientHandler.crystalCluster;
                    break;
                }

                RenderFakeBlocks.drawFakeBlock(texture, minX, minY, minZ);
            }
        }

        GlStateManager.popMatrix();
    }
}
