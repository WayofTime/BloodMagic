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

public class BindingAlchemyCircleRenderer extends AlchemyArrayRenderer
{
	public static final int numberOfSweeps = 5;
	public static final int startTime = 50;
	public static final int sweepTime = 40;
	public static final int inwardRotationTime = 50;
	public static final float arcLength = (float) Math.sqrt(2 * (2 * 2) - 2 * 2 * 2 * Math.cos(2 * Math.PI * 2 / 5));
	public static final float theta2 = (float) (18f * Math.PI / 180f);
	public static final int endTime = 300;
	public final ResourceLocation[] arraysResources;
	public float offsetFromFace = -0.9f;

	public BindingAlchemyCircleRenderer()
	{
		super(new ResourceLocation("bloodmagic", "textures/models/alchemyarrays/bindingarray.png"));
		arraysResources = new ResourceLocation[5];
		arraysResources[0] = new ResourceLocation("bloodmagic", "textures/models/alchemyarrays/bindinglightningarray.png");
		arraysResources[1] = new ResourceLocation("bloodmagic", "textures/models/alchemyarrays/bindinglightningarray.png");
		arraysResources[2] = new ResourceLocation("bloodmagic", "textures/models/alchemyarrays/bindinglightningarray.png");
		arraysResources[3] = new ResourceLocation("bloodmagic", "textures/models/alchemyarrays/bindinglightningarray.png");
		arraysResources[4] = new ResourceLocation("bloodmagic", "textures/models/alchemyarrays/bindinglightningarray.png");
	}

	public float getRotation(int circle, float craftTime)
	{
		float offset = 2;
		if (circle == -1 && craftTime >= offset)
		{
			return (craftTime - offset) * 360 * 2 / 5 / sweepTime;
		}
		if (craftTime >= offset)
		{
			float modifier = (float) Math.pow(craftTime - offset, 1.5);
			return modifier * 0.5f;
		}
		return 0;
	}

	public float getSecondaryRotation(int circle, float craftTime)
	{
		float offset = 50;
		if (craftTime >= offset)
		{
			float modifier = (float) Math.pow(craftTime - offset, 1.7);
			return modifier * 0.5f;
		}
		return 0;
	}

	public float getVerticalOffset(int circle, float craftTime)
	{
		if (circle >= 0 && circle <= 4)
		{
			if (craftTime >= 5)
			{
				if (craftTime <= 40)
				{
					return (float) ((0.4) * Math.pow((craftTime - 5) / 35f, 3));
				} else
				{
					return 0.4f;
				}
			}

			return 0;
		}

		if (craftTime >= 5)
		{
			if (craftTime <= 40)
			{
				return (float) ((0.4) * Math.pow((craftTime - 5) / 35f, 3));
			} else
			{
				return 0.4f;
			}
		}
		return 0;
	}

	public float getInwardRotation(int circle, float craftTime)
	{
		float offset = startTime + numberOfSweeps * sweepTime;
		if (craftTime >= offset)
		{
			if (craftTime <= offset + inwardRotationTime)
			{
				return 90f / inwardRotationTime * (craftTime - offset);
			} else
			{
				return 90;
			}
		}

		return 0;
	}

	public void renderAt(TileAlchemyArray tileArray, double x, double y, double z, float craftTime, PoseStack matrixStack, MultiBufferSource renderer, int combinedLightIn, int combinedOverlayIn)
	{
		matrixStack.pushPose();

		matrixStack.translate(0.5, 0.5, 0.5);

		float rot = getRotation(-1, craftTime);
		float secondaryRot = getSecondaryRotation(craftTime);

		float size = 3;
		Direction dirRotation = tileArray.getRotation();

		matrixStack.pushPose();
		matrixStack.translate(0, getVerticalOffset(craftTime), 0);
		matrixStack.mulPose(Axis.YP.rotationDegrees( -dirRotation.toYRot()));

		matrixStack.pushPose();

		matrixStack.mulPose(Axis.YP.rotationDegrees( rot));
//		matrixStack.rotate(new Quaternion(Direction.NORTH.toVector3f(), secondaryRot, true));
//		matrixStack.rotate(new Quaternion(Direction.EAST.toVector3f(), secondaryRot * 0.45812f, true));

		VertexConsumer twoDBuffer = renderer.getBuffer(RenderType.entityTranslucent(arrayResource));
		Model2D arrayModel = new BloodMagicRenderer.Model2D();
		arrayModel.minX = -0.5;
		arrayModel.maxX = +0.5;
		arrayModel.minY = -0.5;
		arrayModel.maxY = +0.5;
		arrayModel.resource = arrayResource;

		matrixStack.scale(size, size, size);

		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, 0xFFFFFFFF, combinedLightIn, combinedOverlayIn);

