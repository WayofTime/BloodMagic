package wayoftime.bloodmagic.util.handler.event;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer.Model3D;
import wayoftime.bloodmagic.client.render.RenderResizableCuboid;
import wayoftime.bloodmagic.client.utils.BMRenderTypes;
import wayoftime.bloodmagic.common.item.ItemRitualDiviner;
import wayoftime.bloodmagic.common.item.ItemRitualReader;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRitualReaderState;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.tile.TileMasterRitualStone;
import wayoftime.bloodmagic.will.DemonWillHolder;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, value = Dist.CLIENT)
//@OnlyIn(Dist.CLIENT)
public class ClientHandler
{
	public static final boolean SUPPRESS_ASSET_ERRORS = true;
	public static ResourceLocation ritualStoneBlank = BloodMagic.rl("block/ritualstone");;
	public static ResourceLocation ritualStoneWater = BloodMagic.rl("block/waterritualstone");;
	public static ResourceLocation ritualStoneFire = BloodMagic.rl("block/fireritualstone");;
	public static ResourceLocation ritualStoneEarth = BloodMagic.rl("block/earthritualstone");;
	public static ResourceLocation ritualStoneAir = BloodMagic.rl("block/airritualstone");;
	public static ResourceLocation ritualStoneDawn = BloodMagic.rl("block/dawnritualstone");;
	public static ResourceLocation ritualStoneDusk = BloodMagic.rl("block/duskritualstone");;
	public static ResourceLocation boarder = new ResourceLocation("block/chiseled_sandstone");
	public static TextureAtlasSprite blankBloodRune;
	public static TextureAtlasSprite stoneBrick;
	public static TextureAtlasSprite glowstone;
//	public static TextureAtlasSprite bloodStoneBrick;
	public static TextureAtlasSprite beacon;
//	public static TextureAtlasSprite crystalCluster;
	public static Minecraft minecraft = Minecraft.getInstance();
	private static TileMasterRitualStone mrsHoloTile;
	private static TileMasterRitualStone mrsRangeTile;
	private static Ritual mrsHoloRitual;
	private static Direction mrsHoloDirection;
	private static boolean mrsHoloDisplay;
	private static boolean mrsRangeDisplay;

	public static DemonWillHolder currentAura;

	static HashMap<String, ResourceLocation> resourceMap = new HashMap<String, ResourceLocation>();

	public static Minecraft mc()
	{
		return Minecraft.getInstance();
	}

	public static void bindTexture(String path)
	{
		mc().getTextureManager().bindTexture(getResource(path));
	}

	public static void bindAtlas()
	{
		mc().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
	}

	public static ResourceLocation getResource(String path)
	{
		ResourceLocation rl = resourceMap.containsKey(path) ? resourceMap.get(path) : new ResourceLocation(path);
		if (!resourceMap.containsKey(path))
			resourceMap.put(path, rl);
		return rl;
	}

