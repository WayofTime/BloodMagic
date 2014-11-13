package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.ice;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ToolDefaultIce;

public class CSEToolDefaultIce extends ComplexSpellEffect
{
	public CSEToolDefaultIce() 
	{
		super(ComplexSpellType.ICE, ComplexSpellModifier.DEFAULT);
	}
	
	public CSEToolDefaultIce(int power, int cost, int potency)
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
			((SpellParadigmTool)parad).addLeftClickEffect(new ToolDefaultIce(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));

			((SpellParadigmTool)parad).addToolString("FrostTouch", "FrostTouch" + " " + SpellHelper.getNumeralForInt((this.powerEnhancement + 1)));

			((SpellParadigmTool)parad).addCritChance("FrostCrit", this.potencyEnhancement * 0.5f);		
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolDefaultIce(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (500 * (1 + this.powerEnhancement * 0.3f) * (1 + this.potencyEnhancement * 0.1f) * Math.pow(0.85, costEnhancement));
	}
}
