package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.nbt.NBTTagCompound;

/**
 * New wrapper class to enclose the ComplexSpellEffect
 */
public class SpellEffect 
{
	public ComplexSpellType type;
	public ComplexSpellModifier modifier;
	
	protected int powerEnhancement;
    protected int costEnhancement;
    protected int potencyEnhancement;
    
    public SpellEffect()
    {
    	this(ComplexSpellType.FIRE);
    }
    
    public SpellEffect(ComplexSpellType type)
    {
    	this(type, ComplexSpellModifier.DEFAULT);
    }
    
    public SpellEffect(ComplexSpellType type, ComplexSpellModifier modifier)
    {
    	this.type = type;
    	this.modifier = modifier;
    	
    	this.powerEnhancement = 0;
    	this.potencyEnhancement = 0;
    	this.costEnhancement = 0;
    }
    
    public void enhanceEffect(SpellEnhancement enh)
    {
        if (enh != null)
        {
            switch (enh.getState())
            {
                case SpellEnhancement.POWER:
                    this.powerEnhancement++;
                    break;
                case SpellEnhancement.EFFICIENCY:
                    this.costEnhancement++;
                    break;
                case SpellEnhancement.POTENCY:
                    this.potencyEnhancement++;
                    break;
            }
        }
    }
    
    public void modifyEffect(ComplexSpellModifier mod)
    {
    	if(mod != null)
    	{
    		this.modifier = mod;
    	}
    }
    
    public void modifyParadigm(SpellParadigm parad) //When modifying the paradigm it will instead get the class name and ask the registry
    {
    	if(parad == null)
    	{
    		return;
    	}
    	
        Class paraClass = parad.getClass();
        
        ComplexSpellEffect effect = SpellEffectRegistry.getSpellEffect(paraClass, type, modifier, powerEnhancement, potencyEnhancement, costEnhancement);
        
        if(effect != null)
        {
        	effect.modifyParadigm(parad);
        }
    }
    
    public int getCostOfEffect(SpellParadigm parad)
    {
    	if(parad == null)
    	{
    		return 0;
    	}
    	
        Class paraClass = parad.getClass();
        
    	ComplexSpellEffect effect = SpellEffectRegistry.getSpellEffect(paraClass, type, modifier, powerEnhancement, potencyEnhancement, costEnhancement);
        
    	if(effect == null)
    	{
    		return 0;
    	}
    	
    	return effect.getCostOfEffect();
    }
    
    public NBTTagCompound getTag()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Class", this.getClass().getName());
        tag.setString("type", SpellEffectRegistry.getKeyForType(type));
        tag.setString("modifier", SpellEffectRegistry.getKeyForModifier(modifier));
        tag.setInteger("power", powerEnhancement);
        tag.setInteger("cost", costEnhancement);
        tag.setInteger("potency", potencyEnhancement);

