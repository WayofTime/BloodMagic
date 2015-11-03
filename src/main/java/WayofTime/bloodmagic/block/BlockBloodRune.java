package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockBloodRune extends Block
{
    public static final String[] names = { "speed", "efficiency", "sacrifice", "self_sacrifice", "displacement", "capacity", "orb_capacity", "better_capacity", "acceleration" };
    public static final PropertyInteger INTEGER = PropertyInteger.create("rune", 0, 5);

    public BlockBloodRune()
    {
        super(Material.iron);
        this.setDefaultState(this.blockState.getBaseState().withProperty(INTEGER, 0));
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
    protected BlockState createBlockState()
    {
        return new BlockState(this, INTEGER);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(INTEGER, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(INTEGER)).intValue();
    }

    @Override
    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.equals(ModBlocks.blood_rune))
        {
            par3List.add(new ItemStack(par1, 1, 0));
            par3List.add(new ItemStack(par1, 1, 1));
            par3List.add(new ItemStack(par1, 1, 2));
            par3List.add(new ItemStack(par1, 1, 3));
            par3List.add(new ItemStack(par1, 1, 4));
            par3List.add(new ItemStack(par1, 1, 5));
        }
        else
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
