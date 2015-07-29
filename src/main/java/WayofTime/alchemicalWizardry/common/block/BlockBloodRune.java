package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockBloodRune extends Block
{
    public BlockBloodRune()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    public int getRuneEffect(int metaData)
    {
        switch (metaData)
        {
            case 0:
                return 0;

            case 1: //Altar Capacity rune
                return 5;

            case 2: //Filling/emptying rune
                return 6;

            case 3: //Orb Capacity rune
                return 7;

            case 4: //Better Capacity rune
                return 8;
                
            case 5: //Acceleration rune
            	return 9;
        }

        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.equals(ModBlocks.bloodRune))
        {
            par3List.add(new ItemStack(par1, 1, 0));
            par3List.add(new ItemStack(par1, 1, 1));
            par3List.add(new ItemStack(par1, 1, 2));
            par3List.add(new ItemStack(par1, 1, 3));
            par3List.add(new ItemStack(par1, 1, 4));
            par3List.add(new ItemStack(par1, 1, 5));
        } else
        {
            super.getSubBlocks(par1, par2CreativeTabs, par3List);
        }
    }

    @Override
    public int damageDropped(IBlockState blockState)
    {
        return blockState.getBlock().damageDropped(blockState);
    }
}
