package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.wind;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ToolOffensiveWind;

public class CSEToolOffensiveWind extends ComplexSpellEffect
{
	public CSEToolOffensiveWind() 
	{
		super(ComplexSpellType.WIND, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEToolOffensiveWind(int power, int cost, int potency)
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
	        ((SpellParadigmTool)parad).addLeftClickEffect(new ToolOffensiveWind(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolOffensiveWind(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return 0; //Cost is on the attack method
	}
}
