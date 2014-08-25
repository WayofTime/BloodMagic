package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectAnimalGrowth extends RitualEffect
{
	public static final int breedingCost = 50;
	public static final int reductusDrain = 1;
	public static final int virtusDrain = 10;
	
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

        if (world.getWorldTime() % 20 != 0)
        {
            return;
        }

        double range = 2;
        
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double) x, (double) y + 1, (double) z, (double) (x + 1), (double) (y + 3), (double) (z + 1)).expand(range, 0, range);
        List<EntityAgeable> list = world.getEntitiesWithinAABB(EntityAgeable.class, axisalignedbb);

        int entityCount = 0;
        boolean flag = false;

        if (currentEssence < this.getCostPerRefresh() * list.size())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            boolean hasReductus = this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);

            for(EntityAgeable entity : list)
            {
                if (entity.getGrowingAge() < 0)
                {
                    entity.addGrowth(5);
                    entityCount++;
                }else 
                {
                	hasReductus = hasReductus && this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);
                	if(hasReductus && entity instanceof EntityAnimal && entity.getGrowingAge() > 0)
                	{
                		EntityAnimal animal = (EntityAnimal)entity;
                    	entity.setGrowingAge(Math.max(0, animal.getGrowingAge() - 20*2));
                    	this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, true);
                    	entityCount++;
                	}
                }	
            }

            data.currentEssence = currentEssence - this.getCostPerRefresh() * entityCount;
            data.markDirty();
        }
        
        boolean hasVirtus = this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false);
        
        if(hasVirtus && SoulNetworkHandler.canSyphonFromOnlyNetwork(owner, breedingCost))
        {
        	List<EntityAnimal> animalList = world.getEntitiesWithinAABB(EntityAnimal.class, axisalignedbb);
        	TileEntity tile = world.getTileEntity(x, y+1, z);
        	IInventory inventory = null;
        	if(tile instanceof IInventory)
        	{
        		inventory = (IInventory)tile;
        	}else
        	{
        		tile = world.getTileEntity(x, y-1, z);
        		if(tile instanceof IInventory)
            	{
            		inventory = (IInventory)tile;
            	}
        	}
        	
        	if(inventory != null)
        	{
        		for(EntityAnimal entityAnimal : animalList)
            	{
        			if(entityAnimal.isInLove() || entityAnimal.isChild() || entityAnimal.getGrowingAge() > 0)
        			{
        				continue;
        			}
        			
        			hasVirtus = hasVirtus && this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false);
        			boolean hasLP = SoulNetworkHandler.canSyphonFromOnlyNetwork(owner, breedingCost);
        			
        			for(int i=0; i<inventory.getSizeInventory(); i++)
        			{
        				ItemStack stack = inventory.getStackInSlot(i);
                		
                		if(stack != null && entityAnimal.isBreedingItem(stack))
                		{
                			inventory.decrStackSize(i, 1);
                			entityAnimal.func_146082_f(null);
                			this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, true);
                			SoulNetworkHandler.syphonFromNetwork(owner, breedingCost);
                			break;
                		}
        			}
            	}
        	}
        }
    }

    @Override
    public int getCostPerRefresh()
    {

        return 2;
    }

    @Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> animalGrowthRitual = new ArrayList();
        animalGrowthRitual.add(new RitualComponent(0, 0, 2, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(2, 0, 0, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(0, 0, -2, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(0, 0, 1, RitualComponent.WATER));
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
