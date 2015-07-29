package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEffectBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockSpellEffect extends BlockOrientable
{
    public BlockSpellEffect()
    {
        super();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TESpellEffectBlock();
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

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.equals(ModBlocks.blockSpellEffect))
        {
            for (int i = 0; i < 4; i++)
            {
                par3List.add(new ItemStack(par1, 1, i));
            }
        } else
        {
            super.getSubBlocks(par1, par2CreativeTabs, par3List);
        }
    }
}
