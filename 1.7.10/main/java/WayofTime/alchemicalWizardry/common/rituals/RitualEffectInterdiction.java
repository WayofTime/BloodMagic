package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualEffectInterdiction extends RitualEffect
{
    public static final int aetherDrain = 1;
    public static final int magicalesDrain = 1;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (currentEssence < this.getCostPerRefresh())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            int d0 = 5;

            List<EntityLivingBase> list = SpellHelper.getLivingEntitiesInRange(world, x + 0.5, y + 0.5, z + 0.5, d0, d0);
            boolean flag = false;

            boolean hasOffensa = this.canDrainReagent(ritualStone, ReagentRegistry.magicalesReagent, magicalesDrain, false);
            boolean playerFlag = false;

            for (EntityLivingBase entityLiving : list)
            {
                if (!((!hasOffensa && entityLiving instanceof EntityPlayer) && (SpellHelper.getUsername((EntityPlayer) entityLiving).equals(owner))))
                {
                    double xDif = entityLiving.posX - x;
                    double yDif = entityLiving.posY - (y + 1);
                    double zDif = entityLiving.posZ - z;
                    entityLiving.motionX = 0.1 * xDif;
                    entityLiving.motionY = 0.1 * yDif;
                    entityLiving.motionZ = 0.1 * zDif;

                    if (hasOffensa && entityLiving instanceof EntityPlayer)
                    {
                        SpellHelper.setPlayerSpeedFromServer((EntityPlayer) entityLiving, 0.1 * xDif, 0.1 * yDif, 0.1 * zDif);
                        playerFlag = true;
                    }
                    entityLiving.fallDistance = 0;
                    flag = true;
                }
            }

            if (playerFlag)
            {
                this.canDrainReagent(ritualStone, ReagentRegistry.magicalesReagent, magicalesDrain, true);
            }

            boolean hasAether = this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, false);

            if (hasAether)
            {
                int aetherDrainRate = 10;

                int horizontalRadius = 5;
                int verticalRadius = 5;
                List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(horizontalRadius, verticalRadius, horizontalRadius));

                if (itemList != null)
                {
                    boolean itemFlag = false;

                    for (EntityItem entity : itemList)
                    {
                        double xDif = entity.posX - x;
                        double yDif = entity.posY - (y + 1);
                        double zDif = entity.posZ - z;
                        entity.motionX = 0.1 * xDif;
                        entity.motionY = 0.1 * yDif;
                        entity.motionZ = 0.1 * zDif;

                        itemFlag = true;
                    }

                    if (itemFlag)
                    {
                        flag = true;
                        if (world.getWorldTime() % aetherDrainRate == 0)
                        {
                            this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, true);
                        }
                    }
                }
            }


            if (world.getWorldTime() % 2 == 0 && flag)
            {
                SoulNetworkHandler.syphonFromNetwork(owner, getCostPerRefresh());
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
        ArrayList<RitualComponent> interdictionRitual = new ArrayList();
        interdictionRitual.add(new RitualComponent(1, 0, 0, 4));
        interdictionRitual.add(new RitualComponent(-1, 0, 0, 4));
        interdictionRitual.add(new RitualComponent(0, 0, 1, 4));
        interdictionRitual.add(new RitualComponent(0, 0, -1, 4));
        interdictionRitual.add(new RitualComponent(-1, 0, 1, 4));
        interdictionRitual.add(new RitualComponent(1, 0, 1, 4));
        interdictionRitual.add(new RitualComponent(-1, 0, -1, 4));
        interdictionRitual.add(new RitualComponent(1, 0, -1, 4));
        return interdictionRitual;
    }
}
