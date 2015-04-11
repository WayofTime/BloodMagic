package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.alchemy.energy.IReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.omega.OmegaParadigm;
import WayofTime.alchemicalWizardry.common.omega.OmegaRegistry;
import WayofTime.alchemicalWizardry.common.omega.OmegaStructureHandler;
import WayofTime.alchemicalWizardry.common.omega.OmegaStructureParameters;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectOmegaTest extends RitualEffect
{
	public static final boolean isTesting = true;
	public static final int drainTotal = 32 * 1000;
	
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (world.getWorldTime() % 200 != 0)
        {
            return;
        }

        OmegaStructureParameters param = OmegaStructureHandler.getStructureStabilityFactor(world, x, y, z, 5, new Int3(0,1,0));
        int stab = param.stability;
        int enchantability = param.enchantability;
        int enchantmentLevel = param.enchantmentLevel;
        
        System.out.println("Stability: " + stab + ", Enchantability: " + enchantability + ", Enchantment Level: " + enchantmentLevel);
        
        double range = 0.5;

        List<EntityPlayer> playerList = SpellHelper.getPlayersInRange(world, x + 0.5, y + 1.5, z + 0.5, range, range);
        
        Reagent reagent = null;
        
        Map<Reagent, Integer> reagentMap = new HashMap();
    	for(int i=0; i<4; i++)
    	{
    		Int3 jarLoc = this.getJarLocation(i);
    		TileEntity tile = world.getTileEntity(x + jarLoc.xCoord, y + jarLoc.yCoord, z + jarLoc.zCoord);
    		if(tile instanceof IReagentHandler)
    		{
    			IReagentHandler container = (IReagentHandler)tile;
    			ReagentContainerInfo[] containerInfoArray = container.getContainerInfo(ForgeDirection.UP);
    			if(containerInfoArray == null)
    			{
    				continue;
    			}
    			
    			for(ReagentContainerInfo containerInfo : containerInfoArray)
    			{
    				ReagentStack containedReagent = containerInfo.reagent;
    				if(containedReagent == null)
    				{
    					continue;
    				}
    				Reagent rea = containedReagent.reagent;
    				int amt = containedReagent.amount;
    				if(reagentMap.containsKey(rea))
    				{
        				reagentMap.put(rea, reagentMap.get(rea) + amt);
    				}else
    				{
        				reagentMap.put(rea, amt);
    				}
    			}
    		}
    	}
    	
    	for(Entry<Reagent, Integer> entry : reagentMap.entrySet())
    	{
    		if(entry.getValue() >= drainTotal)
    		{
        		reagent = entry.getKey();
        		break;
    		}
    	}
    	
    	if(reagent == null)
    	{
    		return;
    	}
    	
    	int tickDuration = isTesting ? 20 * 30 : 15 * 20 * 60 + (int)((15 * 20 * 60) * Math.sqrt(stab / 700));
    	
    	int affinity = 0;
        
        for(EntityPlayer player : playerList)
        {        	
        	OmegaParadigm waterParadigm = OmegaRegistry.getParadigmForReagent(reagent);
        	if(waterParadigm != null && waterParadigm.convertPlayerArmour(player, x, y, z, stab, affinity, enchantability, enchantmentLevel))
        	{
        		APISpellHelper.setPlayerCurrentReagentAmount(player, tickDuration);
            	APISpellHelper.setPlayerMaxReagentAmount(player, tickDuration);
            	APISpellHelper.setPlayerReagentType(player, reagent);
            	APISpellHelper.setCurrentAdditionalMaxHP(player, waterParadigm.getMaxAdditionalHealth());
    			NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getReagentBarPacket(reagent, APISpellHelper.getPlayerCurrentReagentAmount(player), APISpellHelper.getPlayerMaxReagentAmount(player)), (EntityPlayerMP)player);
    			
    			if(!isTesting)
    			{
    				int drainLeft = this.drainTotal;
    				for(int i = 0; i < 4; i++)
    				{
    					if(drainLeft <= 0)
    					{
    						break;
    					}
    					Int3 jarLoc = this.getJarLocation(i);
    		    		TileEntity tile = world.getTileEntity(x + jarLoc.xCoord, y + jarLoc.yCoord, z + jarLoc.zCoord);
    		    		if(tile instanceof IReagentHandler)
    		    		{
    		    			IReagentHandler container = (IReagentHandler)tile;
    		    			ReagentStack drained = container.drain(ForgeDirection.UP, new ReagentStack(reagent, drainLeft), true);
    		    			if(drained != null)
    		    			{
        		    			drainLeft -= drained.amount;
        		    			world.markBlockForUpdate(x + jarLoc.xCoord, y + jarLoc.yCoord, z + jarLoc.zCoord);
    		    			}
    		    		}
    				}
    				
    				ritualStone.setActive(false);
    			}
    			
    			break;
        	}
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
        ArrayList<RitualComponent> animalGrowthRitual = new ArrayList();
        animalGrowthRitual.add(new RitualComponent(0, 0, 2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(2, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(0, 0, -2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(0, 0, 1, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(1, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(0, 0, -1, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(1, 0, 2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(-1, 0, 2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(1, 0, -2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(-1, 0, -2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(2, 0, 1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(2, 0, -1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(-2, 0, 1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(-2, 0, -1, RitualComponent.AIR));
        return animalGrowthRitual;
    }
    
    public Int3 getJarLocation(int i)
    {
    	switch(i)
    	{
    	case 0:
    		return new Int3(-3,0,0);
    	case 1:
    		return new Int3(3,0,0);
    	case 2:
    		return new Int3(0,0,-3);
    	case 3:
    		return new Int3(0,0,3);
    	default:
    		return new Int3(0,0,0);
    	}
    }
}
