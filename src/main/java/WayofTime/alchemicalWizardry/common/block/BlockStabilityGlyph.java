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
import WayofTime.alchemicalWizardry.common.omega.IStabilityGlyph;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStabilityGlyph extends Block implements IStabilityGlyph
{
    @SideOnly(Side.CLIENT)
	private IIcon stability1;
	
    public BlockStabilityGlyph()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("stabilityGlyph");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:LargeBloodStoneBrick");
        this.stability1 = iconRegister.registerIcon("AlchemicalWizardry:GlyphStability1");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        switch (meta)
        {
            case 0:
                return stability1;
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
			return faceCount * 2;
		default: 
			return faceCount;
		}
	}
	
	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		for(int i=0; i<1; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
    }
}
