package WayofTime.bloodmagic.client.render;

import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.handler.event.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderMasterRitualStone extends TileEntitySpecialRenderer<TileMasterRitualStone>
{
    public static Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void renderTileEntityAt(TileMasterRitualStone masterRitualStone, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (masterRitualStone.isDisplay())
        {
            renderRitualStones(masterRitualStone, partialTicks);
        }
    }

    public static void renderRitualStones(TileMasterRitualStone masterRitualStone, float partialTicks)
    {
        EntityPlayerSP player = mc.thePlayer;
        World world = player.worldObj;
        EnumFacing direction = masterRitualStone.getCurrentDisplayedDirection();
        Ritual ritual = masterRitualStone.getCurrentDisplayedRitual();

        if (ritual == null)
            return;


        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();
        GlStateManager.color(1F, 1F, 1F, 0.6125F);

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
}
