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
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellModifierBlock;


public class BlockSpellModifier extends BlockOrientable 
{
	public BlockSpellModifier(int id) 
	{
		super(id);
		setUnlocalizedName("blockSpellModifier");
	}
	
	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TESpellModifierBlock();
    }
	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.blockID == ModBlocks.blockSpellModifier.blockID)
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
