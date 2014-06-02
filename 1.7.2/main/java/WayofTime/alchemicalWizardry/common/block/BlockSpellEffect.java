package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEffectBlock;


public class BlockSpellEffect extends BlockOrientable 
{
	public BlockSpellEffect() 
	{
		super();
		this.setBlockName("blockSpellEffect");
	}
	
	@Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TESpellEffectBlock();
    }
	
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.equals(ModBlocks.blockSpellEffect))
        {
        	for(int i=0; i<4; i++)
        	{
        		par3List.add(new ItemStack(par1, 1, i));
        	}
        } else
        {
            super.getSubBlocks(par1, par2CreativeTabs, par3List);
        }
    }
}
