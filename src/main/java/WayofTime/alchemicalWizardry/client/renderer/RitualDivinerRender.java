package WayofTime.alchemicalWizardry.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.Vector3;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.common.items.ItemRitualDiviner;


/*
 *  Created in Scala by Alex-Hawks
 *  Translated and implemented by Arcaratus
 */
@SideOnly(Side.CLIENT)
public class RitualDivinerRender
{
    @SubscribeEvent
    public void render(RenderWorldLastEvent event)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayer player = minecraft.thePlayer;
        World world = player.worldObj;

        if (minecraft.objectMouseOver == null || minecraft.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
        {
            return;
        }
        
        BlockPos pos = minecraft.objectMouseOver.func_178782_a();

        TileEntity tileEntity = world.getTileEntity(pos);

        if (!(tileEntity instanceof IMasterRitualStone))
        {
            return;
        }

        Vector3 vec3 = new Vector3(pos.getX(), pos.getY(), pos.getZ());
        double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
        double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
        double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

        if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof ItemRitualDiviner)
        {
            ItemRitualDiviner ritualDiviner = (ItemRitualDiviner) player.inventory.getCurrentItem().getItem();
            int direction = ritualDiviner.getDirection(player.inventory.getCurrentItem());
            RitualEffect ritualEffect = getEffectFromString(ritualDiviner.getCurrentRitual(player.inventory.getCurrentItem()));

            if (ritualEffect == null)
            {
                return;
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            for (RitualComponent ritualComponent : ritualEffect.getRitualComponentList())
            {
                Vector3 vX = vec3.add(new Vector3(ritualComponent.getX(direction), ritualComponent.getY(), ritualComponent.getZ(direction)));
                double minX = vX.x - posX;
                double minY = vX.y - posY;
                double minZ = vX.z - posZ;

                if (!world.getBlockState(new BlockPos(vX.x, vX.y, vX.z)).getBlock().isOpaqueCube())
                {
                    RenderFakeBlocks.drawFakeBlock(vX, ModBlocks.ritualStone, ritualComponent.getStoneType(), minX, minY, minZ, world);
                }
            }
            GL11.glPopMatrix();
        }
    }

    public static RitualEffect getEffectFromString(String name)
    {
        Rituals ritual = Rituals.ritualMap.get(name);

        if (ritual == null)
            return null;

        return ritual.effect;
    }
}
