package WayofTime.alchemicalWizardry.client.renderer;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.Vector3;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.common.items.ItemRitualDiviner;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class RitualDivinerRender
{
    @SubscribeEvent
    public void render(RenderWorldLastEvent event)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityClientPlayerMP player = minecraft.thePlayer;
        World world = player.worldObj;

        if (minecraft.objectMouseOver == null || minecraft.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
        {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(minecraft.objectMouseOver.blockX, minecraft.objectMouseOver.blockY, minecraft.objectMouseOver.blockZ);

        if (!(tileEntity instanceof IMasterRitualStone))
        {
            return;
        }

        Vector3 vec3 = new Vector3(minecraft.objectMouseOver.blockX, minecraft.objectMouseOver.blockY, minecraft.objectMouseOver.blockZ);
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

            for (RitualComponent ritualComponent : ritualEffect.getRitualComponentList())
            {
                Vector3 vX = vec3.add(new Vector3(ritualComponent.getX(direction), ritualComponent.getY(), ritualComponent.getZ(direction)));
                double minX = vX.x - posX;
                double minY = vX.y - posY;
                double minZ = vX.z - posZ;

                if (!world.getBlock(vX.x, vX.y, vX.z).isOpaqueCube())
                {
                    RenderFakeBlocks.drawFakeBlock(vX, ModBlocks.ritualStone, ritualComponent.getStoneType(), minX, minY, minZ, world);
                }
            }
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
