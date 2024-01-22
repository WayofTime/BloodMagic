package wayoftime.bloodmagic.client.render.alchemyarray;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer.Model2D;
import wayoftime.bloodmagic.common.tile.TileAlchemyArray;
import wayoftime.bloodmagic.client.render.RenderResizableQuadrilateral;

public class BeaconAlchemyCircleRenderer extends AlchemyArrayRenderer
{
	public BeaconAlchemyCircleRenderer(ResourceLocation arrayResource)
	{
		super(arrayResource);
	}

	@Override
	public float getRotation(float craftTime)
	{
		float offset = 2;
		if (craftTime >= offset)
		{
			float modifier = (craftTime - offset) * 5f;
			return modifier * 1f;
		}
		return 0;
	}

	@Override
	public float getSecondaryRotation(float craftTime)
	{
		float offset = 50;
		float secondaryOffset = 150;
		if (craftTime >= offset)
		{
			if (craftTime < secondaryOffset)
			{
				float modifier = 90 * (craftTime - offset) / (secondaryOffset - offset);
				return modifier;
			} else
			{
				return 90;
			}
		}
		return 0;
	}

	public float getSizeModifier(float craftTime)
	{
		return 1.0f;
	}

//	public float getVerticalOffset(float craftTime)
//	{
//		if (craftTime >= 5)
//		{
//			if (craftTime <= 40)
//			{
//				return (float) (-0.4 + (0.4) * Math.pow((craftTime - 5) / 35f, 3));
//			} else
//			{
//				return 0;
//			}
//		}
//		return -0.4f;
//	}

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
		matrixStack.mulPose(Axis.YP.rotationDegrees(-rotation.toYRot()));

		matrixStack.pushPose();

		matrixStack.mulPose(Axis.YP.rotationDegrees( rot));
		matrixStack.mulPose(Axis.XP.rotationDegrees(-secondaryRot));
//		matrixStack.rotate(new Quaternion(Direction.EAST.toVector3f(), secondaryRot * 0.45812f, true));

		VertexConsumer twoDBuffer = renderer.getBuffer(RenderType.entityTranslucent(arrayResource));
		Model2D arrayModel = new BloodMagicRenderer.Model2D();
		arrayModel.minX = -0.5;
		arrayModel.maxX = +0.5;
		arrayModel.minY = -0.5;
		arrayModel.maxY = +0.5;
		arrayModel.resource = arrayResource;

		matrixStack.scale(size, size, size);

//		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, 0x000000FF, 0xFFFFFFFF, OverlayTexture.NO_OVERLAY);
		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, 0xFFFFFFFF, 0x00F000F0, combinedOverlayIn);

		matrixStack.popPose();
		matrixStack.popPose();
		matrixStack.popPose();
	}
}
