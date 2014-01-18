package WayofTime.alchemicalWizardry.common.spell.complex;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class SpellParadigmProjectile extends SpellParadigm
{
	public DamageSource damageSource = DamageSource.generic;
	public float damage = 1;
	public int cost = 0;
	public List<IProjectileImpactEffect> impactList = new ArrayList();
	
	@Override
	public void enhanceParadigm(SpellEnhancement enh) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void castSpell(World world, EntityPlayer entityPlayer, ItemStack itemStack) 
	{
		// TODO Auto-generated method stub
		
	}
}
