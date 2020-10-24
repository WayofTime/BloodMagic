package wayoftime.bloodmagic.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRendersAsItem;

public class BloodLightRenderer<T extends Entity & IRendersAsItem> extends SpriteRenderer<T>
{
	public BloodLightRenderer(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, Minecraft.getInstance().getItemRenderer());
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
}
