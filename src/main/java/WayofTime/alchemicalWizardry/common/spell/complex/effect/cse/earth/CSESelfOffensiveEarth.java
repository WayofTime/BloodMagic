package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.SelfOffensiveEarth;

public class CSESelfOffensiveEarth extends ComplexSpellEffect
{
	public CSESelfOffensiveEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSESelfOffensiveEarth(int power, int cost, int potency)
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
			((SpellParadigmSelf)parad).addSelfSpellEffect(new SelfOffensiveEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSESelfOffensiveEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (10 * Math.pow(2 * this.powerEnhancement + 1, 2) * (this.potencyEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
