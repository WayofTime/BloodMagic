package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BloodStoneBrick extends Block
{
	public BloodStoneBrick(int par1)
	{
		super(par1, Material.iron);
		setHardness(2.0F);
		setResistance(5.0F);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		setUnlocalizedName("bloodStoneBrick");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodStoneBrick");
	}
}
