package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifier;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancement;

public abstract class SpellEffect 
{
	public int modifierState = SpellModifier.DEFAULT;
	public int powerEnhancement = 0;
	public int costEnhancement = 0;
	
	public void enhanceEffect(SpellEnhancement enh)
	{
		if(enh!=null)
		{
			switch(enh.getState())
			{
			case SpellEnhancement.POWER: this.powerEnhancement++; break;
			case SpellEnhancement.EFFICIENCY: this.costEnhancement++; break;
			}
		}
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
	
	public void modifySelfParadigm(SpellParadigmSelf parad)
	{
		switch(modifierState)
		{
		case SpellModifier.DEFAULT: this.defaultModificationSelf(parad); break;
		case SpellModifier.OFFENSIVE: this.offensiveModificationSelf(parad); break;
		case SpellModifier.DEFENSIVE: this.defensiveModificationSelf(parad); break;
		case SpellModifier.ENVIRONMENTAL: this.environmentalModificationSelf(parad); break;
		}
	}
	
	public abstract void defaultModificationSelf(SpellParadigmSelf parad);
	public abstract void offensiveModificationSelf(SpellParadigmSelf parad);
	public abstract void defensiveModificationSelf(SpellParadigmSelf parad);
	public abstract void environmentalModificationSelf(SpellParadigmSelf parad);
	
	public void modifyMeleeParadigm(SpellParadigmMelee parad)
	{
		switch(modifierState)
		{
		case SpellModifier.DEFAULT: this.defaultModificationMelee(parad); break;
		case SpellModifier.OFFENSIVE: this.offensiveModificationMelee(parad); break;
		case SpellModifier.DEFENSIVE: this.defensiveModificationMelee(parad); break;
		case SpellModifier.ENVIRONMENTAL: this.environmentalModificationMelee(parad); break;
		}
	}
	
	public abstract void defaultModificationMelee(SpellParadigmMelee parad);
	public abstract void offensiveModificationMelee(SpellParadigmMelee parad);
	public abstract void defensiveModificationMelee(SpellParadigmMelee parad);
	public abstract void environmentalModificationMelee(SpellParadigmMelee parad);
	
	public int getCostForProjectile()
	{
		switch(this.modifierState)
		{
		case SpellModifier.DEFAULT: return this.getCostForDefaultProjectile();
		case SpellModifier.OFFENSIVE: return this.getCostForOffenseProjectile();
		case SpellModifier.DEFENSIVE: return this.getCostForDefenseProjectile();
		case SpellModifier.ENVIRONMENTAL: return this.getCostForEnvironmentProjectile();
		}
		return 0;
	}
	
	public abstract int getCostForDefaultProjectile();
	public abstract int getCostForOffenseProjectile();
	public abstract int getCostForDefenseProjectile();
	public abstract int getCostForEnvironmentProjectile();
	
	public int getCostForSelf()
	{
		switch(this.modifierState)
		{
		case SpellModifier.DEFAULT: return this.getCostForDefaultSelf();
		case SpellModifier.OFFENSIVE: return this.getCostForOffenseSelf();
		case SpellModifier.DEFENSIVE: return this.getCostForDefenseSelf();
		case SpellModifier.ENVIRONMENTAL: return this.getCostForEnvironmentSelf();
		}
		return 0;
	}
	
	public abstract int getCostForDefaultSelf();
	public abstract int getCostForOffenseSelf();
	public abstract int getCostForDefenseSelf();
	public abstract int getCostForEnvironmentSelf();
	
	public int getCostForMelee()
	{
		switch(this.modifierState)
		{
		case SpellModifier.DEFAULT: return this.getCostForDefaultMelee();
		case SpellModifier.OFFENSIVE: return this.getCostForOffenseMelee();
		case SpellModifier.DEFENSIVE: return this.getCostForDefenseMelee();
		case SpellModifier.ENVIRONMENTAL: return this.getCostForEnvironmentMelee();
		}
		return 0;
	}
	
	public abstract int getCostForDefaultMelee();
	public abstract int getCostForOffenseMelee();
	public abstract int getCostForDefenseMelee();
	public abstract int getCostForEnvironmentMelee();
}
