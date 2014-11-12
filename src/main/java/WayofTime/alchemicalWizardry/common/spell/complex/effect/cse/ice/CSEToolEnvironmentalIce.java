package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.ice;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class CSEToolEnvironmentalIce extends ComplexSpellEffect
{
	public CSEToolEnvironmentalIce() 
	{
		super(ComplexSpellType.ICE, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSEToolEnvironmentalIce(int power, int cost, int potency)
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
			((SpellParadigmTool)parad).addToolString("SilkTouch", "Silk Touch" + " " + SpellHelper.getNumeralForInt((this.powerEnhancement + 1)));

			((SpellParadigmTool)parad).setSilkTouch(true);
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolEnvironmentalIce(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (1000 * Math.pow(0.85, costEnhancement));
	}
}
