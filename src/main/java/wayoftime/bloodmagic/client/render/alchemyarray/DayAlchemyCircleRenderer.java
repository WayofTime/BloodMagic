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

public class DayAlchemyCircleRenderer extends AlchemyArrayRenderer
{
	private final ResourceLocation spikesResource;
	private final ResourceLocation circleResource;

	public DayAlchemyCircleRenderer(ResourceLocation arrayResource, ResourceLocation spikesResource, ResourceLocation circleResource)
	{
		super(arrayResource);
		this.spikesResource = spikesResource;
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

	public float getVerticalOffset(float craftTime)
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

	public float getSizeModifier(float craftTime)
	{
		return 1.0f;
	}

	public float getSecondarySizeModifier(float craftTime)
	{
		if (craftTime >= 40)
		{
			if (craftTime <= 160)
			{
				return (float) ((2f) * Math.pow((craftTime - 40) / 120f, 3));
			} else
			{
				return 2;
			}
		}

		return 0;
	}

	public float getTertiarySizeModifier(float craftTime)
	{
		if (craftTime >= 40)
		{
			if (craftTime <= 100)
			{
				return (float) ((1f) * Math.pow((craftTime - 40) / 60f, 3));
			} else
			{
				return 1;
			}
		}

		return 0;
	}

	public float getSpikeVerticalOffset(float craftTime)
	{
		if (craftTime >= 40)
		{
			if (craftTime <= 100)
			{
				return (float) (-0.4 + (0.4) * Math.pow((craftTime - 40) / 60f, 3));
			} else if (craftTime <= 140)
			{
				return -0.01f * (craftTime - 100);
			} else
			{
				return -0.4f;
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
		matrixStack.mulPose(Axis.YP.rotationDegrees( -rotation.toYRot()));

		matrixStack.pushPose();

		matrixStack.mulPose(Axis.ZN.rotationDegrees( rot ));
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

		matrixStack.pushPose();
		matrixStack.translate(0, getSpikeVerticalOffset(craftTime), 0);
		matrixStack.mulPose(Axis.YP.rotationDegrees( -rotation.toYRot()));

		matrixStack.pushPose();

		matrixStack.mulPose(Axis.ZN.rotationDegrees( rot));
		matrixStack.mulPose(Axis.YP.rotationDegrees(-secondaryRot));
//		matrixStack.rotate(new Quaternion(Direction.EAST.toVector3f(), secondaryRot * 0.45812f, true));

		twoDBuffer = renderer.getBuffer(RenderType.entityTranslucent(spikesResource));
		arrayModel = new BloodMagicRenderer.Model2D();
		arrayModel.minX = -0.5;
		arrayModel.maxX = +0.5;
		arrayModel.minY = -0.5;
		arrayModel.maxY = +0.5;
		arrayModel.resource = spikesResource;

		float secondarySize = 1.0F * getSecondarySizeModifier(craftTime);

		matrixStack.scale(secondarySize, secondarySize, secondarySize);

		int colorWanted = 0xFFFFFFFF;

		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, colorWanted, 0x00F000F0, combinedOverlayIn);

		matrixStack.popPose();
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0, getCentralCircleOffset(craftTime), 0);
		matrixStack.mulPose(Axis.YP.rotationDegrees( -rotation.toYRot()));

		matrixStack.pushPose();

		matrixStack.mulPose(Axis.ZN.rotationDegrees( rot));
		matrixStack.mulPose(Axis.YP.rotationDegrees( -secondaryRot));
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
		matrixStack.popPose();
	}
}
