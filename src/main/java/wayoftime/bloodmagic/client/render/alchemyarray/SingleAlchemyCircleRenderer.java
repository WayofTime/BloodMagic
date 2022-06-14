package wayoftime.bloodmagic.client.render.alchemyarray;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Quaternion;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer.Model2D;
import wayoftime.bloodmagic.client.render.RenderResizableQuadrilateral;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public class SingleAlchemyCircleRenderer extends AlchemyArrayRenderer
{
	public SingleAlchemyCircleRenderer(ResourceLocation arrayResource)
	{
		super(arrayResource);
	}

	@Override
	public float getRotation(float craftTime)
	{
		float offset = 2;
		if (craftTime >= offset)
		{
			float modifier = (craftTime - offset) * 2f;
			return modifier * 1f;
		}
		return 0;
	}

	public float getSecondaryRotation(float craftTime)
	{
		return 0;
	}

	public float getSizeModifier(float craftTime)
	{
		return 1.0f;
	}

	public void renderAt(TileAlchemyArray tileArray, double x, double y, double z, float craftTime, PoseStack matrixStack, MultiBufferSource renderer, int combinedLightIn, int combinedOverlayIn)
	{
		matrixStack.pushPose();

		matrixStack.translate(0.5, 0.5, 0.5);

		float rot = getRotation(craftTime);
		float secondaryRot = getSecondaryRotation(craftTime);

		float size = 1.0F * getSizeModifier(craftTime);
		Direction rotation = tileArray.getRotation();

		matrixStack.pushPose();
		matrixStack.translate(0, getVerticalOffset(craftTime), 0);
		matrixStack.mulPose(new Quaternion(Direction.UP.step(), -rotation.toYRot(), true));

		matrixStack.pushPose();

		matrixStack.mulPose(new Quaternion(Direction.NORTH.step(), rot, true));
		matrixStack.mulPose(new Quaternion(Direction.UP.step(), secondaryRot, true));
//		matrixStack.rotate(new Quaternion(Direction.EAST.toVector3f(), secondaryRot * 0.45812f, true));

		VertexConsumer twoDBuffer = renderer.getBuffer(RenderType.entityTranslucent(arrayResource));
		Model2D arrayModel = new BloodMagicRenderer.Model2D();
		arrayModel.minX = -0.5;
		arrayModel.maxX = +0.5;
		arrayModel.minY = -0.5;
		arrayModel.maxY = +0.5;
		arrayModel.resource = arrayResource;

		matrixStack.scale(size, size, size);

		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, 0xFFFFFFFF, 0x00F000F0, combinedOverlayIn);

		matrixStack.popPose();
		matrixStack.popPose();
		matrixStack.popPose();
	}
}
