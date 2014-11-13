package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.ice;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.SelfDefensiveIce;

public class CSESelfDefensiveIce extends ComplexSpellEffect
{
	public CSESelfDefensiveIce() 
	{
		super(ComplexSpellType.ICE, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSESelfDefensiveIce(int power, int cost, int potency)
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
			((SpellParadigmSelf)parad).addSelfSpellEffect(new SelfDefensiveIce(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSESelfDefensiveIce(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (200 * (3 * powerEnhancement + 1) * (2 * potencyEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
