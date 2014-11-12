package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ToolDefaultFire;

public class CSEToolDefaultFire extends ComplexSpellEffect
{
	public CSEToolDefaultFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.DEFAULT);
	}
	
	public CSEToolDefaultFire(int power, int cost, int potency)
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
			((SpellParadigmTool) parad).addItemManipulatorEffect(new ToolDefaultFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolDefaultFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return 1000;
	}
}
