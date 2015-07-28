package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ToolOffensiveEarth;

public class CSEToolOffensiveEarth extends ComplexSpellEffect
{
	public CSEToolOffensiveEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEToolOffensiveEarth(int power, int cost, int potency)
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
	        ((SpellParadigmTool)parad).addItemManipulatorEffect(new ToolOffensiveEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolOffensiveEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return 1000;
	}
}
