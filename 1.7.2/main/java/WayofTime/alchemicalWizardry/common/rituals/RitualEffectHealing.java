package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;

public class RitualEffectHealing extends RitualEffect
{
    public final int timeDelay = 50;
    //public final int amount = 10;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
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
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (world.getWorldTime() % this.timeDelay != 0)
        {
            return;
        }

//		if(!(world.getBlockTileEntity(x, y-1, z) instanceof TEAltar))
//		{
//			return;
//		}
        //tileAltar = (TEAltar)world.getBlockTileEntity(x,y-1,z);
        int d0 = 10;
        int vertRange = 10;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) x, (double) y, (double) z, (double) (x + 1), (double) (y + 1), (double) (z + 1)).expand(d0, vertRange, d0);
        List list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
        Iterator iterator1 = list.iterator();
        EntityLivingBase entity;
        int entityCount = 0;
        boolean flag = false;

        while (iterator1.hasNext())
        {
            entity = (EntityLivingBase) iterator1.next();

            if (entity instanceof EntityPlayer)
            {
                entityCount += 10;
            } else
            {
                entityCount++;
            }
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
                entity = (EntityLivingBase) iterator2.next();

                if (entity.getHealth() + 0.1f < entity.getMaxHealth())
                {
                    entity.addPotionEffect(new PotionEffect(Potion.regeneration.id, timeDelay + 2, 0));

                    //entity.setHealth(entity.getHealth()-1);

                    //entity.attackEntityFrom(DamageSource.outOfWorld, 1);

                    if (entity instanceof EntityPlayer)
                    {
                        entityCount += 10;
                    } else
                    {
                        entityCount++;
                    }
                }

//                if(entity.getHealth()<=0.2f)
//                {
//                	entity.onDeath(DamageSource.inFire);
//                }
                //tileAltar.sacrificialDaggerCall(this.amount, true);
            }

            data.currentEssence = currentEssence - this.getCostPerRefresh() * entityCount;
            data.markDirty();
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        // TODO Auto-generated method stub
        return 20;
    }

    @Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> healingRitual = new ArrayList();
        healingRitual.add(new RitualComponent(4, 0, 0, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(5, 0, -1, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(5, 0, 1, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(-4, 0, 0, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(-5, 0, -1, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(-5, 0, 1, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(0, 0, 4, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(-1, 0, 5, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(1, 0, 5, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(0, 0, -4, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(-1, 0, -5, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(1, 0, -5, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(3, 0, 5, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(5, 0, 3, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(3, 0, -5, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(5, 0, -3, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(-3, 0, 5, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(-5, 0, 3, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(-3, 0, -5, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(-5, 0, -3, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.DUSK));
        healingRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.DUSK));
        healingRitual.add(new RitualComponent(3, 0, -3, RitualComponent.DUSK));
        healingRitual.add(new RitualComponent(3, 0, 3, RitualComponent.DUSK));
        healingRitual.add(new RitualComponent(4, 0, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(4, -1, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, 0, 4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, -1, 4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, 0, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(4, 0, -5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(4, -1, -5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, 0, -4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, -1, -4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, 0, -5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-4, 0, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-4, -1, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, 0, 4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, -1, 4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, 0, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-4, 0, -5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-4, -1, -5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, 0, -4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, -1, -4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, 0, -5, RitualComponent.EARTH));
        return healingRitual;
	}
}
