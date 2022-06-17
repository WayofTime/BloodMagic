package wayoftime.bloodmagic.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;

@OnlyIn(Dist.CLIENT)
public class EntityMeteorRenderer extends EntityRenderer<Entity>
{
//	 private static final ResourceLocation COW_LOCATION = new ResourceLocation("textures/entity/cow/cow.png");
//
//	   public CowRenderer(EntityRendererProvider.Context p_173956_) {
//	      super(p_173956_, new CowModel<>(p_173956_.bakeLayer(ModelLayers.COW)), 0.7F);
//	   }
//
//	   public ResourceLocation getTextureLocation(Cow p_114029_) {
//	      return COW_LOCATION;
//	   }

	public Model model;
	private static final ResourceLocation COW_LOCATION = BloodMagic.rl("textures/models/meteor.png");
	private float scale = 1.0f;

	public EntityMeteorRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager);
		this.shadowRadius = 0.5F;
		this.model = new CowModel<>(renderManager.bakeLayer(ModelLayers.COW));
	}

	public void render(Entity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
	{
		matrixStackIn.pushPose();
//	      matrixStackIn.translate(0.0D, 0.375D, 0.0D);
//	      matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
//	      float f = (float)entityIn.getTimeSinceHit() - partialTicks;
//	      float f1 = entityIn.getDamageTaken() - partialTicks;
//	      if (f1 < 0.0F) {
//	         f1 = 0.0F;
//	      }
//
//	      if (f > 0.0F) {
//	         matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.sin(f) * f * f1 / 10.0F * (float)entityIn.getForwardDirection()));
//	      }
//
//	      float f2 = entityIn.getRockingAngle(partialTicks);
//	      if (!MathHelper.epsilonEquals(f2, 0.0F)) {
//	         matrixStackIn.rotate(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), entityIn.getRockingAngle(partialTicks), true));
//	      }
//
//	      matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
//	      matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
//	      this.modelBoat.setRotationAngles(entityIn, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
		VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entityIn)));
		this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//	      if (!entityIn.canSwim()) {
//	         IVertexBuilder ivertexbuilder1 = bufferIn.getBuffer(RenderType.getWaterMask());
//	         this.modelBoat.waterPatch().render(matrixStackIn, ivertexbuilder1, packedLightIn, OverlayTexture.NO_OVERLAY);
//	      }

		matrixStackIn.popPose();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(Entity entity)
	{
		return COW_LOCATION;
	}
}