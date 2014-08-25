package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryEventHooks;
import WayofTime.alchemicalWizardry.common.CoordAndRange;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectSpawnWard extends RitualEffect
{
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

//        if (world.getWorldTime() % 20 != 0)
//        {
//            return;
//        }

        if (currentEssence < this.getCostPerRefresh())
        {
        	SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
        	int horizRange = 32;
        	int vertRange = 32;
        	
        	int dimension = world.provider.dimensionId;
        	
        	if(AlchemicalWizardryEventHooks.respawnMap.containsKey(new Integer(dimension)))
        	{
        		List<CoordAndRange> list = AlchemicalWizardryEventHooks.respawnMap.get(new Integer(dimension));
        		if(list != null)
        		{
        			if(!list.contains(new CoordAndRange(x,y,z,horizRange,vertRange)))
        			{
        				boolean hasFoundAndRemoved = false;
        				for(CoordAndRange coords : list)
            			{
            				int xLocation = coords.xCoord;
            				int yLocation = coords.yCoord;
            				int zLocation = coords.zCoord;
            				
            				if(xLocation == x && yLocation == y && zLocation == z)
            				{
            					list.remove(coords);
            					hasFoundAndRemoved = true;
            					break;
            				}
            			}
        				list.add(new CoordAndRange(x,y,z,horizRange,vertRange));
        			}	
        		}else
        		{
        			list = new LinkedList();
            		list.add(new CoordAndRange(x,y,z,horizRange,vertRange));
            		AlchemicalWizardryEventHooks.respawnMap.put(new Integer(dimension), list);
        		}
        	}else
        	{
        		List<CoordAndRange> list = new LinkedList();
        		list.add(new CoordAndRange(x,y,z,horizRange,vertRange));
        		AlchemicalWizardryEventHooks.respawnMap.put(new Integer(dimension), list);
        	}
        	

            data.currentEssence = currentEssence - this.getCostPerRefresh();
            data.markDirty();
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
		ArrayList<RitualComponent> animalGrowthRitual = new ArrayList();
        animalGrowthRitual.add(new RitualComponent(0, 0, 2, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(2, 0, 0, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(0, 0, -2, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(1, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(0, 0, -1, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(1, 0, 2, RitualComponent.EARTH));
        animalGrowthRitual.add(new RitualComponent(-1, 0, 2, RitualComponent.EARTH));
        animalGrowthRitual.add(new RitualComponent(1, 0, -2, RitualComponent.EARTH));
        animalGrowthRitual.add(new RitualComponent(-1, 0, -2, RitualComponent.EARTH));
        animalGrowthRitual.add(new RitualComponent(2, 0, 1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(2, 0, -1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(-2, 0, 1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(-2, 0, -1, RitualComponent.AIR));
        return animalGrowthRitual;
	}
}
