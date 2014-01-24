package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancement;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancementCost;

public class TESpellEnhancementBlock extends TESpellBlock 
{
	@Override
	protected void applySpellChange(SpellParadigm parad) 
	{
		int i = -1;
		
		switch(this.enhancementType())
		{
		case 0:
			i = parad.getBufferedEffectPower();
			break;
		case 1:
			i = parad.getBufferedEffectCost();
			break;
		case 2:
			i = parad.getBufferedEffectPotency();
			break;
		}
		
		if(i!=-1 && i<this.getLimit())
		{
			parad.applyEnhancement(getSpellEnhancement());
		}
		else if(i<this.getLimit())
		{
			this.doBadStuff();
		}
	}
	
	public SpellEnhancement getSpellEnhancement()
	{
		return new SpellEnhancementCost();
	}
	
	public int getLimit()
	{
		return 5;
	}
	
	public int enhancementType() //0 is power, 1 is cost, 2 is potency
	{
		return 0;
	}
	
	public void doBadStuff()
	{
		
	}
}
