package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.wind;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ProjectileOffensiveWind;

public class CSEProjectileOffensiveWind extends ComplexSpellEffect
{
	public CSEProjectileOffensiveWind() 
	{
		super(ComplexSpellType.WIND, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEProjectileOffensiveWind(int power, int cost, int potency)
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
			((SpellParadigmProjectile)parad).addImpactEffect(new ProjectileOffensiveWind(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileOffensiveWind(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (100 * (0.5 * this.potencyEnhancement + 1) * (this.powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
