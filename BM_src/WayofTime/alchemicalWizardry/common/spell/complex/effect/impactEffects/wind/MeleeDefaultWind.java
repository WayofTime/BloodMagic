package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ExtrapolatedMeleeEntityEffect;

public class MeleeDefaultWind extends ExtrapolatedMeleeEntityEffect
{
	public MeleeDefaultWind(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		this.setRange(4+2.0f*potency);
		this.setRadius(4+2.0f*potency);
		this.setMaxNumberHit(potency+1);
	}

	@Override
	protected boolean entityEffect(World world, Entity entity, EntityPlayer player) 
	{
		double wantedVel = -(0.5d+0.7d*this.powerUpgrades);
		
		if(entity instanceof EntityLiving)
		{
			double dist = Math.sqrt(entity.getDistanceToEntity(player));
			double xVel = wantedVel*(entity.posX - player.posX)/dist;
			double yVel = wantedVel*(entity.posY - player.posY)/dist;
			double zVel = wantedVel*(entity.posZ - player.posZ)/dist;
			
			entity.motionX = xVel;
			entity.motionY = yVel;
			entity.motionZ = zVel;
			
			return true;
		}
		
		return false;
	}
}
