package WayofTime.bloodmagic.util.handler.event;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.util.BMLog;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.ritual.RitualRegistry;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.ritual.RitualComponent;
import WayofTime.bloodmagic.client.hud.HUDElement;
import WayofTime.bloodmagic.client.key.KeyBindings;
import WayofTime.bloodmagic.client.render.block.RenderFakeBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.item.ItemRitualDiviner;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.SigilHoldingPacketProcessor;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.GhostItemHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.*;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class ClientHandler {
    // Quick toggle for error suppression. Set to false if you wish to hide model errors.
    public static final boolean SUPPRESS_ASSET_ERRORS = true;
    public static final List<HUDElement> hudElements = new ArrayList<>();
    public static TextureAtlasSprite ritualStoneBlank;
    public static TextureAtlasSprite ritualStoneWater;
    public static TextureAtlasSprite ritualStoneFire;
    public static TextureAtlasSprite ritualStoneEarth;
    public static TextureAtlasSprite ritualStoneAir;
    public static TextureAtlasSprite ritualStoneDawn;
    public static TextureAtlasSprite ritualStoneDusk;
    public static TextureAtlasSprite blankBloodRune;
    public static TextureAtlasSprite stoneBrick;
    public static TextureAtlasSprite glowstone;
    public static TextureAtlasSprite bloodStoneBrick;
    public static TextureAtlasSprite beacon;
    public static TextureAtlasSprite crystalCluster;
    public static Minecraft minecraft = Minecraft.getMinecraft();
    private static TileMasterRitualStone mrsHoloTile;
    private static Ritual mrsHoloRitual;
    private static EnumFacing mrsHoloDirection;
    private static boolean mrsHoloDisplay;

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }

        if (GhostItemHelper.hasGhostAmount(stack)) {
            int amount = GhostItemHelper.getItemGhostAmount(stack);
            if (amount == 0) {
                event.getToolTip().add(TextHelper.localize("tooltip.bloodmagic.ghost.everything"));
            } else {
                event.getToolTip().add(TextHelper.localize("tooltip.bloodmagic.ghost.amount", amount));
            }
        }
    }

    @SubscribeEvent
    public static void onSoundEvent(PlaySoundEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null && player.isPotionActive(RegistrarBloodMagic.DEAFNESS)) {
            event.setResultSound(null);
        }
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        final String BLOCKS = "blocks";

        ritualStoneBlank = forName(event.getMap(), "RitualStone", BLOCKS);
        ritualStoneWater = forName(event.getMap(), "WaterRitualStone", BLOCKS);
        ritualStoneFire = forName(event.getMap(), "FireRitualStone", BLOCKS);
        ritualStoneEarth = forName(event.getMap(), "EarthRitualStone", BLOCKS);
        ritualStoneAir = forName(event.getMap(), "AirRitualStone", BLOCKS);
        ritualStoneDawn = forName(event.getMap(), "LightRitualStone", BLOCKS);
        ritualStoneDusk = forName(event.getMap(), "DuskRitualStone", BLOCKS);

        blankBloodRune = forName(event.getMap(), "BlankRune", BLOCKS);
        stoneBrick = event.getMap().registerSprite(new ResourceLocation("minecraft:blocks/stonebrick"));
        glowstone = event.getMap().registerSprite(new ResourceLocation("minecraft:blocks/glowstone"));
        bloodStoneBrick = forName(event.getMap(), "BloodStoneBrick", BLOCKS);
        beacon = event.getMap().registerSprite(new ResourceLocation("minecraft:blocks/beacon"));
        crystalCluster = forName(event.getMap(), "ShardCluster", BLOCKS);
    }

    @SubscribeEvent
    public static void render(RenderWorldLastEvent event) {
        EntityPlayerSP player = minecraft.player;
        World world = player.getEntityWorld();

        if (mrsHoloTile != null) {
            if (world.getTileEntity(mrsHoloTile.getPos()) instanceof TileMasterRitualStone) {
                if (mrsHoloDisplay)
                    renderRitualStones(mrsHoloTile, event.getPartialTicks());
                else
                    ClientHandler.setRitualHoloToNull();
            } else {
                ClientHandler.setRitualHoloToNull();
            }
        }

        if (minecraft.objectMouseOver == null || minecraft.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK)
            return;

        TileEntity tileEntity = world.getTileEntity(minecraft.objectMouseOver.getBlockPos());

        if (tileEntity instanceof TileMasterRitualStone && !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemRitualDiviner)
            renderRitualStones(player, event.getPartialTicks());
    }

    @SubscribeEvent
    public static void onMouseEvent(MouseEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (event.getDwheel() != 0 && player != null && player.isSneaking()) {
            ItemStack stack = player.getHeldItemMainhand();

            if (!stack.isEmpty()) {
                Item item = stack.getItem();

                if (item instanceof ItemSigilHolding) {
                    cycleSigil(stack, player, event.getDwheel());
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKey(InputEvent event) {
        if (!minecraft.inGameHasFocus)
            return;

        for (KeyBindings keyBinding : KeyBindings.values())
            if (keyBinding.getKey().isPressed())
                keyBinding.handleKeybind();
    }

    @SubscribeEvent
    public static void onHudRender(RenderGameOverlayEvent.Pre event) {
        for (HUDElement element : hudElements)
            if (element.getElementType() == event.getType() && element.shouldRender(minecraft))
                element.render(minecraft, event.getResolution(), event.getPartialTicks());
    }

    // Stolen from Chisel
    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        if (BloodMagic.IS_DEV && SUPPRESS_ASSET_ERRORS)
            return;

        Stopwatch stopwatch = Stopwatch.createStarted();
        Map<ResourceLocation, Exception> modelErrors = ReflectionHelper.getPrivateValue(ModelLoader.class, event.getModelLoader(), "loadingExceptions");
        Set<ModelResourceLocation> missingVariants = ReflectionHelper.getPrivateValue(ModelLoader.class, event.getModelLoader(), "missingVariants");

        // Collect all Blood Magic model errors
        List<ResourceLocation> errored = new ArrayList<>();
        for (ResourceLocation modelError : modelErrors.keySet())
            if (modelError.getResourceDomain().equalsIgnoreCase(BloodMagic.MODID))
                errored.add(modelError);

        // Collect all Blood Magic variant errors
        List<ModelResourceLocation> missing = new ArrayList<>();
        for (ModelResourceLocation missingVariant : missingVariants)
            if (missingVariant.getResourceDomain().equalsIgnoreCase(BloodMagic.MODID))
                missing.add(missingVariant);

        // Remove discovered model errors
        for (ResourceLocation modelError : errored)
            modelErrors.remove(modelError);

        // Remove discovered variant errors
        missingVariants.removeAll(missing);

        if (errored.size() > 0)
            BMLog.DEBUG.info("Suppressed {} model errors from Blood Magic.", errored.size());
        if (missing.size() > 0)
            BMLog.DEBUG.info("Suppressed {} variant errors from Blood Magic.", missing.size());
        BMLog.DEBUG.info("Suppressed discovered model/variant errors in {}", stopwatch.stop());
    }

    // For some reason, we need some bad textures to be listed in the Crystal and Node models. This will hide that from the end user.
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Post event) {
        if (BloodMagic.IS_DEV && SUPPRESS_ASSET_ERRORS)
            return;

        Stopwatch stopwatch = Stopwatch.createStarted();
        SetMultimap<String, ResourceLocation> missingTextures = ReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "missingTextures");
        Set<String> badTextureDomains = ReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "badTextureDomains");

        String mc = "minecraft";
        String format = "textures/%s.png";
        Set<ResourceLocation> toRemove = new HashSet<>();

        // Find our missing textures and mark them for removal. Cannot directly remove as it would cause a CME
        if (missingTextures.containsKey(mc)) {
            Set<ResourceLocation> missingMCTextures = missingTextures.get(mc);
            for (ResourceLocation texture : missingMCTextures)
                if (texture.getResourcePath().equalsIgnoreCase(String.format(format, "node")) || texture.getResourcePath().equalsIgnoreCase(String.format(format, "crystal")))
                    toRemove.add(texture);
        }

        // Remove all our found errors
        missingTextures.get(mc).removeAll(toRemove);

        // Make sure to only remove the bad MC domain if no other textures are missing
        if (missingTextures.get(mc).isEmpty()) {
            missingTextures.keySet().remove(mc);
            badTextureDomains.remove(mc);
        }
        BMLog.DEBUG.info("Suppressed required texture errors in {}", stopwatch.stop());
    }

    private static void renderRitualStones(EntityPlayerSP player, float partialTicks) {
        World world = player.getEntityWorld();
        ItemRitualDiviner ritualDiviner = (ItemRitualDiviner) player.inventory.getCurrentItem().getItem();
        EnumFacing direction = ritualDiviner.getDirection(player.inventory.getCurrentItem());
        Ritual ritual = RitualRegistry.getRitualForId(ritualDiviner.getCurrentRitual(player.inventory.getCurrentItem()));

        if (ritual == null)
            return;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 0.6125F);

        BlockPos vec3, vX;
        vec3 = minecraft.objectMouseOver.getBlockPos();
        double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        List<RitualComponent> components = Lists.newArrayList();
        ritual.gatherComponents(components::add);
        for (RitualComponent ritualComponent : components) {
            vX = vec3.add(ritualComponent.getOffset(direction));
            double minX = vX.getX() - posX;
            double minY = vX.getY() - posY;
            double minZ = vX.getZ() - posZ;

            if (!world.getBlockState(vX).isOpaqueCube()) {
                TextureAtlasSprite texture = null;

                switch (ritualComponent.getRuneType()) {
                    case BLANK:
                        texture = ritualStoneBlank;
                        break;
                    case WATER:
                        texture = ritualStoneWater;
                        break;
                    case FIRE:
                        texture = ritualStoneFire;
                        break;
                    case EARTH:
                        texture = ritualStoneEarth;
                        break;
                    case AIR:
                        texture = ritualStoneAir;
                        break;
                    case DAWN:
                        texture = ritualStoneDawn;
                        break;
                    case DUSK:
                        texture = ritualStoneDusk;
                        break;
                }

                RenderFakeBlocks.drawFakeBlock(texture, minX, minY, minZ);
            }
        }

        GlStateManager.popMatrix();
    }

    public static void cycleSigil(ItemStack stack, EntityPlayer player, int dWheel) {
        int mode = dWheel;
        if (!ConfigHandler.client.sigilHoldingSkipsEmptySlots) {
            mode = ItemSigilHolding.getCurrentItemOrdinal(stack);
            mode = dWheel < 0 ? ItemSigilHolding.next(mode) : ItemSigilHolding.prev(mode);
        }

        ItemSigilHolding.cycleToNextSigil(stack, mode);
        BloodMagicPacketHandler.INSTANCE.sendToServer(new SigilHoldingPacketProcessor(player.inventory.currentItem, mode));
        ItemStack newStack = ItemSigilHolding.getItemStackInSlot(stack, ItemSigilHolding.getCurrentItemOrdinal(stack));
        player.sendStatusMessage(newStack.isEmpty() ? new TextComponentString("") : newStack.getTextComponent(), true);
    }

    private static TextureAtlasSprite forName(TextureMap textureMap, String name, String dir) {
        return textureMap.registerSprite(new ResourceLocation(Constants.Mod.DOMAIN + dir + "/" + name));
    }

    public static void renderRitualStones(TileMasterRitualStone masterRitualStone, float partialTicks) {
        EntityPlayerSP player = minecraft.player;
        World world = player.getEntityWorld();
        EnumFacing direction = mrsHoloDirection;
        Ritual ritual = mrsHoloRitual;

        if (ritual == null)
            return;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 0.5F);

        BlockPos vec3, vX;
        vec3 = masterRitualStone.getPos();
        double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        List<RitualComponent> components = Lists.newArrayList();
        ritual.gatherComponents(components::add);
        for (RitualComponent ritualComponent : components) {
            vX = vec3.add(ritualComponent.getOffset(direction));
            double minX = vX.getX() - posX;
            double minY = vX.getY() - posY;
            double minZ = vX.getZ() - posZ;

            if (!world.getBlockState(vX).isOpaqueCube()) {
                TextureAtlasSprite texture = null;

                switch (ritualComponent.getRuneType()) {
                    case BLANK:
                        texture = ritualStoneBlank;
                        break;
                    case WATER:
                        texture = ritualStoneWater;
                        break;
                    case FIRE:
                        texture = ritualStoneFire;
                        break;
                    case EARTH:
                        texture = ritualStoneEarth;
                        break;
                    case AIR:
                        texture = ritualStoneAir;
                        break;
                    case DAWN:
                        texture = ritualStoneDawn;
                        break;
                    case DUSK:
                        texture = ritualStoneDusk;
                        break;
                }

                RenderFakeBlocks.drawFakeBlock(texture, minX, minY, minZ);
            }
        }

        GlStateManager.popMatrix();
    }

    public static void setRitualHolo(TileMasterRitualStone masterRitualStone, Ritual ritual, EnumFacing direction, boolean displayed) {
        mrsHoloDisplay = displayed;
        mrsHoloTile = masterRitualStone;
        mrsHoloRitual = ritual;
        mrsHoloDirection = direction;
    }

    public static void setRitualHoloToNull() {
        mrsHoloDisplay = false;
        mrsHoloTile = null;
        mrsHoloRitual = null;
        mrsHoloDirection = EnumFacing.NORTH;
    }
}
