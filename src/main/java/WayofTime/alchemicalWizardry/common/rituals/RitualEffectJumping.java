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

public class RitualEffectJumping extends RitualEffect
{
    public static final int aetherDrain = 10;
    public static final int terraeDrain = 10;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        double range = 0.5;
        List<EntityLivingBase> livingList = SpellHelper.getLivingEntitiesInRange(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, range, range);

        if (currentEssence < this.getCostPerRefresh() * livingList.size())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            int flag = 0;

            boolean hasAether = this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, false);
            boolean hasTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, false);

            for (EntityLivingBase livingEntity : livingList)
            {
                if (livingEntity.isSneaking())
                {
                    continue;
                }

                hasAether = hasAether && this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, false);
                hasTerrae = hasTerrae && this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, false);

                double motionY = 1.5 * (hasAether ? 2 : 1);

                if (livingEntity instanceof EntityPlayer)
                {
                    SpellHelper.setPlayerSpeedFromServer((EntityPlayer) livingEntity, livingEntity.motionX, motionY, livingEntity.motionZ);
                    livingEntity.motionY = motionY;
                    livingEntity.fallDistance = 0;
                    flag++;
                } else
                {
                    livingEntity.motionY = motionY;
                    livingEntity.fallDistance = 0;
                    flag++;
                }

                if (hasAether)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, true);
                }
                if (hasTerrae)
                {
                    if (!livingEntity.isPotionActive(AlchemicalWizardry.customPotionFeatherFall))
                    {
                        livingEntity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionFeatherFall.id, 5 * 20, 0));
                        this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, true);
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
        ArrayList<RitualComponent> jumpingRitual = new ArrayList();

        for (int i = -1; i <= 1; i++)
        {
            jumpingRitual.add(new RitualComponent(1, i, 1, RitualComponent.AIR));
            jumpingRitual.add(new RitualComponent(-1, i, 1, RitualComponent.AIR));
            jumpingRitual.add(new RitualComponent(-1, i, -1, RitualComponent.AIR));
            jumpingRitual.add(new RitualComponent(1, i, -1, RitualComponent.AIR));
        }
        return jumpingRitual;
    }
}
