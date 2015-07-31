package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.MeleeOffensiveEarth;

public class CSEMeleeOffensiveEarth extends ComplexSpellEffect
{
	public CSEMeleeOffensiveEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEMeleeOffensiveEarth(int power, int cost, int potency)
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
			((SpellParadigmMelee)parad).addWorldEffect(new MeleeOffensiveEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeOffensiveEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (20 * Math.pow(1.5 * this.powerEnhancement + 1, 3) * (0.25 * this.powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
