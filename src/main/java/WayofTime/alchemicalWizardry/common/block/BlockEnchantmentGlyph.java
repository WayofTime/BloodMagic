package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.omega.IEnchantmentGlyph;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEnchantmentGlyph extends Block implements IEnchantmentGlyph
{
    public BlockEnchantmentGlyph()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("enchantmentGlypg");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:LargeBloodStoneBrick");
    }

	@Override
	public int getSubtractedStabilityForFaceCount(World world, int x, int y, int z, int meta, int faceCount) 
	{
		return faceCount * 20;
	}

	@Override
	public int getEnchantability(World world, int x, int y, int z, int meta) 
	{
		return 1;
	}
}
