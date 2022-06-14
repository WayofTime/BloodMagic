package wayoftime.bloodmagic.client.render.alchemyarray;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer.Model2D;
import wayoftime.bloodmagic.client.render.RenderResizableQuadrilateral;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public class StaticAlchemyCircleRenderer extends AlchemyArrayRenderer
{
	public StaticAlchemyCircleRenderer(ResourceLocation arrayResource)
	{
		super(arrayResource);
	}

	public float getRotation(float craftTime)
	{
		float offset = 50;
		if (craftTime >= offset)
		{
			float modifier = (float) (craftTime - offset) * 5f;
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

	public void renderAt(TileAlchemyArray tileArray, double x, double y, double z, float craftTime, MatrixStack matrixStack, IRenderTypeBuffer renderer, int combinedLightIn, int combinedOverlayIn)
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
//		matrixStack.rotate(new Quaternion(Direction.NORTH.toVector3f(), secondaryRot, true));
//		matrixStack.rotate(new Quaternion(Direction.EAST.toVector3f(), secondaryRot * 0.45812f, true));

		IVertexBuilder twoDBuffer = renderer.getBuffer(RenderType.entityTranslucent(arrayResource));
		Model2D arrayModel = new BloodMagicRenderer.Model2D();
		arrayModel.minX = -0.5;
		arrayModel.maxX = +0.5;
		arrayModel.minY = -0.5;
		arrayModel.maxY = +0.5;
		arrayModel.resource = arrayResource;

		matrixStack.scale(size, size, size);

		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, 0xFFFFFFFF, 0x00F000F0, OverlayTexture.NO_OVERLAY);

		matrixStack.popPose();
		matrixStack.popPose();
		matrixStack.popPose();
	}
}
