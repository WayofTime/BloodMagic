package WayofTime.bloodmagic.util.handler.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.annot.Handler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.client.hud.HUDElement;
import WayofTime.bloodmagic.client.render.RenderFakeBlocks;
import WayofTime.bloodmagic.item.ItemRitualDiviner;
import WayofTime.bloodmagic.item.ItemRitualReader;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.SigilHoldingPacketProcessor;
import WayofTime.bloodmagic.registry.ModPotions;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.GhostItemHelper;
import WayofTime.bloodmagic.util.handler.BMKeyBinding;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.base.Stopwatch;
import com.google.common.collect.SetMultimap;

@Handler
@SideOnly(Side.CLIENT)
public class ClientHandler
{
    // Quick toggle for error suppression. Set to false if you wish to hide model errors.
    public static final boolean SUPPRESS_ASSET_ERRORS = true;

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
    public static final List<BMKeyBinding> keyBindings = new ArrayList<BMKeyBinding>();
    public static final List<HUDElement> hudElements = new ArrayList<HUDElement>();

    private static TileMasterRitualStone mrsHoloTile;
    private static Ritual mrsHoloRitual;
    private static EnumFacing mrsHoloDirection;
    private static boolean mrsHoloDisplay;

    boolean doCrystalRenderTest = false;
    public static ResourceLocation crystalResource = new ResourceLocation(Constants.Mod.DOMAIN + "textures/entities/defaultCrystalLayer.png");

    // Contrary to what your IDE tells you, this *is* actually needed.
    public static final BMKeyBinding keyOpenSigilHolding = new BMKeyBinding("openSigilHolding", Keyboard.KEY_H, BMKeyBinding.Key.OPEN_SIGIL_HOLDING);

    @SubscribeEvent
    public void onTooltipEvent(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        if (stack == null)
        {
            return;
        }

        if (GhostItemHelper.hasGhostAmount(stack))
        {
            int amount = GhostItemHelper.getItemGhostAmount(stack);
            if (amount == 0)
            {
                event.getToolTip().add(TextHelper.localize("tooltip.BloodMagic.ghost.everything"));
            } else
            {
                event.getToolTip().add(TextHelper.localize("tooltip.BloodMagic.ghost.amount", amount));
            }
        }
    }

    public static int stateMachine = 0;

    @SubscribeEvent
    public void onLivingRenderEvent(RenderLivingEvent.Post<EntityLivingBase> event)
    {
        if (doCrystalRenderTest)
            blarg(crystalResource, event.getRenderer(), event.getEntity(), event.getX(), event.getY(), event.getZ(), event.getEntity().rotationYaw, Minecraft.getMinecraft().getRenderPartialTicks());
    }

    //TODO: START

