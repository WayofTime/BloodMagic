package wayoftime.bloodmagic.client.render.model;

import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelMeteor<T extends Entity> extends EntityModel<T>
{
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("shape1", CubeListBuilder.create().texOffs(0, 0).addBox(-8, -8, -8, 16, 16, 16).mirror(true), PartPose.offset(0, 0, 0));
		partdefinition.addOrReplaceChild("shape2", CubeListBuilder.create().texOffs(0, 32).addBox(3F, -10F, -1F, 12, 12, 12).mirror(true), PartPose.offset(0, 0, 0));
		partdefinition.addOrReplaceChild("shape3", CubeListBuilder.create().texOffs(0, 32).addBox(0F, 0F, -10F, 12, 12, 12).mirror(true), PartPose.offset(0, 0, 0));
		partdefinition.addOrReplaceChild("shape4", CubeListBuilder.create().texOffs(0, 32).addBox(1F, 2F, 2F, 12, 12, 12).mirror(true), PartPose.offset(0, 0, 0));
		partdefinition.addOrReplaceChild("shape5", CubeListBuilder.create().texOffs(0, 32).addBox(-12F, -5F, 0F, 12, 12, 12).mirror(true), PartPose.offset(0, 0, 0));
		partdefinition.addOrReplaceChild("shape6", CubeListBuilder.create().texOffs(0, 32).addBox(-13F, -2F, -11F, 12, 12, 12).mirror(true), PartPose.offset(0, 0, 0));
		partdefinition.addOrReplaceChild("shape7", CubeListBuilder.create().texOffs(0, 32).addBox(-6F, -14F, -9F, 12, 12, 12).mirror(true), PartPose.offset(0, 0, 0));

//		int i = 12;
//		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F).texOffs(22, 0).addBox("right_horn", -5.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F).texOffs(22, 0).addBox("left_horn", 4.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F), PartPose.offset(0.0F, 4.0F, -8.0F));
//		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F).texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));
//		CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
//		partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-4.0F, 12.0F, 7.0F));
//		partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder, PartPose.offset(4.0F, 12.0F, 7.0F));
//		partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder, PartPose.offset(-4.0F, 12.0F, -6.0F));
//		partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder, PartPose.offset(4.0F, 12.0F, -6.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	// fields
	ModelPart Shape1;
	ModelPart Shape2;
	ModelPart Shape3;
	ModelPart Shape4;
	ModelPart Shape5;
	ModelPart Shape6;
	ModelPart Shape7;

	public ModelMeteor(ModelPart model)
	{
		this(RenderType::entityCutoutNoCull);

		this.Shape1 = model.getChild("shape1");
		this.Shape2 = model.getChild("shape2");
		this.Shape3 = model.getChild("shape3");
		this.Shape4 = model.getChild("shape4");
		this.Shape5 = model.getChild("shape5");
		this.Shape6 = model.getChild("shape6");
		this.Shape7 = model.getChild("shape7");
	}

	protected ModelMeteor(Function<ResourceLocation, RenderType> p_i225945_1_)
	{
		super(p_i225945_1_);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		this.meteorParts().forEach((shape) -> {
			shape.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
//		Shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//		Shape2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//		Shape3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//		Shape4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//		Shape5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//		Shape6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//		Shape7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	private void setRotation(ModelPart model, float x, float y, float z)
	{
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

	protected Iterable<ModelPart> meteorParts()
	{
		return ImmutableList.of(Shape1, Shape2, Shape3, Shape4, Shape5, Shape6, Shape7);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{

	}
}
