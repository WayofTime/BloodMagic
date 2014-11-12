package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.MeleeDefensiveFire;

public class CSEMeleeDefensiveFire extends ComplexSpellEffect
{
	public CSEMeleeDefensiveFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSEMeleeDefensiveFire(int power, int cost, int potency)
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
			((SpellParadigmMelee) parad).addWorldEffect(new MeleeDefensiveFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeDefensiveFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (30 * (1.5 * potencyEnhancement + 1) * (3 * powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
