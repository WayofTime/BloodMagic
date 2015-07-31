package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.omega.IEnchantmentGlyph;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEnchantmentGlyph extends Block implements IEnchantmentGlyph
{
	@SideOnly(Side.CLIENT)
	private IIcon enchantability;
	@SideOnly(Side.CLIENT)
	private IIcon enchantmentLevel;
	
    public BlockEnchantmentGlyph()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("enchantmentGlyph");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:LargeBloodStoneBrick");
        this.enchantability = iconRegister.registerIcon("AlchemicalWizardry:GlyphEnchantability");
        this.enchantmentLevel = iconRegister.registerIcon("AlchemicalWizardry:GlyphEnchantmentLevel");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        switch (meta)
        {
            case 0:
                return enchantability;
            case 1:
                return enchantmentLevel;
            default:
                return this.blockIcon;
        }
    }

	@Override
	public int getAdditionalStabilityForFaceCount(World world, int x, int y, int z, int meta, int faceCount) 
	{
		switch(meta)
		{
		case 0:
			return -faceCount * 10;
		case 1:
			return -faceCount * 20;
		default: 
			return -faceCount * 20;
		}
	}

	@Override
	public int getEnchantability(World world, int x, int y, int z, int meta) 
	{
		switch(meta)
		{
		case 0:
			return 1;
		default:
			return 0;	
		}
	}

	@Override
	public int getEnchantmentLevel(World world, int x, int y, int z, int meta) 
	{
		switch(meta)
		{
		case 1:
			return 1;
		default:
			return 0;	
		}
	}	
	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		for(int i=0; i<2; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
    }
	
	@Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }
}
