package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.wind;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ToolDefensiveWind;

public class CSEToolDefensiveWind extends ComplexSpellEffect
{
	public CSEToolDefensiveWind() 
	{
		super(ComplexSpellType.WIND, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSEToolDefensiveWind(int power, int cost, int potency)
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
	        ((SpellParadigmTool)parad).addLeftClickEffect(new ToolDefensiveWind(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
	        ((SpellParadigmTool)parad).addToolString("DefWind", "Knockback" + " " + SpellHelper.getNumeralForInt(this.powerEnhancement + 1));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolDefensiveWind(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (150 * (1 + this.powerEnhancement * 0.4f) * (1 + this.potencyEnhancement * 0.3f) * Math.pow(0.85, costEnhancement));
	}
}
