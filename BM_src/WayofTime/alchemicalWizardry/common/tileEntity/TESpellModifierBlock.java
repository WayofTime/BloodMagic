package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifier;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifierDefault;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;

public class TESpellModifierBlock extends TESpellBlock
{
	@Override
	protected void applySpellChange(SpellParadigm parad) 
	{
		parad.modifyBufferedEffect(this.getSpellModifier());
	}
	
	public SpellModifier getSpellModifier()
	{
		return new SpellModifierDefault();
	}
}
