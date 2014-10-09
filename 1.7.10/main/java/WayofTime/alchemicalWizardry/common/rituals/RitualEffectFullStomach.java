package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectFullStomach extends RitualEffect
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

        if (world.getWorldTime() % 20 != 0)
        {
            return;
        }
        
        double horizRange = 5;
        double vertRange = 5;
        
        List<EntityPlayer> playerList = SpellHelper.getPlayersInRange(world, x+0.5, y+0.5, z+0.5, horizRange, vertRange);
        
        if(playerList == null)
        {
        	return;
        }

        if (currentEssence < this.getCostPerRefresh() * playerList.size())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
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
        	
        	int count = 0;
        	
        	if(inventory != null)
        	{	
        		for(EntityPlayer player : playerList)
        		{
        			FoodStats foodStats = player.getFoodStats();
        			float satLevel = foodStats.getSaturationLevel();
        			
        			for(int i=0; i<inventory.getSizeInventory(); i++)
        			{
        				ItemStack stack = inventory.getStackInSlot(i);
                		
                		if(stack != null && stack.getItem() instanceof ItemFood)
                		{
                			ItemFood foodItem = (ItemFood)stack.getItem();
                			
                			int regularHeal = foodItem.func_150905_g(stack);
                			float saturatedHeal = foodItem.func_150906_h(stack);
                			
                			if(saturatedHeal + satLevel <= 20)
                			{
                				foodStats.setFoodSaturationLevel(saturatedHeal + satLevel);
                    			inventory.decrStackSize(i, 1);
                    			count++;
                    			break;
                			}
                		}
        			}
        		}	
        	}
        	
            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * count);
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 100;
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
