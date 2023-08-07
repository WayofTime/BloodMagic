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

public class NightAlchemyCircleRenderer extends AlchemyArrayRenderer
{
	private final ResourceLocation symbolResource;
	private final ResourceLocation circleResource;

	public NightAlchemyCircleRenderer(ResourceLocation arrayResource, ResourceLocation symbolResource, ResourceLocation circleResource)
	{
		super(arrayResource);
		this.symbolResource = symbolResource;
		this.circleResource = circleResource;
	}

	@Override
	public float getRotation(float craftTime)
	{
		return 0;
	}

	public float getSecondaryRotation(float craftTime)
	{
		float offset = 2;
		if (craftTime >= offset)
		{
			float modifier = (craftTime - offset) * (craftTime - offset) * 0.05f;
			return modifier * 1f;
		}
		return 0;
	}

	public float getTertiaryRotation(float craftTime)
	{
		float offset = 60;
		if (craftTime >= offset)
		{
			float modifier = (craftTime - offset) * (craftTime - offset) * 0.15f;
			return modifier * 1f;
		}
		return 0;
	}

	public float getVerticalOffset(float craftTime)
	{
//		if (craftTime >= 40)
//		{
//			if (craftTime <= 100)
//			{
//				return (float) (-0.4 + (0.4) * Math.pow((craftTime - 40) / 60f, 3));
//			} else
//			{
//				return 0;
//			}
//		}
		return -0.4f;
	}

	public float getSizeModifier(float craftTime)
	{
		return 1.0f;
	}

	public float getSecondarySizeModifier(float craftTime)
	{
		if (craftTime >= 100)
		{
			if (craftTime <= 160)
			{
				return (float) ((1f) * Math.pow((craftTime - 100) / 60f, 3)) + 1f;
			} else
			{
				return 2;
			}
		}
		return 1.0f;
	}

	public float getTertiarySizeModifier(float craftTime)
	{
		return 1;
	}

	public float getSymbolVerticaloffset(float craftTime)
	{
		if (craftTime >= 40)
		{
			if (craftTime <= 100)
			{
				return (float) (-0.4 + (0.4) * Math.pow((craftTime - 40) / 60f, 3));
			} else if (craftTime <= 140)
			{
				return 0.01f * (craftTime - 100);
			} else
			{
				return 0.4f;
			}
		}

		return -0.4f;
	}

	public float getCentralCircleOffset(float craftTime)
	{
		if (craftTime >= 40)
		{
			if (craftTime <= 100)
			{
				return (float) (-0.4 + (0.4) * Math.pow((craftTime - 40) / 60f, 3));
			} else
			{
				return 0;
			}
		}

		return -0.4f;
	}

	public float getSymbolPitch(float craftTime)
	{
		if (craftTime > 70)
		{
			if (craftTime <= 100)
			{
				return 90 * (craftTime - 70) / 30f;
			} else
			{
				return 90;
			}
		}
		return 0;
	}

	public float getCentralCirclePitch(float craftTime)
	{
		if (craftTime > 70)
		{
			if (craftTime <= 150)
			{
				return 360 * (craftTime - 70) / 80f;
			} else
			{
				return 360;
			}
		}
		return 0;
	}

	public float moonDisplacement(float craftTime)
	{
		if (craftTime > 40)
		{
			if (craftTime <= 100)
			{
				return (float) (2 * Math.pow((craftTime - 40) / 60f, 3));
			} else
			{
				return 2;
			}
		}
		return 0;
	}

	public float moonArc(float craftTime)
	{
		if (craftTime > 100)
		{
			if (craftTime <= 200)
			{
				return 180 * (craftTime - 100) / 100f;
			} else
			{
				return 180;
			}
		}

		return 0;
	}

