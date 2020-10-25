package wayoftime.bloodmagic.util.handler.event;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer;
import wayoftime.bloodmagic.client.render.BloodMagicRenderer.Model3D;
import wayoftime.bloodmagic.client.render.RenderResizableCuboid;
import wayoftime.bloodmagic.common.item.ItemRitualDiviner;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.tile.TileMasterRitualStone;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ClientHandler
{
	public static final boolean SUPPRESS_ASSET_ERRORS = true;
	public static ResourceLocation ritualStoneBlank = BloodMagic.rl("block/ritualstone");;
	public static ResourceLocation ritualStoneWater = BloodMagic.rl("block/waterritualstone");;
	public static ResourceLocation ritualStoneFire = BloodMagic.rl("block/fireritualstone");;
	public static ResourceLocation ritualStoneEarth = BloodMagic.rl("block/earthritualstone");;
	public static ResourceLocation ritualStoneAir = BloodMagic.rl("block/airritualstone");;
	public static ResourceLocation ritualStoneDawn = BloodMagic.rl("block/dawnritualstone");;
	public static ResourceLocation ritualStoneDusk = BloodMagic.rl("block/lightritualstone");;
	public static TextureAtlasSprite blankBloodRune;
	public static TextureAtlasSprite stoneBrick;
	public static TextureAtlasSprite glowstone;
//	public static TextureAtlasSprite bloodStoneBrick;
	public static TextureAtlasSprite beacon;
//	public static TextureAtlasSprite crystalCluster;
	public static Minecraft minecraft = Minecraft.getInstance();
	private static TileMasterRitualStone mrsHoloTile;
	private static Ritual mrsHoloRitual;
	private static Direction mrsHoloDirection;
	private static boolean mrsHoloDisplay;

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

		if (minecraft.objectMouseOver == null || minecraft.objectMouseOver.getType() != RayTraceResult.Type.BLOCK)
			return;

		TileEntity tileEntity = world.getTileEntity(((BlockRayTraceResult) minecraft.objectMouseOver).getPos());

		if (tileEntity instanceof TileMasterRitualStone && !player.getHeldItemMainhand().isEmpty()
				&& player.getHeldItemMainhand().getItem() instanceof ItemRitualDiviner)
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
		IVertexBuilder buffer = renderer.getBuffer(Atlases.getTranslucentCullBlockType());
		World world = player.getEntityWorld();
		ItemRitualDiviner ritualDiviner = (ItemRitualDiviner) player.inventory.getCurrentItem().getItem();
		Direction direction = ritualDiviner.getDirection(player.inventory.getCurrentItem());
		Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(ritualDiviner.getCurrentRitual(player.inventory.getCurrentItem()));

		if (ritual == null)
			return;

		BlockPos vec3, vX;
		vec3 = ((BlockRayTraceResult) minecraft.objectMouseOver).getPos();
		double posX = player.lastTickPosX + (player.getPosX() - player.lastTickPosX) * partialTicks;
		double posY = player.lastTickPosY + (player.getPosY() - player.lastTickPosY) * partialTicks;
		double posZ = player.lastTickPosZ + (player.getPosZ() - player.lastTickPosZ) * partialTicks;

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);
		for (RitualComponent ritualComponent : components)
		{
			stack.push();
			vX = vec3.add(ritualComponent.getOffset(direction));
			Vector3d eyePos = player.getEyePosition(partialTicks);
			double minX = vX.getX() - eyePos.x;
			double minY = vX.getY() - eyePos.y;
			double minZ = vX.getZ() - eyePos.z;
//			double minX = vX.getX() - posX;
//			double minY = vX.getY() - posY;
//			double minZ = vX.getZ() - posZ;

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
		double posX = player.lastTickPosX + (player.getPosX() - player.lastTickPosX) * partialTicks;
		double posY = player.lastTickPosY + (player.getPosY() - player.lastTickPosY) * partialTicks;
		double posZ = player.lastTickPosZ + (player.getPosZ() - player.lastTickPosZ) * partialTicks;

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);
		for (RitualComponent ritualComponent : components)
		{
			stack.push();
			vX = vec3.add(ritualComponent.getOffset(direction));
			Vector3d eyePos = player.getEyePosition(partialTicks);
			double minX = vX.getX() - eyePos.x;
			double minY = vX.getY() - eyePos.y;
			double minZ = vX.getZ() - eyePos.z;

//			double minX = vX.getX() - posX;
//			double minY = vX.getY() - posY;
//			double minZ = vX.getZ() - posZ;

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

//				RenderFakeBlocks.drawFakeBlock(texture, minX, minY, minZ);
			}

			stack.pop();
		}

//		GlStateManager.popMatrix();
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
}
