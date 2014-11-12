package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileOffensiveEarth;

public class CSEProjectileOffensiveEarth extends ComplexSpellEffect
{
	public CSEProjectileOffensiveEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEProjectileOffensiveEarth(int power, int cost, int potency)
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
			((SpellParadigmProjectile)parad).addImpactEffect(new ProjectileOffensiveEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileOffensiveEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (10 * (1.5 * this.potencyEnhancement + 1) * (Math.pow(1 * this.powerEnhancement + 1, 2)) * Math.pow(0.85, costEnhancement));
	}
}
