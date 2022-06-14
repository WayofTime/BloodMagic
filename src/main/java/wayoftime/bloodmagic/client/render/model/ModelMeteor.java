package wayoftime.bloodmagic.client.render.model;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelMeteor<T extends Entity> extends EntityModel<T>
{
	// fields
	ModelPart Shape1;
	ModelPart Shape2;
	ModelPart Shape3;
	ModelPart Shape4;
	ModelPart Shape5;
	ModelPart Shape6;
	ModelPart Shape7;

	public ModelMeteor()
	{
		this(RenderType::entityCutoutNoCull);
		texWidth = 64;
		texHeight = 64;
		Shape1 = new ModelPart(this, 0, 0);
		Shape1.addBox(-8F, -8F, -8F, 16, 16, 16);
		Shape1.setPos(0F, 0F, 0F);
		Shape1.setTexSize(64, 64);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelPart(this, 0, 32);
		Shape2.addBox(3F, -10F, -1F, 12, 12, 12);
		Shape2.setPos(0F, 0F, 0F);
		Shape2.setTexSize(64, 64);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, 0F);
		Shape3 = new ModelPart(this, 0, 32);
		Shape3.addBox(0F, 0F, -10F, 12, 12, 12);
		Shape3.setPos(0F, 0F, 0F);
		Shape3.setTexSize(64, 64);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		Shape4 = new ModelPart(this, 0, 32);
		Shape4.addBox(1F, 2F, 2F, 12, 12, 12);
		Shape4.setPos(0F, 0F, 0F);
		Shape4.setTexSize(64, 64);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);
		Shape5 = new ModelPart(this, 0, 32);
		Shape5.addBox(-12F, -5F, 0F, 12, 12, 12);
		Shape5.setPos(0F, 0F, 0F);
		Shape5.setTexSize(64, 64);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, 0F);
		Shape6 = new ModelPart(this, 0, 32);
		Shape6.addBox(-13F, -2F, -11F, 12, 12, 12);
		Shape6.setPos(0F, 0F, 0F);
		Shape6.setTexSize(64, 64);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 0F, 0F);
		Shape7 = new ModelPart(this, 0, 32);
		Shape7.addBox(-6F, -14F, -9F, 12, 12, 12);
		Shape7.setPos(0F, 0F, 0F);
		Shape7.setTexSize(64, 64);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 0F, 0F);
	}

	protected ModelMeteor(Function<ResourceLocation, RenderType> p_i225945_1_)
	{
		super(p_i225945_1_);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		Shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		Shape2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		Shape3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		Shape4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		Shape5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		Shape6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		Shape7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	private void setRotation(ModelPart model, float x, float y, float z)
	{
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{

	}
}
