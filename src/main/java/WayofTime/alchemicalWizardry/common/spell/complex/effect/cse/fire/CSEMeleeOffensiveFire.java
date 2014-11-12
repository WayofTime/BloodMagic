package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.MeleeOffensiveFire;

public class CSEMeleeOffensiveFire extends ComplexSpellEffect
{
	public CSEMeleeOffensiveFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEMeleeOffensiveFire(int power, int cost, int potency)
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
			((SpellParadigmMelee) parad).addEntityEffect(new MeleeOffensiveFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeOffensiveFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (500 * (1 + this.potencyEnhancement) * (this.powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
