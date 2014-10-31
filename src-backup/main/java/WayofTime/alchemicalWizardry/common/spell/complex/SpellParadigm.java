package WayofTime.alchemicalWizardry.common.spell.complex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancement;

public abstract class SpellParadigm 
{
	protected List<SpellEffect> bufferedEffectList = new LinkedList();
	public List<String> effectList = new LinkedList();
	
	public void addBufferedEffect(SpellEffect effect)
	{
		if(effect!=null)
		{
			this.bufferedEffectList.add(effect);
			
			effectList.add(effect.getClass().getName());
		}
	}
	
	public void modifyBufferedEffect(SpellModifier modifier)
	{
		SpellEffect effect = this.getBufferedEffect();
		if(effect!=null)
		{
			effect.modifyEffect(modifier);
			
			effectList.add(modifier.getClass().getName());
		}
	}
	
	public void applyEnhancement(SpellEnhancement enh)
	{
		if(enh!=null)
		{
			if(bufferedEffectList.isEmpty())
			{
				this.enhanceParadigm(enh);
			}
			else
			{
				SpellEffect effect = this.getBufferedEffect();
				if(effect!=null)
				{
					effect.enhanceEffect(enh);
				}
			}
			
			effectList.add(enh.getClass().getName());
		}
		
	}
	
	public abstract void enhanceParadigm(SpellEnhancement enh);
	public abstract void castSpell(World world, EntityPlayer entityPlayer, ItemStack itemStack);
	
	public void applySpellEffect(SpellEffect effect)
	{
		effect.modifyParadigm(this);
	}
	
	public void applyAllSpellEffects()
	{
		for(SpellEffect effect : bufferedEffectList)
		{
			this.applySpellEffect(effect);
		}
	}
	
	public SpellEffect getBufferedEffect()
	{
		if(bufferedEffectList.isEmpty())
		{
			return null;
		}
		else
		{
			return bufferedEffectList.get(bufferedEffectList.size()-1);
		}
	}
	
	public int getTotalCost()
	{
		int cost = 0;
		if(this.bufferedEffectList!=null && !this.bufferedEffectList.isEmpty())
		{
			if(this instanceof SpellParadigmProjectile)
			{
				for(SpellEffect effect : bufferedEffectList)
				{
					cost+=effect.getCostForProjectile();
				}
			}else if(this instanceof SpellParadigmSelf)
			{
				for(SpellEffect effect : bufferedEffectList)
				{
					cost+=effect.getCostForSelf();
				}
			}else if(this instanceof SpellParadigmMelee)
			{
				for(SpellEffect effect : bufferedEffectList)
				{
					cost+=effect.getCostForMelee();
				}
			}else if(this instanceof SpellParadigmTool)
			{
				for(SpellEffect effect : bufferedEffectList)
				{
					cost+=effect.getCostForTool();
				}
			}
			
			return (int)(cost*Math.sqrt(this.bufferedEffectList.size()));
		}

		return getDefaultCost();
	}
	
	public abstract int getDefaultCost();
	
	public int getBufferedEffectPower()
	{
		SpellEffect eff = this.getBufferedEffect();
		
		if(eff!=null)
		{
			return eff.getPowerEnhancements();
		}
		
		return 0;
	}
	
	public int getBufferedEffectCost()
	{
		SpellEffect eff = this.getBufferedEffect();
		
		if(eff!=null)
		{
			return eff.getCostEnhancements();
		}
		
		return 0;
	}
	
	public int getBufferedEffectPotency()
	{
		SpellEffect eff = this.getBufferedEffect();
		
		if(eff!=null)
		{
			return eff.getPotencyEnhancements();
		}
		
		return 0;
	}
}