		matrixStack.popPose();

		for (int i = 0; i < 5; i++)
		{
			matrixStack.pushPose();

			float newSize = 1;
			float distance = BindingAlchemyCircleRenderer.getDistanceOfCircle(i, craftTime);
			float angle = BindingAlchemyCircleRenderer.getAngleOfCircle(i, craftTime);
			float rotation = this.getRotation(i, craftTime);

			matrixStack.translate(distance * Math.sin(angle), this.getVerticalOffset(i, craftTime), -distance * Math.cos(angle));
			matrixStack.mulPose(Axis.YP.rotationDegrees( i * 360 / 5 ));
			matrixStack.mulPose(Axis.ZN.rotationDegrees( getInwardRotation(i, craftTime)));
			matrixStack.mulPose(Axis.YP.rotationDegrees( rotation ));
			twoDBuffer = renderer.getBuffer(RenderType.entityTranslucent(arraysResources[i]));
			arrayModel.resource = arraysResources[i];

			matrixStack.scale(newSize, newSize, newSize);

			RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, 0xFFFFFFFF, combinedLightIn, combinedOverlayIn);

			matrixStack.popPose();
		}

		matrixStack.popPose();
		matrixStack.popPose();
	}

	public static float getAngleOfCircle(int circle, float craftTime)
	{
		if (circle >= 0 && circle <= 4)
		{
			float originalAngle = (float) (circle * 2 * Math.PI / 5d);

			double sweep = (craftTime - startTime) / sweepTime;
			if (sweep >= 0 && sweep < numberOfSweeps)
			{
				float offset = ((int) sweep) * sweepTime + startTime;
				originalAngle += 2 * Math.PI * 2 / 5 * (int) sweep + getAngle(craftTime - offset, (int) sweep);
			} else if (sweep >= numberOfSweeps)
			{
				originalAngle += 2 * Math.PI * 2 / 5 * numberOfSweeps + (craftTime - 5 * sweepTime - startTime) * 2 * Math.PI * 2 / 5 / sweepTime;
			}

			return originalAngle;
		}

		return 0;
	}

	public static float getAngle(float craftTime, int sweep)
	{
		float rDP = craftTime / sweepTime * arcLength;
		float rEnd = (float) Math.sqrt(rDP * rDP + 2 * 2 - 2 * rDP * 2 * Math.cos(theta2));
		return (float) (Math.acos((2 * 2 + rEnd * rEnd - rDP * rDP) / (2 * rEnd * 2)));
	}

	/**
	 * Returns the center-to-center distance of this circle.
	 */
	public static float getDistanceOfCircle(int circle, float craftTime)
	{ // TODO Change this so it doesn't use angle, since it is a constant speed.
		double sweep = (craftTime - startTime) / sweepTime;
		if (sweep >= 0 && sweep < numberOfSweeps)
		{
			float offset = ((int) sweep) * sweepTime + startTime;
			float angle = getAngle(craftTime - offset, (int) sweep);
			float thetaPrime = (float) (Math.PI - theta2 - angle);
			// if(thetaPrime > 0 && thetaPrime < Math.PI) {
			return (float) (2 * Math.sin(theta2) / Math.sin(thetaPrime));
			// }
		} else if (sweep >= numberOfSweeps && craftTime < endTime)
		{
			return 2 - 2 * (craftTime - startTime - numberOfSweeps * sweepTime) / (endTime - startTime - numberOfSweeps * sweepTime);
		} else if (craftTime >= endTime)
		{
			return 0;
		}

		return 2;
	}
}
