package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.SelfDefaultEarth;

public class CSESelfDefaultEarth extends ComplexSpellEffect
{
	public CSESelfDefaultEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.DEFAULT);
	}
	
	public CSESelfDefaultEarth(int power, int cost, int potency)
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
			((SpellParadigmSelf)parad).addSelfSpellEffect(new SelfDefaultEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSESelfDefaultEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (20 * Math.pow(1.5 * powerEnhancement + 1, 2) * (2 * this.potencyEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
