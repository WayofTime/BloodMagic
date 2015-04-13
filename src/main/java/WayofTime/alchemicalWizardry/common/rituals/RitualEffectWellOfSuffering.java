package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualEffectWellOfSuffering extends RitualEffect
{
    public static final int timeDelay = 25;
    public static final int amount = 10;
    public static final int offensaDrain =2; // LP amount per damage
    public static final int potentiaDrain =5; // Increase Range
    public static final int sanctusDrain = 2; // Reduce LP use

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (world.getWorldTime() % this.timeDelay != 0)
        {
            return;
        }

        IBloodAltar tileAltar = null;
        boolean testFlag = false;

        for (int i = -5; i <= 5; i++)
        {
            for (int j = -5; j <= 5; j++)
            {
                for (int k = -10; k <= 10; k++)
                {
                    if (world.getTileEntity(x + i, y + k, z + j) instanceof IBloodAltar)
                    {
                        tileAltar = (IBloodAltar) world.getTileEntity(x + i, y + k, z + j);
                        if (tileAltar.getCurrentBlood()!=tileAltar.getCapacity()){
                            testFlag = true;
                        }
                    }
                }
            }
        }

        if (!testFlag)
        {
            return;
        }

        int d0 = 10;
        boolean hasPotentia = this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);
        int vertRange = 10 * (hasPotentia ? 2 : 1);
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double) x, (double) y, (double) z, (double) (x + 1), (double) (y + 1), (double) (z + 1)).expand(d0, vertRange, d0);        
        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);    
        int entityCount = 0;
        
        boolean hasSanctus = this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);
        if (currentEssence < this.getCostPerRefresh() * list.size() * (hasSanctus ? 0.5 :1))
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            for (EntityLivingBase livingEntity : list)
            {
                if (!livingEntity.isEntityAlive() || livingEntity instanceof EntityPlayer || AlchemicalWizardry.wellBlacklist.contains(livingEntity.getClass()))
                {
                    continue;
                }

                if (livingEntity.attackEntityFrom(DamageSource.outOfWorld, 1))
                {
                    entityCount++;
                    tileAltar.sacrificialDaggerCall(this.amount*(canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, true)? 2:1), true);
                }
            }
            if (entityCount > 0)
            {
                if (hasPotentia)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, true);
                }
            SoulNetworkHandler.syphonFromNetwork(owner, (int)(this.getCostPerRefresh() * entityCount* (canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, true)?0.5:1)));
            }
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
