package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ExtrapolatedMeleeEntityEffect;

public class MeleeDefaultIce extends ExtrapolatedMeleeEntityEffect {

	public MeleeDefaultIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		this.setRange(3+0.3f*potency);
		this.setRadius(2+0.3f*potency);
		this.setMaxNumberHit(potency+1);
	}

	@Override
	protected boolean entityEffect(World world, Entity entity, EntityPlayer entityPlayer) 
	{
		if(entity.hurtResistantTime>0)
		{
			entity.hurtResistantTime = Math.max(0, -(potencyUpgrades+1)+entity.hurtResistantTime);
		}
		
		return true;
	}
}
