package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectGrowth extends RitualEffect
{
	private static final int aquasalusDrain = 10;
	private static final int terraeDrain = 20;
	private static final int orbisTerraeDrain = 20;
	
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

        if (currentEssence < this.getCostPerRefresh()*9)
        {
        	SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
        	boolean hasTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, false);
        	boolean hasOrbisTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, orbisTerraeDrain, false);

        	int speed = this.getSpeedForReagents(hasTerrae, hasOrbisTerrae);
            if (world.getWorldTime() % speed != 0)
            {
                return;
            }
            
            if(this.canDrainReagent(ritualStone, ReagentRegistry.aquasalusReagent, aquasalusDrain, false))
        	{
        		int hydrationRange = 1;
            	for(int i=-hydrationRange; i<=hydrationRange; i++)
            	{
            		for(int j=-hydrationRange; j<=hydrationRange; j++)
            		{
                    	if(this.canDrainReagent(ritualStone, ReagentRegistry.aquasalusReagent, aquasalusDrain, false))
                    	{
                    		if(SpellHelper.hydrateSoil(world, x + i, y + 1, z + j))
                    		{
                				this.canDrainReagent(ritualStone, ReagentRegistry.aquasalusReagent, aquasalusDrain, true);
                    		}
                    	}
            		}
            	}
        	}

            int flag = 0;

            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    Block block = world.getBlock(x + i, y + 2, z + j);

                    if (block instanceof IPlantable)
                    {
                        {
                            SpellHelper.sendIndexedParticleToAllAround(world, x, y, z, 20, world.provider.dimensionId, 3, x, y, z);
                            block.updateTick(world, x + i, y + 2, z + j, world.rand);
                            flag++;
                        }
                    }
                }
            }

            if (flag > 0)
            {
            	this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, true);
            	this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, orbisTerraeDrain, true);

                data.currentEssence = currentEssence - this.getCostPerRefresh()*flag;
                data.markDirty();
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
		ArrayList<RitualComponent> growthRitual = new ArrayList();
        growthRitual.add(new RitualComponent(1, 0, 0, 1));
        growthRitual.add(new RitualComponent(-1, 0, 0, 1));
        growthRitual.add(new RitualComponent(0, 0, 1, 1));
        growthRitual.add(new RitualComponent(0, 0, -1, 1));
        growthRitual.add(new RitualComponent(-1, 0, 1, 3));
        growthRitual.add(new RitualComponent(1, 0, 1, 3));
        growthRitual.add(new RitualComponent(-1, 0, -1, 3));
        growthRitual.add(new RitualComponent(1, 0, -1, 3));
        return growthRitual;
	}
    
    public int getSpeedForReagents(boolean hasTerrae, boolean hasOrbisTerrae)
    {
    	if(hasOrbisTerrae)
    	{
    		if(hasTerrae)
    		{
    			return 10;
    		}else
    		{
    			return 15;
    		}
    	}else
    	{
    		if(hasTerrae)
    		{
    			return 20;
    		}else
    		{
    			return 30;
    		}
    	}
    }
}
