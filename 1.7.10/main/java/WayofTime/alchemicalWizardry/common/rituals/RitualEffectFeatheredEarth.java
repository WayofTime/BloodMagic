package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectFeatheredEarth extends RitualEffect //Nullifies all fall damage in the area of effect
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
            world.addWeatherEffect(new EntityLightningBolt(world, x + 4, y + 5, z + 4));
            world.addWeatherEffect(new EntityLightningBolt(world, x + 4, y + 5, z - 4));
            world.addWeatherEffect(new EntityLightningBolt(world, x - 4, y + 5, z - 4));
            world.addWeatherEffect(new EntityLightningBolt(world, x - 4, y + 5, z + 4));
            ritualStone.setCooldown(0);
        }

        int range = 20;
        int verticalRange = 30;
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(range, verticalRange, range));
        int entityCount = 0;
        boolean flag = false;

        for (EntityLivingBase entity : entities)
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
            for (EntityLivingBase entity : entities)
            {
                entity.fallDistance = 0;
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
		ArrayList<RitualComponent> featheredEarthRitual = new ArrayList();
        featheredEarthRitual.add(new RitualComponent(1, 0, 0, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(0, 0, -1, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(2, 0, 2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(2, 0, -2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(1, 0, 3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(0, 0, 3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(1, 0, -3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(0, 0, -3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(3, 0, 1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(3, 0, 0, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(3, 0, -1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-3, 0, 0, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(4, 4, 4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(-4, 4, 4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(-4, 4, -4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(4, 4, -4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(4, 5, 5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(4, 5, 3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(5, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(3, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, 5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, 3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-5, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-3, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(4, 5, -5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(4, 5, -3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(5, 5, -4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(3, 5, -4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, -5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, -3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-5, 5, -4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-3, 5, -4, RitualComponent.AIR));
        return featheredEarthRitual;
	}
}
