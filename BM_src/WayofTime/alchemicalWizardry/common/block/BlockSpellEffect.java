package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEffectBlock;


public class BlockSpellEffect extends BlockOrientable 
{
	public BlockSpellEffect(int id) 
	{
		super(id);
		setUnlocalizedName("blockSpellEffect");
	}
	
	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TESpellEffectBlock();
    }
	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.blockID == ModBlocks.blockSpellEffect.blockID)
        {
        	for(int i=0; i<2; i++)
        	{
        		par3List.add(new ItemStack(par1, 1, i));
        	}
        } else
        {
            super.getSubBlocks(par1, par2CreativeTabs, par3List);
        }
    }
}
