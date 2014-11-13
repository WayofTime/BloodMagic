package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.MeleeEnvironmentalFire;

public class CSEMeleeEnvironmentalFire extends ComplexSpellEffect
{
	public CSEMeleeEnvironmentalFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSEMeleeEnvironmentalFire(int power, int cost, int potency)
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
			((SpellParadigmMelee) parad).addWorldEffect(new MeleeEnvironmentalFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeEnvironmentalFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (25 * Math.pow(1.5 * this.powerEnhancement + 1, 3) * (0.25 * this.powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
