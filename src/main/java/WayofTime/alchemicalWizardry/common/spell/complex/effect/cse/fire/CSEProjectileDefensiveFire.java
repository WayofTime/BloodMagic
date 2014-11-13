package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileDefensiveFire;

public class CSEProjectileDefensiveFire extends ComplexSpellEffect
{
	public CSEProjectileDefensiveFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSEProjectileDefensiveFire(int power, int cost, int potency)
	{
		this();
		
		this.powerEnhancement = power;
		this.costEnhancement = cost;
		this.potencyEnhancement = potency;
	}

	@Override
	public void modifyParadigm(SpellParadigm parad) 
	{
		if(parad instanceof SpellParadigmProjectile)
		{
			((SpellParadigmProjectile) parad).addImpactEffect(new ProjectileDefensiveFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileDefensiveFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (25 * Math.pow(1 * this.powerEnhancement + 1, 2) * (1 * this.potencyEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
