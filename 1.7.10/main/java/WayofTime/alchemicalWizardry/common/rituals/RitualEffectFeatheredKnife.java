package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

public class RitualEffectFeatheredKnife extends RitualEffect
{
    public final int timeDelay = 20;
    public final int amount = 100;

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
        TEAltar tileAltar = null;
        boolean testFlag = false;

        for (int i = -5; i <= 5; i++)
        {
            for (int j = -5; j <= 5; j++)
            {
                for (int k = -10; k <= 10; k++)
                {
                    if (world.getTileEntity(x + i, y + k, z + j) instanceof TEAltar)
                    {
                        tileAltar = (TEAltar) world.getTileEntity(x + i, y + k, z + j);
                        testFlag = true;
                    }
                }
            }
        }

        if (!testFlag)
        {
            return;
        }

        //tileAltar = (TEAltar)world.getBlockTileEntity(x,y-1,z);
        int d0 = 15;
        int vertRange = 20;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double) x, (double) y, (double) z, (double) (x + 1), (double) (y + 1), (double) (z + 1)).expand(d0, vertRange, d0);
        List list = world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
        Iterator iterator1 = list.iterator();
        EntityPlayer entity;
        int entityCount = 0;
        boolean flag = false;

        while (iterator1.hasNext())
        {
            entity = (EntityPlayer) iterator1.next();

            if (entity.getClass().equals(EntityPlayerMP.class) || entity.getClass().equals(EntityPlayer.class))
            {
                entityCount++;
            }
        }

        if (currentEssence < this.getCostPerRefresh() * entityCount)
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

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
                entity = (EntityPlayer) iterator2.next();

                //entity = (EntityPlayer)iterator1.next();
                if (entity.getClass().equals(EntityPlayerMP.class) || entity.getClass().equals(EntityPlayer.class))
                {
                    if (entity.getHealth() > 6.2f)
                    {
                        entity.setHealth(entity.getHealth() - 1);
                        entityCount++;
                        tileAltar.sacrificialDaggerCall(this.amount, false);
                    }
                }

                //entity.setHealth(entity.getHealth()-1);
//                if(entity.getHealth()<=0.2f)
//                {
//                	entity.onDeath(DamageSource.inFire);
//                }
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
		ArrayList<RitualComponent> featheredKnifeRitual = new ArrayList();
        featheredKnifeRitual.add(new RitualComponent(1, 0, 0, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(0, 0, -1, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(2, -1, 0, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(-2, -1, 0, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(0, -1, 2, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(0, -1, -2, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(1, -1, 1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(1, -1, -1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-1, -1, 1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-1, -1, -1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(4, -1, 2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(2, -1, 4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-4, -1, 2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(2, -1, -4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(4, -1, -2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-2, -1, 4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-4, -1, -2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-2, -1, -4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(4, 0, 2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(2, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, 2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(2, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(4, 0, -2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-2, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, -2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-2, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(4, 0, 3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(3, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, 3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(3, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(4, 0, -3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, -3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(3, 0, 3, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(3, 0, -3, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.AIR));
        return featheredKnifeRitual;
	}
}
