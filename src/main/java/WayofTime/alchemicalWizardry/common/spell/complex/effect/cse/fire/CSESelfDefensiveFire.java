package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfDefensiveFire;

public class CSESelfDefensiveFire extends ComplexSpellEffect
{
	public CSESelfDefensiveFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSESelfDefensiveFire(int power, int cost, int potency)
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
			((SpellParadigmSelf) parad).addSelfSpellEffect(new SelfDefensiveFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSESelfDefensiveFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (25 * (3 * this.potencyEnhancement + 1) * (2 * this.powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
