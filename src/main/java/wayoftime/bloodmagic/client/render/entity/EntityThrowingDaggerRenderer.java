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
import wayoftime.bloodmagic.entity.projectile.EntityThrowingDagger;

public class EntityThrowingDaggerRenderer<T extends EntityThrowingDagger> extends EntityRenderer<T>
{
	private final net.minecraft.client.renderer.ItemRenderer itemRenderer;
	private final float scale;
	private final boolean field_229126_f_;

	public EntityThrowingDaggerRenderer(EntityRendererManager p_i226035_1_, net.minecraft.client.renderer.ItemRenderer p_i226035_2_, float p_i226035_3_, boolean p_i226035_4_)
	{
		super(p_i226035_1_);
		this.itemRenderer = p_i226035_2_;
		this.scale = p_i226035_3_;
		this.field_229126_f_ = p_i226035_4_;
	}

	public EntityThrowingDaggerRenderer(EntityRendererManager renderManagerIn)
	{
		this(renderManagerIn, Minecraft.getInstance().getItemRenderer(), 1.0F, false);
	}

	protected int getBlockLight(T entityIn, BlockPos partialTicks)
	{
		return this.field_229126_f_ ? 15 : super.getBlockLight(entityIn, partialTicks);
	}

	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
//		System.out.println("Rendering dagger");
//		if (entityIn.ticksExisted >= 2 || !(this.renderManager.info.getRenderViewEntity().getDistanceSq(entityIn) < 12.25D))
		{
//			ArrowRenderer<?> d;
			matrixStackIn.push();
			matrixStackIn.scale(this.scale, this.scale, this.scale);
//			matrixStackIn.rotate(this.renderManager.getCameraOrientation());
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch) - 45F));

//			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(45F));
//			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
			this.itemRenderer.renderItem(entityIn.getItem(), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
			matrixStackIn.pop();
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(T entity)
	{
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}

}
