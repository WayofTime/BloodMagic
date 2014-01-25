package WayofTime.alchemicalWizardry.common.spell.complex;

import java.util.ArrayList;
import java.util.List;

import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ISelfSpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpellParadigmSelf extends SpellParadigm
{
	public List<ISelfSpellEffect> selfSpellEffectList;
	
	public SpellParadigmSelf()
	{
		selfSpellEffectList = new ArrayList();
	}
	
	@Override
	public void enhanceParadigm(SpellEnhancement enh) 
	{
		
	}

	@Override
	public void castSpell(World world, EntityPlayer entityPlayer, ItemStack itemStack) 
	{
		this.applyAllSpellEffects();
		
		for(ISelfSpellEffect eff : selfSpellEffectList)
		{
			eff.onSelfUse(world, entityPlayer);
		}
		
		int cost = this.getTotalCost();
		
		EnergyItems.syphonBatteries(itemStack, entityPlayer, cost);
	}

	public void addSelfSpellEffect(ISelfSpellEffect eff)
	{
		if(eff!=null)
		{
			this.selfSpellEffectList.add(eff);
		}
	}
	
	@Override
	public int getDefaultCost() 
	{
		return 100;
	}
	
	public static SpellParadigmSelf getParadigmForStringArray(List<String> stringList)
	{
		SpellParadigmSelf parad = new SpellParadigmSelf();
		
		try 
		{
			for(String str : stringList)
			{
				Class clazz = Class.forName(str);
				if(clazz!=null)
				{
					Object obj = clazz.newInstance();
					
					if(obj instanceof SpellEffect)
					{
						parad.addBufferedEffect((SpellEffect)obj);
						continue;
					}
					if(obj instanceof SpellModifier)
					{
						parad.modifyBufferedEffect((SpellModifier)obj);
						continue;
					}
					if(obj instanceof SpellEnhancement)
					{
						parad.applyEnhancement((SpellEnhancement)obj);
						continue;
					}
				}
			}
			
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		
		return parad;
	}
}
