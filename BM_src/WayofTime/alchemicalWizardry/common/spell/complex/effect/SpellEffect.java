package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellEnhancement;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifier;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;

public abstract class SpellEffect 
{
	public int modifierState = SpellModifier.DEFAULT;
	
	public void enhanceEffect(SpellEnhancement enh)
	{
		
	}
	
	public void modifyEffect(SpellModifier mod)
	{
		if(mod!=null)
			modifierState = mod.getModifier();
	}
	
	public void modifyParadigm(SpellParadigm parad)
	{
		if(parad instanceof SpellParadigmProjectile)
		{
			this.modifyProjectileParadigm((SpellParadigmProjectile)parad);
		}
	}
	
	public void modifyProjectileParadigm(SpellParadigmProjectile parad)
	{
		switch(modifierState)
		{
		case SpellModifier.DEFAULT: this.defaultModificationProjectile(parad); break;
		case SpellModifier.OFFENSIVE: this.offensiveModificationProjectile(parad); break;
		case SpellModifier.DEFENSIVE: this.defensiveModificationProjectile(parad); break;
		case SpellModifier.ENVIRONMENTAL: this.environmentalModificationProjectile(parad); break;
		}
	}
	
	public abstract void defaultModificationProjectile(SpellParadigmProjectile parad);
	public abstract void offensiveModificationProjectile(SpellParadigmProjectile parad);
	public abstract void defensiveModificationProjectile(SpellParadigmProjectile parad);
	public abstract void environmentalModificationProjectile(SpellParadigmProjectile parad);
}
