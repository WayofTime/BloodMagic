package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfEnvironmentalWind extends SelfSpellEffect
{
	public SelfEnvironmentalWind(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		double radius = 1.5d*this.potencyUpgrades+1;
		double posX = player.posX;
		double posY = player.posY-0.7d;
		double posZ = player.posZ;
		double wantedVel = 0.7d+0.7d*this.powerUpgrades;
		
		List<Entity> entities = SpellHelper.getEntitiesInRange(world, posX, posY, posZ, radius, radius);
		
		for(Entity entity: entities)
		{
			if((!entity.equals(player))&&entity instanceof EntityLiving)
			{
				double dist = Math.sqrt(entity.getDistanceToEntity(player));
				double xVel = wantedVel*(entity.posX - posX)/dist;
				double yVel = wantedVel*(entity.posY - posY)/dist;
				double zVel = wantedVel*(entity.posZ - posZ)/dist;
				
				entity.motionX = xVel;
				entity.motionY = yVel;
				entity.motionZ = zVel;
			}
		}
	}
}
