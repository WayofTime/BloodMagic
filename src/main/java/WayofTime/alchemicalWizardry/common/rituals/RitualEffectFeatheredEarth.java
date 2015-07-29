package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;

public class RitualEffectFeatheredEarth extends RitualEffect //Nullifies all fall damage in the area of effect
{
    public static final int terraeDrain = 1;
    public static final int orbisTerraeDrain = 1;
    public static final int aetherDrain = 1;

    public static final int costCooldown = 10;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        if (ritualStone.getCooldown() > 0)
        {
        	int x = pos.getX();
        	int y = pos.getY();
        	int z = pos.getZ();
        	
            world.addWeatherEffect(new EntityLightningBolt(world, x + 4, y + 5, z + 4));
            world.addWeatherEffect(new EntityLightningBolt(world, x + 4, y + 5, z - 4));
            world.addWeatherEffect(new EntityLightningBolt(world, x - 4, y + 5, z - 4));
            world.addWeatherEffect(new EntityLightningBolt(world, x - 4, y + 5, z + 4));
            ritualStone.setCooldown(0);
        }

        boolean hasTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, false);
        boolean hasOrbisTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, orbisTerraeDrain, false);
        boolean hasAether = this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, false);

        int range = this.getHorizontalRangeForReagent(hasTerrae, hasOrbisTerrae);
        int verticalRange = hasAether ? 60 : 30;
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(range, verticalRange, range));
        int entityCount = 0;
        boolean flag = false;

        entityCount += entities.size();

        if (currentEssence < this.getCostPerRefresh() * entityCount)
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            for (EntityLivingBase entity : entities)
            {
                entity.fallDistance = 0;
                flag = true;
            }

            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * entityCount);

            if (flag && world.getWorldTime() % costCooldown == 0)
            {
                if (hasTerrae)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, true);
                }
                if (hasOrbisTerrae)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, orbisTerraeDrain, true);
                }
                if (hasAether)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, true);
                }
            }
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

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> featheredEarthRitual = new ArrayList();
        featheredEarthRitual.add(new RitualComponent(1, 0, 0, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(0, 0, -1, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(2, 0, 2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(2, 0, -2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(1, 0, 3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(0, 0, 3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(1, 0, -3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(0, 0, -3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(3, 0, 1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(3, 0, 0, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(3, 0, -1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-3, 0, 0, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(4, 4, 4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(-4, 4, 4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(-4, 4, -4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(4, 4, -4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(4, 5, 5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(4, 5, 3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(5, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(3, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, 5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, 3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-5, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-3, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(4, 5, -5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(4, 5, -3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(5, 5, -4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(3, 5, -4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, -5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, -3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-5, 5, -4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-3, 5, -4, RitualComponent.AIR));
        return featheredEarthRitual;
    }

    public int getHorizontalRangeForReagent(boolean hasTerrae, boolean hasOrbisTerrae)
    {
        if (hasOrbisTerrae)
        {
            if (hasTerrae)
            {
                return 64;
            } else
            {
                return 45;
            }
        } else
        {
            if (hasTerrae)
            {
                return 30;
            } else
            {
                return 20;
            }
        }
    }
}
