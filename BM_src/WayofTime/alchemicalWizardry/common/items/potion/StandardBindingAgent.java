package WayofTime.alchemicalWizardry.common.items.potion;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.IBindingAgent;
import WayofTime.alchemicalWizardry.common.alchemy.AlchemyRecipeRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StandardBindingAgent extends Item implements IBindingAgent
{
	public StandardBindingAgent(int id)
	{
		super(id);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	public float getSuccessRateForPotionNumber(int potions)
	{
		return (float) Math.pow(0.65, potions);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:StandardBindingAgent");
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
