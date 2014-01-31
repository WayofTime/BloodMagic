package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffectFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffectIce;

public class TESpellEffectBlock extends TESpellBlock
{
	@Override
	protected void applySpellChange(SpellParadigm parad) 
	{
		parad.addBufferedEffect(this.getSpellEffect());
	}
	
	public SpellEffect getSpellEffect()
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		switch(meta)
		{
		case 0: return new SpellEffectFire();
		case 1: return new SpellEffectIce();
		}
		return new SpellEffectFire();
	}
}
