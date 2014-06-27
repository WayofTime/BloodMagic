package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectFlight extends RitualEffect
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

        if (ritualStone.getCooldown() > 0)
        {
            //TODO Cool stuffs
            ritualStone.setCooldown(0);
        }

        int range = 20;
        int verticalRange = 30;
        AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(range, verticalRange, range);
        axis.maxY = 256;
        axis.minY = 0;
        List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, axis);
        int entityCount = 0;

        for (EntityPlayer entity : entities)
        {
            entityCount++;
        }

        if (currentEssence < this.getCostPerRefresh() * entityCount)
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            for (EntityPlayer entity : entities)
            {
                entity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionFlight.id, 20, 0));
            }

            data.currentEssence = currentEssence - this.getCostPerRefresh() * entityCount;
            data.markDirty();
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

    @Override
    public int getInitialCooldown()
    {
        return 1;
    }

    @Override
	public List<RitualComponent> getRitualComponentList() 
	{
		 ArrayList<RitualComponent> flightRitual = new ArrayList();
	        flightRitual.add(new RitualComponent(1, 0, 0, RitualComponent.DUSK));
	        flightRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.DUSK));
	        flightRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
	        flightRitual.add(new RitualComponent(0, 0, -1, RitualComponent.DUSK));
	        flightRitual.add(new RitualComponent(2, 0, 2, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(2, 0, -2, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(1, 0, 3, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(0, 0, 3, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(1, 0, -3, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(0, 0, -3, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(3, 0, 1, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(3, 0, 0, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(3, 0, -1, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(-3, 0, 0, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.EARTH));
	        flightRitual.add(new RitualComponent(-3, 0, -4, RitualComponent.WATER));
	        flightRitual.add(new RitualComponent(-4, 0, -3, RitualComponent.WATER));
	        flightRitual.add(new RitualComponent(-3, 0, 4, RitualComponent.WATER));
	        flightRitual.add(new RitualComponent(4, 0, -3, RitualComponent.WATER));
	        flightRitual.add(new RitualComponent(3, 0, -4, RitualComponent.WATER));
	        flightRitual.add(new RitualComponent(-4, 0, 3, RitualComponent.WATER));
	        flightRitual.add(new RitualComponent(3, 0, 4, RitualComponent.WATER));
	        flightRitual.add(new RitualComponent(4, 0, 3, RitualComponent.WATER));
	        flightRitual.add(new RitualComponent(-1, 1, 0, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(1, 1, 0, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(0, 1, -1, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(0, 1, 1, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(-2, 1, 0, RitualComponent.BLANK));
	        flightRitual.add(new RitualComponent(2, 1, 0, RitualComponent.BLANK));
	        flightRitual.add(new RitualComponent(0, 1, -2, RitualComponent.BLANK));
	        flightRitual.add(new RitualComponent(0, 1, 2, RitualComponent.BLANK));
	        flightRitual.add(new RitualComponent(-4, 1, 0, RitualComponent.BLANK));
	        flightRitual.add(new RitualComponent(4, 1, 0, RitualComponent.BLANK));
	        flightRitual.add(new RitualComponent(0, 1, -4, RitualComponent.BLANK));
	        flightRitual.add(new RitualComponent(0, 1, 4, RitualComponent.BLANK));
	        flightRitual.add(new RitualComponent(-5, 1, 0, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(5, 1, 0, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(0, 1, -5, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(0, 1, 5, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(5, 0, 0, RitualComponent.DUSK));
	        flightRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.DUSK));
	        flightRitual.add(new RitualComponent(0, 0, 5, RitualComponent.DUSK));
	        flightRitual.add(new RitualComponent(0, 0, -5, RitualComponent.DUSK));

	        for (int i = 2; i <= 4; i++)
	        {
	            flightRitual.add(new RitualComponent(-i, 2, 0, RitualComponent.EARTH));
	            flightRitual.add(new RitualComponent(i, 2, 0, RitualComponent.EARTH));
	            flightRitual.add(new RitualComponent(0, 2, -i, RitualComponent.EARTH));
	            flightRitual.add(new RitualComponent(0, 2, i, RitualComponent.EARTH));
	        }

	        flightRitual.add(new RitualComponent(2, 4, 1, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(1, 4, 2, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(-2, 4, 1, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(1, 4, -2, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(2, 4, -1, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(-1, 4, 2, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(-2, 4, -1, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(-1, 4, -2, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(2, 4, 2, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(-2, 4, 2, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(2, 4, -2, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(-2, 4, -2, RitualComponent.AIR));
	        flightRitual.add(new RitualComponent(-4, 2, -4, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(4, 2, 4, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(4, 2, -4, RitualComponent.FIRE));
	        flightRitual.add(new RitualComponent(-4, 2, 4, RitualComponent.FIRE));

	        for (int i = -1; i <= 1; i++)
	        {
	            flightRitual.add(new RitualComponent(3, 4, i, RitualComponent.EARTH));
	            flightRitual.add(new RitualComponent(-3, 4, i, RitualComponent.EARTH));
	            flightRitual.add(new RitualComponent(i, 4, 3, RitualComponent.EARTH));
	            flightRitual.add(new RitualComponent(i, 4, -3, RitualComponent.EARTH));
	        }
	        return flightRitual;
	}
}