        return tag;
    }

    public static SpellEffect getEffectFromTag(NBTTagCompound tag)
    {
        try
        {
            Class clazz = Class.forName(tag.getString("Class"));
            if (clazz != null)
            {
                try
                {
                    Object obj = clazz.newInstance();
                    if (obj instanceof SpellEffect)
                    {
                        SpellEffect eff = (SpellEffect) obj;

                        eff.type = SpellEffectRegistry.getTypeForKey(tag.getString("type"));
                        eff.modifier = SpellEffectRegistry.getModifierForKey(tag.getString("modifier"));
                        eff.powerEnhancement = tag.getInteger("power");
                        eff.costEnhancement = tag.getInteger("cost");
                        eff.potencyEnhancement = tag.getInteger("potency");
                        
                        return eff;
                    }
                } catch (InstantiationException e)
                {
                    e.printStackTrace();
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

	public int getPowerEnhancements() 
	{
		return this.powerEnhancement;
	}
	
	public int getPotencyEnhancements() 
	{
		return this.potencyEnhancement;
	}
	
	public int getCostEnhancements() 
	{
		return this.costEnhancement;
	}
}

//package WayofTime.alchemicalWizardry.common.spell.complex.effect;
//
//import WayofTime.alchemicalWizardry.common.spell.complex.*;
//import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancement;
//import net.minecraft.nbt.NBTTagCompound;
//
//public abstract class SpellEffect
//{
//    protected int modifierState;
//    protected int powerEnhancement;
//    protected int costEnhancement;
//    protected int potencyEnhancement;
//
//    public SpellEffect()
//    {
//        this.modifierState = SpellModifier.DEFAULT;
//        this.powerEnhancement = 0;
//        this.costEnhancement = 0;
//        this.potencyEnhancement = 0;
//    }
//
//    public void enhanceEffect(SpellEnhancement enh)
//    {
//        if (enh != null)
//        {
//            switch (enh.getState())
//            {
//                case SpellEnhancement.POWER:
//                    this.powerEnhancement++;
//                    break;
//                case SpellEnhancement.EFFICIENCY:
//                    this.costEnhancement++;
//                    break;
//                case SpellEnhancement.POTENCY:
//                    this.potencyEnhancement++;
//                    break;
//            }
//        }
//    }
//
//    public void modifyEffect(SpellModifier mod)
//    {
//        if (mod != null)
//            modifierState = mod.getModifier();
//    }
//
//    public void modifyParadigm(SpellParadigm parad) //When modifying the paradigm it will instead get the class name and ask the registry
//    {
//        if (parad instanceof SpellParadigmProjectile)
//        {
//            this.modifyProjectileParadigm((SpellParadigmProjectile) parad);
//        }
//        if (parad instanceof SpellParadigmSelf)
//        {
//            this.modifySelfParadigm((SpellParadigmSelf) parad);
//        }
//        if (parad instanceof SpellParadigmMelee)
//        {
//            this.modifyMeleeParadigm((SpellParadigmMelee) parad);
//        }
//        if (parad instanceof SpellParadigmTool)
//        {
//            this.modifyToolParadigm((SpellParadigmTool) parad);
//        }
//    }
//
//    public void modifyProjectileParadigm(SpellParadigmProjectile parad)
//    {
//        switch (modifierState)
//        {
//            case SpellModifier.DEFAULT:
//                this.defaultModificationProjectile(parad);
//                break;
//            case SpellModifier.OFFENSIVE:
//                this.offensiveModificationProjectile(parad);
//                break;
//            case SpellModifier.DEFENSIVE:
//                this.defensiveModificationProjectile(parad);
//                break;
//            case SpellModifier.ENVIRONMENTAL:
//                this.environmentalModificationProjectile(parad);
//                break;
//        }
//    }
//
//    public abstract void defaultModificationProjectile(SpellParadigmProjectile parad);
//
//    public abstract void offensiveModificationProjectile(SpellParadigmProjectile parad);
//
//    public abstract void defensiveModificationProjectile(SpellParadigmProjectile parad);
//
//    public abstract void environmentalModificationProjectile(SpellParadigmProjectile parad);
//
//    public void modifySelfParadigm(SpellParadigmSelf parad)
//    {
//        switch (modifierState)
//        {
//            case SpellModifier.DEFAULT:
//                this.defaultModificationSelf(parad);
//                break;
//            case SpellModifier.OFFENSIVE:
//                this.offensiveModificationSelf(parad);
//                break;
//            case SpellModifier.DEFENSIVE:
//                this.defensiveModificationSelf(parad);
//                break;
//            case SpellModifier.ENVIRONMENTAL:
//                this.environmentalModificationSelf(parad);
//                break;
//        }
//    }
//
//    public abstract void defaultModificationSelf(SpellParadigmSelf parad);
//
//    public abstract void offensiveModificationSelf(SpellParadigmSelf parad);
//
//    public abstract void defensiveModificationSelf(SpellParadigmSelf parad);
//
//    public abstract void environmentalModificationSelf(SpellParadigmSelf parad);
//
//    public void modifyMeleeParadigm(SpellParadigmMelee parad)
//    {
//        switch (modifierState)
//        {
//            case SpellModifier.DEFAULT:
//                this.defaultModificationMelee(parad);
//                break;
//            case SpellModifier.OFFENSIVE:
//                this.offensiveModificationMelee(parad);
//                break;
//            case SpellModifier.DEFENSIVE:
//                this.defensiveModificationMelee(parad);
//                break;
//            case SpellModifier.ENVIRONMENTAL:
//                this.environmentalModificationMelee(parad);
//                break;
//        }
//    }
//
//    public abstract void defaultModificationMelee(SpellParadigmMelee parad);
//
//    public abstract void offensiveModificationMelee(SpellParadigmMelee parad);
//
//    public abstract void defensiveModificationMelee(SpellParadigmMelee parad);
//
//    public abstract void environmentalModificationMelee(SpellParadigmMelee parad);
//
//    public void modifyToolParadigm(SpellParadigmTool parad)
//    {
//        switch (modifierState)
//        {
//            case SpellModifier.DEFAULT:
//                this.defaultModificationTool(parad);
//                break;
//            case SpellModifier.OFFENSIVE:
//                this.offensiveModificationTool(parad);
//                break;
//            case SpellModifier.DEFENSIVE:
//                this.defensiveModificationTool(parad);
//                break;
//            case SpellModifier.ENVIRONMENTAL:
//                this.environmentalModificationTool(parad);
//                break;
//        }
//    }
//
//    public abstract void defaultModificationTool(SpellParadigmTool parad);
//
//    public abstract void offensiveModificationTool(SpellParadigmTool parad);
//
//    public abstract void defensiveModificationTool(SpellParadigmTool parad);
//
//    public abstract void environmentalModificationTool(SpellParadigmTool parad);
//
//    public int getCostForProjectile()
//    {
//        switch (this.modifierState)
//        {
//            case SpellModifier.DEFAULT:
//                return this.getCostForDefaultProjectile();
//            case SpellModifier.OFFENSIVE:
//                return this.getCostForOffenseProjectile();
//            case SpellModifier.DEFENSIVE:
//                return this.getCostForDefenseProjectile();
//            case SpellModifier.ENVIRONMENTAL:
//                return this.getCostForEnvironmentProjectile();
//        }
//        return 0;
//    }
//
//    protected abstract int getCostForDefaultProjectile();
//
//    protected abstract int getCostForOffenseProjectile();
//
//    protected abstract int getCostForDefenseProjectile();
//
//    protected abstract int getCostForEnvironmentProjectile();
//
//    public int getCostForSelf()
//    {
//        switch (this.modifierState)
//        {
//            case SpellModifier.DEFAULT:
//                return this.getCostForDefaultSelf();
//            case SpellModifier.OFFENSIVE:
//                return this.getCostForOffenseSelf();
//            case SpellModifier.DEFENSIVE:
//                return this.getCostForDefenseSelf();
//            case SpellModifier.ENVIRONMENTAL:
//                return this.getCostForEnvironmentSelf();
//        }
//        return 0;
//    }
//
//    protected abstract int getCostForDefaultSelf();
//
//    protected abstract int getCostForOffenseSelf();
//
//    protected abstract int getCostForDefenseSelf();
//
//    protected abstract int getCostForEnvironmentSelf();
//
//    public int getCostForMelee()
//    {
//        switch (this.modifierState)
//        {
//            case SpellModifier.DEFAULT:
//                return this.getCostForDefaultMelee();
//            case SpellModifier.OFFENSIVE:
//                return this.getCostForOffenseMelee();
//            case SpellModifier.DEFENSIVE:
//                return this.getCostForDefenseMelee();
//            case SpellModifier.ENVIRONMENTAL:
//                return this.getCostForEnvironmentMelee();
//        }
//        return 0;
//    }
//
//    protected abstract int getCostForDefaultMelee();
//
//    protected abstract int getCostForOffenseMelee();
//
//    protected abstract int getCostForDefenseMelee();
//
//    protected abstract int getCostForEnvironmentMelee();
//
//    public int getCostForTool()
//    {
//        switch (this.modifierState)
//        {
//            case SpellModifier.DEFAULT:
//                return this.getCostForDefaultTool();
//            case SpellModifier.OFFENSIVE:
//                return this.getCostForOffenseTool();
//            case SpellModifier.DEFENSIVE:
//                return this.getCostForDefenseTool();
//            case SpellModifier.ENVIRONMENTAL:
//                return this.getCostForEnvironmentTool();
//        }
//        return 0;
//    }
//
//    protected abstract int getCostForDefaultTool();
//
//    protected abstract int getCostForOffenseTool();
//
//    protected abstract int getCostForDefenseTool();
//
//    protected abstract int getCostForEnvironmentTool();
//
//    public int getPowerEnhancements()
//    {
//        return this.powerEnhancement;
//    }
//
//    public int getCostEnhancements()
//    {
//        return this.costEnhancement;
//    }
//
//    public int getPotencyEnhancements()
//    {
//        return this.potencyEnhancement;
//    }
//
//    public NBTTagCompound getTag()
//    {
//        NBTTagCompound tag = new NBTTagCompound();
//
//        tag.setString("Class", this.getClass().getName());
//        tag.setInteger("modifier", modifierState);
//        tag.setInteger("power", powerEnhancement);
//        tag.setInteger("cost", costEnhancement);
//        tag.setInteger("potency", potencyEnhancement);
//
//        return tag;
//    }
//
//    public static SpellEffect getEffectFromTag(NBTTagCompound tag)
//    {
//        try
//        {
//            Class clazz = Class.forName(tag.getString("Class"));
//            if (clazz != null)
//            {
//                try
//                {
//                    Object obj = clazz.newInstance();
//                    if (obj instanceof SpellEffect)
//                    {
//                        SpellEffect eff = (SpellEffect) obj;
//
//                        eff.modifierState = tag.getInteger("modifier");
//                        eff.powerEnhancement = tag.getInteger("power");
//                        eff.costEnhancement = tag.getInteger("cost");
//                        eff.potencyEnhancement = tag.getInteger("potency");
//
//                        return eff;
//                    }
//                } catch (InstantiationException e)
//                {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        } catch (ClassNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
