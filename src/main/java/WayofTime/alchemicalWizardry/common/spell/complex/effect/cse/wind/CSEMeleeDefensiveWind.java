package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.wind;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.MeleeDefensiveWind;

public class CSEMeleeDefensiveWind extends ComplexSpellEffect
{
	public CSEMeleeDefensiveWind() 
	{
		super(ComplexSpellType.WIND, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSEMeleeDefensiveWind(int power, int cost, int potency)
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
			((SpellParadigmMelee)parad).addEntityEffect(new MeleeDefensiveWind(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeDefensiveWind(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (150 * (1.0 * this.potencyEnhancement + 1) * (0.7 * this.powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
