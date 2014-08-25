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
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

public class RitualEffectWellOfSuffering extends RitualEffect
{
    public static final int timeDelay = 25;
    public static final int amount = 10;

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

        int d0 = 10;
        int vertRange = 10;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double) x, (double) y, (double) z, (double) (x + 1), (double) (y + 1), (double) (z + 1)).expand(d0, vertRange, d0);
        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        int entityCount = 0;

        if (currentEssence < this.getCostPerRefresh() * list.size())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            for(EntityLivingBase livingEntity : list)
            {
                if (livingEntity instanceof EntityPlayer || AlchemicalWizardry.wellBlacklist.contains(livingEntity.getClass()))
                {
                    continue;
                }

                if(livingEntity.attackEntityFrom(DamageSource.outOfWorld, 1))
                {
                    entityCount++;
                    tileAltar.sacrificialDaggerCall(this.amount, true);
                }
            }

            data.currentEssence = currentEssence - this.getCostPerRefresh() * entityCount;
            data.markDirty();
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 2;
    }

    @Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> wellOfSufferingRitual = new ArrayList();
        wellOfSufferingRitual.add(new RitualComponent(1, 0, 1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(1, 0, -1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, 2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, -2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, 2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, -2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(0, -1, 2, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, 0, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(0, -1, -2, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, 0, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(-3, -1, -3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(3, -1, -3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(-3, -1, 3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(3, -1, 3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, -1, 2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, -1, -2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, -1, 2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, -1, -2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(1, 0, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, 0, 1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(1, 0, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, 0, 1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, 0, -1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, 0, -1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, 1, 0, RitualComponent.AIR));
        wellOfSufferingRitual.add(new RitualComponent(0, 1, 4, RitualComponent.AIR));
        wellOfSufferingRitual.add(new RitualComponent(-4, 1, 0, RitualComponent.AIR));
        wellOfSufferingRitual.add(new RitualComponent(0, 1, -4, RitualComponent.AIR));
        return wellOfSufferingRitual;
	}
}
