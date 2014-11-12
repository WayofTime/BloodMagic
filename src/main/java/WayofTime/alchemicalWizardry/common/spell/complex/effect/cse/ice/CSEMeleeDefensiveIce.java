package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.ice;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeDefensiveIce;

public class CSEMeleeDefensiveIce extends ComplexSpellEffect
{
	public CSEMeleeDefensiveIce() 
	{
		super(ComplexSpellType.ICE, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSEMeleeDefensiveIce(int power, int cost, int potency)
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
			((SpellParadigmMelee)parad).addWorldEffect(new MeleeDefensiveIce(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeDefensiveIce(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (50 * (0.5 * potencyEnhancement + 1) * (0.7 * powerEnhancement + 1) * (0.5 * powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
