package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualEffectContainment extends RitualEffect
{
    public static final String[] TIME_SINCE_IGNITED = new String[]{"timeSinceIgnited", "field_70833_d", "bq"};
    public static final int crepitousDrain = 1;
    public static final int terraeDrain = 3;
    public static final int magicalesDrain = 10;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        if (currentEssence < this.getCostPerRefresh())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            int d0 = 5;
            List<Entity> entityList = SpellHelper.getEntitiesInRange(world, x + 0.5, y + 0.5, z + 0.5, d0, d0);
            boolean flag = false;
            boolean hasCrepitous = this.canDrainReagent(ritualStone, ReagentRegistry.crepitousReagent, crepitousDrain, false);
            boolean hasTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, false);
            boolean hasMagicales = this.canDrainReagent(ritualStone, ReagentRegistry.magicalesReagent, magicalesDrain, false);

            for (Entity entity : entityList)
            {
                if (!(entity instanceof EntityLivingBase))
                {
                    continue;
                }

                EntityLivingBase livingEntity = (EntityLivingBase) entity;

                if (livingEntity instanceof EntityPlayer)
                {
                    continue;
                }


                double xDif = livingEntity.posX - (x + 0.5);
                double yDif = livingEntity.posY - (y + 3);
                double zDif = livingEntity.posZ - (z + 0.5);
                livingEntity.motionX = -0.05 * xDif;
                livingEntity.motionY = -0.05 * yDif;
                livingEntity.motionZ = -0.05 * zDif;
                flag = true;

                livingEntity.fallDistance = 0;

                if (hasMagicales && this.canDrainReagent(ritualStone, ReagentRegistry.magicalesReagent, magicalesDrain, false))
                {
                    if (!livingEntity.isPotionActive(AlchemicalWizardry.customPotionPlanarBinding))
                    {
                        livingEntity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionPlanarBinding.id, 100, 0));
                        this.canDrainReagent(ritualStone, ReagentRegistry.magicalesReagent, magicalesDrain, true);
                    }
                }

                if (hasCrepitous && this.canDrainReagent(ritualStone, ReagentRegistry.crepitousReagent, crepitousDrain, false))
                {
                    if (entity instanceof EntityCreeper)
                    {
                        ReflectionHelper.setPrivateValue(EntityCreeper.class, (EntityCreeper) entity, 2, TIME_SINCE_IGNITED);
                        ((EntityCreeper) entity).setAttackTarget(null);
                        this.canDrainReagent(ritualStone, ReagentRegistry.crepitousReagent, crepitousDrain, true);
                    }
                }
            }

            if (world.getWorldTime() % 2 == 0 && flag)
            {
                SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 1;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> containmentRitual = new ArrayList();
        containmentRitual.add(new RitualComponent(1, 0, 0, 3));
        containmentRitual.add(new RitualComponent(-1, 0, 0, 3));
        containmentRitual.add(new RitualComponent(0, 0, 1, 3));
        containmentRitual.add(new RitualComponent(0, 0, -1, 3));
        containmentRitual.add(new RitualComponent(2, 0, 2, 3));
        containmentRitual.add(new RitualComponent(2, 0, -2, 3));
        containmentRitual.add(new RitualComponent(-2, 0, 2, 3));
        containmentRitual.add(new RitualComponent(-2, 0, -2, 3));
        containmentRitual.add(new RitualComponent(1, 5, 0, 3));
        containmentRitual.add(new RitualComponent(-1, 5, 0, 3));
        containmentRitual.add(new RitualComponent(0, 5, 1, 3));
        containmentRitual.add(new RitualComponent(0, 5, -1, 3));
        containmentRitual.add(new RitualComponent(2, 5, 2, 3));
        containmentRitual.add(new RitualComponent(2, 5, -2, 3));
        containmentRitual.add(new RitualComponent(-2, 5, 2, 3));
        containmentRitual.add(new RitualComponent(-2, 5, -2, 3));
        return containmentRitual;
    }
}
