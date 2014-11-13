package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.wind;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.SelfOffensiveWind;

public class CSESelfOffensiveWind extends ComplexSpellEffect
{
	public CSESelfOffensiveWind() 
	{
		super(ComplexSpellType.WIND, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSESelfOffensiveWind(int power, int cost, int potency)
	{
		this();
		
		this.powerEnhancement = power;
		this.costEnhancement = cost;
		this.potencyEnhancement = potency;
	}

	@Override
	public void modifyParadigm(SpellParadigm parad) 
	{
		if(parad instanceof SpellParadigmSelf)
		{
			((SpellParadigmSelf)parad).addSelfSpellEffect(new SelfOffensiveWind(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSESelfOffensiveWind(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (100 * (0.5 * this.powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