	public static TextureAtlasSprite getSprite(ResourceLocation rl)
	{
		return mc().getModelManager().getAtlasTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE).getSprite(rl);
	}

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event)
	{
		final String BLOCKS = "block/";

//		ritualStoneBlank = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(BloodMagic.rl(block//" + "blankrune"));
////		ritualStoneBlank = forName(event.getMap(), "ritualstone", BLOCKS);
//		ritualStoneWater = forName(event.getMap(), "waterritualstone", BLOCKS);
//		ritualStoneFire = forName(event.getMap(), "fireritualstone", BLOCKS);
//		ritualStoneEarth = forName(event.getMap(), "earthritualstone", BLOCKS);
//		ritualStoneAir = forName(event.getMap(), "airritualstone", BLOCKS);
//		ritualStoneDawn = forName(event.getMap(), "lightritualstone", BLOCKS);
//		ritualStoneDusk = forName(event.getMap(), "duskritualstone", BLOCKS);

		blankBloodRune = forName(event.getMap(), "blankrune", BLOCKS);
		stoneBrick = event.getMap().getSprite(new ResourceLocation("minecraft:block/stonebrick"));
		glowstone = event.getMap().getSprite(new ResourceLocation("minecraft:block/glowstone"));
//		bloodStoneBrick = forName(event.getMap(), "BloodStoneBrick", BLOCKS);
		beacon = event.getMap().getSprite(new ResourceLocation("minecraft:block/beacon"));
//		crystalCluster = forName(event.getMap(), "ShardCluster", BLOCKS);
	}

	@SubscribeEvent
	public static void render(RenderWorldLastEvent event)
	{
		ClientPlayerEntity player = minecraft.player;
		World world = player.getEntityWorld();

		if (mrsHoloTile != null)
		{
			if (world.getTileEntity(mrsHoloTile.getPos()) instanceof TileMasterRitualStone)
			{
				if (mrsHoloDisplay)
				{
					IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
					MatrixStack stack = event.getMatrixStack();
					renderRitualStones(stack, buffers, mrsHoloTile, event.getPartialTicks());
					RenderSystem.disableDepthTest();
					buffers.finish();
				} else
					ClientHandler.setRitualHoloToNull();
			} else
			{
				ClientHandler.setRitualHoloToNull();
			}
		}

		if (mrsRangeTile != null)
		{
			if (world.getTileEntity(mrsRangeTile.getPos()) instanceof TileMasterRitualStone)
			{
				if (mrsRangeDisplay)
				{
					IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
					MatrixStack stack = event.getMatrixStack();
					renderRangeHologram(stack, buffers, mrsRangeTile, event.getPartialTicks());
					RenderSystem.disableDepthTest();
					buffers.finish();
				} else
					ClientHandler.setRitualRangeHoloToNull();
			} else
			{
				ClientHandler.setRitualRangeHoloToNull();
			}
		}

		if (minecraft.objectMouseOver == null || minecraft.objectMouseOver.getType() != RayTraceResult.Type.BLOCK)
			return;

		TileEntity tileEntity = world.getTileEntity(((BlockRayTraceResult) minecraft.objectMouseOver).getPos());

		if (tileEntity instanceof TileMasterRitualStone && !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemRitualDiviner)
		{
			IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
			MatrixStack stack = event.getMatrixStack();
			renderRitualStones(stack, buffers, player, event.getPartialTicks());
			RenderSystem.disableDepthTest();
			buffers.finish();
		}
	}

	private static TextureAtlasSprite forName(AtlasTexture textureMap, String name, String dir)
	{
		return textureMap.getSprite(new ResourceLocation(BloodMagic.MODID + dir + "/" + name));
	}

	private static void renderRitualStones(MatrixStack stack, IRenderTypeBuffer renderer, ClientPlayerEntity player, float partialTicks)
	{
		ActiveRenderInfo activerenderinfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
		Vector3d eyePos = activerenderinfo.getProjectedView();
		IVertexBuilder buffer = renderer.getBuffer(Atlases.getTranslucentCullBlockType());
		World world = player.getEntityWorld();
		ItemRitualDiviner ritualDiviner = (ItemRitualDiviner) player.inventory.getCurrentItem().getItem();
		Direction direction = ritualDiviner.getDirection(player.inventory.getCurrentItem());
		Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(ritualDiviner.getCurrentRitual(player.inventory.getCurrentItem()));

		if (ritual == null)
			return;

		BlockPos vec3, vX;
		vec3 = ((BlockRayTraceResult) minecraft.objectMouseOver).getPos();

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);
		for (RitualComponent ritualComponent : components)
		{
			stack.push();
			vX = vec3.add(ritualComponent.getOffset(direction));
			double minX = vX.getX() - eyePos.x;
			double minY = vX.getY() - eyePos.y;
			double minZ = vX.getZ() - eyePos.z;

			stack.translate(minX, minY, minZ);

			if (!world.getBlockState(vX).isOpaqueCube(world, vX))
			{
				ResourceLocation rl = null;

				switch (ritualComponent.getRuneType())
				{
				case BLANK:
					rl = ritualStoneBlank;
					break;
				case WATER:
					rl = ritualStoneWater;
					break;
				case FIRE:
					rl = ritualStoneFire;
					break;
				case EARTH:
					rl = ritualStoneEarth;
					break;
				case AIR:
					rl = ritualStoneAir;
					break;
				case DAWN:
					rl = ritualStoneDawn;
					break;
				case DUSK:
					rl = ritualStoneDusk;
					break;
				}

				Model3D model = getBlockModel(rl);

				RenderResizableCuboid.INSTANCE.renderCube(model, stack, buffer, 0xDDFFFFFF, 0x00F000F0, OverlayTexture.NO_OVERLAY);
			}
			stack.pop();
		}
	}

	public static void renderRitualStones(MatrixStack stack, IRenderTypeBuffer renderer, TileMasterRitualStone masterRitualStone, float partialTicks)
	{
		ActiveRenderInfo activerenderinfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
		Vector3d eyePos = activerenderinfo.getProjectedView();
		IVertexBuilder buffer = renderer.getBuffer(Atlases.getTranslucentCullBlockType());
		ClientPlayerEntity player = minecraft.player;
		World world = player.getEntityWorld();
		Direction direction = mrsHoloDirection;
		Ritual ritual = mrsHoloRitual;

		if (ritual == null)
		{
			return;
		}

		BlockPos vec3, vX;
		vec3 = masterRitualStone.getPos();

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);
		for (RitualComponent ritualComponent : components)
		{
			stack.push();
			vX = vec3.add(ritualComponent.getOffset(direction));

			double minX = vX.getX() - eyePos.x;
			double minY = vX.getY() - eyePos.y;
			double minZ = vX.getZ() - eyePos.z;

			stack.translate(minX, minY, minZ);

			if (!world.getBlockState(vX).isOpaqueCube(world, vX))
			{
				ResourceLocation rl = null;

				switch (ritualComponent.getRuneType())
				{
				case BLANK:
					rl = ritualStoneBlank;
					break;
				case WATER:
					rl = ritualStoneWater;
					break;
				case FIRE:
					rl = ritualStoneFire;
					break;
				case EARTH:
					rl = ritualStoneEarth;
					break;
				case AIR:
					rl = ritualStoneAir;
					break;
				case DAWN:
					rl = ritualStoneDawn;
					break;
				case DUSK:
					rl = ritualStoneDusk;
					break;
				}

				Model3D model = getBlockModel(rl);

				RenderResizableCuboid.INSTANCE.renderCube(model, stack, buffer, 0xDDFFFFFF, 0x00F000F0, OverlayTexture.NO_OVERLAY);
			}

			stack.pop();
		}
	}

	public static void renderRangeHologram(MatrixStack stack, IRenderTypeBuffer renderer, TileMasterRitualStone masterRitualStone, float partialTicks)
	{
		ActiveRenderInfo activerenderinfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
		Vector3d eyePos = activerenderinfo.getProjectedView();
		IVertexBuilder buffer = renderer.getBuffer(Atlases.getTranslucentCullBlockType());
		ClientPlayerEntity player = minecraft.player;
		World world = player.getEntityWorld();

		if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemRitualReader)
		{
			ItemStack itemStack = player.getHeldItemMainhand();
			EnumRitualReaderState state = ((ItemRitualReader) itemStack.getItem()).getState(itemStack);
			if (state == EnumRitualReaderState.SET_AREA)
			{
				String range = ((ItemRitualReader) itemStack.getItem()).getCurrentBlockRange(itemStack);
				AreaDescriptor descriptor = masterRitualStone.getBlockRange(range);
				if (descriptor == null)
				{
					return;
				}

				stack.push();
				BlockPos vec3;
				vec3 = masterRitualStone.getPos();
				AxisAlignedBB aabb = descriptor.getAABB(vec3);
				double sizeOffset = -1d / 16d;
				if (aabb.contains(eyePos))
				{
					sizeOffset *= -1;
				}

				double minX = aabb.minX - eyePos.x + sizeOffset;
				double minY = aabb.minY - eyePos.y + sizeOffset;
				double minZ = aabb.minZ - eyePos.z + sizeOffset;

				stack.translate(minX, minY, minZ);

				ResourceLocation rl = boarder;
				Model3D model = getBlockModelWithSize(rl, aabb.getXSize() - 2 * sizeOffset, aabb.getYSize() - 2 * sizeOffset, aabb.getZSize() - 2 * sizeOffset);
				RenderResizableCuboid.INSTANCE.renderCube(model, stack, buffer, 0x99FF4444, 0x00F000F0, OverlayTexture.NO_OVERLAY);
				stack.pop();
			}
		}
	}

	private static Model3D getBlockModel(ResourceLocation rl)
	{
		Model3D model = new BloodMagicRenderer.Model3D();
		model.setTexture(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(rl));
		model.minX = 0;
		model.minY = 0;
		model.minZ = 0;
		model.maxX = 1;
		model.maxY = 1;
		model.maxZ = 1;

		return model;
	}

	private static Model3D getBlockModelWithSize(ResourceLocation rl, double maxX, double maxY, double maxZ)
	{
		Model3D model = new BloodMagicRenderer.Model3D();
		model.setTexture(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(rl));
		model.minX = 0;
		model.minY = 0;
		model.minZ = 0;
		model.maxX = maxX;
		model.maxY = maxY;
		model.maxZ = maxZ;

		return model;
	}

	public static void setRitualHolo(TileMasterRitualStone masterRitualStone, Ritual ritual, Direction direction, boolean displayed)
	{
		mrsHoloDisplay = displayed;
		mrsHoloTile = masterRitualStone;
		mrsHoloRitual = ritual;
		mrsHoloDirection = direction;
	}

	public static void setRitualHoloToNull()
	{
		mrsHoloDisplay = false;
		mrsHoloTile = null;
		mrsHoloRitual = null;
		mrsHoloDirection = Direction.NORTH;
	}

	public static void setRitualRangeHolo(TileMasterRitualStone masterRitualStone, boolean displayed)
	{
		mrsRangeDisplay = displayed;
		mrsRangeTile = masterRitualStone;
	}

	public static void setRitualRangeHoloToNull()
	{
		mrsRangeDisplay = false;
		mrsRangeTile = null;
	}

	public static void handleGuiTank(MatrixStack transform, IFluidTank tank, int x, int y, int w, int h, int oX, int oY, int oW, int oH, int mX, int mY, String originalTexture, List<ITextComponent> tooltip)
	{
		handleGuiTank(transform, tank.getFluid(), tank.getCapacity(), x, y, w, h, oX, oY, oW, oH, mX, mY, originalTexture, tooltip);
	}

	public static void handleGuiTank(MatrixStack transform, FluidStack fluid, int capacity, int x, int y, int w, int h, int oX, int oY, int oW, int oH, int mX, int mY, String originalTexture, List<ITextComponent> tooltip)
	{
		if (tooltip == null)
		{
			transform.push();
			IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			if (fluid != null && fluid.getFluid() != null)
			{
				int fluidHeight = (int) (h * (fluid.getAmount() / (float) capacity));
				drawRepeatedFluidSpriteGui(buffer, transform, fluid, x, y + h - fluidHeight, w, fluidHeight);
				RenderSystem.color3f(1, 1, 1);
			}
			int xOff = (w - oW) / 2;
			int yOff = (h - oH) / 2;
			RenderType renderType = BMRenderTypes.getGui(new ResourceLocation(originalTexture));
			drawTexturedRect(buffer.getBuffer(renderType), transform, x + xOff, y + yOff, oW, oH, 256f, oX, oX + oW, oY, oY + oH);
			buffer.finish(renderType);
			transform.pop();
		} else
		{
			if (mX >= x && mX < x + w && mY >= y && mY < y + h)
				addFluidTooltip(fluid, tooltip, capacity);
		}
	}

	public static void drawRepeatedFluidSpriteGui(IRenderTypeBuffer buffer, MatrixStack transform, FluidStack fluid, float x, float y, float w, float h)
	{
		RenderType renderType = BMRenderTypes.getGui(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
		IVertexBuilder builder = buffer.getBuffer(renderType);
		drawRepeatedFluidSprite(builder, transform, fluid, x, y, w, h);
	}

	public static void drawRepeatedFluidSprite(IVertexBuilder builder, MatrixStack transform, FluidStack fluid, float x, float y, float w, float h)
	{
		TextureAtlasSprite sprite = getSprite(fluid.getFluid().getAttributes().getStillTexture(fluid));
		int col = fluid.getFluid().getAttributes().getColor(fluid);
		int iW = sprite.getWidth();
		int iH = sprite.getHeight();
		if (iW > 0 && iH > 0)
			drawRepeatedSprite(builder, transform, x, y, w, h, iW, iH, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV(), (col >> 16 & 255) / 255.0f, (col >> 8 & 255) / 255.0f, (col & 255) / 255.0f, 1);
	}

	public static void drawRepeatedSprite(IVertexBuilder builder, MatrixStack transform, float x, float y, float w, float h, int iconWidth, int iconHeight, float uMin, float uMax, float vMin, float vMax, float r, float g, float b, float alpha)
	{
		int iterMaxW = (int) (w / iconWidth);
		int iterMaxH = (int) (h / iconHeight);
		float leftoverW = w % iconWidth;
		float leftoverH = h % iconHeight;
		float leftoverWf = leftoverW / (float) iconWidth;
		float leftoverHf = leftoverH / (float) iconHeight;
		float iconUDif = uMax - uMin;
		float iconVDif = vMax - vMin;
		for (int ww = 0; ww < iterMaxW; ww++)
		{
			for (int hh = 0; hh < iterMaxH; hh++)
				drawTexturedRect(builder, transform, x + ww * iconWidth, y + hh * iconHeight, iconWidth, iconHeight, r, g, b, alpha, uMin, uMax, vMin, vMax);
			drawTexturedRect(builder, transform, x + ww * iconWidth, y + iterMaxH * iconHeight, iconWidth, leftoverH, r, g, b, alpha, uMin, uMax, vMin, (vMin + iconVDif * leftoverHf));
		}
		if (leftoverW > 0)
		{
			for (int hh = 0; hh < iterMaxH; hh++)
				drawTexturedRect(builder, transform, x + iterMaxW * iconWidth, y + hh * iconHeight, leftoverW, iconHeight, r, g, b, alpha, uMin, (uMin + iconUDif * leftoverWf), vMin, vMax);
			drawTexturedRect(builder, transform, x + iterMaxW * iconWidth, y + iterMaxH * iconHeight, leftoverW, leftoverH, r, g, b, alpha, uMin, (uMin + iconUDif * leftoverWf), vMin, (vMin + iconVDif * leftoverHf));
		}
	}

	public static void drawTexturedRect(IVertexBuilder builder, MatrixStack transform, float x, float y, float w, float h, float r, float g, float b, float alpha, float u0, float u1, float v0, float v1)
	{
		Matrix4f mat = transform.getLast().getMatrix();
		builder.pos(mat, x, y + h, 0).color(r, g, b, alpha).tex(u0, v1).overlay(OverlayTexture.NO_OVERLAY).lightmap(0xf000f0).normal(1, 1, 1).endVertex();
		builder.pos(mat, x + w, y + h, 0).color(r, g, b, alpha).tex(u1, v1).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(1, 1, 1).endVertex();
		builder.pos(mat, x + w, y, 0).color(r, g, b, alpha).tex(u1, v0).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(1, 1, 1).endVertex();
		builder.pos(mat, x, y, 0).color(r, g, b, alpha).tex(u0, v0).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(1, 1, 1).endVertex();
	}

	public static void drawTexturedRect(IVertexBuilder builder, MatrixStack transform, int x, int y, int w, int h, float picSize, int u0, int u1, int v0, int v1)
	{
		drawTexturedRect(builder, transform, x, y, w, h, 1, 1, 1, 1, u0 / picSize, u1 / picSize, v0 / picSize, v1 / picSize);
	}

	public static void addFluidTooltip(FluidStack fluid, List<ITextComponent> tooltip, int tankCapacity)
	{
		if (!fluid.isEmpty())
			tooltip.add(applyFormat(fluid.getDisplayName(), fluid.getFluid().getAttributes().getRarity(fluid).color));
		else
			tooltip.add(new TranslationTextComponent("gui.bloodmagic.empty"));
//		if (fluid.getFluid() instanceof IEFluid)
//			((IEFluid) fluid.getFluid()).addTooltipInfo(fluid, null, tooltip);

		if (mc().gameSettings.advancedItemTooltips && !fluid.isEmpty())
		{
			if (!Screen.hasShiftDown())
				tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.holdShiftForInfo"));
			else
			{
				// TODO translation keys
				tooltip.add(applyFormat(new StringTextComponent("Fluid Registry: " + fluid.getFluid().getRegistryName()), TextFormatting.DARK_GRAY));
				tooltip.add(applyFormat(new StringTextComponent("Density: " + fluid.getFluid().getAttributes().getDensity(fluid)), TextFormatting.DARK_GRAY));
				tooltip.add(applyFormat(new StringTextComponent("Temperature: " + fluid.getFluid().getAttributes().getTemperature(fluid)), TextFormatting.DARK_GRAY));
				tooltip.add(applyFormat(new StringTextComponent("Viscosity: " + fluid.getFluid().getAttributes().getViscosity(fluid)), TextFormatting.DARK_GRAY));
				tooltip.add(applyFormat(new StringTextComponent("NBT Data: " + fluid.getTag()), TextFormatting.DARK_GRAY));
			}
		}

		if (tankCapacity > 0)
			tooltip.add(applyFormat(new StringTextComponent(fluid.getAmount() + "/" + tankCapacity + "mB"), TextFormatting.GRAY));
		else
			tooltip.add(applyFormat(new StringTextComponent(fluid.getAmount() + "mB"), TextFormatting.GRAY));
	}

	public static IFormattableTextComponent applyFormat(ITextComponent component, TextFormatting... color)
	{
		Style style = component.getStyle();
		for (TextFormatting format : color) style = style.applyFormatting(format);
		return component.deepCopy().setStyle(style);
	}

}
