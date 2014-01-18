package WayofTime.alchemicalWizardry.common.spell.complex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancement;

public class SpellParadigmProjectile extends SpellParadigm
{
	public DamageSource damageSource = DamageSource.generic;
	public float damage = 1;
	public int cost = 0;
	public List<IProjectileImpactEffect> impactList = new ArrayList();
	public List<IProjectileUpdateEffect> updateEffectList = new ArrayList();
	public boolean penetration = false;
	public int ricochetMax = 0;
	
	@Override
	public void enhanceParadigm(SpellEnhancement enh) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void castSpell(World world, EntityPlayer entityPlayer, ItemStack itemStack) 
	{
		EntitySpellProjectile proj = new EntitySpellProjectile(world, entityPlayer);
		this.prepareProjectile(proj);
		world.spawnEntityInWorld(proj);
	}
	
	public static SpellParadigmProjectile getParadigmForStringArray(List<String> stringList)
	{
		SpellParadigmProjectile parad = new SpellParadigmProjectile();
		
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
	
	public void prepareProjectile(EntitySpellProjectile proj)
	{
		proj.setDamage(damage);
		proj.setImpactList(impactList);
		proj.setUpdateEffectList(updateEffectList); 
		proj.setPenetration(penetration);
		proj.setEffectList(effectList);
		proj.setRicochetMax(ricochetMax); 
	}

	@Override
	public int getDefaultCost() 
	{
		// TODO Auto-generated method stub
		return 50;
	}
	
}
