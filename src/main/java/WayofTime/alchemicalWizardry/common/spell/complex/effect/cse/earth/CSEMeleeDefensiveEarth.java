package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.MeleeDefensiveEarth;

public class CSEMeleeDefensiveEarth extends ComplexSpellEffect
{
	public CSEMeleeDefensiveEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSEMeleeDefensiveEarth(int power, int cost, int potency)
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
			((SpellParadigmMelee)parad).addWorldEffect(new MeleeDefensiveEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeDefensiveEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (5 * (1.2 * this.powerEnhancement + 1) * (1.0f / 3.0f * Math.pow(this.potencyEnhancement, 2) + 2 + 1.0f / 2.0f * this.potencyEnhancement) * Math.pow(0.85, costEnhancement));
	}
}
