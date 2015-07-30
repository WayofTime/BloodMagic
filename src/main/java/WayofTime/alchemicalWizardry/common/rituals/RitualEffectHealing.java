package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectHealing extends RitualEffect
{
    public static final int reductusDrain = 10;
    public static final int virtusDrain = 10;
    public static final int praesidiumDrain = 2;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        int timeDelay = 50;

        if (world.getWorldTime() % timeDelay != 0)
        {
            return;
        }

        boolean hasPraesidium = this.canDrainReagent(ritualStone, ReagentRegistry.praesidiumReagent, praesidiumDrain, false);

        int range = 15 * (hasPraesidium ? 3 : 1);
        int vertRange = 15 * (hasPraesidium ? 3 : 1);

        List<EntityLivingBase> list = SpellHelper.getLivingEntitiesInRange(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, range, vertRange);
        int entityCount = 0;

        for (EntityLivingBase livingEntity : list)
        {
            if (livingEntity instanceof EntityPlayer)
            {
                entityCount += 10;
            } else
            {
                entityCount++;
            }
        }

        boolean hasVirtus = this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false);

        int cost = this.getCostPerRefresh() * (hasVirtus ? 3 : 1);
        int potency = hasVirtus ? 1 : 0;

        if (currentEssence < cost * entityCount)
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            entityCount = 0;

            boolean hasReductus = this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);

            for (EntityLivingBase livingEntity : list)
            {
                hasReductus = hasReductus && this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);
                if (hasReductus && !(livingEntity instanceof EntityPlayer))
                {
                    continue;
                }

                if (livingEntity.getHealth() + 0.1f < livingEntity.getMaxHealth())
                {
                    PotionEffect effect = livingEntity.getActivePotionEffect(Potion.regeneration);
                    if (effect == null || (effect.getAmplifier() <= potency && effect.getDuration() <= timeDelay))
                    {
                        if (!hasVirtus || (this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false)))
                        {
                            livingEntity.addPotionEffect(new PotionEffect(Potion.regeneration.id, timeDelay + 2, potency));
                            if (hasReductus)
                            {
                                this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, true);
                            }
                            if (hasVirtus)
                            {
                                this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, true);
                            }

                            if (livingEntity instanceof EntityPlayer)
                            {
                                entityCount += 10;
                            } else
                            {
                                entityCount++;
                            }
                        }
                    }
                }
            }

            if (entityCount > 0)
            {
                if (hasPraesidium)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.praesidiumReagent, praesidiumDrain, true);
                }
                SoulNetworkHandler.syphonFromNetwork(owner, cost * entityCount);
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 20;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> healingRitual = new ArrayList<RitualComponent>();
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
