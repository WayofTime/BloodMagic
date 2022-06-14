package wayoftime.bloodmagic.client.hud.element;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.client.hud.ElementRegistry;

@OnlyIn(Dist.CLIENT)
public abstract class HUDElement
{

	private int width;
	private int height;
	protected int blitOffset = 0;

	public HUDElement(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public boolean shouldRender(Minecraft minecraft)
	{
		return true;
	}

	public abstract void draw(PoseStack matrixStack, float partialTicks, int drawX, int drawY);

	public final int getWidth()
	{
		return width;
	}

	public final int getHeight()
	{
		return height;
	}

//	protected void drawTexturedModalRect(double x, double y, double textureX, double textureY, double width, double height)
//	{
//		float f = 0.00390625F;
//		float f1 = 0.00390625F;
//		Tessellator tessellator = Tessellator.getInstance();
//		BufferBuilder buffer = tessellator.getBuffer();
//		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
//		buffer.pos(x + 0, y + height, 0).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
//		buffer.pos(x + width, y + height, 0).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
//		buffer.pos(x + width, y + 0, 0).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
//		buffer.pos(x + 0, y + 0, 0).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
//		tessellator.draw();
//	}

	public void blit(PoseStack matrixStack, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight)
	{
		GuiComponent.blit(matrixStack, x, y, this.blitOffset, (float) uOffset, (float) vOffset, uWidth, vHeight, 256, 256);
	}

	@Override
	public String toString()
	{
		Vec2 point = ElementRegistry.getPosition(ElementRegistry.getKey(this));
		return ElementRegistry.getKey(this) + "@" + point.x + "," + point.y;
	}
}
