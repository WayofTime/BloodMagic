package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.alchemicalWizardry.api.harvest.HarvestRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectHarvest extends RitualEffect
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
        int maxCount = 9;

        if (currentEssence < this.getCostPerRefresh() * maxCount)
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            if (world.getWorldTime() % 20 != 0)
            {
                return;
            }

            int flag = 0;
            int range = 4;

            for (int i = -range; i <= range; i++)
            {
                for (int j = -range; j <= range; j++)
                {
                	for(int k = -range; k<=range; k++)
                	{
                        if(HarvestRegistry.harvestBlock(world, x + i, y + j, z + k))
                        {
                        	flag++;
                        }
                	}
                }
            }

            if (flag > 0)
            {
                data.currentEssence = currentEssence - this.getCostPerRefresh() * Math.min(maxCount, flag);
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
		ArrayList<RitualComponent> harvestRitual = new ArrayList();

		harvestRitual.add(new RitualComponent(1,0,1,RitualComponent.DUSK));
		harvestRitual.add(new RitualComponent(1,0,-1,RitualComponent.DUSK));
		harvestRitual.add(new RitualComponent(-1,0,-1,RitualComponent.DUSK));
		harvestRitual.add(new RitualComponent(-1,0,1,RitualComponent.DUSK));
		harvestRitual.add(new RitualComponent(2,0,0,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(-2,0,0,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(0,0,2,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(0,0,-2,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(3,0,1,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(3,0,-1,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(-3,0,1,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(-3,0,-1,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(1,0,3,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(-1,0,3,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(1,0,-3,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(-1,0,-3,RitualComponent.EARTH));
		harvestRitual.add(new RitualComponent(2,0,3,RitualComponent.WATER));
		harvestRitual.add(new RitualComponent(3,0,2,RitualComponent.WATER));
		harvestRitual.add(new RitualComponent(2,0,-3,RitualComponent.WATER));
		harvestRitual.add(new RitualComponent(-3,0,2,RitualComponent.WATER));
		harvestRitual.add(new RitualComponent(-2,0,3,RitualComponent.WATER));
		harvestRitual.add(new RitualComponent(3,0,-2,RitualComponent.WATER));
		harvestRitual.add(new RitualComponent(-2,0,-3,RitualComponent.WATER));
		harvestRitual.add(new RitualComponent(-3,0,-2,RitualComponent.WATER));

		
        return harvestRitual;
	}
}
