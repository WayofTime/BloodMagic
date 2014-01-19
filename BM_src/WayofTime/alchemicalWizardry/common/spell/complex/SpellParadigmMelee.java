package WayofTime.alchemicalWizardry.common.spell.complex;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.enhancement.SpellEnhancement;

public class SpellParadigmMelee extends SpellParadigm 
{
	private List<IMeleeSpellEntityEffect> entityEffectList;
	private List<IMeleeSpellWorldEffect> worldEffectList;
	
	public SpellParadigmMelee()
	{
		this.entityEffectList = new ArrayList();
		this.worldEffectList = new ArrayList();
	}
	
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

	public void addEntityEffect(IMeleeSpellEntityEffect eff)
	{
		if(eff!=null)
		{
			this.entityEffectList.add(eff);
		}
	}
	
	public void addWorldEffect(IMeleeSpellWorldEffect eff)
	{
		if(eff!=null)
		{
			this.worldEffectList.add(eff);
		}
	}
	
	@Override
	public int getDefaultCost() 
	{
		return 0;
	}
}
