package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualEffectLeap extends RitualEffect
{
    public static final int aetherDrain = 10;
    public static final int terraeDrain = 10;
    public static final int reductusDrain = 10;
    public static final int tenebraeDrain = 10;
    public static final int sanctusDrain = 10;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        double range = 2.0;

        List<EntityLivingBase> livingList = SpellHelper.getLivingEntitiesInRange(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, range, range);

        if (livingList == null)
        {
            return;
        }

        if (currentEssence < this.getCostPerRefresh() * livingList.size())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            boolean hasAether = this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, false);
            boolean hasTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, false);
            boolean hasReductus = this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);
            boolean hasTenebrae = this.canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, tenebraeDrain, false);
            boolean hasSanctus = this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);

            int direction = ritualStone.getDirection();

            int flag = 0;

            for (EntityLivingBase livingEntity : livingList)
            {
                if (livingEntity.isSneaking())
                {
                    continue;
                }

                hasAether = hasAether && this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, false);
                hasTerrae = hasTerrae && this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, false);
                hasReductus = hasReductus && this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);
                hasTenebrae = hasTenebrae && this.canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, tenebraeDrain, false);
                hasSanctus = hasSanctus && this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);

                double motionY = hasTerrae ? 0.6 : 1.2;
                double speed = hasAether ? 6.0 : 3.0;

                if (!(hasTenebrae || hasSanctus) || livingEntity instanceof EntityPlayer)
                {
                    livingEntity.motionY = motionY;
                    livingEntity.fallDistance = 0;

                    if(livingEntity instanceof EntityPlayer)
                    {
                    	switch (direction)
                        {
                            case 1:
                                SpellHelper.setPlayerSpeedFromServer((EntityPlayer) livingEntity, 0, motionY, -speed);
                                break;

                            case 2:
                                SpellHelper.setPlayerSpeedFromServer((EntityPlayer) livingEntity, speed, motionY, 0);
                                break;

                            case 3:
                                SpellHelper.setPlayerSpeedFromServer((EntityPlayer) livingEntity, 0, motionY, speed);
                                break;

                            case 4:
                                SpellHelper.setPlayerSpeedFromServer((EntityPlayer) livingEntity, -speed, motionY, 0);
                                break;
                        }
                    }else
                    {
                    	livingEntity.motionY = motionY;

                        switch (direction)
                        {
                            case 1:
                                livingEntity.motionX = 0.0;
                                livingEntity.motionZ = -speed;
                                break;

                            case 2:
                                livingEntity.motionX = speed;
                                livingEntity.motionZ = 0.0;
                                break;

                            case 3:
                                livingEntity.motionX = 0.0;
                                livingEntity.motionZ = -speed;
                                break;

                            case 4:
                                livingEntity.motionX = -speed;
                                livingEntity.motionZ = 0.0;
                                break;
                        }
                    }
                    

                    flag++;
                } else
                {
                    if ((hasSanctus && !livingEntity.isChild()) || (hasTenebrae && livingEntity.isChild()))
                    {
                        continue;
                    }

                    livingEntity.motionY = motionY;

                    switch (direction)
                    {
                        case 1:
                            livingEntity.motionX = 0.0;
                            livingEntity.motionZ = -speed;
                            break;

                        case 2:
                            livingEntity.motionX = speed;
                            livingEntity.motionZ = 0.0;
                            break;

                        case 3:
                            livingEntity.motionX = 0.0;
                            livingEntity.motionZ = -speed;
                            break;

                        case 4:
                            livingEntity.motionX = -speed;
                            livingEntity.motionZ = 0.0;
                            break;
                    }

                    if (hasTenebrae)
                    {
                        this.canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, tenebraeDrain, true);
                    }
                    if (hasSanctus)
                    {
                        this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, true);
                    }

                    livingEntity.fallDistance = 0;
                    flag++;
                }

                if (hasAether)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, true);
                }
                if (hasTerrae)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, true);
                }
                if (hasReductus)
                {
                    if (!livingEntity.isPotionActive(AlchemicalWizardry.customPotionFeatherFall))
                    {
                        livingEntity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionFeatherFall.id, 3 * 20, 0));
                        this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, true);
                    }
                }

            }

            if (flag > 0)
            {
                SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * flag);
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 5;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> leapingRitual = new ArrayList();
        leapingRitual.add(new RitualComponent(0, 0, -2, RitualComponent.DUSK));
        leapingRitual.add(new RitualComponent(1, 0, -1, RitualComponent.AIR));
        leapingRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.AIR));

        for (int i = 0; i <= 2; i++)
        {
            leapingRitual.add(new RitualComponent(2, 0, i, RitualComponent.AIR));
            leapingRitual.add(new RitualComponent(-2, 0, i, RitualComponent.AIR));
        }
        return leapingRitual;
    }
}
