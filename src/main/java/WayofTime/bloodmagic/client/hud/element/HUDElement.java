package WayofTime.bloodmagic.client.hud.element;

import WayofTime.bloodmagic.client.hud.ElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector2f;
import java.awt.Point;

@SideOnly(Side.CLIENT)
public abstract class HUDElement {

    private int width;
    private int height;

    public HUDElement(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean shouldRender(Minecraft minecraft) {
        return true;
    }

    public abstract void draw(ScaledResolution resolution, float partialTicks, int drawX, int drawY);

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    protected void drawTexturedModalRect(double x, double y, double textureX, double textureY, double width, double height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x + 0, y + height, 0).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        buffer.pos(x + width, y + height, 0).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        buffer.pos(x + width, y + 0, 0).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
        buffer.pos(x + 0, y + 0, 0).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }

    @Override
    public String toString() {
        Vector2f point = ElementRegistry.getPosition(ElementRegistry.getKey(this));
        return ElementRegistry.getKey(this) + "@" + point.x + "," + point.y;
    }
}
