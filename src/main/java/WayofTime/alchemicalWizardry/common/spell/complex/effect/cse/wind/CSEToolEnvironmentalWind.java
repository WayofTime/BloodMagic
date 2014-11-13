package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.wind;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ToolEnvironmentalWind;

public class CSEToolEnvironmentalWind extends ComplexSpellEffect
{
	public CSEToolEnvironmentalWind() 
	{
		super(ComplexSpellType.WIND, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSEToolEnvironmentalWind(int power, int cost, int potency)
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
	        ((SpellParadigmTool)parad).addBlockBreakEffect(new ToolEnvironmentalWind(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolEnvironmentalWind(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (150 * (1 + this.powerEnhancement) * Math.pow(0.85, costEnhancement));
	}
}
