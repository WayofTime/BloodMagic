package WayofTime.bloodmagic.client.render.block;

import WayofTime.bloodmagic.tile.TileBloodTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBloodTank extends TileEntitySpecialRenderer<TileBloodTank>
{
    private static final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void renderTileEntityAt(TileBloodTank bloodTank, double x, double y, double z, float partialTicks, int destroyStage)
    {
        Tessellator tessellator = Tessellator.getInstance();

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

        GlStateManager.disableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        Fluid renderFluid = bloodTank.getClientRenderFluid();
        if (renderFluid != null)
            renderFluid(bloodTank, tessellator, renderFluid);

        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

        GlStateManager.popMatrix();
    }

    private void renderFluid(TileBloodTank bloodTank, Tessellator tessellator, Fluid renderFluid)
    {
        VertexBuffer buffer = tessellator.getBuffer();

        TextureAtlasSprite fluid = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(renderFluid.getStill().toString());
        fluid = checkIcon(fluid);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

//        GlStateManager.enableBlend();
//        GlStateManager.disableLighting();
//        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GlStateManager.color(1F, 1F, 1F, 1F);

//        tessellator.setColorRGBA(255, 255, 255, 128);
//        tessellator.setBrightness(240);

        float scale = bloodTank.getRenderHeight();
        float u1 = fluid.getMinU();
        float v1 = fluid.getMinV();
        float u2 = fluid.getMaxU();
        float v2 = fluid.getMaxV();

        float edge = 0.9375F;
        float otherEdge = 0.0625F;

        if (scale > 0)
        {
            float offset = 0.002F;

            // Top
            buffer.pos(0, scale - offset, 0).tex(u1, v1).color(255, 255, 255, 128).endVertex();
            buffer.pos(0, scale - offset, 1).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            buffer.pos(1, scale - offset, 1).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            buffer.pos(1, scale - offset, 0).tex(u2, v1).color(255, 255, 255, 128).endVertex();

            // Bottom
            buffer.pos(1, offset, 0).tex(u1, v1).color(255, 255, 255, 128).endVertex();
            buffer.pos(1, offset, 1).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            buffer.pos(0, offset, 1).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            buffer.pos(0, offset, 0).tex(u2, v1).color(255, 255, 255, 128).endVertex();

            if (scale > otherEdge)
            {
                if (scale > edge)
                    scale = edge;

                v2 -= (fluid.getMaxV() - fluid.getMinV()) * (1 - scale);

                //NORTH
                buffer.pos(edge, scale + offset, 0).tex(u1, v1).color(255, 255, 255, 128).endVertex();
                buffer.pos(edge, otherEdge, 0).tex(u1, v2).color(255, 255, 255, 128).endVertex();
                buffer.pos(otherEdge, otherEdge, 0).tex(u2, v2).color(255, 255, 255, 128).endVertex();
                buffer.pos(otherEdge, scale + offset, 0).tex(u2, v1).color(255, 255, 255, 128).endVertex();

                //EAST
                buffer.pos(0, otherEdge - offset, edge + offset).tex(u1, v2).color(255, 255, 255, 128).endVertex();
                buffer.pos(0, scale + offset, edge + offset).tex(u1, v1).color(255, 255, 255, 128).endVertex();
                buffer.pos(0, scale + offset, otherEdge - offset).tex(u2, v1).color(255, 255, 255, 128).endVertex();
                buffer.pos(0, otherEdge - offset, otherEdge - offset).tex(u2, v2).color(255, 255, 255, 128).endVertex();

                //SOUTH
                buffer.pos(1, offset, 1 - offset).tex(u1, v2).color(255, 255, 255, 128).endVertex();
                buffer.pos(1, scale, 1 - offset).tex(u1, v1).color(255, 255, 255, 128).endVertex();
                buffer.pos(0, scale, 1 - offset).tex(u2, v1).color(255, 255, 255, 128).endVertex();
                buffer.pos(0, offset, 1 - offset).tex(u2, v2).color(255, 255, 255, 128).endVertex();

                //WEST
                buffer.pos(1 - offset, scale, 1).tex(u1, v1).color(255, 255, 255, 128).endVertex();
                buffer.pos(1 - offset, offset, 1).tex(u1, v2).color(255, 255, 255, 128).endVertex();
                buffer.pos(1 - offset, offset, 0).tex(u2, v2).color(255, 255, 255, 128).endVertex();
                buffer.pos(1 - offset, scale, 0).tex(u2, v1).color(255, 255, 255, 128).endVertex();
            }
        }
        tessellator.draw();
    }

    public static TextureAtlasSprite checkIcon(TextureAtlasSprite icon)
    {
        if (icon == null)
            return mc.getTextureMapBlocks().getMissingSprite();
        return icon;
    }
}
