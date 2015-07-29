package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.alchemicalWizardry.common.omega.IStabilityGlyph;

public class BlockStabilityGlyph extends Block implements IStabilityGlyph
{
    public BlockStabilityGlyph()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
    }

	@Override
	public int getAdditionalStabilityForFaceCount(World world, BlockPos pos, int meta, int faceCount) 
	{
		switch(meta)
		{
		case 0:
			return faceCount * 2;
		default: 
			return faceCount;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		for(int i=0; i<1; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
    }
}
