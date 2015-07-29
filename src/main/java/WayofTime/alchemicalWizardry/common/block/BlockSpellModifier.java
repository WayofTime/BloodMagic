package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellModifierBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockSpellModifier extends BlockOrientable
{
    public BlockSpellModifier()
    {
        super();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TESpellModifierBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.equals(ModBlocks.blockSpellModifier))
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

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }
}
