package wayoftime.bloodmagic.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class Sprite
{

	private final ResourceLocation textureLocation;
	private final int textureX;
	private final int textureY;
	private final int textureWidth;
	private final int textureHeight;

	public Sprite(ResourceLocation textureLocation, int textureX, int textureY, int textureWidth, int textureHeight)
	{
		this.textureLocation = textureLocation;
		this.textureX = textureX;
		this.textureY = textureY;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

	public ResourceLocation getTextureLocation()
	{
		return textureLocation;
	}

	public int getTextureX()
	{
		return textureX;
	}

	public int getTextureY()
	{
		return textureY;
	}

	public int getTextureWidth()
	{
		return textureWidth;
	}

	public int getTextureHeight()
	{
		return textureHeight;
	}

	public void draw(PoseStack matrixStack, int x, int y)
	{
		RenderSystem.setShaderTexture(0, getTextureLocation());
		GuiComponent.blit(matrixStack, x, y, 0, getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight(), 256, 256);
	}
}
