package WayofTime.bloodmagic.util.handler;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.client.render.RenderFakeBlocks;
import WayofTime.bloodmagic.item.ItemRitualDiviner;
import WayofTime.bloodmagic.util.GhostItemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler
{
    public static int currentLP = 0;
    public static int capacity = 0;

    public TextureAtlasSprite ritualStoneBlankIcon, ritualStoneWaterIcon, ritualStoneFireIcon, ritualStoneEarthIcon, ritualStoneAirIcon, ritualStoneDawnIcon, ritualStoneDuskIcon;

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
                event.getToolTip().add("Everything");
            } else
            {
                event.getToolTip().add("Ghost item amount: " + amount);
            }
        }
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event)
    {
        final String BLOCKS = "blocks";

        ritualStoneBlankIcon = forName(event.getMap(), "RitualStone", BLOCKS);
        ritualStoneWaterIcon = forName(event.getMap(), "WaterRitualStone", BLOCKS);
        ritualStoneFireIcon = forName(event.getMap(), "FireRitualStone", BLOCKS);
        ritualStoneEarthIcon = forName(event.getMap(), "EarthRitualStone", BLOCKS);
        ritualStoneAirIcon = forName(event.getMap(), "AirRitualStone", BLOCKS);
        ritualStoneDawnIcon = forName(event.getMap(), "LightRitualStone", BLOCKS);
        ritualStoneDuskIcon = forName(event.getMap(), "DuskRitualStone", BLOCKS);
    }

    @SubscribeEvent
    public void render(RenderWorldLastEvent event)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.thePlayer;
        World world = player.worldObj;

        if (minecraft.objectMouseOver == null || minecraft.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(minecraft.objectMouseOver.getBlockPos());

        if (tileEntity instanceof IMasterRitualStone)
        {
            if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof ItemRitualDiviner)
            {
                ItemRitualDiviner ritualDiviner = (ItemRitualDiviner) player.inventory.getCurrentItem().getItem();
                EnumFacing direction = ritualDiviner.getDirection(player.inventory.getCurrentItem());
                Ritual ritual = RitualRegistry.getRitualForId(ritualDiviner.getCurrentRitual(player.inventory.getCurrentItem()));

                if (ritual == null)
                {
                    return;
                }

                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                BlockPos vec3 = new BlockPos(minecraft.objectMouseOver.getBlockPos().getX(), minecraft.objectMouseOver.getBlockPos().getY(), minecraft.objectMouseOver.getBlockPos().getZ());
                double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
                double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
                double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

                for (RitualComponent ritualComponent : ritual.getComponents())
                {
                    BlockPos vX = vec3.add(new BlockPos(ritualComponent.getX(direction), ritualComponent.getY(), ritualComponent.getZ(direction)));
                    double minX = vX.getX() - posX;
                    double minY = vX.getY() - posY;
                    double minZ = vX.getZ() - posZ;

                    if (!world.getBlockState(vX).isOpaqueCube())
                    {
                        TextureAtlasSprite texture = null;

                        switch (ritualComponent.getRuneType())
                        {
                        case BLANK:
                            texture = ritualStoneBlankIcon;
                            break;
                        case WATER:
                            texture = ritualStoneWaterIcon;
                            break;
                        case FIRE:
                            texture = ritualStoneFireIcon;
                            break;
                        case EARTH:
                            texture = ritualStoneEarthIcon;
                            break;
                        case AIR:
                            texture = ritualStoneAirIcon;
                            break;
                        case DAWN:
                            texture = ritualStoneDawnIcon;
                            break;
                        case DUSK:
                            texture = ritualStoneDuskIcon;
                            break;
                        }

                        RenderFakeBlocks.drawFakeBlock(texture, minX, minY, minZ, world);
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

    private static TextureAtlasSprite forName(TextureMap textureMap, String name, String dir)
    {
        return textureMap.registerSprite(new ResourceLocation(Constants.Mod.DOMAIN + dir + "/" + name));
    }
}
