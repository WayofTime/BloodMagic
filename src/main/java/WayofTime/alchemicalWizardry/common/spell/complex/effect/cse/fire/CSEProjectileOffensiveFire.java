package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileOffensiveFire;

public class CSEProjectileOffensiveFire extends ComplexSpellEffect
{
	public CSEProjectileOffensiveFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEProjectileOffensiveFire(int power, int cost, int potency)
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
			((SpellParadigmProjectile) parad).addImpactEffect(new ProjectileOffensiveFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileOffensiveFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (10 * Math.pow((this.powerEnhancement) * 1.3 + 1, 2) * ((1.5 * this.potencyEnhancement + 1)) * Math.pow(0.85, costEnhancement));
	}
}