	public void renderAt(TileAlchemyArray tileArray, double x, double y, double z, float craftTime, PoseStack matrixStack, MultiBufferSource renderer, int combinedLightIn, int combinedOverlayIn)
	{
		matrixStack.pushPose();

		matrixStack.translate(0.5, 0.5, 0.5);

		float rot = getRotation(craftTime);
		float secondaryRot = getSecondaryRotation(craftTime);
		float tertiaryRot = getTertiaryRotation(craftTime);

		float size = 1.0F * getSizeModifier(craftTime);
		Direction rotation = tileArray.getRotation();

		matrixStack.pushPose();
		matrixStack.translate(0, getVerticalOffset(craftTime), 0);
		matrixStack.mulPose(Axis.YP.rotationDegrees( -rotation.toYRot()));

		matrixStack.pushPose();

		matrixStack.mulPose(Axis.ZN.rotationDegrees( rot));
		matrixStack.mulPose(Axis.YP.rotationDegrees( secondaryRot));
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

//		matrixStack.push();
//		matrixStack.translate(0, getSymbolVerticaloffset(craftTime), 0);
//		matrixStack.rotate(new Quaternion(Direction.UP.toVector3f(), -rotation.getHorizontalAngle(), true));
//
//		matrixStack.push();
//
		float pitch = getSymbolPitch(craftTime);
//		matrixStack.rotate(new Quaternion(Direction.WEST.toVector3f(), pitch, true));
//		matrixStack.rotate(new Quaternion(Direction.NORTH.toVector3f(), tertiaryRot, true));
////		matrixStack.rotate(new Quaternion(Direction.EAST.toVector3f(), secondaryRot * 0.45812f, true));
//
//		twoDBuffer = renderer.getBuffer(RenderType.getEntityTranslucent(symbolResource));
//		arrayModel = new BloodMagicRenderer.Model2D();
//		arrayModel.minX = -0.5;
//		arrayModel.maxX = +0.5;
//		arrayModel.minY = -0.5;
//		arrayModel.maxY = +0.5;
//		arrayModel.resource = symbolResource;
//
		float secondarySize = 1.0F * getSecondarySizeModifier(craftTime);
//
//		matrixStack.scale(secondarySize, secondarySize, secondarySize);
//
		int colorWanted = 0xFFFFFFFF;
//
//		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, colorWanted, 0x00F000F0, combinedOverlayIn);
//
//		matrixStack.pop();
//		matrixStack.pop();

		matrixStack.pushPose();
		matrixStack.translate(0, getCentralCircleOffset(craftTime), 0);
		matrixStack.mulPose(Axis.YP.rotationDegrees( -rotation.toYRot() ));

		matrixStack.pushPose();

		pitch = getCentralCirclePitch(craftTime);
		matrixStack.mulPose(Axis.XN.rotationDegrees( pitch ));
		matrixStack.mulPose(Axis.YP.rotationDegrees( -secondaryRot ));
//		matrixStack.rotate(new Quaternion(Direction.EAST.toVector3f(), secondaryRot * 0.45812f, true));

		twoDBuffer = renderer.getBuffer(RenderType.entityTranslucent(circleResource));
		arrayModel = new BloodMagicRenderer.Model2D();
		arrayModel.minX = -0.5;
		arrayModel.maxX = +0.5;
		arrayModel.minY = -0.5;
		arrayModel.maxY = +0.5;
		arrayModel.resource = circleResource;

		float tertiarySize = 1.0F * getTertiarySizeModifier(craftTime);

		matrixStack.scale(tertiarySize, tertiarySize, tertiarySize);

		colorWanted = 0xFFFFFFFF;

		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, colorWanted, 0x00F000F0, combinedOverlayIn);

		matrixStack.popPose();
		matrixStack.popPose();

		// Moon going over the array

		matrixStack.pushPose();
		matrixStack.translate(0, getCentralCircleOffset(craftTime), 0);
		matrixStack.mulPose(Axis.YP.rotationDegrees( -rotation.toYRot() ));
		matrixStack.mulPose(Axis.ZP.rotationDegrees( moonArc(craftTime)));
		matrixStack.translate(moonDisplacement(craftTime), 0, 0);

		matrixStack.pushPose();

		pitch = getSymbolPitch(craftTime);

		matrixStack.mulPose(Axis.XN.rotationDegrees( pitch ));
		matrixStack.mulPose(Axis.ZN.rotationDegrees( tertiaryRot));
//		matrixStack.rotate(new Quaternion(Direction.EAST.toVector3f(), secondaryRot * 0.45812f, true));

		twoDBuffer = renderer.getBuffer(RenderType.entityTranslucent(symbolResource));
		arrayModel = new BloodMagicRenderer.Model2D();
		arrayModel.minX = -0.5;
		arrayModel.maxX = +0.5;
		arrayModel.minY = -0.5;
		arrayModel.maxY = +0.5;
		arrayModel.resource = symbolResource;

		secondarySize = 1.0F;

		matrixStack.scale(secondarySize, secondarySize, secondarySize);

		colorWanted = 0xFFFFFFFF;

		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, colorWanted, 0x00F000F0, combinedOverlayIn);

		matrixStack.popPose();
		matrixStack.popPose();

		matrixStack.popPose();
	}
}
