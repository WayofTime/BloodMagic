package WayofTime.alchemicalWizardry.common.rituals;

import java.util.Iterator;
import java.util.List;

import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RitualEffectInterdiction extends RitualEffect
{
    @Override
    public void performEffect(TEMasterStone ritualStone)
    {
        String owner = ritualStone.getOwner();
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork)worldSave.loadItemData(LifeEssenceNetwork.class, owner);

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
        }
        else
        {
            int d0 = 5;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1)).expand(d0, d0, d0);
            axisalignedbb.maxY = Math.min((double)world.getHeight(), (double)(y + 1 + d0));
            List list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
            Iterator iterator = list.iterator();
            EntityLivingBase entityplayer;
            boolean flag = false;

            while (iterator.hasNext())
            {
                entityplayer = (EntityLivingBase)iterator.next();

                if (!(entityplayer.getEntityName().equals(owner)))
                {
                    double xDif = entityplayer.posX - x;
                    double yDif = entityplayer.posY - (y + 1);
                    double zDif = entityplayer.posZ - z;
                    entityplayer.motionX = 0.1 * xDif;
                    entityplayer.motionY = 0.1 * yDif;
                    entityplayer.motionZ = 0.1 * zDif;
                    entityplayer.fallDistance = 0;

                    if (!(entityplayer instanceof EntityPlayer))
                    {
                        flag = true;
                    }

                    //entityplayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                }
            }

            if (world.getWorldTime() % 2 == 0 && flag)
            {
                data.currentEssence = currentEssence - this.getCostPerRefresh();
                data.markDirty();
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 1;
    }
}
