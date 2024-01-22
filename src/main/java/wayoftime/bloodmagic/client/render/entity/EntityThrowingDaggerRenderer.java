package wayoftime.bloodmagic.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import wayoftime.bloodmagic.entity.projectile.AbstractEntityThrowingDagger;

public class EntityThrowingDaggerRenderer<T extends AbstractEntityThrowingDagger> extends EntityRenderer<T>
{
	private final net.minecraft.client.renderer.entity.ItemRenderer itemRenderer;
	private final float scale;
	private final boolean fullBright;

	public EntityThrowingDaggerRenderer(EntityRendererProvider.Context renderManager, float p_i226035_3_, boolean p_i226035_4_)
	{
		super(renderManager);
		this.itemRenderer = renderManager.getItemRenderer();
		this.scale = p_i226035_3_;
		this.fullBright = p_i226035_4_;
	}

	public EntityThrowingDaggerRenderer(EntityRendererProvider.Context renderManager)
	{
		this(renderManager, 1.0F, false);
	}

	protected int getBlockLightLevel(T entityIn, BlockPos partialTicks)
	{
		return this.fullBright ? 15 : super.getBlockLightLevel(entityIn, partialTicks);
	}

	public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
	{
//		System.out.println("Rendering dagger");
//		if (entityIn.ticksExisted >= 2 || !(this.renderManager.info.getRenderViewEntity().getDistanceSq(entityIn) < 12.25D))
		{
//			ArrowRenderer<?> d;
			matrixStackIn.pushPose();
			matrixStackIn.scale(this.scale, this.scale, this.scale);
			matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
			matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot()) - 45F));

			this.itemRenderer.renderStatic(entityIn.getItem(), ItemDisplayContext.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn,entityIn.level(), entityIn.getId());
			matrixStackIn.popPose();
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(T entity)
	{
		return TextureAtlas.LOCATION_BLOCKS;
	}

}
