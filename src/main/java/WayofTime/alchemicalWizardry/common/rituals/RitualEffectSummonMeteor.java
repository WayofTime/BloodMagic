package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityMeteor;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualEffectSummonMeteor extends RitualEffect
{
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (ritualStone.getCooldown() > 0)
        {
            ritualStone.setCooldown(0);
        }

        if (currentEssence < this.getCostPerRefresh())
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            List<EntityItem> entities = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1));

            if (entities == null)
            {
                return;
            }

            for (EntityItem entityItem : entities)
            {
                if (entityItem != null && MeteorRegistry.isValidParadigmItem(entityItem.getEntityItem()))
                {
                    int meteorID = MeteorRegistry.getParadigmIDForItem(entityItem.getEntityItem());
                    EntityMeteor meteor = new EntityMeteor(world, x + 0.5f, 257, z + 0.5f, meteorID);
                    meteor.motionY = -1.0f;

                    if (this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, 1000, true))
                    {
                        meteor.hasTerrae = true;
                    }
                    if (this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, 1000, true))
                    {
                        meteor.hasOrbisTerrae = true;
                    }
                    if (this.canDrainReagent(ritualStone, ReagentRegistry.crystallosReagent, 1000, true))
                    {
                        meteor.hasCrystallos = true;
                    }
                    if (this.canDrainReagent(ritualStone, ReagentRegistry.incendiumReagent, 1000, true))
                    {
                        meteor.hasIncendium = true;
                    }
                    if (this.canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, 1000, true))
                    {
                        meteor.hasTennebrae = true;
                    }

                    entityItem.setDead();
                    world.spawnEntityInWorld(meteor);
                    ritualStone.setActive(false);
                    break;
                }
            }

            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> meteorRitual = new ArrayList();
        meteorRitual.add(new RitualComponent(2, 0, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 0, 2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 0, -2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 0, 1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(3, 0, -1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(1, 0, 3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(1, 0, -3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(4, 0, 2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(4, 0, -2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-4, 0, 2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-4, 0, -2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(2, 0, 4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-2, 0, 4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(2, 0, -4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-2, 0, -4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(5, 0, 3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(5, 0, -3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-5, 0, 3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-5, 0, -3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(3, 0, 5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-3, 0, 5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(3, 0, -5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-3, 0, -5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-4, 0, -4, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-4, 0, 4, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(4, 0, 4, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(4, 0, -4, RitualComponent.DUSK));

        for (int i = 4; i <= 6; i++)
        {
            meteorRitual.add(new RitualComponent(i, 0, 0, RitualComponent.EARTH));
            meteorRitual.add(new RitualComponent(-i, 0, 0, RitualComponent.EARTH));
            meteorRitual.add(new RitualComponent(0, 0, i, RitualComponent.EARTH));
            meteorRitual.add(new RitualComponent(0, 0, -i, RitualComponent.EARTH));
        }

        meteorRitual.add(new RitualComponent(8, 0, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(-8, 0, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 0, 8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 0, -8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(8, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(-8, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, 8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, -8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(7, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(-7, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, 7, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, -7, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(7, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-7, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, 7, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, -7, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(6, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-6, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, 6, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, -6, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(6, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-6, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, 6, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, -6, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(5, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-5, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, 5, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, -5, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(5, 4, 0, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-5, 4, 0, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(0, 4, 5, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(0, 4, -5, RitualComponent.AIR));

        for (int i = -1; i <= 1; i++)
        {
            meteorRitual.add(new RitualComponent(i, 4, 4, RitualComponent.AIR));
            meteorRitual.add(new RitualComponent(i, 4, -4, RitualComponent.AIR));
            meteorRitual.add(new RitualComponent(4, 4, i, RitualComponent.AIR));
            meteorRitual.add(new RitualComponent(-4, 4, i, RitualComponent.AIR));
        }

        meteorRitual.add(new RitualComponent(2, 4, 4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(4, 4, 2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(2, 4, -4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-4, 4, 2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-2, 4, 4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(4, 4, -2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-2, 4, -4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-4, 4, -2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(2, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, 2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-2, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, -2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, -3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(2, 4, -3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, 2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-2, 4, -3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, -2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, -3, RitualComponent.FIRE));
        return meteorRitual;
    }
}
