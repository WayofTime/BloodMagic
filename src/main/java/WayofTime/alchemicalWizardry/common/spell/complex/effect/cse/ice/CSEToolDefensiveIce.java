package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.ice;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ToolDefensiveIce;

public class CSEToolDefensiveIce extends ComplexSpellEffect
{
	public CSEToolDefensiveIce() 
	{
		super(ComplexSpellType.ICE, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSEToolDefensiveIce(int power, int cost, int potency)
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
			((SpellParadigmTool)parad).addToolSummonEffect(new ToolDefensiveIce(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolDefensiveIce(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (500 * (1 + this.powerEnhancement * 0.2) * (1 + this.potencyEnhancement * 0.5) * Math.pow(0.85, costEnhancement));
	}
}
