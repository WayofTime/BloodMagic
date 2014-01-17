package WayofTime.alchemicalWizardry.common.items.potion;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.alchemy.AlchemyRecipeRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AlchemyReagent extends Item
{
	public AlchemyReagent(int id)
	{
		super(id);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		setMaxStackSize(64);
		// TODO Auto-generated constructor stub
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		if (itemID == AlchemicalWizardry.incendium.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Incendium");
			return;
		}

		if (itemID == AlchemicalWizardry.magicales.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Magicales");
			return;
		}

		if (itemID == AlchemicalWizardry.sanctus.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Sanctus");
			return;
		}

		if (itemID == AlchemicalWizardry.aether.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Aether");
			return;
		}

		if (itemID == AlchemicalWizardry.simpleCatalyst.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SimpleCatalyst");
			return;
		}

		if (itemID == AlchemicalWizardry.crepitous.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Crepitous");
			return;
		}

		if (itemID == AlchemicalWizardry.crystallos.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Crystallos");
			return;
		}

		if (itemID == AlchemicalWizardry.terrae.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Terrae");
			return;
		}

		if (itemID == AlchemicalWizardry.aquasalus.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Aquasalus");
			return;
		}

		if (itemID == AlchemicalWizardry.tennebrae.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Tennebrae");
			return;
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Used in alchemy");

		if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(par1ItemStack);

			if (recipe != null)
			{
				par3List.add(EnumChatFormatting.BLUE + "Recipe:");

				for (ItemStack item: recipe)
				{
					if (item != null)
					{
						par3List.add("" + item.getDisplayName());
					}
				}
			}
		}
		else
		{
			par3List.add("-Press " + EnumChatFormatting.BLUE + "shift" + EnumChatFormatting.GRAY + " for Recipe-");
		}
	}
}
