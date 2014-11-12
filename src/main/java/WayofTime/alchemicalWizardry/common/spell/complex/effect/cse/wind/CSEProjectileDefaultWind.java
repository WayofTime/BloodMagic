package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.wind;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ProjectileDefaultWind;

public class CSEProjectileDefaultWind extends ComplexSpellEffect
{
	public CSEProjectileDefaultWind() 
	{
		super(ComplexSpellType.WIND, ComplexSpellModifier.DEFAULT);
	}
	
	public CSEProjectileDefaultWind(int power, int cost, int potency)
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
			((SpellParadigmProjectile)parad).addImpactEffect(new ProjectileDefaultWind(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileDefaultWind(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (100 * (this.potencyEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
