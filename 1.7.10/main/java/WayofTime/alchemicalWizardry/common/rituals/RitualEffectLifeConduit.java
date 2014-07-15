package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

public class RitualEffectLifeConduit extends RitualEffect
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

        TEAltar tileAltar = null;
        boolean testFlag = false;

        for (int i = -5; i <= 5; i++)
        {
            for (int j = -5; j <= 5; j++)
            {
                for (int k = -10; k <= 10; k++)
                {
                    if (world.getTileEntity(x + i, y + k, z + j) instanceof TEAltar)
                    {
                        tileAltar = (TEAltar) world.getTileEntity(x + i, y + k, z + j);
                        testFlag = true;
                    }
                }
            }
        }

        if (!testFlag)
        {
            return;
        }

        //tileAltar = (TEAltar)world.getBlockTileEntity(x,y-1,z);
        int d0 = 15;
        int vertRange = 20;
        
        EntityPlayer entityOwner = null;
        List<EntityPlayer> list = SpellHelper.getPlayersInRange(world, x, y, z, d0, vertRange);
        
        for(EntityPlayer player : list)
        {
        	if(SpellHelper.getUsername(player).equals(owner))
        	{
            	entityOwner = player;
        	}
        }

        if (entityOwner == null)
        {
            return;
        }
        
        int fillAmount = Math.min(currentEssence/2,tileAltar.fill(ForgeDirection.UP, new FluidStack(AlchemicalWizardry.lifeEssenceFluid,10000), false));
        
        {
        	tileAltar.fill(ForgeDirection.UP, new FluidStack(AlchemicalWizardry.lifeEssenceFluid,fillAmount), true);
        	if(entityOwner.getHealth() > 2.0f && fillAmount != 0)
        	{
        		entityOwner.setHealth(2.0f);
        	}
        	data.currentEssence = currentEssence - fillAmount*2;
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
		ArrayList<RitualComponent> conduitRitual = new ArrayList();
        
		conduitRitual.add(new RitualComponent(-1,0,-1,RitualComponent.FIRE));
		conduitRitual.add(new RitualComponent(-1,0,1,RitualComponent.FIRE));
		conduitRitual.add(new RitualComponent(1,0,1,RitualComponent.FIRE));
		conduitRitual.add(new RitualComponent(1,0,-1,RitualComponent.FIRE));
		
		for(int i=0; i<4; i++)
		{
			conduitRitual.add(new RitualComponent(-2,i,-2,RitualComponent.AIR));
			conduitRitual.add(new RitualComponent(-2,i,2,RitualComponent.AIR));
			conduitRitual.add(new RitualComponent(2,i,2,RitualComponent.AIR));
			conduitRitual.add(new RitualComponent(2,i,-2,RitualComponent.AIR));
		}
		
		conduitRitual.add(new RitualComponent(4,1,4,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(4,1,-4,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(-4,1,-4,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(-4,1,4,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(3,1,4,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(4,1,3,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(-3,1,4,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(-4,1,3,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(3,1,-4,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(4,1,-3,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(-3,1,-4,RitualComponent.EARTH));
		conduitRitual.add(new RitualComponent(-4,1,-3,RitualComponent.EARTH));

		
		for(int i=0; i<2; i++)
		{
			conduitRitual.add(new RitualComponent(4,i+2,4,RitualComponent.WATER));
			conduitRitual.add(new RitualComponent(4,i+2,-4,RitualComponent.WATER));
			conduitRitual.add(new RitualComponent(-4,i+2,-4,RitualComponent.WATER));
			conduitRitual.add(new RitualComponent(-4,i+2,4,RitualComponent.WATER));
		}
		
		conduitRitual.add(new RitualComponent(4,4,4,RitualComponent.DUSK));
		conduitRitual.add(new RitualComponent(4,4,-4,RitualComponent.DUSK));
		conduitRitual.add(new RitualComponent(-4,4,-4,RitualComponent.DUSK));
		conduitRitual.add(new RitualComponent(-4,4,4,RitualComponent.DUSK));
		
		conduitRitual.add(new RitualComponent(6,0,5,RitualComponent.FIRE));
		conduitRitual.add(new RitualComponent(5,0,6,RitualComponent.FIRE));
		conduitRitual.add(new RitualComponent(-6,0,5,RitualComponent.FIRE));
		conduitRitual.add(new RitualComponent(-5,0,6,RitualComponent.FIRE));
		conduitRitual.add(new RitualComponent(6,0,-5,RitualComponent.FIRE));
		conduitRitual.add(new RitualComponent(5,0,-6,RitualComponent.FIRE));
		conduitRitual.add(new RitualComponent(-6,0,-5,RitualComponent.FIRE));
		conduitRitual.add(new RitualComponent(-5,0,-6,RitualComponent.FIRE));

		for(int i=0; i<2; i++)
		{
			conduitRitual.add(new RitualComponent(6,i,6,RitualComponent.FIRE));
			conduitRitual.add(new RitualComponent(6,i,-6,RitualComponent.FIRE));
			conduitRitual.add(new RitualComponent(-6,i,6,RitualComponent.FIRE));
			conduitRitual.add(new RitualComponent(-6,i,-6,RitualComponent.FIRE));
		}
		
		for(int i=0; i<3; i++)
		{
			conduitRitual.add(new RitualComponent(6,i+2,6,RitualComponent.BLANK));
			conduitRitual.add(new RitualComponent(6,i+2,-6,RitualComponent.BLANK));
			conduitRitual.add(new RitualComponent(-6,i+2,6,RitualComponent.BLANK));
			conduitRitual.add(new RitualComponent(-6,i+2,-6,RitualComponent.BLANK));
		}
		
		conduitRitual.add(new RitualComponent(6,5,6,RitualComponent.DUSK));
		conduitRitual.add(new RitualComponent(6,5,-6,RitualComponent.DUSK));
		conduitRitual.add(new RitualComponent(-6,5,6,RitualComponent.DUSK));
		conduitRitual.add(new RitualComponent(-6,5,-6,RitualComponent.DUSK));
		
        return conduitRitual;
	}
}
