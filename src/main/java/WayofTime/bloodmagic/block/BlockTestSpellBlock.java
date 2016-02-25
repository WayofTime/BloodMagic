package WayofTime.bloodmagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;

public class BlockTestSpellBlock extends Block
{
    public static final PropertyDirection INPUT = PropertyDirection.create("input");
    public static final PropertyDirection OUTPUT = PropertyDirection.create("output");

    public BlockTestSpellBlock()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);

        setUnlocalizedName(Constants.Mod.MODID + ".testSpellBlock");
        setRegistryName("BlockTestSpellBlock");
        setCreativeTab(BloodMagic.tabBloodMagic);
        this.setDefaultState(this.blockState.getBaseState().withProperty(INPUT, EnumFacing.DOWN).withProperty(OUTPUT, EnumFacing.UP));
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(INPUT, EnumFacing.DOWN).withProperty(OUTPUT, EnumFacing.UP);
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] { INPUT, OUTPUT });
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos)
    {
        return false;
    }
}
