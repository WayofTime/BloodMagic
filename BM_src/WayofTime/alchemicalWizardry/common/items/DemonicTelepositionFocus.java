package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DemonicTelepositionFocus extends TelepositionFocus
{
	public DemonicTelepositionFocus(int par1)
	{
		super(par1, 4);
		// TODO Auto-generated constructor stub
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		//TODO
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DemonicTeleposerFocus");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("A stronger version of the focus,");
		par3List.add("using a demonic shard");

		if (!(par1ItemStack.stackTagCompound == null))
		{
			NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

			if (!par1ItemStack.stackTagCompound.getString("ownerName").equals(""))
			{
				par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
			}

			par3List.add("Coords: " + itemTag.getInteger("xCoord") + ", " + itemTag.getInteger("yCoord") + ", " + itemTag.getInteger("zCoord"));
			par3List.add("Bound Dimension: " + getDimensionID(par1ItemStack));
		}
	}
}
