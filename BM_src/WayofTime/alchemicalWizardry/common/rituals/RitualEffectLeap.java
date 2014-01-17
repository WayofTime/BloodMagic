package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class RitualEffectLeap extends RitualEffect
{
    @Override
    public void performEffect(TEMasterStone ritualStone)
    {
        String owner = ritualStone.getOwner();
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;
        World world = ritualStone.worldObj;
        int x = ritualStone.xCoord;
        int y = ritualStone.yCoord;
        int z = ritualStone.zCoord;

        if (currentEssence < this.getCostPerRefresh())
        {
            EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            int direction = ritualStone.getDirection();
            int d0 = 2;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) x, (double) y - 1, (double) z, (double) (x + 1), (double) (y + 2), (double) (z + 1)).expand(d0, 0, d0);
            List list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
            Iterator iterator = list.iterator();
            EntityLivingBase entityplayer;
            boolean flag = false;

            while (iterator.hasNext())
            {
                entityplayer = (EntityLivingBase) iterator.next();

                if (entityplayer instanceof EntityPlayer)
                {
                    entityplayer.motionY = 1.2;
                    entityplayer.fallDistance = 0;

                    switch (direction)
                    {
                        case 1:
                            PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(0, 1.2, -3.0), (Player) entityplayer);
                            break;

                        case 2:
                            PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(3.0, 1.2, 0), (Player) entityplayer);
                            break;

                        case 3:
                            PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(0, 1.2, 3.0), (Player) entityplayer);
                            break;

                        case 4:
                            PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(-3.0, 1.2, 0), (Player) entityplayer);
                            break;
                    }

                    flag = true;
                } else
                //if (!(entityplayer.getEntityName().equals(owner)))
                {
//                    double xDif = entityplayer.posX - xCoord;
//                    double yDif = entityplayer.posY - (yCoord + 1);
//                    double zDif = entityplayer.posZ - zCoord;
                    //entityplayer.motionX=0.1*xDif;
                    entityplayer.motionY = 1.2;

                    switch (direction)
                    {
                        case 1:
                            entityplayer.motionX = 0.0;
                            entityplayer.motionZ = -3.0;
                            break;

                        case 2:
                            entityplayer.motionX = 3.0;
                            entityplayer.motionZ = 0.0;
                            break;

                        case 3:
                            entityplayer.motionX = 0.0;
                            entityplayer.motionZ = -3.0;
                            break;

                        case 4:
                            entityplayer.motionX = -3.0;
                            entityplayer.motionZ = 0.0;
                            break;
                    }

                    //entityplayer.motionZ=0.1*zDif;
                    entityplayer.fallDistance = 0;
                    flag = true;
                    //entityplayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                }
            }

            if (flag)
            {
                data.currentEssence = currentEssence - this.getCostPerRefresh();
                data.markDirty();
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 5;
    }
}
