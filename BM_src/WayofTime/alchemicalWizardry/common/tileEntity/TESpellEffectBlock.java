package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffectFire;

public class TESpellEffectBlock extends TESpellBlock
{
	@Override
	protected void applySpellChange(SpellParadigm parad) 
	{
		parad.addBufferedEffect(this.getSpellEffect());
	}
	
	public SpellEffect getSpellEffect()
	{
		return new SpellEffectFire();
	}
}
