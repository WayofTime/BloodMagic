package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffectEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffectFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffectIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffectWind;

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
		case 2: return new SpellEffectWind();
		case 3: return new SpellEffectEarth();
		}
		return new SpellEffectFire();
	}
	
	@Override
	public String getResourceLocationForMeta(int meta)
    {
		switch(meta)
		{
		case 0: return "alchemicalwizardry:textures/models/SpellEffectFire.png";
		case 1: return "alchemicalwizardry:textures/models/SpellEffectIce.png";
		case 2: return "alchemicalwizardry:textures/models/SpellEffectWind.png";
		case 3: return "alchemicalwizardry:textures/models/SpellEffectEarth.png";
		}
    	return "";
    }
}
