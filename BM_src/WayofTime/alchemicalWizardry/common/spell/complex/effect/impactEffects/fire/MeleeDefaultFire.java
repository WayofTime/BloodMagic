package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ExtrapolatedMeleeEntityEffect;

public class MeleeDefaultFire extends ExtrapolatedMeleeEntityEffect 
{
	public MeleeDefaultFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		this.setRange(3+0.3f*potency);
		this.setRadius(2+0.3f*potency);
		this.setMaxNumberHit(potency+1);
	}

	@Override
	protected boolean entityEffect(World world, Entity entity, EntityPlayer entityPlayer) 
	{
		if(entity instanceof EntityLiving)
		{
			entity.setFire(3*this.powerUpgrades+3);
			return true;
		}
		
		return false;
	}
}
