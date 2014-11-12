package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.wind;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.MeleeOffensiveWind;

public class CSEMeleeOffensiveWind extends ComplexSpellEffect
{
	public CSEMeleeOffensiveWind() 
	{
		super(ComplexSpellType.WIND, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEMeleeOffensiveWind(int power, int cost, int potency)
	{
		this();
		
		this.powerEnhancement = power;
		this.costEnhancement = cost;
		this.potencyEnhancement = potency;
	}

	@Override
	public void modifyParadigm(SpellParadigm parad) 
	{
		if(parad instanceof SpellParadigmMelee)
		{
			((SpellParadigmMelee)parad).addEntityEffect(new MeleeOffensiveWind(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeOffensiveWind(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (250 * (1.0 * this.potencyEnhancement + 1) * (0.7 * this.powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
