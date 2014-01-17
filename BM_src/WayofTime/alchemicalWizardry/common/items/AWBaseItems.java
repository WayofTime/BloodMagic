package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;

public class AWBaseItems extends Item
{
	public AWBaseItems(int id)
	{
		super(id);
		setMaxStackSize(64);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		if (itemID == AlchemicalWizardry.blankSlate.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BlankSlate");
		}
		else if (itemID == AlchemicalWizardry.reinforcedSlate.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ReinforcedSlate");
		}
		else if (itemID == AlchemicalWizardry.imbuedSlate.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:InfusedSlate");
		}
		else if (itemID == AlchemicalWizardry.demonicSlate.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DemonSlate");
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Infused stone inside of");
		par3List.add("a blood altar");
	}
}
