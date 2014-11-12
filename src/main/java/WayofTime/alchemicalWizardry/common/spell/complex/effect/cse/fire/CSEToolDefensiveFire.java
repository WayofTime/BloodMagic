package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class CSEToolDefensiveFire extends ComplexSpellEffect
{
	public CSEToolDefensiveFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.DEFENSIVE);
	}
	
	public CSEToolDefensiveFire(int power, int cost, int potency)
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
			((SpellParadigmTool)parad).addCritChance("defFire", this.potencyEnhancement);

			((SpellParadigmTool)parad).addDuration("defFire", 1200 * this.powerEnhancement);

			((SpellParadigmTool)parad).addToolString("defFire", "Unbreaking" + " " + SpellHelper.getNumeralForInt(this.powerEnhancement + 1));	
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEToolDefensiveFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (500 * (1 + this.powerEnhancement * 0.5f) * (1 + this.potencyEnhancement) * Math.pow(0.85, costEnhancement));
	}
}
