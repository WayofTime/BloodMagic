package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ExtrapolatedMeleeEntityEffect;

public class MeleeDefensiveWind extends ExtrapolatedMeleeEntityEffect
{
	public MeleeDefensiveWind(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		this.setRange(3+0.3f*potency);
		this.setRadius(2+0.3f*potency);
		this.setMaxNumberHit(potency+1);
	}

	@Override
	protected boolean entityEffect(World world, Entity entity, EntityPlayer player) 
	{
		double wantedVel = 0.5d+0.5d*this.powerUpgrades;
		
		if(entity instanceof EntityLiving)
		{
			entity.motionY = wantedVel;

			return true;
		}
		
		return false;
	}
}
