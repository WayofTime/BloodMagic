package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ArmourUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BloodShard extends Item implements ArmourUpgrade
{
	public BloodShard(int par1)
	{
		super(par1);
		maxStackSize = 64;
		//setEnergyUsed(100);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		if (itemID == AlchemicalWizardry.weakBloodShard.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WeakBloodShard");
			return;
		}

		if (itemID == AlchemicalWizardry.demonBloodShard.itemID)
		{
			itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DemonBloodShard");
			return;
		}
	}

	public int getBloodShardLevel()
	{
		if (itemID == AlchemicalWizardry.weakBloodShard.itemID)
		{
			return 1;
		}
		else if (itemID == AlchemicalWizardry.demonBloodShard.itemID)
		{
			return 2;
		}

		return 0;
	}

	@Override
	public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isUpgrade()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getEnergyForTenSeconds()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
