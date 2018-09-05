package com.wayoftime.bloodmagic.client.render;

import com.wayoftime.bloodmagic.core.RegistrarBloodMagic;
import com.wayoftime.bloodmagic.tile.TileBloodAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TESRBloodAltar extends TileEntitySpecialRenderer<TileBloodAltar> {

    private static final float MIN_HEIGHT = 0.499f;
    private static final float MAX_HEIGHT = 0.745f;
    private static final float SIZE = 0.8F;

    @Override
    public void render(TileBloodAltar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        renderFluid(te, x, y, z);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        renderItem(te, x, y, z);
        GlStateManager.popMatrix();
    }

    private void renderFluid(TileBloodAltar te, double x, double y, double z) {
        FluidTank fluidTank = (FluidTank) te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
        float level = (float) fluidTank.getFluidAmount() / (float) fluidTank.getCapacity();
        if (level <= 0)
            return;

        GlStateManager.translate(x, y, z);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        TextureAtlasSprite stillImage = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(RegistrarBloodMagic.FLUID_LIFE_ESSENCE.getStill().toString());
        if (stillImage == null)
            return;

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        setGLColorFromInt(RegistrarBloodMagic.FLUID_LIFE_ESSENCE.getColor());
        GlStateManager.translate(0.5, MIN_HEIGHT + ((MAX_HEIGHT - MIN_HEIGHT) * level), 0.5);

        double uMin = (double) stillImage.getMinU();
        double uMax = (double) stillImage.getMaxU();
        double vMin = (double) stillImage.getMinV();
        double vMax = (double) stillImage.getMaxV();

        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(SIZE / 2f, 0, SIZE / 2f).tex(uMax, vMax).endVertex();
        builder.pos(SIZE / 2f, 0, -SIZE / 2f).tex(uMax, vMin).endVertex();
        builder.pos(-SIZE / 2f, 0, -SIZE / 2f).tex(uMin, vMin).endVertex();
        builder.pos(-SIZE / 2f, 0, SIZE / 2f).tex(uMin, vMax).endVertex();
        tessellator.draw();
    }

    private void renderItem(TileBloodAltar te, double x, double y, double z) {
        ItemStack contained = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0);
        if (contained.isEmpty())
            return;

        GlStateManager.translate(x, y, z);
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        float rotation = 720.0F * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL;

        GlStateManager.translate(0.5F, 0.9F, 0.5F);
        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);

        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(contained, ItemCameraTransforms.TransformType.GROUND);
        RenderHelper.disableStandardItemLighting();
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        GlStateManager.color(red, green, blue, 1.0F);
    }
}
