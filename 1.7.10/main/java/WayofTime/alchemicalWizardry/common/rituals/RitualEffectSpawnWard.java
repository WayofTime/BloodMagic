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

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
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
        	

            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 15;
    }

    @Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> wardRitualRitual = new ArrayList();
		
		for(int i=2; i<=4; i++)
		{
	        if(i <= 3)
	        {
	        	wardRitualRitual.add(new RitualComponent(0, 0, i, RitualComponent.AIR));
		        wardRitualRitual.add(new RitualComponent(0, 0, -i, RitualComponent.AIR));
		        wardRitualRitual.add(new RitualComponent(i, 0, 0, RitualComponent.AIR));
		        wardRitualRitual.add(new RitualComponent(-i, 0, 0, RitualComponent.AIR));
	        }
	        
	        wardRitualRitual.add(new RitualComponent(i, 0, i, RitualComponent.FIRE));
	        wardRitualRitual.add(new RitualComponent(i, 0, -i, RitualComponent.FIRE));
	        wardRitualRitual.add(new RitualComponent(-i, 0, -i, RitualComponent.FIRE));
	        wardRitualRitual.add(new RitualComponent(-i, 0, i, RitualComponent.FIRE));
		}
		
        wardRitualRitual.add(new RitualComponent(0, 0, 5, RitualComponent.DUSK));
        wardRitualRitual.add(new RitualComponent(0, 0, -5, RitualComponent.DUSK));
        wardRitualRitual.add(new RitualComponent(5, 0, 0, RitualComponent.DUSK));
        wardRitualRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.DUSK));

        wardRitualRitual.add(new RitualComponent(6, 0, 5, RitualComponent.WATER));
        wardRitualRitual.add(new RitualComponent(5, 0, 6, RitualComponent.WATER));
        wardRitualRitual.add(new RitualComponent(6, 0, -5, RitualComponent.WATER));
        wardRitualRitual.add(new RitualComponent(-5, 0, 6, RitualComponent.WATER));
        wardRitualRitual.add(new RitualComponent(-6, 0, 5, RitualComponent.WATER));
        wardRitualRitual.add(new RitualComponent(5, 0, -6, RitualComponent.WATER));
        wardRitualRitual.add(new RitualComponent(-6, 0, -5, RitualComponent.WATER));
        wardRitualRitual.add(new RitualComponent(-5, 0, -6, RitualComponent.WATER));
        
        return wardRitualRitual;
	}
}
