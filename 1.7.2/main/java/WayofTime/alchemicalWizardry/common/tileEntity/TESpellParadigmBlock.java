package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;

public class TESpellParadigmBlock extends TESpellBlock 
{
	public SpellParadigm getSpellParadigm()
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		switch(meta)
		{
		case 0: return new SpellParadigmProjectile();
		case 1: return new SpellParadigmSelf();
		case 2: return new SpellParadigmMelee();
		case 3: return new SpellParadigmTool();
		}
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
//		if(parad instanceof SpellParadigmSelf)
//		{
//			List<String> stringList = parad.effectList;
//			SpellParadigmSelf spellParadSelf = SpellParadigmSelf.getParadigmForStringArray(stringList);
//			for(String str : stringList)
//			{
//				ChatMessageComponent chat = new ChatMessageComponent();
//				chat.addText(str);
//				entity.sendChatToPlayer(chat);
//			}
//			spellParadSelf.castSpell(world, entity, spellCasterStack);
//		}
//		else if(parad instanceof SpellParadigmProjectile)
//		{
//			List<String> stringList = parad.effectList;
//			SpellParadigmProjectile spellParadSelf = SpellParadigmProjectile.getParadigmForStringArray(stringList);
//			for(String str : stringList)
//			{
//				ChatMessageComponent chat = new ChatMessageComponent();
//				chat.addText(str);
//				entity.sendChatToPlayer(chat);
//			}
//			spellParadSelf.castSpell(world, entity, spellCasterStack);
//		}else
		{
			parad.applyAllSpellEffects();
			parad.castSpell(world, entity, spellCasterStack);
		}
		
	}
	
	@Override
	public String getResourceLocationForMeta(int meta)
    {
		switch(meta)
		{
		case 0: return "alchemicalwizardry:textures/models/SpellParadigmProjectile.png";
		case 1: return "alchemicalwizardry:textures/models/SpellParadigmSelf.png";
		case 2: return "alchemicalwizardry:textures/models/SpellParadigmMelee.png";
		case 3: return "alchemicalwizardry:textures/models/SpellParadigmTool.png";
		}
    	return "alchemicalwizardry:textures/models/SpellParadigmProjectile.png";
    }
	
	@Override
    public void setInputDirection(ForgeDirection direction)
    {

    }
	
	@Override
	public ForgeDirection getInputDirection()
	{
		return ForgeDirection.UNKNOWN;
	}
}
