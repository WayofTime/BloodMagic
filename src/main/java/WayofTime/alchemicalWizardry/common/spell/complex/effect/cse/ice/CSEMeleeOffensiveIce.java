package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.ice;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeOffensiveIce;

public class CSEMeleeOffensiveIce extends ComplexSpellEffect
{
	public CSEMeleeOffensiveIce() 
	{
		super(ComplexSpellType.ICE, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEMeleeOffensiveIce(int power, int cost, int potency)
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
			((SpellParadigmMelee)parad).addEntityEffect(new MeleeOffensiveIce(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeOffensiveIce(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (40 * (1.5 * potencyEnhancement + 1) * Math.pow(1.5, powerEnhancement) * Math.pow(0.85, costEnhancement));
	}
}
