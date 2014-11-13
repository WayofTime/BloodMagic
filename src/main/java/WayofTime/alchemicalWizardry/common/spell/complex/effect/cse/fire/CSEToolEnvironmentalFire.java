package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ToolEnvironmentalFire;

public class CSEToolEnvironmentalFire extends ComplexSpellEffect
{
	public CSEToolEnvironmentalFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSEToolEnvironmentalFire(int power, int cost, int potency)
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
			((SpellParadigmTool)parad).addBlockBreakEffect(new ToolEnvironmentalFire(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));

			((SpellParadigmTool)parad).addToolString("envFire", "Magma Plume" + " " + SpellHelper.getNumeralForInt(this.powerEnhancement + 1));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolEnvironmentalFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
		return 0;
	}
}
