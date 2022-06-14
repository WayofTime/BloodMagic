package wayoftime.bloodmagic.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;

public class SoulSnareRenderer<T extends Entity & ItemSupplier> extends ThrownItemRenderer<T>
{
	public SoulSnareRenderer(EntityRenderDispatcher renderManagerIn)
	{
		super(renderManagerIn, Minecraft.getInstance().getItemRenderer());
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
	{
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
}