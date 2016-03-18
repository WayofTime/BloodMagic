package WayofTime.bloodmagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import WayofTime.bloodmagic.BloodMagic;

public abstract class BlockRoutingNode extends BlockContainer
{
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.378F, 0.378F, 0.378F, 0.625F, 0.625F, 0.625F);

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

        this.setDefaultState(this.blockState.getBaseState().withProperty(DOWN, false).withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer)
    {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
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
        return state.withProperty(UP, this.shouldConnect(state, worldIn, pos.up())).withProperty(DOWN, this.shouldConnect(state, worldIn, pos.down())).withProperty(NORTH, this.shouldConnect(state, worldIn, pos.north())).withProperty(EAST, this.shouldConnect(state, worldIn, pos.east())).withProperty(SOUTH, this.shouldConnect(state, worldIn, pos.south())).withProperty(WEST, this.shouldConnect(state, worldIn, pos.west()));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, UP, DOWN, NORTH, EAST, WEST, SOUTH);
    }

    public boolean shouldConnect(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        return block.isFullBlock(state) && block.isFullCube(state);
    }
}
