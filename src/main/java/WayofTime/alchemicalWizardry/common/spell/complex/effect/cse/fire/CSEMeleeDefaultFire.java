package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.MeleeDefaultFire;

public class CSEMeleeDefaultFire extends ComplexSpellEffect
{
	public CSEMeleeDefaultFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.DEFAULT);
	}
	
	public CSEMeleeDefaultFire(int power, int cost, int potency)
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
			((SpellParadigmMelee) parad).addEntityEffect(new MeleeDefaultFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeDefaultFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (25 * (1.2 * this.potencyEnhancement + 1) * (2.5 * this.powerEnhancement + 2) * Math.pow(0.85, costEnhancement));
	}
}
