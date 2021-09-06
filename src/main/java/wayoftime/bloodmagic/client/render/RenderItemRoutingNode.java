package wayoftime.bloodmagic.client.render;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigManager;
import wayoftime.bloodmagic.common.routing.INodeRenderer;
import wayoftime.bloodmagic.tile.routing.TileRoutingNode;

public class RenderItemRoutingNode extends TileEntityRenderer<TileRoutingNode>
{
	private static final ResourceLocation beamTexture = new ResourceLocation(BloodMagic.MODID, "textures/entities/nodebeam.png");
	private static final Minecraft mc = Minecraft.getInstance();

	public RenderItemRoutingNode(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	public void render(TileRoutingNode tileNode, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderer, int combinedLightIn, int combinedOverlayIn)
	{
//		matrixStack.push();
//
//		matrixStack.translate(0.5, 0.5, 0.5);
//
//		float rot = getRotation(craftTime);
//		float secondaryRot = getSecondaryRotation(craftTime);
//
//		float size = 1.0F * getSizeModifier(craftTime);
//		Direction rotation = tileArray.getRotation();
//
//		matrixStack.push();
//		matrixStack.translate(0, getVerticalOffset(craftTime), 0);
//		matrixStack.rotate(new Quaternion(Direction.UP.toVector3f(), -rotation.getHorizontalAngle(), true));
//
//		matrixStack.push();
//
//		matrixStack.rotate(new Quaternion(Direction.UP.toVector3f(), rot, true));
//		matrixStack.rotate(new Quaternion(Direction.NORTH.toVector3f(), secondaryRot, true));
//		matrixStack.rotate(new Quaternion(Direction.EAST.toVector3f(), secondaryRot * 0.45812f, true));
//
//		IVertexBuilder twoDBuffer = renderer.getBuffer(RenderType.getEntityTranslucent(arrayResource));
//		Model2D arrayModel = new BloodMagicRenderer.Model2D();
//		arrayModel.minX = -0.5;
//		arrayModel.maxX = +0.5;
//		arrayModel.minY = -0.5;
//		arrayModel.maxY = +0.5;
//		arrayModel.resource = arrayResource;
//
//		matrixStack.scale(size, size, size);
//
//		RenderResizableQuadrilateral.INSTANCE.renderSquare(arrayModel, matrixStack, twoDBuffer, 0xFFFFFFFF, 0x00F000F0, OverlayTexture.NO_OVERLAY);
//
//		matrixStack.pop();
//		matrixStack.pop();
//		matrixStack.pop();

		if (mc.player.getHeldItemMainhand().getItem() instanceof INodeRenderer || ConfigManager.CLIENT.alwaysRenderRoutingLines.get())
		// ConfigHandler.client.alwaysRenderRoutingLines)
		{

			List<BlockPos> connectionList = tileNode.getConnected();
			BlockPos masterPos = tileNode.getMasterPos();
//			System.out.println("Calling rendering. Length of connections list: " + connectionList.size());
			for (BlockPos wantedPos : connectionList)
			{
				BlockPos offsetPos = wantedPos.subtract(tileNode.getPos());

				// The beam renders towards the east by default.

				int xd = offsetPos.getX();
				int yd = offsetPos.getY();
				int zd = offsetPos.getZ();
				boolean doRender = wantedPos.equals(masterPos) || (xd > 0 || (xd == 0 && zd > 0 || (zd == 0 && yd > 0)));
				if (!doRender)
				{
					continue;
				}

				double distance = Math.sqrt(xd * xd + yd * yd + zd * zd);
				double subLength = MathHelper.sqrt(xd * xd + zd * zd);
				float rotYaw = -((float) (Math.atan2(xd, zd) * 180.0D / Math.PI));
				float rotPitch = ((float) (Math.atan2(yd, subLength) * 180.0D / Math.PI));

//				float scaleFactor = 0.06f;

				matrixStack.push();

				matrixStack.translate(0.5, 0.5, 0.5);
				matrixStack.rotate(new Quaternion(Direction.UP.toVector3f(), -rotYaw, true));
				matrixStack.rotate(new Quaternion(Direction.WEST.toVector3f(), rotPitch - 90, true));
				matrixStack.push();

				long i = tileNode.getWorld().getGameTime();
//			      List<BeaconTileEntity.BeamSegment> list = tileEntityIn.getBeamSegments();
				int j = 0;

				float[] colors = new float[] { 0, 1, 1 };
				// TODO: Re-enable once you've confirmed that all nodes are updated client-side.
//				if (masterPos.equals(BlockPos.ZERO))
//				{
//					colors = new float[] { 1, 1, 1 };
//				}

				float height = (float) distance;

//				ShaderHelper.useShader(ShaderHelper.psiBar, (int) i);
				renderBeamSegment(matrixStack, renderer, partialTicks, i, j, height, colors);
//				ShaderHelper.releaseShader();
//
				matrixStack.pop();
				matrixStack.pop();

			}
		}
	}

	private static void renderBeamSegment(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, float partialTicks, long totalWorldTime, float yOffset, float height, float[] colors)
	{
		renderBeamSegment(matrixStackIn, bufferIn, beamTexture, partialTicks, 1.0F, totalWorldTime, yOffset, height, colors, 0.06F, 0.1F);
	}

	public static void renderBeamSegment(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, ResourceLocation textureLocation, float partialTicks, float textureScale, long totalWorldTime, float yOffset, float height, float[] colors, float beamRadius, float glowRadius)
	{
		float i = yOffset + height;
		matrixStackIn.push();
//		matrixStackIn.translate(0.5D, 0.0D, 0.5D);
		float f = (float) Math.floorMod(totalWorldTime, 40L) + partialTicks;
		float f1 = height < 0 ? f : -f;
		float f2 = MathHelper.frac(f1 * 0.2F - (float) MathHelper.floor(f1 * 0.1F));
		float f3 = colors[0];
		float f4 = colors[1];
		float f5 = colors[2];
		matrixStackIn.push();
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f * 2.25F - 45.0F));
		float f6 = 0.0F;
		float f8 = 0.0F;
		float f9 = -beamRadius;
		float f10 = 0.0F;
		float f11 = 0.0F;
		float f12 = -beamRadius;
		float f13 = 0.0F;
		float f14 = 1.0F;
		float f15 = -1.0F + f2;
		float f16 = (float) height * textureScale * (0.5F / beamRadius) + f15;
		renderPart(matrixStackIn, bufferIn.getBuffer(RenderType.getBeaconBeam(textureLocation, false)), f3, f4, f5, 1.0F, yOffset, i, 0.0F, beamRadius, beamRadius, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15);
		matrixStackIn.pop();
		f6 = -glowRadius;
		float f7 = -glowRadius;
		f8 = -glowRadius;
		f9 = -glowRadius;
		f13 = 0.0F;
		f14 = 1.0F;
		f15 = -1.0F + f2;
		f16 = (float) height * textureScale + f15;
		renderPart(matrixStackIn, bufferIn.getBuffer(RenderType.getBeaconBeam(textureLocation, true)), f3, f4, f5, 0.125F, yOffset, i, f6, f7, glowRadius, f8, f9, glowRadius, glowRadius, glowRadius, 0.0F, 1.0F, f16, f15);
		matrixStackIn.pop();
	}

	private static void renderPart(MatrixStack matrixStackIn, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, float yMin, float yMax, float p_228840_8_, float p_228840_9_, float p_228840_10_, float p_228840_11_, float p_228840_12_, float p_228840_13_, float p_228840_14_, float p_228840_15_, float u1, float u2, float v1, float v2)
	{
		MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
		Matrix4f matrix4f = matrixstack$entry.getMatrix();
		Matrix3f matrix3f = matrixstack$entry.getNormal();
		addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, p_228840_8_, p_228840_9_, p_228840_10_, p_228840_11_, u1, u2, v1, v2);
		addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, p_228840_14_, p_228840_15_, p_228840_12_, p_228840_13_, u1, u2, v1, v2);
		addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, p_228840_10_, p_228840_11_, p_228840_14_, p_228840_15_, u1, u2, v1, v2);
		addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, p_228840_12_, p_228840_13_, p_228840_8_, p_228840_9_, u1, u2, v1, v2);
	}

	private static void addQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, float yMin, float yMax, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2)
	{
		addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMax, x1, z1, u2, v1);
		addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMin, x1, z1, u2, v2);
		addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMin, x2, z2, u1, v2);
		addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMax, x2, z2, u1, v1);
	}

	private static void addVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, float y, float x, float z, float texU, float texV)
	{
		bufferIn.pos(matrixPos, x, (float) y, z).color(red, green, blue, alpha).tex(texU, texV).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrixNormal, 0.0F, 1.0F, 0.0F).endVertex();
	}

	@Override
	public boolean isGlobalRenderer(TileRoutingNode te)
	{
		return true;
	}

//	@Override
//    public boolean shouldRenderOffScreen(TileEntity p_188185_1_) {
//        return true;
//    }
}
