package WayofTime.bloodmagic.client.hud;

import WayofTime.bloodmagic.util.handler.event.ClientHandler;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

@Getter
@Setter
public abstract class HUDElement
{

    private int xOffset;
    private int yOffset;
    private final int xOffsetDefault;
    private final int yOffsetDefault;
    private final RenderGameOverlayEvent.ElementType elementType;

    public HUDElement(int xOffset, int yOffset, RenderGameOverlayEvent.ElementType elementType)
    {
        this.xOffset = xOffset;
        this.xOffsetDefault = xOffset;
        this.yOffset = yOffset;
        this.yOffsetDefault = yOffset;
        this.elementType = elementType;

        ClientHandler.hudElements.add(this);
    }

    public abstract void render(Minecraft minecraft, ScaledResolution resolution, float partialTicks);

    public abstract boolean shouldRender(Minecraft minecraft);

    public void onPositionChanged()
    {

    }

    public void resetToDefault()
    {
        this.xOffset = xOffsetDefault;
        this.yOffset = yOffsetDefault;
    }

    public void drawTexturedModalRect(double x, double y, double textureX, double textureY, double width, double height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double) (x + 0), (double) (y + height), 0).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        vertexbuffer.pos((double) (x + width), (double) (y + height), 0).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        vertexbuffer.pos((double) (x + width), (double) (y + 0), 0).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
        vertexbuffer.pos((double) (x + 0), (double) (y + 0), 0).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }
}
