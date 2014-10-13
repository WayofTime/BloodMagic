package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryEventHooks;
import WayofTime.alchemicalWizardry.common.CoordAndRange;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RitualEffectVeilOfEvil extends RitualEffect
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

        if (currentEssence < this.getCostPerRefresh())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            int horizRange = 32;
            int vertRange = 32;

            int dimension = world.provider.dimensionId;

            if (AlchemicalWizardryEventHooks.forceSpawnMap.containsKey(new Integer(dimension)))
            {
                List<CoordAndRange> list = AlchemicalWizardryEventHooks.forceSpawnMap.get(new Integer(dimension));
                if (list != null)
                {
                    if (!list.contains(new CoordAndRange(x, y, z, horizRange, vertRange)))
                    {
                        boolean hasFoundAndRemoved = false;
                        for (CoordAndRange coords : list)
                        {
                            int xLocation = coords.xCoord;
                            int yLocation = coords.yCoord;
                            int zLocation = coords.zCoord;

                            if (xLocation == x && yLocation == y && zLocation == z)
                            {
                                list.remove(coords);
                                hasFoundAndRemoved = true;
                                break;
                            }
                        }
                        list.add(new CoordAndRange(x, y, z, horizRange, vertRange));
                    }
                } else
                {
                    list = new LinkedList();
                    list.add(new CoordAndRange(x, y, z, horizRange, vertRange));
                    AlchemicalWizardryEventHooks.forceSpawnMap.put(new Integer(dimension), list);
                }
            } else
            {
                List<CoordAndRange> list = new LinkedList();
                list.add(new CoordAndRange(x, y, z, horizRange, vertRange));
                AlchemicalWizardryEventHooks.forceSpawnMap.put(new Integer(dimension), list);
            }


            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
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
        ArrayList<RitualComponent> veilRitual = new ArrayList();

        veilRitual.add(new RitualComponent(1, 0, 2, RitualComponent.DUSK));
        veilRitual.add(new RitualComponent(2, 0, 1, RitualComponent.DUSK));
        veilRitual.add(new RitualComponent(1, 0, -2, RitualComponent.DUSK));
        veilRitual.add(new RitualComponent(-2, 0, 1, RitualComponent.DUSK));
        veilRitual.add(new RitualComponent(-1, 0, 2, RitualComponent.DUSK));
        veilRitual.add(new RitualComponent(2, 0, -1, RitualComponent.DUSK));
        veilRitual.add(new RitualComponent(-1, 0, -2, RitualComponent.DUSK));
        veilRitual.add(new RitualComponent(-2, 0, -1, RitualComponent.DUSK));

        veilRitual.add(new RitualComponent(3, 0, 3, RitualComponent.FIRE));
        veilRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.FIRE));
        veilRitual.add(new RitualComponent(3, 0, -3, RitualComponent.FIRE));
        veilRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.FIRE));

        for (int i = 0; i <= 1; i++)
        {
            veilRitual.add(new RitualComponent((4 + i), i, 0, RitualComponent.DUSK));
            veilRitual.add(new RitualComponent((4 + i), i, -1, RitualComponent.BLANK));
            veilRitual.add(new RitualComponent((4 + i), i, 1, RitualComponent.BLANK));

            veilRitual.add(new RitualComponent(-(4 + i), i, 0, RitualComponent.DUSK));
            veilRitual.add(new RitualComponent(-(4 + i), i, -1, RitualComponent.BLANK));
            veilRitual.add(new RitualComponent(-(4 + i), i, 1, RitualComponent.BLANK));

            veilRitual.add(new RitualComponent(0, i, (4 + i), RitualComponent.DUSK));
            veilRitual.add(new RitualComponent(1, i, (4 + i), RitualComponent.BLANK));
            veilRitual.add(new RitualComponent(-1, i, (4 + i), RitualComponent.BLANK));

            veilRitual.add(new RitualComponent(0, i, -(4 + i), RitualComponent.DUSK));
            veilRitual.add(new RitualComponent(1, i, -(4 + i), RitualComponent.BLANK));
            veilRitual.add(new RitualComponent(-1, i, -(4 + i), RitualComponent.BLANK));

            veilRitual.add(new RitualComponent(4, i, 5, RitualComponent.EARTH));
            veilRitual.add(new RitualComponent(5, i, 4, RitualComponent.EARTH));
            veilRitual.add(new RitualComponent(4, i, -5, RitualComponent.EARTH));
            veilRitual.add(new RitualComponent(-5, i, 4, RitualComponent.EARTH));
            veilRitual.add(new RitualComponent(-4, i, 5, RitualComponent.EARTH));
            veilRitual.add(new RitualComponent(5, i, -4, RitualComponent.EARTH));
            veilRitual.add(new RitualComponent(-4, i, -5, RitualComponent.EARTH));
            veilRitual.add(new RitualComponent(-5, i, -4, RitualComponent.EARTH));
        }

        veilRitual.add(new RitualComponent(5, 1, 5, RitualComponent.BLANK));
        veilRitual.add(new RitualComponent(-5, 1, 5, RitualComponent.BLANK));
        veilRitual.add(new RitualComponent(5, 1, -5, RitualComponent.BLANK));
        veilRitual.add(new RitualComponent(-5, 1, -5, RitualComponent.BLANK));

        return veilRitual;
    }
}