package wayoftime.bloodmagic.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import wayoftime.bloodmagic.entity.projectile.AbstractEntityThrowingDagger;

public class EntityThrowingDaggerRenderer<T extends AbstractEntityThrowingDagger> extends EntityRenderer<T>
{
	private final net.minecraft.client.renderer.ItemRenderer itemRenderer;
	private final float scale;
	private final boolean fullBright;

	public EntityThrowingDaggerRenderer(EntityRendererManager p_i226035_1_, net.minecraft.client.renderer.ItemRenderer p_i226035_2_, float p_i226035_3_, boolean p_i226035_4_)
	{
		super(p_i226035_1_);
		this.itemRenderer = p_i226035_2_;
		this.scale = p_i226035_3_;
		this.fullBright = p_i226035_4_;
	}

	public EntityThrowingDaggerRenderer(EntityRendererManager renderManagerIn)
	{
		this(renderManagerIn, Minecraft.getInstance().getItemRenderer(), 1.0F, false);
	}

	protected int getBlockLightLevel(T entityIn, BlockPos partialTicks)
	{
		return this.fullBright ? 15 : super.getBlockLightLevel(entityIn, partialTicks);
	}

	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
//		System.out.println("Rendering dagger");
//		if (entityIn.ticksExisted >= 2 || !(this.renderManager.info.getRenderViewEntity().getDistanceSq(entityIn) < 12.25D))
		{
//			ArrowRenderer<?> d;
			matrixStackIn.pushPose();
			matrixStackIn.scale(this.scale, this.scale, this.scale);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.yRotO, entityIn.yRot) - 90.0F));
			matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot) - 45F));

			this.itemRenderer.renderStatic(entityIn.getItem(), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
			matrixStackIn.popPose();
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(T entity)
	{
		return AtlasTexture.LOCATION_BLOCKS;
	}

}
