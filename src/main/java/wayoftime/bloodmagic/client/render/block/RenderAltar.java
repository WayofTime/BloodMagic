package wayoftime.bloodmagic.client.render.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer.Model3D;
import wayoftime.bloodmagic.client.render.RenderResizableCuboid;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.tile.TileAltar;

public class RenderAltar extends TileEntityRenderer<TileAltar>
{
	public RenderAltar(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	private static final float MIN_HEIGHT = 0.499f;
	private static final float MAX_HEIGHT = 0.745f;

	@Override
	public void render(TileAltar tileAltar, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn)
	{
		ItemStack inputStack = tileAltar.getItem(0);

		float level = ((float) tileAltar.getCurrentBlood()) / (float) tileAltar.getCapacity();

		this.renderItem(inputStack, tileAltar, matrixStack, buffer, combinedLightIn, combinedOverlayIn);

		renderFluid(level, matrixStack, buffer, combinedLightIn, combinedOverlayIn);

//		if (tileAltar.getCurrentTierDisplayed() != AltarTier.ONE)
//			renderHologram(tileAltar, tileAltar.getCurrentTierDisplayed(), partialTicks);
	}

	private void renderFluid(float fluidLevel, MatrixStack matrixStack, IRenderTypeBuffer renderer, int combinedLightIn, int combinedOverlayIn)
	{
		Fluid fluid = BloodMagicBlocks.LIFE_ESSENCE_FLUID.get();
		FluidStack fluidStack = new FluidStack(fluid, 1000);

		FluidRenderData data = new FluidRenderData(fluidStack);
		matrixStack.pushPose();

		Model3D model = getFluidModel(fluidLevel, data);
		IVertexBuilder buffer = renderer.getBuffer(Atlases.translucentCullBlockSheet());

//		matrixStack.translate(data.loca, y, z);
//		int glow = data.calculateGlowLight(0);
		RenderResizableCuboid.INSTANCE.renderCube(model, matrixStack, buffer, data.getColorARGB(1), combinedLightIn, combinedOverlayIn);

		matrixStack.popPose();
	}

	private void renderItem(ItemStack stack, TileAltar tileAltar, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn)
	{
		matrixStack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		ItemRenderer itemRenderer = mc.getItemRenderer();
		if (!stack.isEmpty())
		{
			matrixStack.translate(0.5, 1, 0.5);
			matrixStack.pushPose();

			float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

			matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			RenderHelper.turnBackOn();
			IBakedModel ibakedmodel = itemRenderer.getModel(stack, tileAltar.getLevel(), (LivingEntity) null);
			itemRenderer.render(stack, ItemCameraTransforms.TransformType.FIXED, true, matrixStack, buffer, combinedLightIn, combinedOverlayIn, ibakedmodel); // renderItem
			RenderHelper.turnOff();

			matrixStack.popPose();
		}

		matrixStack.popPose();
	}

	private Model3D getFluidModel(float fluidLevel, FluidRenderData data)
	{
		Model3D model = new BloodMagicRenderer.Model3D();
		model.setTexture(data.getTexture());
		model.minX = 0.1;
		model.minY = MIN_HEIGHT;
		model.minZ = 0.1;
		model.maxX = 0.9;
		model.maxY = (MAX_HEIGHT - MIN_HEIGHT) * fluidLevel + MIN_HEIGHT;
		model.maxZ = 0.9;

		return model;
	}

	public class FluidRenderData
	{
		public BlockPos location;

		public int height;
		public int length;
		public int width;

		public final FluidStack fluidType;

		public FluidRenderData(FluidStack fluidType)
		{
			this.fluidType = fluidType;
		}

		public TextureAtlasSprite getTexture()
		{
			return Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(fluidType.getFluid().getAttributes().getStillTexture());
		}

		public boolean isGaseous()
		{
			return fluidType.getFluid().getAttributes().isGaseous(fluidType);
		}

		public int getColorARGB(float scale)
		{
			return fluidType.getFluid().getAttributes().getColor(fluidType);
		}

		public int calculateGlowLight(int light)
		{
			return light;
		}

		@Override
		public int hashCode()
		{
			int code = super.hashCode();
			code = 31 * code + fluidType.getFluid().getRegistryName().hashCode();
			if (fluidType.hasTag())
			{
				code = 31 * code + fluidType.getTag().hashCode();
			}
			return code;
		}

		@Override
		public boolean equals(Object data)
		{
			return super.equals(data) && data instanceof FluidRenderData
					&& fluidType.isFluidEqual(((FluidRenderData) data).fluidType);
		}
	}
//
//	private void renderHologram(TileAltar altar, AltarTier tier, float partialTicks)
//	{
//		EntityPlayerSP player = Minecraft.getMinecraft().player;
//		World world = player.world;
//
//		if (tier == AltarTier.ONE)
//			return;
//
//		GlStateManager.pushMatrix();
//		GlStateManager.enableBlend();
//		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		GlStateManager.color(1F, 1F, 1F, 0.6125F);
//
//		BlockPos vec3, vX;
//		vec3 = altar.getPos();
//		double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
//		double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
//		double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
//
//		for (AltarComponent altarComponent : tier.getAltarComponents())
//		{
//			vX = vec3.add(altarComponent.getOffset());
//			double minX = vX.getX() - posX;
//			double minY = vX.getY() - posY;
//			double minZ = vX.getZ() - posZ;
//
//			if (!world.getBlockState(vX).isOpaqueCube())
//			{
//				TextureAtlasSprite texture = null;
//
//				switch (altarComponent.getComponent())
//				{
//				case BLOODRUNE:
//					texture = ClientHandler.blankBloodRune;
//					break;
//				case NOTAIR:
//					texture = ClientHandler.stoneBrick;
//					break;
//				case GLOWSTONE:
//					texture = ClientHandler.glowstone;
//					break;
//				case BLOODSTONE:
//					texture = ClientHandler.bloodStoneBrick;
//					break;
//				case BEACON:
//					texture = ClientHandler.beacon;
//					break;
//				case CRYSTAL:
//					texture = ClientHandler.crystalCluster;
//					break;
//				}
//
//				RenderFakeBlocks.drawFakeBlock(texture, minX, minY, minZ);
//			}
//		}
//
//		GlStateManager.popMatrix();
//	}

//	private static void setGLColorFromInt(int color)
//	{
//		float red = (color >> 16 & 0xFF) / 255.0F;
//		float green = (color >> 8 & 0xFF) / 255.0F;
//		float blue = (color & 0xFF) / 255.0F;
//
//		GlStateManager.color(red, green, blue, 1.0F);
//	}
}
