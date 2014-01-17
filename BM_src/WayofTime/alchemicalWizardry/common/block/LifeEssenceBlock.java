package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LifeEssenceBlock extends BlockFluidClassic
{
	public LifeEssenceBlock(int id)
	{
		super(id, AlchemicalWizardry.lifeEssenceFluid, Material.water);
		AlchemicalWizardry.lifeEssenceFluid.setBlockID(id);
		//setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		return blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("AlchemicalWizardry:lifeEssenceStill");
		getFluid().setIcons(blockIcon);
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z)
	{
		if (world.getBlockMaterial(x,  y,  z).isLiquid())
		{
			return false;
		}

		return super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z)
	{
		if (world.getBlockMaterial(x,  y,  z).isLiquid())
		{
			return false;
		}

		return super.displaceIfPossible(world, x, y, z);
	}
}
