package WayofTime.bloodmagic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Sprite {

    private final ResourceLocation textureLocation;
    private final int textureX;
    private final int textureY;
    private final int textureWidth;
    private final int textureHeight;

    public Sprite(ResourceLocation textureLocation, int textureX, int textureY, int textureWidth, int textureHeight) {
        this.textureLocation = textureLocation;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }

    public int getTextureX() {
        return textureX;
    }

    public int getTextureY() {
        return textureY;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public void draw(int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(textureLocation);
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos((double) x, (double) (y + getTextureHeight()), 1.0F).tex((double) ((float) (getTextureX()) * f), (double) ((float) (getTextureY() + getTextureHeight()) * f1)).endVertex();
        buffer.pos((double) (x + getTextureWidth()), (double) (y + getTextureHeight()), 1.0F).tex((double) ((float) (getTextureX() + getTextureWidth()) * f), (double) ((float) (getTextureY() + getTextureHeight()) * f1)).endVertex();
        buffer.pos((double) (x + getTextureWidth()), (double) (y), 1.0F).tex((double) ((float) (getTextureX() + getTextureWidth()) * f), (double) ((float) (getTextureY()) * f1)).endVertex();
        buffer.pos((double) x, (double) (y), 1.0F).tex((double) ((float) (getTextureX()) * f), (double) ((float) (getTextureY()) * f1)).endVertex();
        tessellator.draw();
    }
}
