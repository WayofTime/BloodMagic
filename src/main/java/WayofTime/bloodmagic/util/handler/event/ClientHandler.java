package WayofTime.bloodmagic.util.handler.event;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.client.render.RenderMasterRitualStone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
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
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import WayofTime.bloodmagic.annot.Handler;
import WayofTime.bloodmagic.api.Constants;
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
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.GhostItemHelper;
import WayofTime.bloodmagic.util.handler.BMKeyBinding;
import WayofTime.bloodmagic.util.helper.TextHelper;

@Handler
@SideOnly(Side.CLIENT)
public class ClientHandler
{
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

        if (minecraft.objectMouseOver == null || minecraft.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK)
            return;

        TileEntity tileEntity = world.getTileEntity(minecraft.objectMouseOver.getBlockPos());

        if (tileEntity instanceof TileMasterRitualStone && player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemRitualDiviner && !((TileMasterRitualStone) tileEntity).isDisplay())
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
                    texture = ClientHandler.ritualStoneBlank;
                    break;
                case WATER:
                    texture = ClientHandler.ritualStoneWater;
                    break;
                case FIRE:
                    texture = ClientHandler.ritualStoneFire;
                    break;
                case EARTH:
                    texture = ClientHandler.ritualStoneEarth;
                    break;
                case AIR:
                    texture = ClientHandler.ritualStoneAir;
                    break;
                case DAWN:
                    texture = ClientHandler.ritualStoneDawn;
                    break;
                case DUSK:
                    texture = ClientHandler.ritualStoneDusk;
                    break;
                }

                RenderFakeBlocks.drawFakeBlock(texture, minX, minY, minZ, world);
            }
        }

        GlStateManager.popMatrix();
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
