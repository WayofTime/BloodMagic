package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualEffectFlight extends RitualEffect
{
    public static final int aetherDrain = 10;
    public static final int reductusDrain = 5;
    public static final int reagentCooldown = 50;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        int range = 20;
        int verticalRange = 30;
        AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(range, verticalRange, range);
        axis.maxY = 256;
        axis.minY = 0;
        List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, axis);
        int entityCount = 0;

        boolean hasAether = this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, false);
        boolean hasReductus = this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);

        for (EntityPlayer entity : entities)
        {
            entityCount++;
        }

        if (currentEssence < this.getCostPerRefresh() * entityCount)
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            entityCount = 0;
            EntityPlayer ownerEntity = SpellHelper.getPlayerForUsername(owner);
            for (EntityPlayer entity : entities)
            {
                if (hasReductus && entity != ownerEntity)
                {
                    continue;
                }
                entity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionFlight.id, hasAether ? 30 * 20 : 20, 0));
                entityCount++;
            }

            if (entityCount > 0 && world.getWorldTime() % reagentCooldown == 0)
            {
                if (hasAether)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, true);
                }
                if (hasReductus)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, true);
                }
            }

            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * entityCount);
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
        ArrayList<RitualComponent> flightRitual = new ArrayList();
        flightRitual.add(new RitualComponent(1, 0, 0, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(0, 0, -1, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(2, 0, 2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(2, 0, -2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(1, 0, 3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(0, 0, 3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(1, 0, -3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(0, 0, -3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(3, 0, 1, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(3, 0, 0, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(3, 0, -1, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-3, 0, 0, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-3, 0, -4, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(-4, 0, -3, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(-3, 0, 4, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(4, 0, -3, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(3, 0, -4, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(-4, 0, 3, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(3, 0, 4, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(4, 0, 3, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(-1, 1, 0, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(1, 1, 0, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(0, 1, -1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(0, 1, 1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-2, 1, 0, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(2, 1, 0, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(0, 1, -2, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(0, 1, 2, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(-4, 1, 0, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(4, 1, 0, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(0, 1, -4, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(0, 1, 4, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(-5, 1, 0, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(5, 1, 0, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(0, 1, -5, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(0, 1, 5, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(5, 0, 0, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(0, 0, 5, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(0, 0, -5, RitualComponent.DUSK));

        for (int i = 2; i <= 4; i++)
        {
            flightRitual.add(new RitualComponent(-i, 2, 0, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(i, 2, 0, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(0, 2, -i, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(0, 2, i, RitualComponent.EARTH));
        }

        flightRitual.add(new RitualComponent(2, 4, 1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(1, 4, 2, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-2, 4, 1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(1, 4, -2, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(2, 4, -1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-1, 4, 2, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-2, 4, -1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-1, 4, -2, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(2, 4, 2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(-2, 4, 2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(2, 4, -2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(-2, 4, -2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(-4, 2, -4, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(4, 2, 4, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(4, 2, -4, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-4, 2, 4, RitualComponent.FIRE));

        for (int i = -1; i <= 1; i++)
        {
            flightRitual.add(new RitualComponent(3, 4, i, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(-3, 4, i, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(i, 4, 3, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(i, 4, -3, RitualComponent.EARTH));
        }
        return flightRitual;
    }
}
