package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import net.minecraft.nbt.NBTTagCompound;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifier;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancement;

public abstract class SpellEffect 
{
	protected int modifierState;
	protected int powerEnhancement;
	protected int costEnhancement;
	protected int potencyEnhancement;
	
	public SpellEffect()
	{
		this.modifierState = SpellModifier.DEFAULT;
		this.powerEnhancement = 0;
		this.costEnhancement = 0;
		this.potencyEnhancement = 0;
	}
	
	public void enhanceEffect(SpellEnhancement enh)
	{
		if(enh!=null)
		{
			switch(enh.getState())
			{
			case SpellEnhancement.POWER: this.powerEnhancement++; break;
			case SpellEnhancement.EFFICIENCY: this.costEnhancement++; break;
			case SpellEnhancement.POTENCY: this.potencyEnhancement++; break;
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
		if(parad instanceof SpellParadigmSelf)
		{
			this.modifySelfParadigm((SpellParadigmSelf)parad);
		}
		if(parad instanceof SpellParadigmMelee)
		{
			this.modifyMeleeParadigm((SpellParadigmMelee)parad);
		}
		if(parad instanceof SpellParadigmTool)
		{
			this.modifyToolParadigm((SpellParadigmTool)parad);
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
	
	public void modifyToolParadigm(SpellParadigmTool parad)
	{
		switch(modifierState)
		{
		case SpellModifier.DEFAULT: this.defaultModificationTool(parad); break;
		case SpellModifier.OFFENSIVE: this.offensiveModificationTool(parad); break;
		case SpellModifier.DEFENSIVE: this.defensiveModificationTool(parad); break;
		case SpellModifier.ENVIRONMENTAL: this.environmentalModificationTool(parad); break;
		}
	}
	
	public abstract void defaultModificationTool(SpellParadigmTool parad);
	public abstract void offensiveModificationTool(SpellParadigmTool parad);
	public abstract void defensiveModificationTool(SpellParadigmTool parad);
	public abstract void environmentalModificationTool(SpellParadigmTool parad);
	
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
	
	protected abstract int getCostForDefaultProjectile();
	protected abstract int getCostForOffenseProjectile();
	protected abstract int getCostForDefenseProjectile();
	protected abstract int getCostForEnvironmentProjectile();
	
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
	
	protected abstract int getCostForDefaultSelf();
	protected abstract int getCostForOffenseSelf();
	protected abstract int getCostForDefenseSelf();
	protected abstract int getCostForEnvironmentSelf();
	
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
	
	protected abstract int getCostForDefaultMelee();
	protected abstract int getCostForOffenseMelee();
	protected abstract int getCostForDefenseMelee();
	protected abstract int getCostForEnvironmentMelee();
	
	public int getCostForTool()
	{
		switch(this.modifierState)
		{
		case SpellModifier.DEFAULT: return this.getCostForDefaultTool();
		case SpellModifier.OFFENSIVE: return this.getCostForOffenseTool();
		case SpellModifier.DEFENSIVE: return this.getCostForDefenseTool();
		case SpellModifier.ENVIRONMENTAL: return this.getCostForEnvironmentTool();
		}
		return 0;
	}
	
	protected abstract int getCostForDefaultTool();
	protected abstract int getCostForOffenseTool();
	protected abstract int getCostForDefenseTool();
	protected abstract int getCostForEnvironmentTool();
	
	public int getPowerEnhancements()
	{
		return this.powerEnhancement;
	}
	
	public int getCostEnhancements()
	{
		return this.costEnhancement;
	}
	
	public int getPotencyEnhancements()
	{
		return this.potencyEnhancement;
	}
	
	public NBTTagCompound getTag()
	{
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Class", this.getClass().getName());
        tag.setInteger("modifier", modifierState);
        tag.setInteger("power", powerEnhancement);
        tag.setInteger("cost", costEnhancement);
        tag.setInteger("potency", potencyEnhancement);
        
        return tag;
	}
	
	public static SpellEffect getEffectFromTag(NBTTagCompound tag)
	{
		try {
			Class clazz = Class.forName(tag.getString("Class"));
			if(clazz !=null)
			{
				try {
					Object obj = clazz.newInstance();
					if(obj instanceof SpellEffect)
					{
						SpellEffect eff = (SpellEffect) obj;
						
						eff.modifierState = tag.getInteger("modifier");
						eff.powerEnhancement = tag.getInteger("power");
						eff.costEnhancement = tag.getInteger("cost");
						eff.potencyEnhancement = tag.getInteger("potency");
						
						return eff;
					}
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
