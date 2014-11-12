package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.MeleeEnvironmentalEarth;

public class CSEMeleeEnvironmentalEarth extends ComplexSpellEffect
{
	public CSEMeleeEnvironmentalEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSEMeleeEnvironmentalEarth(int power, int cost, int potency)
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
			((SpellParadigmMelee)parad).addWorldEffect(new MeleeEnvironmentalEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeEnvironmentalEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (500 * Math.pow(2 * this.potencyEnhancement + 1, 3) * (0.25 * this.powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
