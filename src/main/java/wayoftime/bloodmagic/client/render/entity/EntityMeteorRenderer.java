package wayoftime.bloodmagic.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.render.BloodMagicModelLayerLocations;
import wayoftime.bloodmagic.client.render.model.ModelMeteor;

@OnlyIn(Dist.CLIENT)
public class EntityMeteorRenderer extends EntityRenderer<Entity>
{
	public Model model;
	private static final ResourceLocation METEOR_LOCATION = BloodMagic.rl("textures/models/meteor.png");
	private float scale = 1.0f;

	public EntityMeteorRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager);
		this.shadowRadius = 0.5F;
		this.model = new ModelMeteor<>(renderManager.bakeLayer(BloodMagicModelLayerLocations.METEOR_LOC));
	}

	public void render(Entity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
	{
		matrixStackIn.pushPose();

		VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entityIn)));
		this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		matrixStackIn.popPose();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(Entity entity)
	{
		return METEOR_LOCATION;
	}
}