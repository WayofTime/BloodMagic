package wayoftime.bloodmagic.client.hud.element;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

	public abstract void draw(GuiGraphics guiGraphics, float partialTicks, int drawX, int drawY);

	public final int getWidth()
	{
		return width;
	}

	public final int getHeight()
	{
		return height;
	}

//	public void blit(PoseStack matrixStack, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight)
//	{
//		GuiComponent.blit(matrixStack, x, y, this.blitOffset, (float) uOffset, (float) vOffset, uWidth, vHeight, 256, 256);
//	}

	@Override
	public String toString()
	{
		Vec2 point = ElementRegistry.getPosition(ElementRegistry.getKey(this));
		return ElementRegistry.getKey(this) + "@" + point.x + "," + point.y;
	}
}
