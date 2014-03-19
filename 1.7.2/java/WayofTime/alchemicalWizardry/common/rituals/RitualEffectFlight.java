package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class RitualEffectFlight extends RitualEffect
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
        World world = ritualStone.getWorldObj();
        int x = ritualStone.xCoord;
        int y = ritualStone.yCoord;
        int z = ritualStone.zCoord;

        if (ritualStone.getCooldown() > 0)
        {
            //TODO Cool stuffs
            ritualStone.setCooldown(0);
        }

        int range = 20;
        int verticalRange = 30;
        AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(range, verticalRange, range);
        axis.maxY = 256;
        axis.minY = 0;
        List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, axis);
        int entityCount = 0;

        for (EntityPlayer entity : entities)
        {
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
            for (EntityPlayer entity : entities)
            {
                entity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionFlight.id, 20, 0));
            }

            data.currentEssence = currentEssence - this.getCostPerRefresh() * entityCount;
            data.markDirty();
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

    @Override
    public int getInitialCooldown()
    {
        return 1;
    }
}
