package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.wind;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ProjectileEnvironmentalWind;

public class CSEProjectileEnvironmentalWind extends ComplexSpellEffect
{
	public CSEProjectileEnvironmentalWind() 
	{
		super(ComplexSpellType.WIND, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSEProjectileEnvironmentalWind(int power, int cost, int potency)
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
			((SpellParadigmProjectile)parad).addUpdateEffect(new ProjectileEnvironmentalWind(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileEnvironmentalWind(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (50 * (this.powerEnhancement + 1) * (this.potencyEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
