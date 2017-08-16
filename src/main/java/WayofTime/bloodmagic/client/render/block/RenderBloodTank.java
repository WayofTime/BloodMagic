package WayofTime.bloodmagic.client.render.block;

import WayofTime.bloodmagic.tile.TileBloodTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBloodTank extends TileEntitySpecialRenderer<TileBloodTank> {
    private static final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(TileBloodTank bloodTank, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (bloodTank == null)
            return;

        Fluid renderFluid = bloodTank.getClientRenderFluid();
        if (bloodTank.getRenderHeight() == 0 || renderFluid == null)
            return;

        GlStateManager.pushMatrix();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        renderFluid(bloodTank.getRenderHeight(), renderFluid, x, y, z);

        GlStateManager.popMatrix();
    }

    public void renderFluid(float maxHeight, Fluid renderFluid, double x, double y, double z) {
        maxHeight = maxHeight * 0.575F;

        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();

        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        TextureAtlasSprite fluid = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(renderFluid.getStill().toString());
        fluid = fluid == null ? mc.getTextureMapBlocks().getMissingSprite() : fluid;

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        final int rgbaColor = renderFluid.getColor();
        final int rColor = rgbaColor >> 16 & 0xFF;
        final int gColor = rgbaColor >> 8 & 0xFF;
        final int bColor = rgbaColor & 0xFF;
        final int aColor = rgbaColor >> 24 & 0xFF;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(rColor, gColor, bColor, aColor);

        float u1 = fluid.getMinU();
        float v1 = fluid.getMinV();
        float u2 = fluid.getMaxU();
        float v2 = fluid.getMaxV();

        if (maxHeight > 0) {
            float texWidth = u2 - u1;

            // TOP
            buffer.pos(0.25, maxHeight + 0.05, 0.25).tex(u1 + 0.75 * texWidth, v1 + (maxHeight + 0.05) * texWidth).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.25, maxHeight + 0.05, 0.75).tex(u1 + 0.75 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.75, maxHeight + 0.05, 0.75).tex(u1 + 0.25 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.75, maxHeight + 0.05, 0.25).tex(u1 + 0.25 * texWidth, v1 + (maxHeight + 0.05) * texWidth).color(rColor, gColor, bColor, aColor).endVertex();

            // NORTH
            buffer.pos(0.75, maxHeight + 0.05, 0.25).tex(u1 + 0.75 * texWidth, v1 + (maxHeight + 0.05) * texWidth).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.75, 0, 0.25).tex(u1 + 0.75 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.25, 0, 0.25).tex(u1 + 0.25 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.25, maxHeight + 0.05, 0.25).tex(u1 + 0.25 * texWidth, v1 + (maxHeight + 0.05) * texWidth).color(rColor, gColor, bColor, aColor).endVertex();

            // EAST
            buffer.pos(0.25, 0, 0.75).tex(u1 + 0.75 * texWidth, v1 + (maxHeight + 0.05) * texWidth).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.25, maxHeight + 0.05, 0.75).tex(u1 + 0.75 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.25, maxHeight + 0.05, 0.25).tex(u1 + 0.25 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.25, 0, 0.25).tex(u1 + 0.25 * texWidth, v1 + (maxHeight + 0.05) * texWidth).color(rColor, gColor, bColor, aColor).endVertex();

            // SOUTH
            buffer.pos(0.75, 0, 0.75).tex(u1 + 0.75 * texWidth, v1 + (maxHeight + 0.05) * texWidth).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.75, maxHeight + 0.05, 0.75).tex(u1 + 0.75 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.25, maxHeight + 0.05, 0.75).tex(u1 + 0.25 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.25, 0, 0.75).tex(u1 + 0.25 * texWidth, v1 + (maxHeight + 0.05) * texWidth).color(rColor, gColor, bColor, aColor).endVertex();

            // WEST
            buffer.pos(0.75, maxHeight + 0.05, 0.75).tex(u1 + 0.75 * texWidth, v1 + (maxHeight + 0.05) * texWidth).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.75, 0, 0.75).tex(u1 + 0.75 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.75, 0, 0.25).tex(u1 + 0.25 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
            buffer.pos(0.75, maxHeight + 0.05, 0.25).tex(u1 + 0.25 * texWidth, v1 + (maxHeight + 0.05) * texWidth).color(rColor, gColor, bColor, aColor).endVertex();
        }

        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
    }
}
