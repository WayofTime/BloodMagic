package WayofTime.bloodmagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import WayofTime.bloodmagic.BloodMagic;

public abstract class BlockRoutingNode extends BlockContainer
{
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockRoutingNode()
    {
        super(Material.rock);

        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);

        setBlockBounds(0.378F, 0.378F, 0.378F, 0.625F, 0.625F, 0.625F);

        this.setDefaultState(this.blockState.getBaseState().withProperty(DOWN, false).withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean canRenderInLayer(EnumWorldBlockLayer layer)
    {
        return layer == EnumWorldBlockLayer.CUTOUT_MIPPED || layer == EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(UP, this.shouldConnect(worldIn, pos.up())).withProperty(DOWN, this.shouldConnect(worldIn, pos.down())).withProperty(NORTH, this.shouldConnect(worldIn, pos.north())).withProperty(EAST, this.shouldConnect(worldIn, pos.east())).withProperty(SOUTH, this.shouldConnect(worldIn, pos.south())).withProperty(WEST, this.shouldConnect(worldIn, pos.west()));
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, UP, DOWN, NORTH, EAST, WEST, SOUTH);
    }

    public boolean shouldConnect(IBlockAccess world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        return block.isFullBlock() && block.isFullCube();
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }
}
