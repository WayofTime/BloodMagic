package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.ice;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeDefaultIce;

public class CSEMeleeDefaultIce extends ComplexSpellEffect
{
	public CSEMeleeDefaultIce() 
	{
		super(ComplexSpellType.ICE, ComplexSpellModifier.DEFAULT);
	}
	
	public CSEMeleeDefaultIce(int power, int cost, int potency)
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
			((SpellParadigmMelee)parad).addEntityEffect(new MeleeDefaultIce(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEMeleeDefaultIce(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (250 * (potencyEnhancement + 1) * (1.5 * powerEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
