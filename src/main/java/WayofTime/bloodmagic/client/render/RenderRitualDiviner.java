package WayofTime.bloodmagic.client.render;

import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.client.helper.MiscIcons;
import WayofTime.bloodmagic.client.helper.RenderFakeBlocks;
import WayofTime.bloodmagic.item.ItemRitualDiviner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderRitualDiviner
{
    @SubscribeEvent
    public void render(RenderWorldLastEvent event)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.thePlayer;
        World world = player.worldObj;

        if (minecraft.objectMouseOver == null || minecraft.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
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

                Vec3 vec3 = new Vec3(minecraft.objectMouseOver.getBlockPos().getX(), minecraft.objectMouseOver.getBlockPos().getY(), minecraft.objectMouseOver.getBlockPos().getZ());
                double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
                double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
                double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

                for (RitualComponent ritualComponent : ritual.getComponents())
                {
                    Vec3 vX = vec3.add(new Vec3(ritualComponent.getX(direction), ritualComponent.getY(), ritualComponent.getZ(direction)));
                    double minX = vX.xCoord - posX;
                    double minY = vX.yCoord - posY;
                    double minZ = vX.zCoord - posZ;

                    if (!world.getBlockState(new BlockPos(vX.xCoord, vX.yCoord, vX.zCoord)).getBlock().isOpaqueCube())
                    {
                        TextureAtlasSprite texture = null;

                        switch (ritualComponent.getRuneType())
                        {
                            case BLANK:
                                texture = MiscIcons.INSTANCE.ritualStoneBlankIcon;
                                break;
                            case WATER:
                                texture = MiscIcons.INSTANCE.ritualStoneWaterIcon;
                                break;
                            case FIRE:
                                texture = MiscIcons.INSTANCE.ritualStoneFireIcon;
                                break;
                            case EARTH:
                                texture = MiscIcons.INSTANCE.ritualStoneEarthIcon;
                                break;
                            case AIR:
                                texture = MiscIcons.INSTANCE.ritualStoneAirIcon;
                                break;
                            case DAWN:
                                texture = MiscIcons.INSTANCE.ritualStoneDawnIcon;
                                break;
                            case DUSK:
                                texture = MiscIcons.INSTANCE.ritualStoneDuskIcon;
                                break;
                        }

                        RenderFakeBlocks.drawFakeBlock(texture, minX, minY, minZ, world);
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }
}
