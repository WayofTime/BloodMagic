package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifier;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifierDefault;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifierDefensive;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifierEnvironmental;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifierOffensive;
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
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		switch(meta)
		{
		case 0: return new SpellModifierDefault();
		case 1: return new SpellModifierOffensive();
		case 2: return new SpellModifierDefensive();
		case 3: return new SpellModifierEnvironmental();
		}
		return new SpellModifierDefault();
	}
}
