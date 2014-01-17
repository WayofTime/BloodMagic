package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class RitualEffectAnimalGrowth extends RitualEffect
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

        if (world.getWorldTime() % 20 != 0)
        {
            return;
        }

        int d0 = 2;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) x, (double) y + 1, (double) z, (double) (x + 1), (double) (y + 3), (double) (z + 1)).expand(d0, 0, d0);
        List list = world.getEntitiesWithinAABB(EntityAgeable.class, axisalignedbb);
        Iterator iterator1 = list.iterator();
        EntityAgeable entity;
        int entityCount = 0;
        boolean flag = false;

        while (iterator1.hasNext())
        {
            entity = (EntityAgeable) iterator1.next();
            entityCount++;
        }

        if (currentEssence < this.getCostPerRefresh() * entityCount)
        {
            EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            Iterator iterator2 = list.iterator();
            entityCount = 0;

            while (iterator2.hasNext())
            {
                entity = (EntityAgeable) iterator2.next();

                if (entity.getGrowingAge() < 0)
                {
                    entity.addGrowth(5);
                    entityCount++;
                }
            }

            data.currentEssence = currentEssence - this.getCostPerRefresh() * entityCount;
            data.markDirty();
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        // TODO Auto-generated method stub
        return 2;
    }
}
