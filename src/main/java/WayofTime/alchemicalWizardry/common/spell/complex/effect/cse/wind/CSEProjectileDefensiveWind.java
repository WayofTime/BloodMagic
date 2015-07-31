package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.wind;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmProjectile;

public class CSEProjectileDefensiveWind extends ComplexSpellEffect
{
	public CSEProjectileDefensiveWind() 
	{
		super(ComplexSpellType.WIND, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSEProjectileDefensiveWind(int power, int cost, int potency)
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
			((SpellParadigmProjectile)parad).isSilkTouch = true;
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileDefensiveWind(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (100 * (this.potencyEnhancement + 1));
	}
}
