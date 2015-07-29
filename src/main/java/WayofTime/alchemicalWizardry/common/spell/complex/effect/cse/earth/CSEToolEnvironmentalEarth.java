package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ToolEnvironmentalEarth;

public class CSEToolEnvironmentalEarth extends ComplexSpellEffect
{
	public CSEToolEnvironmentalEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSEToolEnvironmentalEarth(int power, int cost, int potency)
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
	        ((SpellParadigmTool)parad).addDigAreaEffect(new ToolEnvironmentalEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolEnvironmentalEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (10 * (1 + this.potencyEnhancement * 0.8) * Math.pow(1.5 * this.powerEnhancement + 3, 2) * Math.pow(0.85, this.costEnhancement));
	}
}