    public void blarg(ResourceLocation resource, RenderLivingBase<EntityLivingBase> renderer, EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
//        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<T>(entity, this, x, y, z))) return;
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        renderer.getMainModel().swingProgress = entity.getSwingProgress(partialTicks);
        boolean shouldSit = entity.isRiding() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
        renderer.getMainModel().isRiding = shouldSit;
        renderer.getMainModel().isChild = entity.isChild();

        try
        {
            float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
            float f2 = f1 - f;

            if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase)
            {
                EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
                f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                f2 = f1 - f;
                float f3 = MathHelper.wrapDegrees(f2);

                if (f3 < -85.0F)
                {
                    f3 = -85.0F;
                }

                if (f3 >= 85.0F)
                {
                    f3 = 85.0F;
                }

                f = f1 - f3;

                if (f3 * f3 > 2500.0F)
                {
                    f += f3 * 0.2F;
                }
            }

            float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            this.renderLivingAt(entity, x, y, z);
            float f8 = this.handleRotationFloat(entity, partialTicks);
            this.rotateCorpse(entity, f8, f, partialTicks);
            float f4 = this.prepareScale(entity, partialTicks);
            float f5 = 0.0F;
            float f6 = 0.0F;

            if (!entity.isRiding())
            {
                f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

                if (entity.isChild())
                {
                    f6 *= 3.0F;
                }

                if (f5 > 1.0F)
                {
                    f5 = 1.0F;
                }
            }

            GlStateManager.enableAlpha();
            renderer.getMainModel().setLivingAnimations(entity, f6, f5, partialTicks);
            renderer.getMainModel().setRotationAngles(f6, f5, f8, f2, f7, f4, entity);

//            if (this.renderOutlines)
//            {
//                boolean flag1 = this.setScoreTeamColor(entity);
//                GlStateManager.enableColorMaterial();
//                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
//
//                if (!this.renderMarker)
//                {
//                    this.renderModel(resource, renderer, entity, f6, f5, f8, f2, f7, f4);
//                }
//
////                if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator())
////                {
////                    this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
////                }
//
//                GlStateManager.disableOutlineMode();
//                GlStateManager.disableColorMaterial();
//
////                if (flag1)
////                {
////                    this.unsetScoreTeamColor();
////                }
//            } else
            {
//                boolean flag = this.setDoRenderBrightness(entity, partialTicks);
                this.renderModel(resource, renderer, entity, f6, f5, f8, f2, f7, f4);

//                if (flag)
//                {
//                    renderer.unsetBrightness();
//                }

                GlStateManager.depthMask(true);

                if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator())
                {
//                    this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                }
            }

            GlStateManager.disableRescaleNormal();
        } catch (Exception exception)
        {
//            LOGGER.error((String)"Couldn\'t render entity", (Throwable)exception);
        }

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
//        super.doRender(entity, x, y, z, entityYaw, partialTicks);
//        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<T>(entity, this, x, y, z));
    }

    protected void renderModel(ResourceLocation resource, RenderLivingBase<EntityLivingBase> renderer, EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        boolean flag = !entitylivingbaseIn.isInvisible();// || this.renderOutlines;
        boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);

        if (flag || flag1)
        {
            renderer.bindTexture(resource);

            if (flag1)
            {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }

            renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

            if (flag1)
            {
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }

    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks)
    {
        float f;

        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F)
        {
            ;
        }

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        return prevYawOffset + partialTicks * f;
    }

    protected void renderLivingAt(EntityLivingBase entityLivingBaseIn, double x, double y, double z)
    {
        GlStateManager.translate((float) x, (float) y, (float) z);
    }

    protected void rotateCorpse(EntityLivingBase entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);

        if (entityLiving.deathTime > 0)
        {
            float f = ((float) entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt_float(f);

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            GlStateManager.rotate(f * this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
        } else
        {
            String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());

            if (s != null && (s.equals("Dinnerbone") || s.equals("Grumm")) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer) entityLiving).isWearing(EnumPlayerModelParts.CAPE)))
            {
                GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    protected float getDeathMaxRotation(EntityLivingBase entity)
    {
        return 90.0F;
    }

    public float prepareScale(EntityLivingBase entitylivingbaseIn, float partialTicks)
    {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
//        this.preRenderCallback(entitylivingbaseIn, partialTicks);
        float f = 0.0625F;
        GlStateManager.translate(0.0F, -1.501F, 0.0F);
        return f;
    }

    protected float handleRotationFloat(EntityLivingBase livingBase, float partialTicks)
    {
        if (livingBase instanceof EntityChicken)
        {
            EntityChicken chickenEntity = (EntityChicken) livingBase;
            float f = chickenEntity.oFlap + (chickenEntity.wingRotation - chickenEntity.oFlap) * partialTicks;
            float f1 = chickenEntity.oFlapSpeed + (chickenEntity.destPos - chickenEntity.oFlapSpeed) * partialTicks;
            return (MathHelper.sin(f) + 1.0F) * f1;
        }
        return (float) livingBase.ticksExisted + partialTicks;
    }

    //TODO: END

    @SubscribeEvent
    public void onSoundEvent(PlaySoundEvent event)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null && player.isPotionActive(ModPotions.deafness))
        {
            event.setResultSound(null);
        }
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event)
    {
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
    public void render(RenderWorldLastEvent event)
    {
        EntityPlayerSP player = minecraft.thePlayer;
        World world = player.worldObj;

        if (mrsHoloTile != null)
        {
            if (world.getTileEntity(mrsHoloTile.getPos()) instanceof TileMasterRitualStone)
            {
                if (mrsHoloDisplay)
                    renderRitualStones(mrsHoloTile, event.getPartialTicks());
                else
                    ClientHandler.setRitualHoloToNull();
            } else
            {
                ClientHandler.setRitualHoloToNull();
            }
        }

        if (minecraft.objectMouseOver == null || minecraft.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK)
            return;

        TileEntity tileEntity = world.getTileEntity(minecraft.objectMouseOver.getBlockPos());

        if (tileEntity instanceof TileMasterRitualStone && player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemRitualDiviner)
            renderRitualStones(player, event.getPartialTicks());

        if (tileEntity instanceof TileMasterRitualStone && player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemRitualReader)
            renderRitualInformation(player, event.getPartialTicks());
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (event.getDwheel() != 0 && player != null && player.isSneaking())
        {
            ItemStack stack = player.getHeldItemMainhand();

            if (stack != null)
            {
                Item item = stack.getItem();

                if (item instanceof ItemSigilHolding)
                {
                    cycleSigil(stack, player, event.getDwheel());
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onKey(InputEvent event)
    {
        if (!minecraft.inGameHasFocus)
            return;

        for (BMKeyBinding key : keyBindings)
        {
            if (key.isPressed())
                key.handleKeyPress();
        }
    }

    @SubscribeEvent
    public void onHudRender(RenderGameOverlayEvent.Pre event)
    {
        for (HUDElement element : hudElements)
            if (element.getElementType() == event.getType() && element.shouldRender(minecraft))
                element.render(minecraft, event.getResolution(), event.getPartialTicks());
    }

    // Stolen from Chisel
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event)
    {
        if (BloodMagic.isDev() && SUPPRESS_ASSET_ERRORS)
            return;

        Stopwatch stopwatch = Stopwatch.createStarted();
        Map<ResourceLocation, Exception> modelErrors = ReflectionHelper.getPrivateValue(ModelLoader.class, event.getModelLoader(), "loadingExceptions");
        Set<ModelResourceLocation> missingVariants = ReflectionHelper.getPrivateValue(ModelLoader.class, event.getModelLoader(), "missingVariants");

        // Collect all Blood Magic model errors
        List<ResourceLocation> errored = new ArrayList<ResourceLocation>();
        for (ResourceLocation modelError : modelErrors.keySet())
            if (modelError.getResourceDomain().equalsIgnoreCase(Constants.Mod.MODID))
                errored.add(modelError);

        // Collect all Blood Magic variant errors
        List<ModelResourceLocation> missing = new ArrayList<ModelResourceLocation>();
        for (ModelResourceLocation missingVariant : missingVariants)
            if (missingVariant.getResourceDomain().equalsIgnoreCase(Constants.Mod.MODID))
                missing.add(missingVariant);

        // Remove discovered model errors
        for (ResourceLocation modelError : errored)
            modelErrors.remove(modelError);

        // Remove discovered variant errors
        missingVariants.removeAll(missing);

        if (errored.size() > 0)
            BloodMagic.instance.getLogger().info("Suppressed {} model errors from Blood Magic.", errored.size());
        if (missing.size() > 0)
            BloodMagic.instance.getLogger().info("Suppressed {} variant errors from Blood Magic.", missing.size());
        BloodMagic.instance.getLogger().debug("Suppressed discovered model/variant errors in {}", stopwatch.stop());
    }

    // For some reason, we need some bad textures to be listed in the Crystal and Node models. This will hide that from the end user.
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Post event)
    {
        if (BloodMagic.isDev() && SUPPRESS_ASSET_ERRORS)
            return;

        Stopwatch stopwatch = Stopwatch.createStarted();
        SetMultimap<String, ResourceLocation> missingTextures = ReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "missingTextures");
        Set<String> badTextureDomains = ReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "badTextureDomains");

        String mc = "minecraft";
        String format = "textures/%s.png";
        Set<ResourceLocation> toRemove = new HashSet<ResourceLocation>();

        // Find our missing textures and mark them for removal. Cannot directly remove as it would cause a CME
        if (missingTextures.containsKey(mc))
        {
            Set<ResourceLocation> missingMCTextures = missingTextures.get(mc);
            for (ResourceLocation texture : missingMCTextures)
                if (texture.getResourcePath().equalsIgnoreCase(String.format(format, "node")) || texture.getResourcePath().equalsIgnoreCase(String.format(format, "crystal")))
                    toRemove.add(texture);
        }

        // Remove all our found errors
        missingTextures.get(mc).removeAll(toRemove);

        // Make sure to only remove the bad MC domain if no other textures are missing
        if (missingTextures.get(mc).isEmpty())
        {
            missingTextures.keySet().remove(mc);
            badTextureDomains.remove(mc);
        }
        BloodMagic.instance.getLogger().debug("Suppressed required texture errors in {}", stopwatch.stop());
    }

    private void cycleSigil(ItemStack stack, EntityPlayer player, int dWheel)
    {
        int mode = dWheel;
        if (!ConfigHandler.sigilHoldingSkipsEmptySlots)
        {
            mode = ItemSigilHolding.getCurrentItemOrdinal(stack);
            mode = dWheel < 0 ? ItemSigilHolding.next(mode) : ItemSigilHolding.prev(mode);
        }

        ItemSigilHolding.cycleToNextSigil(stack, mode);
        BloodMagicPacketHandler.INSTANCE.sendToServer(new SigilHoldingPacketProcessor(player.inventory.currentItem, mode));
    }

    private static TextureAtlasSprite forName(TextureMap textureMap, String name, String dir)
    {
        return textureMap.registerSprite(new ResourceLocation(Constants.Mod.DOMAIN + dir + "/" + name));
    }

    private void renderRitualInformation(EntityPlayerSP player, float partialTicks)
    {
        World world = player.worldObj;
        TileMasterRitualStone mrs = (TileMasterRitualStone) world.getTileEntity(minecraft.objectMouseOver.getBlockPos());
        Ritual ritual = mrs.getCurrentRitual();

        if (ritual != null)
        {
            List<String> ranges = ritual.getListOfRanges();
            for (String range : ranges)
            {
                AreaDescriptor areaDescriptor = ritual.getBlockRange(range);

                for (BlockPos pos : areaDescriptor.getContainedPositions(minecraft.objectMouseOver.getBlockPos()))
                    RenderFakeBlocks.drawFakeBlock(ritualStoneBlank, pos.getX(), pos.getY(), pos.getZ(), world);
            }
        }
    }

    private void renderRitualStones(EntityPlayerSP player, float partialTicks)
    {
        World world = player.worldObj;
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

        for (RitualComponent ritualComponent : ritual.getComponents())
        {
            vX = vec3.add(ritualComponent.getOffset(direction));
            double minX = vX.getX() - posX;
            double minY = vX.getY() - posY;
            double minZ = vX.getZ() - posZ;

            if (!world.getBlockState(vX).isOpaqueCube())
            {
                TextureAtlasSprite texture = null;

                switch (ritualComponent.getRuneType())
                {
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

                RenderFakeBlocks.drawFakeBlock(texture, minX, minY, minZ, world);
            }
        }

        GlStateManager.popMatrix();
    }

    public static void renderRitualStones(TileMasterRitualStone masterRitualStone, float partialTicks)
    {
        EntityPlayerSP player = minecraft.thePlayer;
        World world = player.worldObj;
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

        for (RitualComponent ritualComponent : ritual.getComponents())
        {
            vX = vec3.add(ritualComponent.getOffset(direction));
            double minX = vX.getX() - posX;
            double minY = vX.getY() - posY;
            double minZ = vX.getZ() - posZ;

            if (!world.getBlockState(vX).isOpaqueCube())
            {
                TextureAtlasSprite texture = null;

                switch (ritualComponent.getRuneType())
                {
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

                RenderFakeBlocks.drawFakeBlock(texture, minX, minY, minZ, world);
            }
        }

        GlStateManager.popMatrix();
    }

    public static void setRitualHolo(TileMasterRitualStone masterRitualStone, Ritual ritual, EnumFacing direction, boolean displayed)
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
        mrsHoloDirection = EnumFacing.NORTH;
    }

    protected void renderHotbarItem(int x, int y, float partialTicks, EntityPlayer player, @Nullable ItemStack stack)
    {
        if (stack != null)
        {
            float animation = (float) stack.animationsToGo - partialTicks;

            if (animation > 0.0F)
            {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + animation / 5.0F;
                GlStateManager.translate((float) (x + 8), (float) (y + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }

            minecraft.getRenderItem().renderItemAndEffectIntoGUI(player, stack, x, y);

            if (animation > 0.0F)
                GlStateManager.popMatrix();

            minecraft.getRenderItem().renderItemOverlays(minecraft.fontRendererObj, stack, x, y);
        }
    }
}
