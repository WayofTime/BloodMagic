package wayoftime.bloodmagic.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer.Model3D;
import wayoftime.bloodmagic.client.render.RenderResizableCuboid;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.tile.TileAltar;

//public class BeaconRenderer implements BlockEntityRenderer<BeaconBlockEntity> {
//	   public static final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");
//	   public static final int MAX_RENDER_Y = 1024;
//
//	   public BeaconRenderer(BlockEntityRendererProvider.Context p_173529_) {

public class RenderAltar implements BlockEntityRenderer<TileAltar>
{
	public RenderAltar(BlockEntityRendererProvider.Context context)
	{
//		super(rendererDispatcherIn);
	}

	private static final float MIN_HEIGHT = 0.499f;
	private static final float MAX_HEIGHT = 0.745f;

	@Override
	public void render(TileAltar tileAltar, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn)
	{
		ItemStack inputStack = tileAltar.getItem(0);

		float level = ((float) tileAltar.getCurrentBlood()) / (float) tileAltar.getCapacity();

		this.renderItem(inputStack, tileAltar, matrixStack, buffer, combinedLightIn, combinedOverlayIn);

		renderFluid(level, matrixStack, buffer, combinedLightIn, combinedOverlayIn);

//		if (tileAltar.getCurrentTierDisplayed() != AltarTier.ONE)
//			renderHologram(tileAltar, tileAltar.getCurrentTierDisplayed(), partialTicks);
	}

	private void renderFluid(float fluidLevel, PoseStack matrixStack, MultiBufferSource renderer, int combinedLightIn, int combinedOverlayIn)
	{
		Fluid fluid = BloodMagicFluids.LIFE_ESSENCE_FLUID.get();
		FluidStack fluidStack = new FluidStack(fluid, 1000);

		FluidRenderData data = new FluidRenderData(fluidStack);
		matrixStack.pushPose();

		Model3D model = getFluidModel(fluidLevel, data);
		VertexConsumer buffer = renderer.getBuffer(Sheets.translucentCullBlockSheet());

//		matrixStack.translate(data.loca, y, z);
//		int glow = data.calculateGlowLight(0);
		RenderResizableCuboid.INSTANCE.renderCube(model, matrixStack, buffer, data.getColorARGB(1), combinedLightIn, combinedOverlayIn);

		matrixStack.popPose();
	}

	private void renderItem(ItemStack stack, TileAltar tileAltar, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn)
	{
		matrixStack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		ItemRenderer itemRenderer = mc.getItemRenderer();
		if (!stack.isEmpty())
		{
			matrixStack.translate(0.5, 1, 0.5);
			matrixStack.pushPose();

			float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

			matrixStack.mulPose(Axis.YP.rotationDegrees(rotation));
			matrixStack.scale(0.5F, 0.5F, 0.5F);
//			Lighting.turnBackOn();
			BakedModel ibakedmodel = itemRenderer.getModel(stack, tileAltar.getLevel(), (LivingEntity) null, 1);
			itemRenderer.render(stack, ItemDisplayContext.FIXED, true, matrixStack, buffer, combinedLightIn, combinedOverlayIn, ibakedmodel); // renderItem

//			int k = this.getLightVal(p_115076_, 15728880, p_115081_);
//            p_115079_.scale(0.5F, 0.5F, 0.5F);
//            this.itemRenderer.renderStatic(itemstack, ItemTransforms.TransformType.FIXED, k, OverlayTexture.NO_OVERLAY, p_115079_, p_115080_, p_115076_.getId());

//			Lighting.turnOff();

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
			return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(IClientFluidTypeExtensions.of(fluidType.getFluid()).getStillTexture());
		}

		public boolean isGaseous()
		{
			// TODO: FIX GASES - Gases don't exist in fluidtypes
			return fluidType.getFluid().getFluidType().isLighterThanAir();
		}

		public int getColorARGB(float scale)
		{
			return IClientFluidTypeExtensions.of(fluidType.getFluid()).getTintColor(fluidType);
		}

		public int calculateGlowLight(int light)
		{
			return light;
		}

		@Override
		public int hashCode()
		{
			int code = super.hashCode();
			code = 31 * code + ForgeRegistries.FLUIDS.getKey(fluidType.getFluid()).hashCode();
			if (fluidType.hasTag())
			{
				code = 31 * code + fluidType.getTag().hashCode();
			}
			return code;
		}

		@Override
		public boolean equals(Object data)
		{
			return super.equals(data) && data instanceof FluidRenderData && fluidType.isFluidEqual(((FluidRenderData) data).fluidType);
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
