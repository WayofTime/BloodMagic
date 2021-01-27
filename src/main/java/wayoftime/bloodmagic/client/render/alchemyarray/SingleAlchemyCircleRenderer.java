package wayoftime.bloodmagic.client.render.alchemyarray;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
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

	public void renderAt(TileAlchemyArray tileArray, double x, double y, double z, float craftTime, MatrixStack matrixStack, IRenderTypeBuffer renderer, int combinedLightIn, int combinedOverlayIn)
	{
		matrixStack.push();

		matrixStack.translate(0.5, 0.5, 0.5);

		float rot = getRotation(craftTime);
		float secondaryRot = getSecondaryRotation(craftTime);

		float size = 1.0F * getSizeModifier(craftTime);
		Direction rotation = tileArray.getRotation();

		matrixStack.push();
		matrixStack.translate(0, getVerticalOffset(craftTime), 0);
		matrixStack.rotate(new Quaternion(Direction.UP.toVector3f(), -rotation.getHorizontalAngle(), true));

		matrixStack.push();

		matrixStack.rotate(new Quaternion(Direction.NORTH.toVector3f(), rot, true));
		matrixStack.rotate(new Quaternion(Direction.UP.toVector3f(), secondaryRot, true));
//		matrixStack.rotate(new Quaternion(Direction.EAST.toVector3f(), secondaryRot * 0.45812f, true));

		IVertexBuilder twoDBuffer = renderer.getBuffer(RenderType.getEntityTranslucent(arrayResource));
		Model2D arrayModel = new BloodMagicRenderer.Model2D();
		arrayModel.minX = -0.5;
		arrayModel.maxX = +0.5;
		arrayModel.minY = -0.5;
		arrayModel.maxY = +0.5;
		arrayModel.resource = arrayResource;

		matrixStack.scale(size, size, size);

		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, 0xFFFFFFFF, 0x00F000F0, combinedOverlayIn);

		matrixStack.pop();
		matrixStack.pop();
		matrixStack.pop();
	}
}
