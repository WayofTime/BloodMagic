package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfEnvironmentalFire;

public class CSESelfEnvironmentalFire extends ComplexSpellEffect
{
	public CSESelfEnvironmentalFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSESelfEnvironmentalFire(int power, int cost, int potency)
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
			((SpellParadigmSelf) parad).addSelfSpellEffect(new SelfEnvironmentalFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSESelfEnvironmentalFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) ((15 * Math.pow(1.7, powerEnhancement) + 10 * Math.pow(potencyEnhancement, 1.8)) * Math.pow(0.85, costEnhancement));
	}
}
