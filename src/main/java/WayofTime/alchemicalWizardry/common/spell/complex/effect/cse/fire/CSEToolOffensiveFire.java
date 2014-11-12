package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ToolOffensiveFire;

public class CSEToolOffensiveFire extends ComplexSpellEffect
{
	public CSEToolOffensiveFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.OFFENSIVE);
	}
	
	public CSEToolOffensiveFire(int power, int cost, int potency)
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
			((SpellParadigmTool)parad).addLeftClickEffect(new ToolOffensiveFire(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));

			((SpellParadigmTool)parad).addToolString("offFire", "Fire Aspect" + " " + SpellHelper.getNumeralForInt(this.powerEnhancement + 1));		
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolOffensiveFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (1000 * (1 + this.powerEnhancement * 0.3f) * (1 + this.potencyEnhancement * 0.2f) * Math.pow(0.85, costEnhancement));
	}
}
