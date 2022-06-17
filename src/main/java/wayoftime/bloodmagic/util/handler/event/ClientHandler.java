package wayoftime.bloodmagic.util.handler.event;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer.Model3D;
import wayoftime.bloodmagic.client.render.RenderResizableCuboid;
import wayoftime.bloodmagic.common.item.ItemRitualDiviner;
import wayoftime.bloodmagic.common.item.ItemRitualReader;
import wayoftime.bloodmagic.common.tile.TileMasterRitualStone;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRitualReaderState;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
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
		mc().getTextureManager().bindForSetup(getResource(path));
	}

	public static void bindAtlas()
	{
		mc().getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);
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
		return mc().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(rl);
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

		blankBloodRune = forName(event.getAtlas(), "blankrune", BLOCKS);
		stoneBrick = event.getAtlas().getSprite(new ResourceLocation("minecraft:block/stonebrick"));
		glowstone = event.getAtlas().getSprite(new ResourceLocation("minecraft:block/glowstone"));
//		bloodStoneBrick = forName(event.getMap(), "BloodStoneBrick", BLOCKS);
		beacon = event.getAtlas().getSprite(new ResourceLocation("minecraft:block/beacon"));
//		crystalCluster = forName(event.getMap(), "ShardCluster", BLOCKS);
	}

	@SubscribeEvent
	public static void render(RenderLevelLastEvent event)
	{
		LocalPlayer player = minecraft.player;
		Level world = player.getCommandSenderWorld();

		if (mrsHoloTile != null)
		{
			if (world.getBlockEntity(mrsHoloTile.getBlockPos()) instanceof TileMasterRitualStone)
			{
				if (mrsHoloDisplay)
				{
					MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
					PoseStack stack = event.getPoseStack();
					renderRitualStones(stack, buffers, mrsHoloTile, event.getPartialTick());
					RenderSystem.disableDepthTest();
					buffers.endBatch();
				} else
					ClientHandler.setRitualHoloToNull();
			} else
			{
				ClientHandler.setRitualHoloToNull();
			}
		}

		if (mrsRangeTile != null)
		{
			if (world.getBlockEntity(mrsRangeTile.getBlockPos()) instanceof TileMasterRitualStone)
			{
				if (mrsRangeDisplay)
				{
					MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
					PoseStack stack = event.getPoseStack();
					renderRangeHologram(stack, buffers, mrsRangeTile, event.getPartialTick());
					RenderSystem.disableDepthTest();
					buffers.endBatch();
				} else
					ClientHandler.setRitualRangeHoloToNull();
			} else
			{
				ClientHandler.setRitualRangeHoloToNull();
			}
		}

		if (minecraft.hitResult == null || minecraft.hitResult.getType() != HitResult.Type.BLOCK)
			return;

		BlockEntity tileEntity = world.getBlockEntity(((BlockHitResult) minecraft.hitResult).getBlockPos());

		if (tileEntity instanceof TileMasterRitualStone && !player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof ItemRitualDiviner)
		{
			MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
			PoseStack stack = event.getPoseStack();
			renderRitualStones(stack, buffers, player, event.getPartialTick());
			RenderSystem.disableDepthTest();
			buffers.endBatch();
		}
	}

	private static TextureAtlasSprite forName(TextureAtlas textureMap, String name, String dir)
	{
		return textureMap.getSprite(new ResourceLocation(BloodMagic.MODID + dir + "/" + name));
	}

	private static void renderRitualStones(PoseStack stack, MultiBufferSource renderer, LocalPlayer player, float partialTicks)
	{
		Camera activerenderinfo = Minecraft.getInstance().gameRenderer.getMainCamera();
		Vec3 eyePos = activerenderinfo.getPosition();
		VertexConsumer buffer = renderer.getBuffer(Sheets.translucentCullBlockSheet());
		Level world = player.getCommandSenderWorld();
		ItemRitualDiviner ritualDiviner = (ItemRitualDiviner) player.getInventory().getSelected().getItem();
		Direction direction = ritualDiviner.getDirection(player.getInventory().getSelected());
		Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(ritualDiviner.getCurrentRitual(player.getInventory().getSelected()));

		if (ritual == null)
			return;

		BlockPos vec3, vX;
		vec3 = ((BlockHitResult) minecraft.hitResult).getBlockPos();

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);
		for (RitualComponent ritualComponent : components)
		{
			stack.pushPose();
			vX = vec3.offset(ritualComponent.getOffset(direction));
			double minX = vX.getX() - eyePos.x;
			double minY = vX.getY() - eyePos.y;
			double minZ = vX.getZ() - eyePos.z;

			stack.translate(minX, minY, minZ);

			if (!world.getBlockState(vX).isSolidRender(world, vX))
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
			stack.popPose();
		}
	}

	public static void renderRitualStones(PoseStack stack, MultiBufferSource renderer, TileMasterRitualStone masterRitualStone, float partialTicks)
	{
		Camera activerenderinfo = Minecraft.getInstance().gameRenderer.getMainCamera();
		Vec3 eyePos = activerenderinfo.getPosition();
		VertexConsumer buffer = renderer.getBuffer(Sheets.translucentCullBlockSheet());
		LocalPlayer player = minecraft.player;
		Level world = player.getCommandSenderWorld();
		Direction direction = mrsHoloDirection;
		Ritual ritual = mrsHoloRitual;

		if (ritual == null)
		{
			return;
		}

		BlockPos vec3, vX;
		vec3 = masterRitualStone.getBlockPos();

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);
		for (RitualComponent ritualComponent : components)
		{
			stack.pushPose();
			vX = vec3.offset(ritualComponent.getOffset(direction));

			double minX = vX.getX() - eyePos.x;
			double minY = vX.getY() - eyePos.y;
			double minZ = vX.getZ() - eyePos.z;

			stack.translate(minX, minY, minZ);

			if (!world.getBlockState(vX).isSolidRender(world, vX))
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

			stack.popPose();
		}
	}

	public static void renderRangeHologram(PoseStack stack, MultiBufferSource renderer, TileMasterRitualStone masterRitualStone, float partialTicks)
	{
		Camera activerenderinfo = Minecraft.getInstance().gameRenderer.getMainCamera();
		Vec3 eyePos = activerenderinfo.getPosition();
		VertexConsumer buffer = renderer.getBuffer(Sheets.translucentCullBlockSheet());
		LocalPlayer player = minecraft.player;
		Level world = player.getCommandSenderWorld();

		if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof ItemRitualReader)
		{
			ItemStack itemStack = player.getMainHandItem();
			EnumRitualReaderState state = ((ItemRitualReader) itemStack.getItem()).getState(itemStack);
			if (state == EnumRitualReaderState.SET_AREA)
			{
				String range = ((ItemRitualReader) itemStack.getItem()).getCurrentBlockRange(itemStack);
				AreaDescriptor descriptor = masterRitualStone.getBlockRange(range);
				if (descriptor == null)
				{
					return;
				}

				stack.pushPose();
				BlockPos vec3;
				vec3 = masterRitualStone.getBlockPos();
				AABB aabb = descriptor.getAABB(vec3);
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
				Model3D model = getBlockModelWithSize(rl, aabb.getXsize() - 2 * sizeOffset, aabb.getYsize() - 2 * sizeOffset, aabb.getZsize() - 2 * sizeOffset);
				RenderResizableCuboid.INSTANCE.renderCube(model, stack, buffer, 0x99FF4444, 0x00F000F0, OverlayTexture.NO_OVERLAY);
				stack.popPose();
			}
		}
	}

	private static Model3D getBlockModel(ResourceLocation rl)
	{
		Model3D model = new BloodMagicRenderer.Model3D();
		model.setTexture(Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(rl));
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
		model.setTexture(Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(rl));
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

	public static void handleGuiTank(PoseStack transform, IFluidTank tank, int x, int y, int w, int h, int oX, int oY, int oW, int oH, int mX, int mY, String originalTexture, List<Component> tooltip)
	{
		handleGuiTank(transform, tank.getFluid(), tank.getCapacity(), x, y, w, h, oX, oY, oW, oH, mX, mY, originalTexture, tooltip);
	}

	public static void handleGuiTank(PoseStack transform, FluidStack fluid, int capacity, int x, int y, int w, int h, int oX, int oY, int oW, int oH, int mX, int mY, String originalTexture, List<Component> tooltip)
	{
		if (tooltip == null)
		{
			transform.pushPose();
			MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
			if (fluid != null && fluid.getFluid() != null)
			{
				int fluidHeight = (int) (h * (fluid.getAmount() / (float) capacity));
				drawRepeatedFluidSpriteGui(buffer, transform, fluid, x, y + h - fluidHeight, w, fluidHeight);
				RenderSystem.setShaderColor(1, 1, 1, 1);
			}
			int xOff = (w - oW) / 2;
			int yOff = (h - oH) / 2;
//			RenderSystem.setShaderTe
			RenderType renderType = RenderType.solid();
			drawTexturedRect(buffer.getBuffer(renderType), transform, x + xOff, y + yOff, oW, oH, 256f, oX, oX + oW, oY, oY + oH);
			buffer.endBatch(renderType);
			transform.popPose();
		} else
		{
			if (mX >= x && mX < x + w && mY >= y && mY < y + h)
				addFluidTooltip(fluid, tooltip, capacity);
		}
	}

	public static void drawRepeatedFluidSpriteGui(MultiBufferSource buffer, PoseStack transform, FluidStack fluid, float x, float y, float w, float h)
	{
//		RenderType renderType = BMRenderTypes.getGui(InventoryMenu.BLOCK_ATLAS);
		RenderType renderType = RenderType.solid();
		VertexConsumer builder = buffer.getBuffer(renderType);
		drawRepeatedFluidSprite(builder, transform, fluid, x, y, w, h);
	}

	public static void drawRepeatedFluidSprite(VertexConsumer builder, PoseStack transform, FluidStack fluid, float x, float y, float w, float h)
	{
		TextureAtlasSprite sprite = getSprite(fluid.getFluid().getAttributes().getStillTexture(fluid));
		int col = fluid.getFluid().getAttributes().getColor(fluid);
		int iW = sprite.getWidth();
		int iH = sprite.getHeight();
		if (iW > 0 && iH > 0)
			drawRepeatedSprite(builder, transform, x, y, w, h, iW, iH, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), (col >> 16 & 255) / 255.0f, (col >> 8 & 255) / 255.0f, (col & 255) / 255.0f, 1);
	}

	public static void drawRepeatedSprite(VertexConsumer builder, PoseStack transform, float x, float y, float w, float h, int iconWidth, int iconHeight, float uMin, float uMax, float vMin, float vMax, float r, float g, float b, float alpha)
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

	public static void drawTexturedRect(VertexConsumer builder, PoseStack transform, float x, float y, float w, float h, float r, float g, float b, float alpha, float u0, float u1, float v0, float v1)
	{
		Matrix4f mat = transform.last().pose();
		builder.vertex(mat, x, y + h, 0).color(r, g, b, alpha).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xf000f0).normal(1, 1, 1).endVertex();
		builder.vertex(mat, x + w, y + h, 0).color(r, g, b, alpha).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(1, 1, 1).endVertex();
		builder.vertex(mat, x + w, y, 0).color(r, g, b, alpha).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(1, 1, 1).endVertex();
		builder.vertex(mat, x, y, 0).color(r, g, b, alpha).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(1, 1, 1).endVertex();
	}

	public static void drawTexturedRect(VertexConsumer builder, PoseStack transform, int x, int y, int w, int h, float picSize, int u0, int u1, int v0, int v1)
	{
		drawTexturedRect(builder, transform, x, y, w, h, 1, 1, 1, 1, u0 / picSize, u1 / picSize, v0 / picSize, v1 / picSize);
	}

	public static void addFluidTooltip(FluidStack fluid, List<Component> tooltip, int tankCapacity)
	{
		if (!fluid.isEmpty())
			tooltip.add(applyFormat(fluid.getDisplayName(), fluid.getFluid().getAttributes().getRarity(fluid).color));
		else
			tooltip.add(new TranslatableComponent("gui.bloodmagic.empty"));
//		if (fluid.getFluid() instanceof IEFluid)
//			((IEFluid) fluid.getFluid()).addTooltipInfo(fluid, null, tooltip);

		if (mc().options.advancedItemTooltips && !fluid.isEmpty())
		{
			if (!Screen.hasShiftDown())
				tooltip.add(new TranslatableComponent("tooltip.bloodmagic.holdShiftForInfo"));
			else
			{
				// TODO translation keys
				tooltip.add(applyFormat(new TextComponent("Fluid Registry: " + fluid.getFluid().getRegistryName()), ChatFormatting.DARK_GRAY));
				tooltip.add(applyFormat(new TextComponent("Density: " + fluid.getFluid().getAttributes().getDensity(fluid)), ChatFormatting.DARK_GRAY));
				tooltip.add(applyFormat(new TextComponent("Temperature: " + fluid.getFluid().getAttributes().getTemperature(fluid)), ChatFormatting.DARK_GRAY));
				tooltip.add(applyFormat(new TextComponent("Viscosity: " + fluid.getFluid().getAttributes().getViscosity(fluid)), ChatFormatting.DARK_GRAY));
				tooltip.add(applyFormat(new TextComponent("NBT Data: " + fluid.getTag()), ChatFormatting.DARK_GRAY));
			}
		}

		if (tankCapacity > 0)
			tooltip.add(applyFormat(new TextComponent(fluid.getAmount() + "/" + tankCapacity + "mB"), ChatFormatting.GRAY));
		else
			tooltip.add(applyFormat(new TextComponent(fluid.getAmount() + "mB"), ChatFormatting.GRAY));
	}

	public static MutableComponent applyFormat(Component component, ChatFormatting... color)
	{
		Style style = component.getStyle();
		for (ChatFormatting format : color) style = style.applyFormat(format);
		return component.copy().setStyle(style);
	}

}
