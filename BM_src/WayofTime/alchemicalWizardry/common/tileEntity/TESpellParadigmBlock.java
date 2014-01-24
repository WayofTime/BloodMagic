package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;

public class TESpellParadigmBlock extends TESpellBlock 
{
	public SpellParadigm getSpellParadigm()
	{
		return new SpellParadigmSelf();
	}

	@Override
	protected void applySpellChange(SpellParadigm parad) 
	{
		return;
	}
	
	public boolean canInputRecieve()
	{
		return false;
	}
	
	public void castSpell(World world, EntityPlayer entity, ItemStack spellCasterStack)
	{
		SpellParadigm parad = this.getSpellParadigm();
		this.modifySpellParadigm(parad);
		parad.castSpell(world, entity, spellCasterStack);
	}
}
