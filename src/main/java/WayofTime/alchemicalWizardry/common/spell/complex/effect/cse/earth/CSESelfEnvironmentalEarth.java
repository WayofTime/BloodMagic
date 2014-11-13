package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.SelfEnvironmentalEarth;

public class CSESelfEnvironmentalEarth extends ComplexSpellEffect
{
	public CSESelfEnvironmentalEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSESelfEnvironmentalEarth(int power, int cost, int potency)
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
			((SpellParadigmSelf)parad).addSelfSpellEffect(new SelfEnvironmentalEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSESelfEnvironmentalEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (250 * (1.2 * this.potencyEnhancement + 1) * (3 * this.powerEnhancement + 2.5) * Math.pow(0.85, costEnhancement));
	}
}
