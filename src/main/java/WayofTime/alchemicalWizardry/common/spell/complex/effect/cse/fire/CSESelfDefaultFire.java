package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfDefaultFire;

public class CSESelfDefaultFire extends ComplexSpellEffect
{
	public CSESelfDefaultFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.DEFAULT);
	}
	
	public CSESelfDefaultFire(int power, int cost, int potency)
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
			((SpellParadigmSelf) parad).addSelfSpellEffect(new SelfDefaultFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSESelfDefaultFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return 10 * (int) (10 * Math.pow(1.5, this.powerEnhancement + 1.5 * this.potencyEnhancement) * Math.pow(0.85, costEnhancement));
	}
}
