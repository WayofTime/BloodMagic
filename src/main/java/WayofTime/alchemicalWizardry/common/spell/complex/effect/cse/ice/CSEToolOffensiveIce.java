package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.ice;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class CSEToolOffensiveIce extends ComplexSpellEffect
{
	public CSEToolOffensiveIce() 
	{
		super(ComplexSpellType.ICE, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEToolOffensiveIce(int power, int cost, int potency)
	{
		this();
		
		this.powerEnhancement = power;
		this.costEnhancement = cost;
		this.potencyEnhancement = potency;
	}

	@Override
	public void modifyParadigm(SpellParadigm parad) 
	{
		if(parad instanceof SpellParadigmTool)
		{
			((SpellParadigmTool)parad).addDamageToHash("Sharpness", (this.powerEnhancement + 1) * 1.5f);

			((SpellParadigmTool)parad).addToolString("Sharpness", "Sharpness" + " " + SpellHelper.getNumeralForInt((this.powerEnhancement + 1)));

			((SpellParadigmTool)parad).addCritChance("SharpCrit", this.potencyEnhancement);		
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolOffensiveIce(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (1000 * (1 + this.powerEnhancement * 0.3f) * (1 + this.potencyEnhancement * 0.2f) * Math.pow(0.85, costEnhancement));
	}
}
