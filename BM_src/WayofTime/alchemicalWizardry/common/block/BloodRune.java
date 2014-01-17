package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BloodRune extends Block
{
	//private Icon bloodRuneIcon;
	private Icon altarCapacityRuneIcon;
	private Icon dislocationRuneIcon;
	private Icon orbCapacityRuneIcon;

	public BloodRune(int id)
	{
		super(id, Material.iron);
		setUnlocalizedName("bloodRune");
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		setHardness(2.0F);
		setResistance(5.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("AlchemicalWizardry:BlankRune");
		altarCapacityRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:AltarCapacityRune");
		dislocationRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:DislocationRune");
		orbCapacityRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:OrbCapacityRune");
	}

	public int getRuneEffect(int metaData)
	{
		switch (metaData)
		{
		case 0:
			return 0;

		case 1: //Altar Capacity rune
			return 5;

		case 2: //Filling/emptying rune
			return 6;

		case 3: //Orb Capacity rune
			return 7;
		}

		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		if (blockID == AlchemicalWizardry.bloodRune.blockID)
		{
			par3List.add(new ItemStack(par1, 1, 0));
			par3List.add(new ItemStack(par1, 1, 1));
			par3List.add(new ItemStack(par1, 1, 2));
			par3List.add(new ItemStack(par1, 1, 3));
		}
		else
		{
			super.getSubBlocks(par1, par2CreativeTabs, par3List);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		switch (meta)
		{
		case 0:
			return blockIcon;

		case 1:
			return altarCapacityRuneIcon;

		case 2:
			return dislocationRuneIcon;

		case 3:
			return orbCapacityRuneIcon;

		default:
			return blockIcon;
		}
	}

	@Override
	public int damageDropped(int metadata)
	{
		return metadata;
	}
}
